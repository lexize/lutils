package org.lexize.lutils.providers;

import org.lexize.lutils.annotations.LDocsFuncOverload;
import org.lexize.lutils.annotations.LInclude;
import org.lexize.lutils.streams.LInputStream;
import org.moon.figura.lua.LuaWhitelist;

@LuaWhitelist
public class LStreamProvider extends LProvider<LInputStream> {
    @LInclude
    @LDocsFuncOverload(
            argumentTypes = LInputStream.class,
            argumentNames = "source",
            description = "Provides specified stream as data stream",
            returnType = LInputStream.class
    )
    public LInputStream getStream(LInputStream source) {
        return source;
    }
}
