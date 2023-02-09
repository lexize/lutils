package org.lexize.lutils.submodules.json.converters;

import com.google.gson.*;
import org.lexize.lutils.submodules.json.LJsonArray;
import org.lexize.lutils.submodules.json.LJsonObject;
import org.lexize.lutils.submodules.json.LJsonSerializer;
import org.lexize.lutils.submodules.json.LJsonValue;

import java.lang.reflect.Type;

public class LJsonArrayConverter implements JsonSerializer<LJsonArray>, JsonDeserializer<LJsonArray> {
    @Override
    public LJsonArray deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if (!jsonElement.isJsonArray()) return null;
        JsonArray jarray = jsonElement.getAsJsonArray();
        LJsonArray array = new LJsonArray();
        for (JsonElement elem :
                jarray) {
            if (elem.isJsonNull()) {
                array.getData().add(null);
                continue;
            }
            if (elem.isJsonPrimitive()) {
                array.getData().add(LJsonSerializer.fromJsonPrimitive(elem.getAsJsonPrimitive()));
                continue;
            }
            Class<? extends LJsonValue<?>> c = elem.isJsonObject() ? LJsonObject.class : LJsonArray.class;
            array.getData().add(jsonDeserializationContext.deserialize(elem, c));
        }
        return array;
    }

    @Override
    public JsonElement serialize(LJsonArray lJsonArray, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray array = new JsonArray();
        for (Object v :
                lJsonArray.getData()) {
            JsonElement e = jsonSerializationContext.serialize(v);
            array.add(e);
        }
        return array;
    }
}
