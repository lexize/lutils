package org.lexize.lutils.readers;

import com.google.gson.Gson;
import org.lexize.lutils.streams.LInputStream;
import org.lexize.lutils.submodules.json.LJsonSerializer;
import org.moon.figura.lua.LuaWhitelist;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

@LuaWhitelist
public class LJsonReader extends LReader<Object> {
    private final LJsonSerializer serializer;
    private final Charset charset;
    public LJsonReader(LJsonSerializer serializer, Charset charset) {
        this.charset = charset;
        this.serializer = serializer;
    }
    @Override
    public Object readFrom(InputStream stream) {
        try {
            byte[] jsonStringBytes = stream.readAllBytes();
            String json = new String(jsonStringBytes, charset);
            return serializer.deserialize(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
