package org.lexize.lutils.submodules.json;

import org.moon.figura.lua.LuaWhitelist;

import java.util.HashMap;

@LuaWhitelist
public class LJson {
    @LuaWhitelist
    public LJsonSerializer newSerializer(HashMap<String, ?> settings) {
        return new LJsonSerializer(settings != null ? settings : new HashMap<>());
    }
    @LuaWhitelist
    public LJsonArray newArray() {
        return new LJsonArray();
    }
    @LuaWhitelist
    public LJsonObject newObject() {
        return new LJsonObject();
    }
}
