package org.lexize.lutils.submodules.regex;

import org.lexize.lutils.annotations.LField;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.moon.figura.lua.LuaWhitelist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final HashMap<Object,LRegexGroup> groups;
    public LRegexMatch(Matcher m) {
        var groupNames = new HashMap<Integer, String>();
        for (Map.Entry<String, Integer> kv :
                LRegex.getGroupNames(m).entrySet()) {
            groupNames.put(kv.getValue(), kv.getKey());
        }
        content = m.group();
        start = m.start();
        end = m.end();
        HashMap<Object,LRegexGroup> groups = new HashMap<Object,LRegexGroup>();
        for (int i = 1; i <= m.groupCount(); i++) {
            var g = new LRegexGroup(m.group(i), m.start(i), m.end(i));
            groups.put(i,g);
            if (groupNames.containsKey(i)) groups.put(groupNames.get(i), g);
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
