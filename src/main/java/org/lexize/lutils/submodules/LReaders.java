package org.lexize.lutils.submodules;

import org.lexize.lutils.annotations.LField;
import org.lexize.lutils.readers.LStringReader;
import org.lexize.lutils.readers.LReader;
import org.lexize.lutils.readers.LStringReader;
import org.luaj.vm2.LuaValue;
import org.moon.figura.lua.LuaWhitelist;

import java.nio.charset.StandardCharsets;

@LuaWhitelist
@LField(value = "string", type = LStringReader.class)
@LField(value = "string_utf8", type = LStringReader.class)
@LField(value = "string_utf16", type = LStringReader.class)
@LField(value = "string_utf16be", type = LStringReader.class)
@LField(value = "string_utf16le", type = LStringReader.class)
@LField(value = "string_iso88591", type = LStringReader.class)
@LField(value = "string_us_ascii", type = LStringReader.class)
public class LReaders {
    public static final LReaders INSTANCE = new LReaders();
    public static final LReader<String> STRING_READER = new LStringReader();
    public static final LReader<String> STRING_READER_UTF_16 = new LStringReader(StandardCharsets.UTF_16);
    public static final LReader<String> STRING_READER_UTF_16LE = new LStringReader(StandardCharsets.UTF_16LE);
    public static final LReader<String> STRING_READER_UTF_16BE = new LStringReader(StandardCharsets.UTF_16BE);
    public static final LReader<String> STRING_READER_ISO_8859_1 = new LStringReader(StandardCharsets.ISO_8859_1);
    public static final LReader<String> STRING_READER_US_ASCII = new LStringReader(StandardCharsets.US_ASCII);

    @LuaWhitelist
    public Object __index(LuaValue key) {
        if (!key.isstring()) return null;
        return switch (key.tojstring()) {
            case "string", "string_utf8" -> STRING_READER;
            case "string_utf16" -> STRING_READER_UTF_16;
            case "string_utf16le" -> STRING_READER_UTF_16LE;
            case "string_utf16be" -> STRING_READER_UTF_16BE;
            case "string_iso88591" -> STRING_READER_ISO_8859_1;
            case "string_us_ascii" -> STRING_READER_US_ASCII;
            default -> null;
        };
    }
}
