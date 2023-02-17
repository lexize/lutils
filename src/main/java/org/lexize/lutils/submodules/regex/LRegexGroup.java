package org.lexize.lutils.submodules.regex;

import org.lexize.lutils.annotations.LField;
import org.luaj.vm2.LuaValue;
import org.moon.figura.lua.LuaWhitelist;

@LuaWhitelist
@LField(value = "start", type = Integer.class)
@LField(value = "end", type = Integer.class)
@LField(value = "content", type = String.class)
public class LRegexGroup {
    private final String content;
    private final int start;
    private final int end;

    public LRegexGroup(String content, int start, int end) {
        this.content = content;
        this.start = start;
        this.end = end;
    }

    @LuaWhitelist
    public Object __index(LuaValue k) {
        if (!k.isstring()) return null;
        return switch (k.tojstring()) {
            case "content" -> content;
            case "start" -> start;
            case "end" -> end;
            default -> null;
        };
    }
}
