package org.lexize.lutils.submodules;

import org.lexize.lutils.annotations.LField;
import org.lexize.lutils.providers.LStreamProvider;
import org.lexize.lutils.providers.LStringProvider;
import org.luaj.vm2.LuaValue;
import org.moon.figura.lua.LuaWhitelist;

import java.nio.charset.StandardCharsets;

@LuaWhitelist
@LField(value = "string", type = LStringProvider.class)
@LField(value = "string_utf8", type = LStringProvider.class)
@LField(value = "string_utf16", type = LStringProvider.class)
@LField(value = "string_utf16be", type = LStringProvider.class)
@LField(value = "string_utf16le", type = LStringProvider.class)
@LField(value = "string_iso88591", type = LStringProvider.class)
@LField(value = "string_us_ascii", type = LStringProvider.class)
@LField(value = "stream", type = LStreamProvider.class)
public class LProviders {
    public static final LProviders INSTANCE = new LProviders();
    public static final LStringProvider STRING_PROVIDER = new LStringProvider();
    public static final LStringProvider STRING_PROVIDER_UTF_16 = new LStringProvider(StandardCharsets.UTF_16);
    public static final LStringProvider STRING_PROVIDER_UTF_16BE = new LStringProvider(StandardCharsets.UTF_16BE);
    public static final LStringProvider STRING_PROVIDER_UTF_16LE = new LStringProvider(StandardCharsets.UTF_16LE);
    public static final LStringProvider STRING_PROVIDER_ISO_8859_1 = new LStringProvider(StandardCharsets.ISO_8859_1);
    public static final LStringProvider STRING_PROVIDER_US_ASCII = new LStringProvider(StandardCharsets.US_ASCII);
    public static final LStreamProvider STREAM_PROVIDER = new LStreamProvider();

    @LuaWhitelist
    public Object __index(LuaValue key) {
        if (!key.isstring()) return null;
        return switch (key.tojstring()) {
            case "string", "string_utf8" -> STRING_PROVIDER;
            case "string_utf16" -> STRING_PROVIDER_UTF_16;
            case "string_utf16be" -> STRING_PROVIDER_UTF_16BE;
            case "string_utf16le" -> STRING_PROVIDER_UTF_16LE;
            case "string_iso88591" -> STRING_PROVIDER_ISO_8859_1;
            case "string_us_ascii" -> STRING_PROVIDER_US_ASCII;
            case "stream" -> STREAM_PROVIDER;
            default -> null;
        };
    }
}
