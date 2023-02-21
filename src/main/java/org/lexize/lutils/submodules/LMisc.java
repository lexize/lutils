package org.lexize.lutils.submodules;

import org.lexize.lutils.annotations.LDescription;
import org.lexize.lutils.annotations.LDocsFuncOverload;
import org.lexize.lutils.annotations.LField;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.moon.figura.lua.LuaWhitelist;
import org.moon.figura.lua.ReadOnlyLuaTable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@LuaWhitelist
@LDescription("Submodule functions that cant be categorized")
@LField(value = "encodings", type = LuaTable.class)
public class LMisc {
    public enum CharsetVal {
        UTF_8(StandardCharsets.UTF_8),
        UTF_16(StandardCharsets.UTF_16),
        UTF_16BE(StandardCharsets.UTF_16BE),
        UTF_16LE(StandardCharsets.UTF_16LE),
        ISO_8859_1(StandardCharsets.ISO_8859_1),
        US_ASCII(StandardCharsets.US_ASCII);
        public final Charset charset;
        CharsetVal(Charset charset) {
            this.charset = charset;
        }
    }

    public static ReadOnlyLuaTable ENCODINGS = new ReadOnlyLuaTable(new LuaTable() {{
        for (CharsetVal v :
                CharsetVal.values()) {
            set(v.name(),v.name());
        }
    }});

    @LuaWhitelist
    @LDocsFuncOverload(argumentNames = {"string", "charset"}, argumentTypes = {String.class,String.class}, returnType = byte[].class, description =
            "Gets bytes of specified string with specified charset. If charset not specified UTF_8 will be used")
    public List<Integer> stringToBytes(String string, String charset) {
        if (charset == null) {
            charset = "UTF_8";
        }
        CharsetVal chrset;
        try {
            chrset = CharsetVal.valueOf(charset);
        } catch (IllegalArgumentException e) {
            chrset = CharsetVal.UTF_8;
        }
        byte[] bytes = string.getBytes(chrset.charset);
        List<Integer> byteList = new ArrayList<>();
        for (byte b :
                bytes) {
            byteList.add((int) b);
        }
        return byteList;
    }

    @LuaWhitelist
    @LDocsFuncOverload(argumentNames = {"bytes", "charset"}, argumentTypes = {byte[].class,String.class}, returnType = String.class, description =
            "Gets bytes of specified string with specified charset. If charset not specified UTF_8 will be used")
    public String bytesToString(List<Integer> byteList, String charset) throws IOException {
        if (charset == null) {
            charset = "UTF_8";
        }
        CharsetVal chrset;
        try {
            chrset = CharsetVal.valueOf(charset);
        } catch (IllegalArgumentException e) {
            chrset = CharsetVal.UTF_8;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byteList.forEach(baos::write);
        String s = new String(baos.toByteArray());
        baos.close();
        return s;
    }

    @LuaWhitelist
    @LDocsFuncOverload(argumentNames = "base64", argumentTypes = String.class, returnType = byte[].class, description =
            "Decodes provided Base64 string and returns bytes of it")
    public List<Integer> base64ToBytes(String base64) {
        byte[] bytes = Base64.getDecoder().decode(base64);
        List<Integer> byteList = new ArrayList<>();
        for (byte b :
                bytes) {
            byteList.add((int) b);
        }
        return byteList;
    }

    @LuaWhitelist
    @LDocsFuncOverload(argumentNames = "bytes", argumentTypes = byte[].class, returnType = String.class, description =
            "Encodes bytes to Base64 string")
    public String bytesToBase64(List<Integer> byteList) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byteList.forEach(baos::write);
        String base64 = Base64.getEncoder().encodeToString(baos.toByteArray());
        baos.close();
        return base64;
    }

    @LuaWhitelist
    public Object __index(LuaValue key) {
        if (!key.isstring()) return null;
        return switch (key.tojstring()) {
            case "encodings" -> ENCODINGS;
            default -> null;
        };
    }
}
