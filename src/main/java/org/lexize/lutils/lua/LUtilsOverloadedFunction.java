package org.lexize.lutils.lua;

import org.lexize.lutils.exceptions.LuaToJavaConversionError;
import org.lexize.lutils.exceptions.MatchOverloadFailed;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class LUtilsOverloadedFunction extends VarArgFunction {
    private final List<Method> methods;
    private final LUtilsLuaValue parent;

    public LUtilsOverloadedFunction(List<Method> methods, LUtilsLuaValue parent) {
        this.methods = methods;
        this.parent = parent;
    }

    @Override
    public Varargs onInvoke(Varargs args) {
        Varargs fArgs = args.subargs(2);
        for (Method m:
                methods) {
            try {
                Object[] overloadArgs = LUtilsLuaValue.matchOverload(m, fArgs);
                return LUtilsLuaValue.fromJavaValue(m.invoke(parent, overloadArgs));
            }
            catch (LuaToJavaConversionError | MatchOverloadFailed e) {
                e.printStackTrace();
            }
            catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                break;
            }
        }
        throw new RuntimeException("No suitable overload found.");
    }
}
