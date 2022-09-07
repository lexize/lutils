package org.lexize.lutils.regex;

import org.moon.figura.lua.LuaWhitelist;

import java.util.LinkedList;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;

@LuaWhitelist
public class LUtilsRegexGroup {
    public Integer _start;
    public Integer _end;
    public String _content;

    private LUtilsRegexGroup(String content, int start, int end) {
        this._content = content;
        this._start = start;
        this._end = end;
    }

    public static LUtilsRegexGroup[] RegexGroupsFromMatcher(Matcher matcher) {
        LinkedList<LUtilsRegexGroup> groups = new LinkedList<>();
        for (int i = 0; i < matcher.groupCount()+1; i++) {
            groups.add(new LUtilsRegexGroup(matcher.group(i), matcher.start(i), matcher.end(i)));
        }
        return groups.toArray(new LUtilsRegexGroup[groups.size()]);
    }

    public static LUtilsRegexGroup[] RegexGroupsFromMatchResult(MatchResult result) {
        LinkedList<LUtilsRegexGroup> groups = new LinkedList<>();
        for (int i = 0; i < result.groupCount()+1; i++) {
            groups.add(new LUtilsRegexGroup(result.group(i), result.start(i), result.end(i)));
        }
        return groups.toArray(new LUtilsRegexGroup[groups.size()]);
    }

    @LuaWhitelist
    public int start() {return _start;}
    @LuaWhitelist
    public int end() {return _end;}
    @LuaWhitelist
    public String content() {return _content;}

    @Override
    public String toString() {
        return _content;
    }
}
