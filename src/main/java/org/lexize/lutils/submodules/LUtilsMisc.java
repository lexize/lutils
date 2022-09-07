package org.lexize.lutils.submodules;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaInteger;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.moon.figura.lua.LuaNotNil;
import org.moon.figura.lua.LuaWhitelist;
import org.moon.figura.lua.ReadOnlyLuaTable;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@LuaWhitelist
public class LUtilsMisc {
    @LuaWhitelist
    public enum LUtilsCharset {
        UTF_8(StandardCharsets.UTF_8),
        UTF_16(StandardCharsets.UTF_16),
        UTF_16LE(StandardCharsets.UTF_16LE),
        UTF_16BE(StandardCharsets.UTF_16BE),
        ISO_8859_1(StandardCharsets.ISO_8859_1),
        US_ASCII(StandardCharsets.US_ASCII)
        ;

        private Charset _charset;

        LUtilsCharset(Charset charset) {
            _charset = charset;
        }
        public Charset getCharset() {
            return _charset;
        }

        public static LUtilsCharset getByName(String name) {
            return LUtilsCharset.valueOf(name);
        }
    }

    public static final ReadOnlyLuaTable charsets;

    static {
        LuaTable table = new LuaTable();
        for (LUtilsCharset ch:
                LUtilsCharset.values()) {
            table.set(ch.name(), ch.name());
        }
        charsets = new ReadOnlyLuaTable(table);
    }

    @LuaWhitelist
    public Object __index(String args) {
        return switch (args) {
            case ("charsets") -> charsets;
            default -> null;
        };
    }

    @LuaWhitelist
    public LuaTable stringToBytes(@LuaNotNil String str, String charsetId) {
        Charset chst = StandardCharsets.UTF_8;
        if (charsetId != null) chst = LUtilsCharset.valueOf(charsetId).getCharset();
        LuaTable tbl = new LuaTable();
        byte[] bytes = str.getBytes(chst);

        for (int i = 0; i < bytes.length; i++) {
            tbl.set(i+1, LuaValue.valueOf(bytes[i]));
        }

        return tbl;
    }

    @LuaWhitelist
    public String bytesToString(@LuaNotNil LuaTable bytesTable, String charsetId) {
        Charset chst = StandardCharsets.UTF_8;
        if (charsetId != null) chst = LUtilsCharset.valueOf(charsetId).getCharset();
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
        return new String(bytes, chst);
    }

}