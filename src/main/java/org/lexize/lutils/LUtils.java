package org.lexize.lutils;

import org.apache.commons.lang3.ArrayUtils;
import org.lexize.lutils.nbt.*;
import org.lexize.lutils.regex.LUtilsRegexGroup;
import org.lexize.lutils.regex.LUtilsRegexMatch;
import org.lexize.lutils.streams.LUtilsInputStream;
import org.lexize.lutils.streams.LUtilsOutputStream;
import org.lexize.lutils.submodules.*;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
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

    @LuaWhitelist
    public LUtilsHttp http = new LUtilsHttp();

    public LUtilsNbt nbt;

    public LUtils() {

    }

    public LUtils(Avatar avatar) {
        _avatar = avatar;
        if(_avatar.isHost) {
            file = new LUtilsFile();
        }
        nbt = new LUtilsNbt(avatar);
        regex = new LUtilsRegex(_avatar);
    }

    @LuaWhitelist
    public Object __index(String ind) {
        return switch(ind) {
          case "json" -> json;
          case "file" -> file;
          case "regex" -> regex;
          case "misc" -> misc;
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
                LUtilsNbtString.class
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
    }
}
