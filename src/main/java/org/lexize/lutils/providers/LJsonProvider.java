package org.lexize.lutils.providers;

import org.lexize.lutils.annotations.LDocsFuncOverload;
import org.lexize.lutils.annotations.LInclude;
import org.lexize.lutils.streams.LInputStream;
import org.lexize.lutils.streams.LJavaInputStream;
import org.lexize.lutils.submodules.json.LJsonSerializer;
import org.moon.figura.lua.LuaWhitelist;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;

@LuaWhitelist
public class LJsonProvider extends LProvider<Object> {
    private final LJsonSerializer serializer;
    private final Charset charset;
    public LJsonProvider(LJsonSerializer serializer, Charset charset) {
        this.serializer = serializer;
        this.charset = charset;
    }
    @LInclude
    @LDocsFuncOverload(
            argumentTypes = Object.class,
            argumentNames = "source",
            description = "Returns stream with bytes of provided object converted to JSON",
            returnType = LInputStream.class
    )
    public LInputStream getStream(Object source) {
        String json = serializer.serialize(source);
        ByteArrayInputStream bais = new ByteArrayInputStream(json.getBytes(charset));
        return new LJavaInputStream(bais);
    }
}
