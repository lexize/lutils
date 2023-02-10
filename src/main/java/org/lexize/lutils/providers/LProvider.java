package org.lexize.lutils.providers;

import org.lexize.lutils.annotations.LDocsFuncOverload;
import org.lexize.lutils.streams.LInputStream;
import org.moon.figura.lua.LuaWhitelist;

public abstract class LProvider<T> {
    @LuaWhitelist
    @LDocsFuncOverload(
            argumentTypes = Object.class,
            argumentNames = "source",
            description = "Returns stream with specified data",
            returnType = LInputStream.class
    )
    public abstract LInputStream getStream(T source);
}
