package org.lexize.lutils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import net.minecraft.text.OrderedText;
import org.apache.commons.lang3.ArrayUtils;
import org.lexize.lutils.submodules.hud.builders.FillRenderTaskBuilder;
import org.lexize.lutils.submodules.hud.builders.TextRenderTaskBuilder;
import org.lexize.lutils.submodules.hud.builders.TextureRenderTaskBuilder;
import org.lexize.lutils.submodules.nbt.*;
import org.lexize.lutils.submodules.regex.LUtilsRegexGroup;
import org.lexize.lutils.submodules.regex.LUtilsRegexMatch;
import org.lexize.lutils.submodules.streams.LUtilsInputStream;
import org.lexize.lutils.submodules.streams.LUtilsOutputStream;
import org.lexize.lutils.submodules.*;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.moon.figura.avatar.Avatar;
import org.moon.figura.lua.FiguraAPI;
import org.moon.figura.lua.LuaWhitelist;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

@LuaWhitelist
public class LUtils implements FiguraAPI {
    private Avatar _avatar;
    public static final String API_NAME = "lutils";
    private static final Gson _json = new GsonBuilder().setPrettyPrinting().create();

    private ExperimentalSettings experimentalSettings;

    @LuaWhitelist
    public LUtilsJson json = new LUtilsJson();
    @LuaWhitelist
    public LUtilsFile file;
    @LuaWhitelist
    public LUtilsRegex regex;
    @LuaWhitelist
    public LUtilsMisc misc = new LUtilsMisc();
    @LuaWhitelist
    public LUtilsHUD hud;
    @LuaWhitelist
    public LUtilsHttp http;
    @LuaWhitelist
    public LUtilsNbt nbt;
    public LUtils() {

    }

    public LUtils(Avatar avatar) {
        loadExperimentalSettings();
        _avatar = avatar;
        if(_avatar.isHost) {
            file = new LUtilsFile();
            if (experimentalSettings.hud_submodule) hud = new LUtilsHUD(_avatar);
        }
        if (experimentalSettings.http_submodule) http = new LUtilsHttp(_avatar);
        if (experimentalSettings.nbt_submodule) nbt = new LUtilsNbt(_avatar);
        regex = new LUtilsRegex(_avatar);
    }

    @LuaWhitelist
    public Object __index(String ind) {
        return switch(ind) {
          case "json" -> json;
          case "file" -> file;
          case "regex" -> regex;
          case "misc" -> misc;
          case "hud" -> hud;
          case "http" -> http;
          case "nbt" -> nbt;
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

    public static Gson getJson() {
        return _json;
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

                LUtilsMisc.class,

                LUtilsHttp.class,

                LVarargs.class,

                LUtilsNbt.class,
                LUtilsNbtByte.class,
                LUtilsNbtByteArray.class,
                LUtilsNbtCompound.class,
                LUtilsNbtDouble.class,
                LUtilsNbtFloat.class,
                LUtilsNbtInt.class,
                LUtilsNbtIntArray.class,
                LUtilsNbtList.class,
                LUtilsNbtLong.class,
                LUtilsNbtLongArray.class,
                LUtilsNbtShort.class,
                LUtilsNbtString.class,

                LUtilsHUD.class,
                FillRenderTaskBuilder.class,
                TextRenderTaskBuilder.class,
                TextureRenderTaskBuilder.class
        );
    }


    public static class Utils {
        public static byte[] tableToByteArray(LuaTable bytesTable) {
            byte[] bytes = new byte[bytesTable.length()];
            for (LuaValue k:
                    bytesTable.keys()) {
                int i;
                try {
                    i = k.checkint();
                } catch (LuaError e) {
                    throw new LuaError("Indexes in array should be only integer's.");
                }
                int v;
                try {
                    v = bytesTable.get(i).checkint();
                }
                catch (LuaError e) {
                    throw new LuaError("Values in array should be integer's.");
                }
                bytes[i-1] = (byte)(v % 256);
            }
            return bytes;
        }

        public static LuaTable byteArrayToTable(byte[] bytes) {
            LuaTable tbl = new LuaTable();

            for (int i = 0; i < bytes.length; i++) {
                tbl.set(i+1, LuaValue.valueOf(bytes[i]));
            }

            return tbl;
        }

        public static <T> T[] combine(T... values) {return values;}

        public static byte[] combineByteArrays(byte[]... arrays) {
            if (arrays.length == 0) return new byte[0];
            if (arrays.length == 1) return arrays[0];


            byte[] cArr = arrays[0];
            byte[] nArr = arrays[1];
            for (int i = 1; i < arrays.length; i++) {
                cArr = ArrayUtils.addAll(cArr, nArr);
                if (i+1 >= arrays.length) break;
                nArr = arrays[i+1];
            }
            return cArr;
        }

        public static String bytesToHex(byte[] bytes) {
            StringBuilder builder = new StringBuilder();
            for (byte b:
                 bytes) {
                builder.append(Character.forDigit((b >> 4)&0xf, 16));
                builder.append(Character.forDigit(b&0xf, 16));
            }
            return builder.toString();
        }

        public static String bytesToHexSplitted(byte[] bytes) {
            StringBuilder builder = new StringBuilder();
            int i = 1;
            for (byte b:
                    bytes) {
                builder.append(Character.forDigit((b >> 4)&0xf, 16));
                builder.append(Character.forDigit(b&0xf, 16));
                if (i < bytes.length) builder.append(' ');
                i++;
            }
            return builder.toString();
        }

        public static Byte[] combineByteArrays(Byte[]... arrays) {
            if (arrays.length == 0) return new Byte[0];
            if (arrays.length == 1) return arrays[0];


            Byte[] cArr = arrays[0];
            Byte[] nArr = arrays[1];
            for (int i = 1; i < arrays.length; i++) {
                cArr = ArrayUtils.addAll(cArr, nArr);
                if (i+1 >= arrays.length) break;
                nArr = arrays[i+1];
            }
            return cArr;
        }

        public static String fromOrderedText(OrderedText text) {
            StringBuilder builder = new StringBuilder();
            text.accept((i,s,c) -> {
                if (i != -1) builder.append((char) c);
                return i != -1;
            });
            return builder.toString();
        }
    }

    public void loadExperimentalSettings() {
        File settingsFile = new File("lutils_experimental_settings.json");
        if (!settingsFile.exists()) {
            experimentalSettings = new ExperimentalSettings();
            try {
                FileOutputStream fos = new FileOutputStream(settingsFile);
                String es = _json.toJson(experimentalSettings);
                fos.write(es.getBytes(StandardCharsets.UTF_8));
                fos.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else {
            try {
                FileInputStream fis = new FileInputStream(settingsFile);
                String es = new String(fis.readAllBytes(), StandardCharsets.UTF_8);
                fis.close();
                experimentalSettings = _json.fromJson(es, ExperimentalSettings.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class ExperimentalSettings {
        @SerializedName("Turn on HTTP submodule")
        public boolean http_submodule = false;
        @SerializedName("Turn on HUD submodule")
        public boolean hud_submodule = false;
        @SerializedName("Turn on NBT submodule")
        public boolean nbt_submodule = false;
    }
}
