package org.lexize.lutils.streams;

import org.moon.figura.lua.LuaNotNil;
import org.moon.figura.lua.LuaWhitelist;

import java.io.IOException;
import java.io.InputStream;

@LuaWhitelist
public class LUtilsInputStream <T extends InputStream> {
    private T _inputStream;

    public LUtilsInputStream(T stream) {
        _inputStream = stream;
    }

    @LuaWhitelist
    public int read() throws IOException {
        return _inputStream.read();
    }

    @LuaWhitelist
    public long skip(@LuaNotNil long n) throws IOException {
        return _inputStream.skip(n);
    }

    @LuaWhitelist
    public void reset() throws IOException {
        _inputStream.reset();
    }

    @LuaWhitelist
    public void close() throws IOException {
        _inputStream.close();
    }

}
