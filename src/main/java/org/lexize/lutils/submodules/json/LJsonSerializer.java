package org.lexize.lutils.submodules.json;

import com.google.gson.*;
import org.lexize.lutils.annotations.LDocsFuncOverload;
import org.lexize.lutils.annotations.LDocsFuncOverloads;
import org.lexize.lutils.providers.LJsonProvider;
import org.lexize.lutils.providers.LStringProvider;
import org.lexize.lutils.readers.LJsonReader;
import org.lexize.lutils.readers.LStringReader;
import org.lexize.lutils.submodules.LMisc;
import org.lexize.lutils.submodules.json.converters.LJsonArrayConverter;
import org.lexize.lutils.submodules.json.converters.LJsonObjectConverter;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.moon.figura.lua.LuaWhitelist;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@LuaWhitelist
public class LJsonSerializer {
    private final Gson serializer;
    public LJsonSerializer(HashMap<String, ?> settings) {
        GsonBuilder builder = new GsonBuilder();
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
        return fromjsonElement(element);
    }

    @LuaWhitelist
    @LDocsFuncOverloads(
            {
                    @LDocsFuncOverload(
                            returnType = LJsonProvider.class,
                            description = "Creates new JSON provider with UTF8 encoding"
                    ),
                    @LDocsFuncOverload(
                            argumentTypes = String.class,
                            argumentNames = "encoding",
                            returnType = LJsonProvider.class,
                            description = "Creates new JSON provider with specified encoding"
                    ),
                    @LDocsFuncOverload(
                            argumentTypes = LStringProvider.class,
                            argumentNames = "provider",
                            returnType = LJsonProvider.class,
                            description = "Creates new JSON provider with encoding taken from specified string provider"
                    )
            }
    )
    public LJsonProvider newProvider(Object v) {
        Charset chst = StandardCharsets.UTF_8;
        if (v != null) {
            if (v instanceof LStringProvider lsp) chst = lsp.charset;
            else if (v instanceof String enc) chst = LMisc.CharsetVal.valueOf(enc).charset;
        }
        return new LJsonProvider(this, chst);
    }
    @LuaWhitelist
    @LDocsFuncOverloads(
            {
                    @LDocsFuncOverload(
                            returnType = LJsonProvider.class,
                            description = "Creates new JSON reader with UTF8 encoding"
                    ),
                    @LDocsFuncOverload(
                            argumentTypes = String.class,
                            argumentNames = "encoding",
                            returnType = LJsonProvider.class,
                            description = "Creates new JSON reader with specified encoding"
                    ),
                    @LDocsFuncOverload(
                            argumentTypes = LStringReader.class,
                            argumentNames = "reader",
                            returnType = LJsonProvider.class,
                            description = "Creates new JSON reader with encoding taken from specified string reader"
                    )
            }
    )
    public LJsonReader newReader(Object v) {
        Charset chst = StandardCharsets.UTF_8;
        if (v != null) {
            if (v instanceof LStringReader lsp) chst = lsp.charset;
            else if (v instanceof String enc) chst = LMisc.CharsetVal.valueOf(enc).charset;
        }
        return new LJsonReader(this, chst);
    }
    public static Object fromjsonElement(JsonElement element) {
        if (element.isJsonNull()) return null;
        else if (element.isJsonPrimitive()) return fromJsonPrimitive(element.getAsJsonPrimitive());
        else if (element.isJsonArray()) {
            List<Object> l = new ArrayList<>();
            JsonArray jarr = element.getAsJsonArray();
            for (JsonElement elem :
                    jarr) {
                l.add(fromjsonElement(elem));
            }
            return l;
        }
        HashMap<String, Object> map = new HashMap<>();
        JsonObject jobj = element.getAsJsonObject();
        for (Map.Entry<String, JsonElement> kv:
                jobj.entrySet()){
            map.put(kv.getKey(), fromjsonElement(kv.getValue()));
        }
        return map;
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
            List<Object> l = new ArrayList<>(tbl.length());
            for (int i = 1; i <= tbl.length(); i++) {
                l.add(fromLuaValue(tbl.get(i)));
            }
            return l;
        }
        else {
            HashMap<String, Object> map = new HashMap<>();
            for (LuaValue k :
                    tbl.keys()) {
                map.put(k.toString(), fromLuaValue(tbl.get(k)));
            }
            return map;
        }
    }
}
