package org.lexize.lutils.providers;

import org.lexize.lutils.annotations.LDocsFuncOverload;
import org.lexize.lutils.annotations.LInclude;
import org.lexize.lutils.streams.LInputStream;
import org.lexize.lutils.streams.LJavaInputStream;
import org.moon.figura.lua.LuaWhitelist;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@LuaWhitelist
public class LStringProvider extends LProvider<String> {
    public final Charset charset;

    public LStringProvider() {
        this.charset = StandardCharsets.UTF_8;
    }
    public LStringProvider(Charset charset) {
        this.charset = charset;
    }

    @LInclude
    @LDocsFuncOverload(
            argumentTypes = String.class,
            argumentNames = "source",
            description = "Returns stream with bytes of provided string",
            returnType = LInputStream.class
    )
    public LInputStream getStream(String source) {
        return new LJavaInputStream(new ByteArrayInputStream(source.getBytes(charset)));
    }
}
