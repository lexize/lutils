package org.lexize.lutils.submodules.regex;

import org.lexize.lutils.annotations.LField;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.moon.figura.lua.LuaWhitelist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;

@LuaWhitelist
@LField(value = "start", type = Integer.class)
@LField(value = "end", type = Integer.class)
@LField(value = "content", type = String.class)
@LField(value = "groups", type = LRegexGroup[].class)
public class LRegexMatch {
    private final String content;
    private final int start;
    private final int end;
    private final List<LRegexGroup> groups;
    public LRegexMatch(Matcher m) {
        content = m.group();
        start = m.start();
        end = m.end();
        ArrayList<LRegexGroup> groups = new ArrayList<>();
        for (int i = 1; i <= m.groupCount(); i++) {
            groups.add(new LRegexGroup(m.group(i), m.start(i), m.end(i)));
        }
        this.groups = groups;
    }

    @LuaWhitelist
    public Object __index(LuaValue k) {
        if (!k.isstring()) return null;
        return switch (k.tojstring()) {
            case "content" -> content;
            case "start" -> start;
            case "end" -> end;
            case "groups" -> groups;
            default -> null;
        };
    }
}
