package org.lexize.lutils.readers;

import org.lexize.lutils.streams.LInputStream;
import org.lexize.lutils.streams.LJavaInputStream;
import org.luaj.vm2.LuaValue;
import org.moon.figura.lua.LuaWhitelist;

import java.io.InputStream;

public abstract class LReader<T> {
    public T readFrom(InputStream stream) {
        return readFrom(new LJavaInputStream(stream));
    }
    @LuaWhitelist
    public abstract T readFrom(LInputStream stream);
}
