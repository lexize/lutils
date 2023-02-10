package org.lexize.lutils.readers;

import org.lexize.lutils.streams.LInputStream;
import org.lexize.lutils.streams.LJavaInputStream;
import org.luaj.vm2.LuaValue;
import org.moon.figura.lua.LuaWhitelist;

import java.io.InputStream;

public abstract class LReader<T> {
    @LuaWhitelist
    public abstract T readFrom(InputStream stream);
}
