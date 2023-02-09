package org.lexize.lutils.streams;

import org.moon.figura.lua.LuaWhitelist;

import java.io.IOException;
import java.io.OutputStream;

public abstract class LOutputStream extends OutputStream {
    @LuaWhitelist
    public abstract void write(int i) throws IOException;
    @LuaWhitelist
    public abstract void flush() throws IOException;
    @LuaWhitelist
    public abstract void close() throws IOException;
}
