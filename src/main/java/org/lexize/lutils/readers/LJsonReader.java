package org.lexize.lutils.readers;

import com.google.gson.Gson;
import org.lexize.lutils.streams.LInputStream;
import org.lexize.lutils.submodules.json.LJsonSerializer;
import org.moon.figura.lua.LuaWhitelist;

import java.io.IOException;
import java.io.InputStream;

@LuaWhitelist
public class LJsonReader extends LReader<Object> {
    private final LJsonSerializer serializer;
    public LJsonReader(LJsonSerializer serializer) {
        this.serializer = serializer;
    }
    @Override
    public Object readFrom(InputStream stream) {
        try {
            byte[] jsonStringBytes = stream.readAllBytes();
            String json = new String(jsonStringBytes);
            return serializer.deserialize(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
