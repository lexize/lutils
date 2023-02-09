package org.lexize.lutils.streams;

import org.moon.figura.lua.LuaWhitelist;

import java.io.IOException;
import java.io.InputStream;

@LuaWhitelist
public class LJavaInputStream extends LInputStream {
    private final InputStream inputStream;

    public InputStream getInputStream() {
        return inputStream;
    }

    public LJavaInputStream(InputStream stream) {
        inputStream = stream;
    }

    @LuaWhitelist
    public int read() throws IOException {
        return inputStream.read();
    }

    @LuaWhitelist
    public long skip(long n) throws IOException {
        return inputStream.skip(n);
    }

    @LuaWhitelist
    public int available() throws IOException {
        return inputStream.available();
    }

    @LuaWhitelist
    public void close() throws IOException {
        inputStream.close();
    }

    @LuaWhitelist
    public void mark(int readlimit) {
        inputStream.mark(readlimit);
    }

    @LuaWhitelist
    public void reset() throws IOException {
        inputStream.reset();
    }

    @LuaWhitelist
    public boolean markSupported() {
        return inputStream.markSupported();
    }
}
