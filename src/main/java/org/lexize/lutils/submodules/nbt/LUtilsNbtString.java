package org.lexize.lutils.submodules.nbt;

import org.lexize.lutils.LUtils;
import org.luaj.vm2.LuaValue;
import org.moon.figura.lua.LuaWhitelist;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@LuaWhitelist
public class LUtilsNbtString extends LUtilsNbtValue<String> implements LUtilsNbtValue.NbtGettable<String>, LUtilsNbtValue.NbtSettable<String> {
    public LUtilsNbtString() {
    }

    public LUtilsNbtString(String value) {
        super(value);
        if(value.length() > 0xFFFF) throw new RuntimeException("Length of string (%s) more than %s".formatted(value.length(), 0xFFFF));
    }

    @Override
    public byte[] getPureData() {
        byte[] prefix = new byte[] {
                (byte) ((value.length() >> 8) & 0xFF),
                (byte) (value.length() & 0xFF)
        };
        byte[] valueBytes = value.getBytes(StandardCharsets.UTF_8);
        return LUtils.Utils.combineByteArrays(prefix,valueBytes);
    }

    @Override
    public byte typeId() {
        return 8;
    }

    @Override
    public String typeName() {
        return "TAG_String";
    }

    @Override
    public NbtReturnValue getValue(byte[] bytes, int offset) {
        int nameLength = (bytes[offset + 1] << 8) + (bytes[offset + 2]);
        byte[] nameBytes = Arrays.copyOfRange(bytes, 3 + offset, 3 + offset + nameLength);
        int prefixOffset = 3 + offset + nameLength;
        byte[] stringLengthBytes = Arrays.copyOfRange(bytes, prefixOffset, prefixOffset+2);
        int stringLength = ((stringLengthBytes[0] & 0xFF) << 8) + ((stringLengthBytes[1] & 0xFF));
        int valueOffset = prefixOffset + 2;
        byte[] valueBytes = Arrays.copyOfRange(bytes, valueOffset, valueOffset+stringLength);
        return new NbtReturnValue<LUtilsNbtString>(new String(nameBytes), new LUtilsNbtString(new String(valueBytes)), valueOffset+stringLength);
    }

    @Override
    public NbtReturnValue getPureValue(byte[] bytes, int offset) {
        int prefixOffset = offset;
        byte[] stringLengthBytes = Arrays.copyOfRange(bytes, prefixOffset, prefixOffset+2);
        int stringLength = ((stringLengthBytes[0] & 0xFF) << 8) + ((stringLengthBytes[1] & 0xFF));
        int valueOffset = prefixOffset + 2;
        byte[] valueBytes = Arrays.copyOfRange(bytes, valueOffset, valueOffset+stringLength);
        return new NbtReturnValue<LUtilsNbtString>(null, new LUtilsNbtString(new String(valueBytes)), valueOffset+stringLength);
    }

    @Override
    public String toString() {
        return toString("");
    }

    @Override
    public String toString(String name) {
        String[] valueLines = value.split("\n");
        if (valueLines.length == 1) return "%s('%s'): '%s'".formatted(typeName(), name, valueLines[0]);
        else {
            StringBuilder b = new StringBuilder();
            b.append("%s('%s'): Multilined string:\n".formatted(typeName(), name));
            for (int i = 0; i < valueLines.length; i++) {
                String s = valueLines[i]
                        .replace("\\", "\\\\")
                        .replace("\"", "\\\"")
                        .replace("\'", "\\\'");
                b.append("\t'%s'%s".formatted(s, i == valueLines.length-1 ? "" : " +\n"));
            }
            return b.toString();
        }
    }

    @Override
    public String get() {
        return value;
    }

    @Override
    public Object __index(LuaValue n) {
        if (n.isstring() && n.checkjstring() == "value") return value;
        return null;
    }

    @Override
    public void set(String v) {
        if(v.length() > 0xFFFF) throw new RuntimeException("Length of string (%s) more than %s".formatted(v.length(), 0xFFFF));
        value = v;
    }

    @Override
    public void __newindex(LuaValue n, LuaValue value) {
        if (!n.isstring()) return;
        if (!value.isstring()) throw new RuntimeException("Value should be string");
        switch (n.tojstring()) {
            case ("value") -> set(value.tojstring());
        }
    }
}
