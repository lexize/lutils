package org.lexize.lutils.submodules;

import org.lexize.ldocs.annotations.LDocsDescription;
import org.lexize.ldocs.annotations.LDocsInclude;
import org.lexize.ldocs.annotations.LDocsProperty;
import org.lexize.lutils.submodules.regex.LUtilsRegexMatch;
import org.luaj.vm2.LuaFunction;
import org.moon.figura.avatar.Avatar;
import org.moon.figura.lua.LuaWhitelist;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@LuaWhitelist
@LDocsDescription("Regex submodule of LUtils")
public class LUtilsRegex {

    private Avatar _avatar;

    public LUtilsRegex(Avatar avatar) {
        _avatar = avatar;
    }

    @LuaWhitelist
    @LDocsInclude
    @LDocsDescription("Check, is input string matches given pattern")
    public boolean isMatches(String pattern_string, String input) {
        return Pattern.matches(pattern_string, input);
    }

    @LuaWhitelist
    @LDocsInclude
    @LDocsDescription("Returns table with matches in input string")
    public List<LUtilsRegexMatch> matches(String pattern_string, String input,
                                                  @LDocsProperty(name = "check", classValue = LUtilsMisc.class)
                                                  Integer flags) {
        Pattern pattern;
        if (flags != null) pattern = Pattern.compile(pattern_string, flags);
        else pattern = Pattern.compile(pattern_string);
        LUtilsRegexMatch[] matches = LUtilsRegexMatch.MatchesByMatcher(pattern.matcher(input));
        return Arrays.stream(matches).toList();
    }

    @LuaWhitelist
    @LDocsInclude
    @LDocsDescription("Replaces matched regions with replace string/return value of replace function")
    public String replace(String pattern_string, String input, Object replaceValue,
                          @LDocsProperty(name = "check", classValue = LUtilsMisc.class)
                          Integer flags) {
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

    @LuaWhitelist
    @LDocsInclude
    @LDocsDescription("Splits string by given pattern")
    public List<String> split(String pattern_string, String input) {
        return Arrays.stream(input.split(pattern_string)).toList();
    }
}
