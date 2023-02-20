package org.lexize.lutils.readers;

import org.lexize.lutils.streams.LInputStream;
import org.moon.figura.lua.LuaWhitelist;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@LuaWhitelist
public class LStringReader extends LReader<String> {
    public final Charset charset;
    public LStringReader() {
        this.charset = StandardCharsets.UTF_8;
    }
    public LStringReader(Charset charset) {
        this.charset = charset;
    }
    @LuaWhitelist
    @Override
    public String readFrom(InputStream stream) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            stream.transferTo(baos);
            byte[] bytes = baos.toByteArray();
            baos.close();
            return new String(bytes, charset);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
