package org.lexize.lutils.streams;

import org.lexize.lutils.annotations.LInclude;
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
    @LInclude
    public int read() throws IOException {
        return inputStream.read();
    }

    @LuaWhitelist
    @LInclude
    public long skip(long n) throws IOException {
        return inputStream.skip(n);
    }

    @LuaWhitelist
    @LInclude
    public int available() throws IOException {
        return inputStream.available();
    }

    @LuaWhitelist
    @LInclude
    public void close() throws IOException {
        inputStream.close();
    }

    @LuaWhitelist
    @LInclude
    public void mark(int readlimit) {
        inputStream.mark(readlimit);
    }

    @LuaWhitelist
    @LInclude
    public void reset() throws IOException {
        inputStream.reset();
    }

    @LuaWhitelist
    @LInclude
    public boolean markSupported() {
        return inputStream.markSupported();
    }
}
