package org.lexize.lutils.submodules;

import org.lexize.lutils.LUtils;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaInteger;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.moon.figura.lua.LuaNotNil;
import org.moon.figura.lua.LuaWhitelist;
import org.moon.figura.lua.ReadOnlyLuaTable;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Pattern;

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
    public static final ReadOnlyLuaTable regex_flags;

    static {
        LuaTable _chTable = new LuaTable();
        for (LUtilsCharset ch:
                LUtilsCharset.values()) {
            _chTable.set(ch.name(), ch.name());
        }
        charsets = new ReadOnlyLuaTable(_chTable);

        LuaTable _rfTable = new LuaTable();
        _rfTable.set("UNIX_LINES",                 Pattern.UNIX_LINES);
        _rfTable.set("CASE_INSENSITIVE",           Pattern.CASE_INSENSITIVE);
        _rfTable.set("COMMENTS",                   Pattern.COMMENTS);
        _rfTable.set("MULTILINE",                  Pattern.MULTILINE);
        _rfTable.set("LITERAL",                    Pattern.LITERAL);
        _rfTable.set("DOTALL",                     Pattern.DOTALL);
        _rfTable.set("UNICODE_CASE",               Pattern.UNICODE_CASE);
        _rfTable.set("CANON_EQ",                   Pattern.CANON_EQ);
        _rfTable.set("UNICODE_CHARACTER_CLASS",    Pattern.UNICODE_CHARACTER_CLASS);
        regex_flags = new ReadOnlyLuaTable(_rfTable);

    }

    @LuaWhitelist
    public Object __index(String args) {
        return switch (args) {
            case ("charsets") -> charsets;
            case ("regex_flags") -> regex_flags;
            default -> null;
        };
    }

    @LuaWhitelist
    public LuaTable stringToBytes(@LuaNotNil String str, String charsetId) {
        Charset chst = StandardCharsets.UTF_8;
        if (charsetId != null) chst = LUtilsCharset.valueOf(charsetId).getCharset();
        byte[] bytes = str.getBytes(chst);

        return LUtils.Utils.byteArrayToTable(bytes);
    }

    @LuaWhitelist
    public String bytesToString(@LuaNotNil LuaTable bytesTable, String charsetId) {
        Charset chst = StandardCharsets.UTF_8;
        if (charsetId != null) chst = LUtilsCharset.valueOf(charsetId).getCharset();
        byte[] bytes = LUtils.Utils.tableToByteArray(bytesTable);
        return new String(bytes, chst);
    }

    @LuaWhitelist
    public String bytesToBase64(@LuaNotNil LuaTable bytesTable) {
        byte[] bytes = LUtils.Utils.tableToByteArray(bytesTable);
        return Base64.getEncoder().encodeToString(bytes);
    }

    @LuaWhitelist
    public LuaTable base64ToBytes(@LuaNotNil String base64) {
        return LUtils.Utils.byteArrayToTable(
                Base64.getDecoder().decode(base64)
        );
    }

}