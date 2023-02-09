package org.lexize.lutils.submodules.json.converters;

import com.google.gson.*;
import org.lexize.lutils.submodules.json.LJsonArray;
import org.lexize.lutils.submodules.json.LJsonObject;
import org.lexize.lutils.submodules.json.LJsonSerializer;
import org.lexize.lutils.submodules.json.LJsonValue;

import java.lang.reflect.Type;
import java.util.Map;

public class LJsonObjectConverter implements JsonSerializer<LJsonObject>, JsonDeserializer<LJsonObject> {
    @Override
    public LJsonObject deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if (!jsonElement.isJsonObject()) return null;
        LJsonObject object = new LJsonObject();
        JsonObject jobject = jsonElement.getAsJsonObject();
        var d = object.getData();
        for (Map.Entry<String, JsonElement> kv:
                jobject.entrySet()) {
            String k = kv.getKey();
            JsonElement elem = kv.getValue();
            if (elem.isJsonNull()) {
                d.put(k,null);
                continue;
            }
            if (elem.isJsonPrimitive()) {
                d.put(k, LJsonSerializer.fromJsonPrimitive(elem.getAsJsonPrimitive()));
                continue;
            }
            Class<? extends LJsonValue<?>> c = elem.isJsonObject() ? LJsonObject.class : LJsonArray.class;
            d.put(k,jsonDeserializationContext.deserialize(elem, c));
        }
        return object;
    }

    @Override
    public JsonElement serialize(LJsonObject lJsonObject, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jobject = new JsonObject();
        for (Map.Entry<String, ?> kv :
                lJsonObject.getData().entrySet()) {
            jobject.add(kv.getKey(), jsonSerializationContext.serialize(kv.getValue()));
        }
        return jobject;
    }
}
