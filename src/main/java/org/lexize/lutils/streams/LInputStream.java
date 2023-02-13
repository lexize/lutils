package org.lexize.lutils.streams;

import org.lexize.lutils.annotations.LDescription;
import org.lexize.lutils.annotations.LDocsFuncOverload;
import org.moon.figura.lua.LuaWhitelist;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class LInputStream extends InputStream {
    @LuaWhitelist
    @LDescription("Reads byte from stream")
    public abstract int read() throws IOException;
    @LuaWhitelist
    @LDescription("Skips specified amount of bytes in stream, and returns how much bytes was actually skipped")
    public abstract long skip(long n) throws IOException;
    @LuaWhitelist
    @LDescription("Returns amount of bytes available")
    public abstract int available() throws IOException;
    @LuaWhitelist
    @LDescription("Closes this stream")
    public abstract void close() throws IOException;
    @LuaWhitelist
    @LDocsFuncOverload(
            description = "Marks current position in this input stream. You can get back to it with reset()",
            argumentTypes = int.class,
            argumentNames = "readlimit"
    )
    public abstract void mark(int readlimit);
    @LuaWhitelist
    @LDescription("Resets stream position to mark")
    public abstract void reset() throws IOException;
    @LuaWhitelist
    @LDescription("Is mark supported for this stream")
    public abstract boolean markSupported();
    @LuaWhitelist
    @LDescription("Transfers remaining data from this stream to specified output stream")
    public long transferTo(OutputStream out) throws IOException {
        return super.transferTo(out);
    }
}
