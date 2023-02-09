package org.lexize.lutils.submodules.json;

import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.moon.figura.lua.LuaWhitelist;

import java.util.*;

@LuaWhitelist
public class LJsonObject implements LJsonValue<Map<String, Object>> {
    private final Map<String, Object> data = new HashMap<>();

    @Override
    public Map<String, Object> getData() {
        return data;
    }

    @LuaWhitelist
    public int size() {
        return data.size();
    }

    @LuaWhitelist
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @LuaWhitelist
    public boolean containsKey(String o) {
        return data.containsKey(o);
    }

    @LuaWhitelist
    public boolean containsValue(Object o) {
        return data.containsValue(o);
    }

    @LuaWhitelist
    public Object get(String o) {
        return data.get(o);
    }

    @LuaWhitelist
    public void put(String s, Object o) {
        data.put(s, o);
    }

    @LuaWhitelist
    public void clear() {
        data.clear();
    }

    @LuaWhitelist
    public Set<String> keySet() {
        return data.keySet();
    }

    @LuaWhitelist
    public Collection<Object> values() {
        return data.values();
    }

    @LuaWhitelist
    public Object getOrDefault(String key, Object defaultValue) {
        return data.getOrDefault(key, defaultValue);
    }

    @LuaWhitelist
    public boolean remove(String key, Object value) {
        return data.remove(key, value);
    }

    @LuaWhitelist
    public Object __index(LuaValue key) {
        if (!key.isstring()) return null;
        String k = key.tojstring();
        return data.get(k);
    }

    @LuaWhitelist
    public void __newindex(LuaValue key, Object o) {
        if (!key.isstring()) return;
        String k = key.tojstring();
        data.put(k, o);
    }
    private LuaFunction createIterator() {
        List<String> keys = data.keySet().stream().toList();
        return new LuaFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                if (keys.size() == 0) return LuaValue.NIL;
                LuaValue t = args.arg1();
                LuaValue k = args.arg(2);
                LuaValue outputkey = null;
                if (k.isnil()) outputkey = LuaValue.valueOf(keys.get(0));
                else if (k.isstring()){
                    String key = k.tojstring();
                    int nextKeyIndex = keys.indexOf(key) + 1;
                    if (nextKeyIndex < keys.size()) {
                        outputkey = LuaValue.valueOf(keys.get(nextKeyIndex));
                    }
                }
                return outputkey != null ? LuaValue.varargsOf(outputkey, t.get(outputkey)) : LuaValue.NIL;
            }
        };
    }
    private LuaFunction emptyIterator() {
        return new LuaFunction() {
            @Override
            public Varargs invoke() {
                return LuaValue.NIL;
            }
        };
    }
    @LuaWhitelist
    public Object[] __pairs(LuaTable tbl) {
        return new Object[] {
                createIterator(), tbl, null
        };
    }

    @LuaWhitelist
    public Object[] __ipairs(LuaTable tbl) {
        return new Object[] {
                emptyIterator(), tbl, 0
        };
    }
}
