package org.lexize.lutils.submodules;

import org.lexize.lutils.annotations.LField;
import org.lexize.lutils.readers.LReader;
import org.lexize.lutils.readers.LStringReader;
import org.luaj.vm2.LuaValue;
import org.moon.figura.lua.LuaWhitelist;

@LuaWhitelist
@LField(value = "string", type = LStringReader.class)
public class LReaders {
    public static final LReaders INSTANCE = new LReaders();
    public static final LReader<String> STRING_READER = new LStringReader();

    @LuaWhitelist
    public Object __index(LuaValue key) {
        if (!key.isstring()) return null;
        return switch (key.tojstring()) {
            case "string" -> STRING_READER;
            //case "stream" -> STREAM_PROVIDER;
            default -> null;
        };
    }
}
