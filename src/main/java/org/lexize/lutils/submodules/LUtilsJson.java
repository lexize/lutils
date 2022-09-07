package org.lexize.lutils.submodules;

import com.google.gson.*;
import org.luaj.vm2.*;
import org.moon.figura.lua.LuaWhitelist;

import java.util.HashMap;
import java.util.Map;

@LuaWhitelist
public class LUtilsJson{

    private Gson _json;

    public LUtilsJson() {
        _json = new GsonBuilder().setPrettyPrinting().create();
    }

    @LuaWhitelist
    public LuaValue fromJson(String jsonString) {
        JsonElement element = _json.fromJson(jsonString, JsonElement.class);
        return fromJsonElement(element);
    }

    @LuaWhitelist
    public String toJson(LuaValue value) {
        JsonElement element = toJsonElement(value);
        return _json.toJson(element);
    }

    private LuaValue fromJsonElement(JsonElement element) {
        if (element.isJsonArray()) return fromJsonArray(element.getAsJsonArray());
        else if (element.isJsonObject()) return fromJsonObject(element.getAsJsonObject());
        else if (element.isJsonPrimitive()) return fromJsonPrimitive(element.getAsJsonPrimitive());
        return LuaValue.NIL;
    }

    private LuaValue fromJsonPrimitive(JsonPrimitive primitive) {
        if (primitive.isBoolean()) return LuaValue.valueOf(primitive.getAsBoolean());
        else if (primitive.isNumber()) return LuaValue.valueOf(primitive.getAsDouble());
        else if (primitive.isString()) return LuaValue.valueOf(primitive.getAsString());
        return null;
    }

    private LuaTable fromJsonObject(JsonObject object) {
        LuaTable table = new LuaTable();
        for (Map.Entry<String, JsonElement> entry:
             object.entrySet()) {
            table.set(entry.getKey(), fromJsonElement(entry.getValue()));
        }
        return table;
    }

    private LuaTable fromJsonArray(JsonArray array) {
        LuaTable table = new LuaTable();
        for (int i = 0; i < array.size(); i++) {
            JsonElement element = array.get(i);
            if (element.isJsonNull()) continue;

            table.set(i+1, fromJsonElement(element));
        }
        return table;
    }

    private JsonElement toJsonElement(LuaValue value) {
        int valueType = value.type();
        return switch (valueType) {
            case LuaValue.TNIL -> JsonNull.INSTANCE;
            case LuaValue.TBOOLEAN -> new JsonPrimitive(value.toboolean());
            case LuaValue.TINT -> new JsonPrimitive(value.toint());
            case LuaValue.TNUMBER -> new JsonPrimitive(value.todouble());
            case LuaValue.TSTRING -> new JsonPrimitive(value.tojstring());
            case LuaValue.TTABLE,
                    LuaValue.TLIGHTUSERDATA,
                    LuaValue.TUSERDATA
                    -> toJsonElementFromLuaTable(value.checktable());
            default -> throw new LuaError("Unexpected type: " + valueType);
        };
    }

    private JsonElement toJsonElementFromLuaTable(LuaTable table) {
        LuaValue[] keys = table.keys();
        for (LuaValue key:
             keys) {
            boolean isNeededType = (key.type() == LuaValue.TINT || key.type() == LuaValue.TNUMBER);
            boolean isInt = key.todouble() % 1 == 0;
            if (!(isNeededType && isInt && key.toint() > 0)) {
                return toJsonObjectFromTable(table);
            }
        }
        return toJsonArrayFromTable(table);
    }

    private JsonObject toJsonObjectFromTable(LuaTable table) {
        JsonObject object = new JsonObject();
        LuaValue[] keys = table.keys();
        for (LuaValue key:
             keys) {
            LuaValue v = table.get(key);
            String k;
            if (key.type() == LuaValue.TSTRING) k = key.tojstring();
            else k = Double.toString(key.todouble());
            object.add(k, toJsonElement(v));
        }
        return object;
    }

    private JsonArray toJsonArrayFromTable(LuaTable table) {
        HashMap<Integer, JsonElement> _tempDict = new HashMap<>();
        int maxKey = 0;
        LuaValue[] keys = table.keys();
        for (LuaValue key:
             keys) {
            int k = key.toint();
            maxKey = Integer.max(maxKey, k);
            LuaValue v = table.get(key);
            _tempDict.put(k, toJsonElement(v));
        }
        JsonArray array = new JsonArray();
        for (int i = 0; i < maxKey; i++) {
            array.add(JsonNull.INSTANCE);
        }
        for (Map.Entry<Integer,JsonElement> element:
             _tempDict.entrySet()) {
            array.set(element.getKey()-1, element.getValue());
        }
        return array;
    }
}
