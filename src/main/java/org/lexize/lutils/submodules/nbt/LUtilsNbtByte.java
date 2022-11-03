package org.lexize.lutils.submodules.nbt;

import org.lexize.lutils.submodules.streams.LUtilsInputStream;
import org.lexize.lutils.submodules.streams.LUtilsOutputStream;
import org.luaj.vm2.LuaValue;
import org.moon.figura.lua.LuaWhitelist;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
    public void writePureData(LUtilsOutputStream luos) throws IOException {
        luos.getOutputStream().write(value);
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
    public NbtReturnValue getValue(LUtilsInputStream stream){
        try {
            InputStream is = stream.getInputStream();
            byte[] nameLengthBytes = is.readNBytes(2);
            int nameLength = (nameLengthBytes[0] << 8) + (nameLengthBytes[1]);
            byte[] nameBytes = is.readNBytes(nameLength);
            byte val = (byte)(is.read());
            return new NbtReturnValue<>(new String(nameBytes), new LUtilsNbtByte(val), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public NbtReturnValue getPureValue(byte[] bytes, int offset) {
        return new NbtReturnValue(null, new LUtilsNbtByte(bytes[offset]), offset+1);
    }

    @Override
    public NbtReturnValue getPureValue(LUtilsInputStream stream){
        try {
            return new NbtReturnValue<>(null, new LUtilsNbtByte((byte) stream.read()), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
