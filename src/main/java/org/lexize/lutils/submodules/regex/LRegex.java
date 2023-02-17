package org.lexize.lutils.submodules.regex;

import org.lexize.lutils.annotations.LDescription;
import org.lexize.lutils.annotations.LDocsFuncOverload;
import org.lexize.lutils.annotations.LDocsFuncOverloads;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.moon.figura.lua.LuaWhitelist;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@LuaWhitelist
@LDescription("Submodule for working with regular expressions")
public class LRegex {
    @LuaWhitelist
    @LDocsFuncOverload(
            argumentNames = {"string", "pattern"},
            argumentTypes = {String.class, String.class},
            returnType = boolean.class,
            description = "Checks, is provided string matches specified pattern"
    )
    public boolean isMatches(String string, String pattern) {
        return string.matches(pattern);
    }

    @LuaWhitelist
    @LDocsFuncOverload(
            argumentNames = {"string", "pattern"},
            argumentTypes = {String.class, String.class},
            returnType = LRegexMatch[].class,
            description = "Returns all pattern matches in specified string"
    )
    public List<LRegexMatch> matches(String string, String pattern) {
        ArrayList<LRegexMatch> regexMatches = new ArrayList<>();
        Pattern p = Pattern.compile(pattern);
        var matcher = p.matcher(string);
        while (matcher.find()) {
            regexMatches.add(new LRegexMatch(matcher));
        }
        return regexMatches;
    }

    @LuaWhitelist
    @LDocsFuncOverloads(
            {
                    @LDocsFuncOverload(
                            argumentNames = {"string", "pattern", "content"},
                            argumentTypes = {String.class, String.class, String.class},
                            returnType = String.class,
                            description = "Replaces all matches in string with specified content, and returns result"
                    ),
                    @LDocsFuncOverload(
                            argumentNames = {"string", "pattern", "function"},
                            argumentTypes = {String.class, String.class, LuaFunction.class},
                            returnType = String.class,
                            description = "Replaces all matches in string with function return value, and returns result. Function accepts match content, start index, end index, and match groups"
                    )
            }
    )
    public String replaceAll(String string, String pattern, Object content) {
        Pattern p = Pattern.compile(pattern);
        var matcher = p.matcher(string);
        if (content instanceof String c) {
            return matcher.replaceAll(c);
        }
        LuaFunction f = (LuaFunction) content;
        return matcher.replaceAll((m) -> {
            LuaTable groups = new LuaTable();
            for (int i = 1; i <= m.groupCount(); i++) {
                LuaTable groupTable = new LuaTable();
                groupTable.set("start", m.start(i));
                groupTable.set("end", m.end(i));
                groupTable.set("content", m.group(i));
                groups.add(groupTable);
            }
            var r = f.invoke(new LuaValue[] {LuaValue.valueOf(m.group()), LuaValue.valueOf(m.start()), LuaValue.valueOf(m.end()), groups});
            return r.tojstring();
        });
    }
    @LuaWhitelist
    @LDocsFuncOverloads(
            {
                    @LDocsFuncOverload(
                            argumentNames = {"string", "pattern", "content"},
                            argumentTypes = {String.class, String.class, String.class},
                            returnType = String.class,
                            description = "Replaces first match in string with specified content, and returns result"
                    ),
                    @LDocsFuncOverload(
                            argumentNames = {"string", "pattern", "function"},
                            argumentTypes = {String.class, String.class, LuaFunction.class},
                            returnType = String.class,
                            description = "Replaces first match in string with function return value, and returns result. Function accepts match content, start index, end index, and match groups"
                    )
            }
    )
    public String replaceFirst(String string, String pattern, Object content) {
        Pattern p = Pattern.compile(pattern);
        var matcher = p.matcher(string);
        if (content instanceof String c) {
            return matcher.replaceFirst(c);
        }
        LuaFunction f = (LuaFunction) content;
        return matcher.replaceFirst((m) -> {
            LuaTable groups = new LuaTable();
            for (int i = 1; i <= m.groupCount(); i++) {
                LuaTable groupTable = new LuaTable();
                groupTable.set("start", m.start(i));
                groupTable.set("end", m.end(i));
                groupTable.set("content", m.group(i));
                groups.add(groupTable);
            }
            var r = f.invoke(new LuaValue[] {LuaValue.valueOf(m.group()), LuaValue.valueOf(m.start()), LuaValue.valueOf(m.end()), groups});
            return r.tojstring();
        });
    }
}
