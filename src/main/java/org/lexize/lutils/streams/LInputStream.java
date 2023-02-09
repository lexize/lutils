package org.lexize.lutils.streams;

import org.moon.figura.lua.LuaWhitelist;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class LInputStream extends InputStream {
    public abstract int read() throws IOException;
    public byte[] readAll() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i;
        while ((i = read()) != -1) {
            baos.write(i);
        }
        byte[] bytes = baos.toByteArray();
        baos.close();
        return bytes;
    }
    public abstract long skip(long n) throws IOException;
    public abstract int available() throws IOException;
    public abstract void close() throws IOException;
    public abstract void mark(int readlimit);
    public abstract void reset() throws IOException;
    public abstract boolean markSupported();
    @LuaWhitelist
    public long transferTo(LOutputStream out) throws IOException {
        long bytesRead = 0;
        int i;
        while ((i = read()) != -1) {
            out.write(i);
            bytesRead++;
        }
        return bytesRead;
    }
    public long transferTo(OutputStream out) throws IOException {
        return transferTo(new LJavaOutputStream(out));
    }
}
