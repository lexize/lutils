package org.lexize.lutils.nbt;

import org.luaj.vm2.LuaValue;
import org.moon.figura.lua.LuaWhitelist;

import java.util.Arrays;

@LuaWhitelist
public class LUtilsNbtByte extends LUtilsNbtValue<Byte> implements LUtilsNbtValue.NbtGettable<Byte>, LUtilsNbtValue.NbtSettable<Byte> {

    public LUtilsNbtByte() {};

    public LUtilsNbtByte(byte value) {
        super(value);
    }

    @Override
    public byte[] getPureData() {
        return new byte[] {
                value
        };
    }

    @Override
    public byte typeId() {
        return 1;
    }

    @Override
    public String typeName() {
        return "TAG_Byte";
    }

    @Override
    public NbtReturnValue getValue(byte[] bytes, int offset) {
        int nameLength = ((bytes[offset+1] << 8) + (bytes[offset+2]));
        byte[] nameStringBytes = Arrays.copyOfRange(bytes, offset + 3, offset + 3 + nameLength);
        int valueOffset = offset+ 3 + nameLength;
        return new NbtReturnValue(new String(nameStringBytes), new LUtilsNbtByte(bytes[valueOffset]), valueOffset+1);
    }

    @Override
    public NbtReturnValue getPureValue(byte[] bytes, int offset) {
        return new NbtReturnValue("", new LUtilsNbtByte(bytes[offset]), offset+1);
    }

    @Override
    public Byte get() {
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
    public void set(Byte v) {
        value = v;
    }

    @Override
    public void __newindex(LuaValue n, LuaValue value) {
        if (!n.isstring()) return;
        if (!value.isint()) throw new RuntimeException("Value should be integer");
        switch (n.tojstring()) {
            case ("value") -> {
                this.value = value.tobyte();
            }
        }
    }
}
