package org.lexize.lutils.streams;

import org.moon.figura.lua.LuaWhitelist;

import java.io.IOException;
import java.io.OutputStream;

@LuaWhitelist
public class LJavaOutputStream extends LOutputStream {
    private final OutputStream outputStream;

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public LJavaOutputStream(OutputStream stream) {
        outputStream = stream;
    }

    public void write(int i) throws IOException {
        outputStream.write(i);
    }

    public void flush() throws IOException {
        outputStream.flush();
    }

    public void close() throws IOException {
        outputStream.close();
    }
}
