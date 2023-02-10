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
import org.lexize.lutils.submodules.LProviders;
import org.lexize.lutils.submodules.LReaders;
import org.lexize.lutils.submodules.http.LHttp;
import org.lexize.lutils.submodules.http.LHttpResponse;
import org.lexize.lutils.submodules.json.LJson;
import org.lexize.lutils.submodules.json.LJsonArray;
import org.lexize.lutils.submodules.json.LJsonObject;
import org.lexize.lutils.submodules.json.LJsonSerializer;
import org.moon.figura.avatar.Avatar;
import org.moon.figura.lua.FiguraAPI;
import org.moon.figura.lua.LuaWhitelist;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@LuaWhitelist
@LDescription("Main class of LUtils. Provides access to all submodules")
@LField(value = "json", type = LJson.class, description = "JSON submodule of LUtils")
@LField(value = "http", type = LHttp.class, description = "HTTP submodule of LUtils")
@LField(value = "file", type = LFile.class, description = "File submodule of LUtils")
@LField(value = "providers", type = LProviders.class, description = "Submodule containing all providers that don't have to be created by user")
@LField(value = "readers", type = LReaders.class, description = "Submodule containing all readers that don't have to be created by user")
public class LUtils implements FiguraAPI {

    private LJson json = new LJson();
    private LHttp http = null;
    private LFile file = new LFile();
    private Avatar avatar;
    public LUtils() {}
    public LUtils(Avatar avatar) {
        this.avatar = avatar;
        http = new LHttp(avatar);
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
            case "providers" -> LProviders.INSTANCE;
            case "readers" -> LReaders.INSTANCE;
            case "file" -> avatar.isHost ? file : null;
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

            LFuture.class
    };
}
