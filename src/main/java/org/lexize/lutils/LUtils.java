package org.lexize.lutils;

import org.lexize.lutils.misc.LFuture;
import org.lexize.lutils.providers.LJsonProvider;
import org.lexize.lutils.providers.LStreamProvider;
import org.lexize.lutils.providers.LStringProvider;
import org.lexize.lutils.readers.LJsonReader;
import org.lexize.lutils.readers.LStringReader;
import org.lexize.lutils.streams.LJavaInputStream;
import org.lexize.lutils.streams.LJavaOutputStream;
import org.lexize.lutils.streams.LLuaInputStream;
import org.lexize.lutils.streams.LLuaOutputStream;
import org.lexize.lutils.submodules.http.LHttp;
import org.lexize.lutils.submodules.http.LHttpResponse;
import org.lexize.lutils.submodules.json.LJson;
import org.lexize.lutils.submodules.json.LJsonArray;
import org.lexize.lutils.submodules.json.LJsonObject;
import org.lexize.lutils.submodules.json.LJsonSerializer;
import org.moon.figura.avatar.Avatar;
import org.moon.figura.lua.FiguraAPI;
import org.moon.figura.lua.LuaWhitelist;

import java.util.Collection;
import java.util.List;

@LuaWhitelist
public class LUtils implements FiguraAPI {
    private final LJson json = new LJson();;
    private final LHttp http = new LHttp();
    public LUtils() {}
    public LUtils(Avatar avatar) {

    }
    @Override
    public FiguraAPI build(Avatar avatar) {
        return new LUtils(avatar);
    }

    @LuaWhitelist
    public Object __index(String key) {
        return switch (key) {
            case "json" -> json;
            case "http" -> http;
            default -> null;
        };
    }

    @Override
    public String getName() {
        return "lutils";
    }

    @Override
    public Collection<Class<?>> getWhitelistedClasses() {
        return List.of(
                LUtils.class,

                LJson.class,
                LJsonArray.class,
                LJsonObject.class,
                LJsonSerializer.class,

                LHttp.class,
                LHttpResponse.class,

                LJavaInputStream.class,
                LJavaOutputStream.class,
                LLuaInputStream.class,
                LLuaOutputStream.class,

                LStringReader.class,
                LJsonReader.class,

                LStringProvider.class,
                LStreamProvider.class,
                LJsonProvider.class,

                LFuture.class
        );
    }
}
