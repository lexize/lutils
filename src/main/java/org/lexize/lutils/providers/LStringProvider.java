package org.lexize.lutils.providers;

import org.lexize.lutils.annotations.LDocsFuncOverload;
import org.lexize.lutils.annotations.LInclude;
import org.lexize.lutils.streams.LInputStream;
import org.lexize.lutils.streams.LJavaInputStream;
import org.moon.figura.lua.LuaWhitelist;

import java.io.ByteArrayInputStream;

@LuaWhitelist
public class LStringProvider extends LProvider<String> {
    @LInclude
    @LDocsFuncOverload(
            argumentTypes = String.class,
            argumentNames = "source",
            description = "Returns stream with bytes of provided string"
    )
    public LInputStream getStream(String source) {
        return new LJavaInputStream(new ByteArrayInputStream(source.getBytes()));
    }
}
