package org.lexize.lutils.lua;

import org.lexize.lutils.annotations.LUtilsInclude;
import org.lexize.lutils.annotations.LUtilsName;
import org.lexize.lutils.exceptions.LuaToJavaConversionError;
import org.lexize.lutils.exceptions.MatchOverloadFailed;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.VarArgFunction;
import org.moon.figura.lua.LuaTypeManager;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.Function;

public class LUtilsLuaValue extends LuaValue {

    private LuaTypeManager typeManager;

    private Map<Class<?>, LuaTable> metatables = new HashMap<>();

    public void setLuaTypeManager(LuaTypeManager manager) {
        typeManager = manager;
    }

    private enum LuaType {
        STRING(l -> l.tojstring()),
        BOOLEAN(l -> l.toboolean()),
        BYTE(l -> l.tobyte()),
        SHORT(l -> l.toshort()),
        INTEGER(l -> l.toint()),
        FLOAT(l -> l.tofloat()),
        LONG(l -> l.tolong()),
        DOUBLE(l -> l.todouble());

        private final Function<LuaValue, Object> returnFunction;

        LuaType(Function<LuaValue, Object> func) {
            returnFunction = func;
        }

        public Object get(LuaValue val) {
            return returnFunction.apply(val);
        }
    }

    protected Map<String, List<Method>> methods = new HashMap<>();

    protected Map<String, VarArgFunction> overloadedFunctions = new HashMap<>();

    protected static Map<Class, LuaType> typesToLua = new HashMap<>() {{
        put(String.class, LuaType.STRING);
        put(Boolean.TYPE, LuaType.BOOLEAN);
        put(Byte.TYPE, LuaType.BYTE);
        put(Short.TYPE, LuaType.SHORT);
        put(Integer.TYPE, LuaType.INTEGER);
        put(Float.TYPE, LuaType.FLOAT);
        put(Long.TYPE, LuaType.LONG);
        put(Double.TYPE, LuaType.DOUBLE);
    }};

    public LUtilsLuaValue() {
        Class<? extends LUtilsLuaValue> valueClass = this.getClass();
        for (Method method:
             valueClass.getMethods()) {
            if (method.isAnnotationPresent(LUtilsInclude.class)) {
                LUtilsName nameAnnotation = method.getAnnotation(LUtilsName.class);
                String methodName = method.getName();
                if (nameAnnotation != null) {
                    methodName = nameAnnotation.value();
                }
                if (!methods.containsKey(methodName)) methods.put(methodName, new LinkedList<>());
                methods.get(methodName).add(method);
            }
        }
        for (String method:
                methods.keySet()) {
            overloadedFunctions.put(method, new LUtilsOverloadedFunction(methods.get(method), this));
        }
    }

    @Override
    public int type() {
        return LuaValue.TVALUE;
    }

    @Override
    public String typename() {
        return "lutils_value";
    }

    @Override
    public LuaValue get(LuaValue key) {
        return key.isstring() ? get(key.tojstring()) : get(key.toint());
    }

    @Override
    public LuaValue get(int key) {
        return super.get(key);
    }

    @Override
    public LuaValue get(String key) {
        if (overloadedFunctions.containsKey(key)) {
            return overloadedFunctions.get(key);
        }
        return super.get(key);
    }

    protected static LuaTable fromArray(Object[] objs) {
        LuaTable vals = new LuaTable();
        for (int i = 0; i < objs.length; i++) {
            vals.set(i+1, fromJavaValue(objs[i]).arg1());
        }
        return vals;
    }

