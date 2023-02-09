package org.lexize.lutils.providers;

import org.lexize.lutils.streams.LInputStream;
import org.moon.figura.lua.LuaWhitelist;

@LuaWhitelist
public class LStreamProvider extends LProvider<LInputStream> {
    public LInputStream getStream(LInputStream source) {
        return source;
    }
}
