package org.lexize.lutils.providers;

import org.lexize.lutils.streams.LInputStream;
import org.lexize.lutils.streams.LJavaInputStream;
import org.moon.figura.lua.LuaWhitelist;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@LuaWhitelist
public class LStringProvider extends LProvider<String> {
    @LuaWhitelist
    @Override
    public LInputStream getStream(String source) {
        return new LJavaInputStream(new ByteArrayInputStream(source.getBytes()));
    }
}
