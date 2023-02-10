package org.lexize.lutils.streams;

import org.lexize.lutils.annotations.LDescription;
import org.moon.figura.lua.LuaWhitelist;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class LInputStream extends InputStream {
    @LuaWhitelist
    public abstract int read() throws IOException;
    @LuaWhitelist
    public abstract long skip(long n) throws IOException;
    @LuaWhitelist
    public abstract int available() throws IOException;
    @LuaWhitelist
    public abstract void close() throws IOException;
    @LuaWhitelist
    public abstract void mark(int readlimit);
    @LuaWhitelist
    public abstract void reset() throws IOException;
    @LuaWhitelist
    public abstract boolean markSupported();
    @LuaWhitelist
    public long transferTo(OutputStream out) throws IOException {
        return super.transferTo(out);
    }
}
