package org.lexize.lutils.submodules;

import org.lexize.lutils.annotations.LField;
import org.lexize.lutils.providers.LStreamProvider;
import org.lexize.lutils.providers.LStringProvider;
import org.luaj.vm2.LuaValue;
import org.moon.figura.lua.LuaWhitelist;

@LuaWhitelist
@LField(value = "string", type = LStringProvider.class)
@LField(value = "stream", type = LStreamProvider.class)
public class LProviders {
    public static final LProviders INSTANCE = new LProviders();
    public static final LStringProvider STRING_PROVIDER = new LStringProvider();
    public static final LStreamProvider STREAM_PROVIDER = new LStreamProvider();

    @LuaWhitelist
    public Object __index(LuaValue key) {
        if (!key.isstring()) return null;
        return switch (key.tojstring()) {
            case "string" -> STRING_PROVIDER;
            case "stream" -> STREAM_PROVIDER;
            default -> null;
        };
    }
}
