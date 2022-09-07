package org.lexize.lutils.streams;

import org.moon.figura.lua.LuaWhitelist;

import java.io.IOException;
import java.io.OutputStream;

@LuaWhitelist
public class LUtilsOutputStream <T extends OutputStream>{

    private T _outputStream;

    public LUtilsOutputStream(T stream) {
        _outputStream = stream;
    }

    @LuaWhitelist
    public void write(int i) throws IOException {
        _outputStream.write(i);
    }

    @LuaWhitelist
    public void close() throws IOException {
        _outputStream.close();
    }
}
