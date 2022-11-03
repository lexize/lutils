package org.lexize.lutils.submodules.nbt;

import org.lexize.lutils.submodules.streams.LUtilsInputStream;
import org.lexize.lutils.submodules.streams.LUtilsOutputStream;
import org.luaj.vm2.LuaValue;
import org.moon.figura.lua.LuaWhitelist;

import java.io.IOException;
import java.io.InputStream;
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
    public void writePureData(LUtilsOutputStream luos) throws IOException {
        luos.getOutputStream().write(new byte[] {
                (byte) ((value >> 8) & 0xFF),
                (byte) (value & 0xFF)
        });
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
    public NbtReturnValue getValue(LUtilsInputStream stream){
        try {
            InputStream is = stream.getInputStream();
            byte[] nameLengthBytes = is.readNBytes(2);
            int nameLength = ((nameLengthBytes[0] << 8) + (nameLengthBytes[1]));
            byte[] nameStringBytes = is.readNBytes(nameLength);
            byte[] valueBytes = is.readNBytes(2);
            short value = (short) (((valueBytes[0] & 0xFF) << 8) +
                    ((valueBytes[1] & 0xFF)));
            return new NbtReturnValue(new String(nameStringBytes), new LUtilsNbtShort(value), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
    public NbtReturnValue getPureValue(LUtilsInputStream stream){
        try {
            byte[] valueBytes = stream.getInputStream().readNBytes(2);
            short value = (short) (((valueBytes[0] & 0xFF) << 8) +
                    ((valueBytes[1] & 0xFF)));
            return new NbtReturnValue(null, new LUtilsNbtShort(value), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
