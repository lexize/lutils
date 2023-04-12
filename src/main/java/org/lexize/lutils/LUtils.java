package org.lexize.lutils;

import org.lexize.lutils.annotations.LDescription;
import org.lexize.lutils.annotations.LField;
import org.lexize.lutils.misc.LFuture;
import org.lexize.lutils.providers.LJsonProvider;
import org.lexize.lutils.providers.LProvider;
import org.lexize.lutils.providers.LStreamProvider;
import org.lexize.lutils.providers.LStringProvider;
import org.lexize.lutils.readers.LJsonReader;
import org.lexize.lutils.readers.LReader;
import org.lexize.lutils.readers.LStringReader;
import org.lexize.lutils.streams.*;
import org.lexize.lutils.submodules.LFile;
import org.lexize.lutils.submodules.LMisc;
import org.lexize.lutils.submodules.LProviders;
import org.lexize.lutils.submodules.LReaders;
import org.lexize.lutils.submodules.http.LHttp;
import org.lexize.lutils.submodules.http.LHttpResponse;
import org.lexize.lutils.submodules.json.LJson;
import org.lexize.lutils.submodules.json.LJsonSerializer;
import org.lexize.lutils.submodules.regex.LRegex;
import org.lexize.lutils.submodules.regex.LRegexGroup;
import org.lexize.lutils.submodules.regex.LRegexMatch;
import org.lexize.lutils.submodules.socket.LSocket;
import org.lexize.lutils.submodules.socket.LSocketClient;
import org.moon.figura.avatar.Avatar;
import org.moon.figura.entries.FiguraAPI;
import org.moon.figura.lua.LuaWhitelist;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


@LuaWhitelist
@LDescription("Main class of LUtils. Provides access to all submodules")
@LField(value = "json", type = LJson.class, description = "JSON submodule of LUtils")
@LField(value = "http", type = LHttp.class, description = "HTTP submodule of LUtils")
@LField(value = "socket", type = LSocket.class, description = "Socket submodule of LUtils")
@LField(value = "file", type = LFile.class, description = "File submodule of LUtils")
@LField(value = "providers", type = LProviders.class, description = "Submodule containing all providers that don't have to be created by user")
@LField(value = "readers", type = LReaders.class, description = "Submodule containing all readers that don't have to be created by user")
@LField(value = "regex", type = LRegex.class, description = "RegEx submodule of LUtils")
@LField(value = "misc", type = LMisc.class, description = "Misc submodule of LUtils")
public class LUtils implements FiguraAPI {
    private static final LJson json = new LJson();
    private LHttp http = null;
    private final LFile file = new LFile();
    private final LMisc misc = new LMisc();
    private LSocket socket;
    private static final LRegex regex = new LRegex();
    private Avatar avatar;
    public LUtils() {}
    public LUtils(Avatar avatar) {
        this.avatar = avatar;
        http = new LHttp(avatar);
        socket = new LSocket(avatar);
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
            case "socket" -> socket;
            case "providers" -> LProviders.INSTANCE;
            case "readers" -> LReaders.INSTANCE;
            case "file" -> avatar.isHost ? file : null;
            case "regex" -> regex;
            case "misc" -> misc;
            default -> null;
        };
    }

    @Override
    public String getName() {
        return "lutils";
    }

    @Override
    public Collection<Class<?>> getWhitelistedClasses() {
        List<Class<?>> classesToRegister = new ArrayList<>();
        for (Class<?> c :
                LUTILS_CLASSES) {
            if (c.isAnnotationPresent(LuaWhitelist.class)) {
                classesToRegister.add(c);
            }
        }
        return classesToRegister;
    }

    @Override
    public Collection<Class<?>> getDocsClasses() {
        return List.of();
    }

    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            switch (args[0]) {
                case "docs":
                    LDocsGenerator.generateApiReference(Path.of(args[1]), LUTILS_CLASSES);
                    break;
            }
        }
    }


    public static final Class<?>[] LUTILS_CLASSES = new Class[] {
            LUtils.class,

            LJson.class,
            LJsonSerializer.class,

            LHttp.class,
            LHttpResponse.class,

            LInputStream.class,
            LOutputStream.class,
            LJavaInputStream.class,
            LJavaOutputStream.class,
            LLuaInputStream.class,
            LLuaOutputStream.class,

            LReaders.class,

            LReader.class,
            LStringReader.class,
            LJsonReader.class,

            LProviders.class,

            LProvider.class,
            LStringProvider.class,
            LStreamProvider.class,
            LJsonProvider.class,

            LFile.class,

            LFuture.class,

            LRegex.class,
            LRegexGroup.class,
            LRegexMatch.class,

            LMisc.class,

            LSocket.class,
            LSocketClient.class
    };
}
