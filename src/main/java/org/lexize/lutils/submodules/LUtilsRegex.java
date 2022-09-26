package org.lexize.lutils.submodules;

import org.lexize.lutils.regex.LUtilsRegexMatch;
import org.luaj.vm2.LuaFunction;
import org.moon.figura.avatars.Avatar;
import org.moon.figura.lua.LuaWhitelist;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@LuaWhitelist
public class LUtilsRegex {

    private Avatar _avatar;

    public LUtilsRegex(Avatar avatar) {
        _avatar = avatar;
    }

    @LuaWhitelist
    public boolean isMatches(String pattern_string, String input) {
        return Pattern.matches(pattern_string, input);
    }

    @LuaWhitelist
    public Map<Integer, LUtilsRegexMatch> matches(String pattern_string, String input, Integer flags) {
        Pattern pattern;
        if (flags != null) pattern = Pattern.compile(pattern_string, flags);
        else pattern = Pattern.compile(pattern_string);
        LUtilsRegexMatch[] matches = LUtilsRegexMatch.MatchesByMatcher(pattern.matcher(input));
        HashMap<Integer, LUtilsRegexMatch> matchTable = new HashMap<>();
        for (LUtilsRegexMatch match:
                matches) {
            matchTable.put(matchTable.size()+1, match);
        }
        return matchTable;
    }

    @LuaWhitelist
    public String replace(String pattern_string, String input, Object replaceValue, Integer flags) {
        Pattern pattern;

        LuaFunction replaceFunction = null;
        String replaceString = null;

        if (replaceValue instanceof LuaFunction f) replaceFunction = f;
        if (replaceValue instanceof String f) replaceString = f;

        if (flags != null) pattern = Pattern.compile(pattern_string, flags);
        else pattern = Pattern.compile(pattern_string);

        Matcher m = pattern.matcher(input);

        if (replaceFunction != null) {
            LuaFunction finalReplaceFunction = replaceFunction;
            return m.replaceAll((mr) -> {

                var output = finalReplaceFunction.invoke(_avatar.luaRuntime.typeManager.javaToLua(new LUtilsRegexMatch(mr)));
                System.out.println(output.tojstring());
                return output.tojstring();
            });
        }
        else {
            return m.replaceAll(replaceString);
        }
    }
}
