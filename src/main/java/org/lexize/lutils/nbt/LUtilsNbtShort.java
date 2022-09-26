package org.lexize.lutils.nbt;

import org.luaj.vm2.LuaValue;
import org.moon.figura.lua.LuaWhitelist;

import java.util.Arrays;

@LuaWhitelist
public class LUtilsNbtShort extends LUtilsNbtValue<Short> implements LUtilsNbtValue.NbtGettable<Short>,LUtilsNbtValue.NbtSettable<Short> {

    public LUtilsNbtShort() {};
    public LUtilsNbtShort(short value) {
        super(value);
    }

    @Override
    public byte[] getPureData() {
        return new byte[] {
                (byte) ((value >> 8) & 0xFF),
                (byte) (value & 0xFF)
        };
    }

    @Override
    public byte typeId() {
        return 2;
    }

    @Override
    public String typeName() {
        return "TAG_Short";
    }

    @Override
    public NbtReturnValue getValue(byte[] bytes, int offset) {
        int nameLength = ((bytes[offset+1] << 8) + (bytes[offset+2]));
        byte[] nameStringBytes = Arrays.copyOfRange(bytes, offset + 3, offset + 3 + nameLength);
        int valueOffset = offset + 3 + nameLength;
        byte[] valueBytes = new byte[] {
                bytes[valueOffset],
                bytes[valueOffset+1]
        };
        short value = (short) (((valueBytes[0] & 0xFF) << 8) +
                ((valueBytes[1] & 0xFF)));
        return new NbtReturnValue(new String(nameStringBytes), new LUtilsNbtShort(value), valueOffset+2);
    }

    @Override
    public NbtReturnValue getPureValue(byte[] bytes, int offset) {
        int valueOffset = offset;
        byte[] valueBytes = new byte[] {
                bytes[valueOffset],
                bytes[valueOffset+1]
        };
        short value = (short) (((valueBytes[0] & 0xFF) << 8) +
                ((valueBytes[1] & 0xFF)));
        return new NbtReturnValue("", new LUtilsNbtShort(value), valueOffset+2);
    }

    @Override
    public Short get() {
        return value;
    }

    @Override
    public Object __index(LuaValue n) {
        if (!n.isstring()) return null;
        return switch (n.tojstring()) {
            case ("value") -> value;
            default -> null;
        };
    }

    @Override
    public void set(Short v) {
        value = v;
    }

    @Override
    public void __newindex(LuaValue n, LuaValue value) {
        if (!n.isstring()) return;
        if (!value.isint()) throw new RuntimeException("Value should be integer");
        switch (n.tojstring()) {
            case ("value") -> {
                this.value = value.toshort();
            }
        }
    }
}
