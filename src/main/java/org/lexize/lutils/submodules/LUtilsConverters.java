package org.lexize.lutils.submodules;

import org.lexize.lutils.LUtils;
import org.luaj.vm2.LuaTable;
import org.moon.figura.lua.LuaWhitelist;

import java.nio.charset.StandardCharsets;

@LuaWhitelist
public class LUtilsConverters {

    @LuaWhitelist
    public LuaTable shortToBytes(short number) {
        return LUtils.Utils.byteArrayToTable(
                new byte[] {
                        (byte)((number >> 8) & 0xFF),
                        (byte)(number & 0xFF)
                }
        );
    }
    @LuaWhitelist
    public LuaTable intToBytes(int number) {
        return LUtils.Utils.byteArrayToTable(
                new byte[] {
                        (byte)((number >> 24) & 0xFF),
                        (byte)((number >> 16) & 0xFF),
                        (byte)((number >> 8) & 0xFF),
                        (byte)(number & 0xFF)
                }
        );
    }

    @LuaWhitelist
    public LuaTable floatToBytes(float number) {
        return intToBytes(Float.floatToIntBits(number));
    }

    @LuaWhitelist
    public LuaTable longToBytes(long number) {
        return LUtils.Utils.byteArrayToTable(
                new byte[] {
                        (byte)((number >> 56) & 0xFF),
                        (byte)((number >> 48) & 0xFF),
                        (byte)((number >> 40) & 0xFF),
                        (byte)((number >> 32) & 0xFF),
                        (byte)((number >> 24) & 0xFF),
                        (byte)((number >> 16) & 0xFF),
                        (byte)((number >> 8) & 0xFF),
                        (byte)(number & 0xFF)
                }
        );
    }

    @LuaWhitelist
    public LuaTable doubleToBytes(double n) {
        return longToBytes(Double.doubleToLongBits(n));
    }

    @LuaWhitelist
    public LuaTable stringToBytes(String s) {
        return LUtils.Utils.byteArrayToTable(s.getBytes(StandardCharsets.UTF_8));
    }
}
