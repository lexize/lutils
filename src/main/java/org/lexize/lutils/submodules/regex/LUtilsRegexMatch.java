package org.lexize.lutils.submodules.regex;

import org.moon.figura.lua.LuaWhitelist;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;

@LuaWhitelist
public class LUtilsRegexMatch {
    private Integer _start;
    private Integer _end;
    private String _content;
    private Map<Integer, LUtilsRegexGroup> _groups;

    private LUtilsRegexMatch(Matcher parent) {
        _start = parent.start();
        _end = parent.end();
        _content = parent.group();
        HashMap<Integer, LUtilsRegexGroup> groupTable = new HashMap<>();
        for (LUtilsRegexGroup group:
                LUtilsRegexGroup.RegexGroupsFromMatcher(parent)) {
            groupTable.put(groupTable.size(), group);
        }
        _groups = groupTable;
    }

    public LUtilsRegexMatch(MatchResult result) {
        _start = result.start();
        _end = result.end();
        _content = result.group();
        HashMap<Integer, LUtilsRegexGroup> groupTable = new HashMap<>();
        for (LUtilsRegexGroup group:
                LUtilsRegexGroup.RegexGroupsFromMatchResult(result)) {
            groupTable.put(groupTable.size(), group);
        }
        _groups = groupTable;
    }

    public static LUtilsRegexMatch[] MatchesByMatcher(Matcher matcher) {
        LinkedList<LUtilsRegexMatch> matches = new LinkedList<>();
        while (matcher.find()) {
            matches.add(new LUtilsRegexMatch(matcher));
        }
        return matches.toArray(new LUtilsRegexMatch[0]);
    }


    @LuaWhitelist
    public int start() {return _start;}
    @LuaWhitelist
    public int end() {return _end;}
    @LuaWhitelist
    public String content() {return _content;}
    @LuaWhitelist
    public Map<Integer, LUtilsRegexGroup> groups() {return _groups;}

    @Override
    public String toString() {
        return _content;
    }
}