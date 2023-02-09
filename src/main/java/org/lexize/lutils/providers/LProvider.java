package org.lexize.lutils.providers;

import org.lexize.lutils.streams.LInputStream;
import org.lexize.lutils.streams.LJavaInputStream;
import org.moon.figura.lua.LuaWhitelist;

import java.io.InputStream;

public abstract class LProvider<T> {
    @LuaWhitelist
    public abstract LInputStream getStream(T source);
}