    protected static LuaTable fromCollection(Collection<?> collection) {
        LuaTable vals = new LuaTable();
        for (Object o:
             collection) {
            vals.add(fromJavaValue(o).arg1());
        }
        return vals;
    }
    protected static Varargs fromJavaValue(Object obj) {
        if (obj == null) return LuaValue.NIL;
        else if (obj instanceof Varargs a) return a;
        else if (obj.getClass().isArray()) return fromArray((Object[]) obj);
        else if (obj instanceof Collection<?> objs) return fromCollection(objs);
        else if (obj instanceof Double d) return LuaValue.valueOf(d);
        else if (obj instanceof String s) return LuaValue.valueOf(s);
        else if (obj instanceof Boolean b) return LuaValue.valueOf(b);
        else if (obj instanceof Integer i) return LuaValue.valueOf(i);
        else if (obj instanceof Float f) return LuaValue.valueOf(f);
        else if (obj instanceof Byte b) return LuaValue.valueOf(b);
        else if (obj instanceof Long l) return LuaValue.valueOf(l);
        else if (obj instanceof Character c) return LuaValue.valueOf(c);
        else if (obj instanceof Short s) return LuaValue.valueOf(s);
        return new LuaUserdata(obj);
    }

    public static Object[] matchOverload(Method overload, Varargs args) throws LuaToJavaConversionError, MatchOverloadFailed {
        Class<?>[] parameters = overload.getParameterTypes();
        List<Object> params = new ArrayList<>();
        boolean lastParameterVarargs = overload.isVarArgs();
        if ((args.narg() != parameters.length && !lastParameterVarargs) ||
                (args.narg() < parameters.length)) throw new MatchOverloadFailed(overload, args);
        for (int i = 0; i < parameters.length; i++) {
            Class parameter = parameters[i];
            LuaValue lValue = args.arg(i+1);
            Object v = null;
            try {
                v = fromLuaValue(lValue, parameter);
            }
            catch (LuaToJavaConversionError e) {
                e.printStackTrace();
                if (i == parameters.length - 1 && lastParameterVarargs) {
                    int l = args.narg() - i;
                    Class componentType = parameter.getComponentType();
                    Object remainingVarArgs = Array.newInstance(componentType, l);
                    for (int j = 0; j < l; j++) {
                        LuaValue lV = args.arg(parameters.length+j);
                        Object vA = fromLuaValue(lV, componentType);
                        Array.set(remainingVarArgs, j, vA);
                    }
                    v = remainingVarArgs;
                }
            }
            params.add(v);
        }
        return params.toArray();
    }

    public static Object fromLuaValue(LuaValue val, Class type) throws LuaToJavaConversionError {
        if (val.isnil()) return null;
        try {
            if (type.isInstance(val)) {
                return val;
            }
            if (val.isuserdata()) {
                Object ud = val.touserdata();
                if (type.isInstance(ud)) {
                    return ud;
                }
            }
            LuaType tp = typesToLua.get(type);
            if (tp != null) {
                return tp.get(val);
            }
            if (val.istable()) {
                if (type.isArray()) {
                    LuaTable table = val.checktable();
                    Class objType = type.getComponentType();

                    Object arr = Array.newInstance(objType, table.length());
                    for (int i = 1; i <= table.length(); i++) {
                        Object v = fromLuaValue(table.get(i), objType);
                        Array.set(arr, i-1, v);
                    }
                    return arr;
                }
                if (Collection.class.isAssignableFrom(type)) {
                    List<Object> list = new ArrayList<>();
                    LuaTable table = val.checktable();

                    Class objType = (Class)(((ParameterizedType) type.getGenericSuperclass()).getActualTypeArguments()[0]);
                    for (int i = 1; i <= table.length(); i++) {
                        Object v = fromLuaValue(table.get(i), objType);
                        list.add(v);
                    }
                    return list;
                }
                if (Map.class.isAssignableFrom(type)) {
                    Map<Object, Object> map = new HashMap<>();
                    LuaTable table = val.checktable();
                    Type[] genericTypes = ((ParameterizedType) type.getGenericSuperclass()).getActualTypeArguments();

                    Class keyClass = (Class) genericTypes[0];
                    Class objClass = (Class) genericTypes[1];

                    for (LuaValue key:
                            table.keys()) {
                        Object k = fromLuaValue(key, keyClass);
                        Object v = fromLuaValue(table.get(key), objClass);
                        map.put(k,v);
                    }
                    return map;
                }
            }
        }
        catch (LuaError ignored) {}
        throw new LuaToJavaConversionError(val.type(), val.typename(), type);
    }
}
