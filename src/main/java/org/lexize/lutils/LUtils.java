package org.lexize.lutils;

import org.lexize.lutils.regex.LUtilsRegexGroup;
import org.lexize.lutils.regex.LUtilsRegexMatch;
import org.lexize.lutils.streams.LUtilsInputStream;
import org.lexize.lutils.streams.LUtilsOutputStream;
import org.lexize.lutils.submodules.LUtilsFile;
import org.lexize.lutils.submodules.LUtilsJson;
import org.lexize.lutils.submodules.LUtilsMisc;
import org.lexize.lutils.submodules.LUtilsRegex;
import org.moon.figura.avatars.Avatar;
import org.moon.figura.lua.FiguraAPI;
import org.moon.figura.lua.LuaWhitelist;

import java.util.Collection;
import java.util.List;

@LuaWhitelist
public class LUtils implements FiguraAPI {
    private Avatar _avatar;
    public static final String API_NAME = "lutils";

    @LuaWhitelist
    public LUtilsJson json = new LUtilsJson();
    @LuaWhitelist
    public LUtilsFile file;
    @LuaWhitelist
    public LUtilsRegex regex;
    @LuaWhitelist
    public LUtilsMisc misc = new LUtilsMisc();

    public LUtils() {

    }

    public LUtils(Avatar avatar) {
        _avatar = avatar;
        LUtilsFile f = null;
        if(_avatar.isHost) f = new LUtilsFile();
        file = f;
        regex = new LUtilsRegex(_avatar);
    }

    @LuaWhitelist
    public Object __index(String ind) {
        return switch(ind) {
          case "json" -> json;
          case "file" -> file;
          case "regex" -> regex;
          case "misc" -> misc;
          default -> null;
        };
    }


    @Override
    public FiguraAPI build(Avatar avatar) {
        return new LUtils(avatar);
    }

    @Override
    public String getName() {
        return API_NAME;
    }

    @Override
    public Collection<Class<?>> getWhitelistedClasses() {
        return List.of(
                LUtils.class,
                LUtilsJson.class,
                LUtilsFile.class,
                LUtilsInputStream.class,
                LUtilsOutputStream.class,
                LUtilsRegex.class,
                LUtilsRegexMatch.class,
                LUtilsRegexGroup.class,
                LUtilsMisc.class
        );
    }
}
