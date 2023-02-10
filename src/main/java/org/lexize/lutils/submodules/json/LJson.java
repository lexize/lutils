package org.lexize.lutils.submodules.json;

import org.lexize.lutils.annotations.LDocsFuncOverload;
import org.luaj.vm2.LuaTable;
import org.moon.figura.lua.LuaWhitelist;

import java.util.HashMap;

@LuaWhitelist
public class LJson {
    @LuaWhitelist
    @LDocsFuncOverload(
            argumentTypes = LuaTable.class,
            argumentNames = "settings",
            description = "/lutils/docs/json/newSerializer.txt",
            resource = true,
            returnType = LJsonSerializer.class
    )
    public LJsonSerializer newSerializer(HashMap<String, ?> settings) {
        return new LJsonSerializer(settings != null ? settings : new HashMap<>());
    }
    public LJsonArray newArray() {
        return new LJsonArray();
    }
    public LJsonObject newObject() {
        return new LJsonObject();
    }
}
