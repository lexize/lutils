package org.lexize.lutils.readers;

import org.lexize.lutils.streams.LInputStream;
import org.moon.figura.lua.LuaWhitelist;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@LuaWhitelist
public class LStringReader extends LReader<String> {
    @LuaWhitelist
    @Override
    public String readFrom(InputStream stream) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            stream.transferTo(baos);
            byte[] bytes = baos.toByteArray();
            baos.close();
            return new String(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
