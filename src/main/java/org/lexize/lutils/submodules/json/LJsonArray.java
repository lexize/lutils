package org.lexize.lutils.submodules.json;

import org.jetbrains.annotations.NotNull;
import org.luaj.vm2.LuaValue;
import org.moon.figura.lua.LuaWhitelist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@LuaWhitelist
public class LJsonArray implements LJsonValue<List<Object>> {
    private final ArrayList<Object> data = new ArrayList<>();
    @Override
    public List<Object> getData() {
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
    public boolean contains(Object o) {
        return data.contains(o);
    }

    @LuaWhitelist
    public boolean add(Object o) {
        return data.add(o);
    }

    @LuaWhitelist
    public boolean addAll(@NotNull Collection<?> collection) {
        return data.addAll(collection);
    }

    @LuaWhitelist
    public void clear() {
        data.clear();
    }

    @LuaWhitelist
    public Object get(int i) {
        return data.get(i);
    }

    @LuaWhitelist
    public Object set(int i, Object o) {
        return data.set(i, o);
    }

    @LuaWhitelist
    public void put(int i, Object o) {
        data.add(i, o);
    }

    @LuaWhitelist
    public Object remove(int i) {
        return data.remove(i);
    }

    @LuaWhitelist
    public int indexOf(Object o) {
        return data.indexOf(o);
    }

    @LuaWhitelist
    public Object __index(LuaValue key) {
        if (!key.isint()) return null;
        int k = key.toint() - 1;
        if (k < 0 || k >= data.size()) return null;
        return data.get(k);
    }

    @LuaWhitelist
    public void __newindex(LuaValue key, Object v) {
        if (!key.isint()) return;
        int k = key.toint() - 1;
        if (k < 0) return;
        data.ensureCapacity(k+1);
        data.set(k, v);
    }
}
