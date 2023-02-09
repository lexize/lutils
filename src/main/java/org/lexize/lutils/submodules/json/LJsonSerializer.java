package org.lexize.lutils.submodules.json;

import com.google.gson.*;
import org.lexize.lutils.providers.LJsonProvider;
import org.lexize.lutils.readers.LJsonReader;
import org.lexize.lutils.submodules.json.converters.LJsonArrayConverter;
import org.lexize.lutils.submodules.json.converters.LJsonObjectConverter;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.moon.figura.lua.LuaWhitelist;

import java.util.HashMap;
import java.util.Map;

@LuaWhitelist
public class LJsonSerializer {
    private final Gson serializer;
    public LJsonSerializer(HashMap<String, ?> settings) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LJsonObject.class, new LJsonObjectConverter());
        builder.registerTypeAdapter(LJsonArray.class, new LJsonArrayConverter());
        for (Map.Entry<String, ?> kv :
                settings.entrySet()) {
            Object v = kv.getValue();
            switch (kv.getKey()) {
                case "prettyPrinting" -> {
                    if (v.equals(true)) builder.setPrettyPrinting();
                }
                case "htmlEscaping" -> {
                    if (v.equals(false)) builder.disableHtmlEscaping();
                }
                case "serializeNulls" -> {
                    if (v.equals(true)) builder.serializeNulls();
                }
            }
        }
        serializer = builder.create();
    }

    @LuaWhitelist
    public String serialize(Object val) {
        if (val instanceof LuaValue v) {
            return serializer.toJson(fromLuaValue(v));
        }
        return serializer.toJson(val);
    }

    @LuaWhitelist
    public Object deserialize(String json) {
        JsonElement element = JsonParser.parseString(json);
        if (element.isJsonNull()) return null;
        if (element.isJsonPrimitive()) {
            return fromJsonPrimitive(element.getAsJsonPrimitive());
        }
        Class<? extends LJsonValue<?>> c = element.isJsonObject() ? LJsonObject.class : LJsonArray.class;
        return serializer.fromJson(element, c);
    }

    @LuaWhitelist
    public LJsonProvider newProvider() {
        return new LJsonProvider(this);
    }
    @LuaWhitelist
    public LJsonReader newReader() {
        return new LJsonReader(this);
    }

    public static Object fromJsonPrimitive(JsonPrimitive primitive) {
        if(primitive.isString()) {
            return primitive.getAsString();
        } else if (primitive.isNumber()) {
            return primitive.getAsDouble();
        }
        return primitive.getAsBoolean();
    }
    private static Object fromLuaValue(LuaValue v) {
        if (v.istable()) return fromTable(v.checktable());
        else if (v.islong()) return v.tolong();
        else if (v.isnumber()) return v.todouble();
        else if (v.isboolean()) return v.toboolean();
        else if (v.isstring()) return v.tojstring();
        else if (v.isuserdata()) return v.touserdata();
        return null;
    }
    private static Object fromTable(LuaTable tbl) {
        boolean isArray = true;
        for (LuaValue k :
                tbl.keys()) {
            if (k.type() == LuaValue.TINT) {
                if (k.toint() > 0) continue;
                else {
                    isArray = false;
                }
            } else if (k.type() == LuaValue.TNUMBER) {
                float n = k.tofloat();
                if (n % 1 == 0 || n > 0) {
                    continue;
                }
                else {
                    isArray = false;
                }
            }
            else isArray = false;
            break;
        }
        if (isArray) {
            LJsonArray arr = new LJsonArray();
            for (int i = 1; i <= tbl.length(); i++) {
                arr.add(fromLuaValue(tbl.get(i)));
            }
            return arr;
        }
        else {
            LJsonObject obj = new LJsonObject();
            for (LuaValue k :
                    tbl.keys()) {
                obj.put(k.toString(), fromLuaValue(tbl.get(k)));
            }
            return obj;
        }
    }
}
