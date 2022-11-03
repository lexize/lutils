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
public class LUtilsNbtFloat extends LUtilsNbtValue<Float> implements LUtilsNbtValue.NbtGettable<Float>, LUtilsNbtValue.NbtSettable<Float> {

    public LUtilsNbtFloat() {}

    public LUtilsNbtFloat(Float value) {
        super(value);
    }

    @Override
    public byte[] getPureData() {
        int value = Float.floatToIntBits(this.value);
        byte[] valueBytes = new byte[4];
        valueBytes[3] = (byte)(value & 0xFF        );
        valueBytes[2] = (byte)((value >> 8) & 0xFF );
        valueBytes[1] = (byte)((value >> 16) & 0xFF);
        valueBytes[0] = (byte)((value >> 24) & 0xFF);

        return valueBytes;
    }

    @Override
    public void writePureData(LUtilsOutputStream luos) throws IOException {
        OutputStream os = luos.getOutputStream();
        int value = Float.floatToIntBits(this.value);
        byte[] valueBytes = new byte[4];
        valueBytes[3] = (byte)(value & 0xFF        );
        valueBytes[2] = (byte)((value >> 8) & 0xFF );
        valueBytes[1] = (byte)((value >> 16) & 0xFF);
        valueBytes[0] = (byte)((value >> 24) & 0xFF);
        os.write(valueBytes);
    }

    @Override
    public byte typeId() {
        return 5;
    }

    @Override
    public String typeName() {
        return "TAG_Float";
    }

    @Override
    public NbtReturnValue getValue(byte[] bytes, int offset) {
        int nameLength = ((bytes[offset+1] << 8) + (bytes[offset+2]));
        byte[] nameStringBytes = Arrays.copyOfRange(bytes, offset+3, offset+3 + nameLength);
        int valueOffset = offset + 3 + nameLength;
        byte[] valueBytes = new byte[] {
                bytes[valueOffset],
                bytes[valueOffset+1],
                bytes[valueOffset+2],
                bytes[valueOffset+3]
        };
        int value = ((valueBytes[0] & 0xFF) << 24) +
                ((valueBytes[1] & 0xFF) << 16) +
                ((valueBytes[2] & 0xFF) << 8 ) +
                ((valueBytes[3] & 0xFF)      );
        return new NbtReturnValue(new String(nameStringBytes), new LUtilsNbtFloat(Float.intBitsToFloat(value)), valueOffset+4);
    }

    @Override
    public NbtReturnValue getValue(LUtilsInputStream stream) {
        try {
            InputStream is = stream.getInputStream();
            byte[] nameLengthBytes = is.readNBytes(2);
            int nameLength = ((nameLengthBytes[0]) << 8) + (nameLengthBytes[1]);
            byte[] nameStringBytes = is.readNBytes(nameLength);
            byte[] valueBytes = is.readNBytes(4);
            int value = ((valueBytes[0] & 0xFF) << 24) +
                    ((valueBytes[1] & 0xFF) << 16) +
                    ((valueBytes[2] & 0xFF) << 8 ) +
                    ((valueBytes[3] & 0xFF)      );
            return new NbtReturnValue(new String(nameStringBytes), new LUtilsNbtFloat(Float.intBitsToFloat(value)), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public NbtReturnValue getPureValue(byte[] bytes, int offset) {
        int valueOffset = offset;
        byte[] valueBytes = new byte[] {
                bytes[valueOffset],
                bytes[valueOffset+1],
                bytes[valueOffset+2],
                bytes[valueOffset+3]
        };
        int value = ((valueBytes[0] & 0xFF) << 24) +
                ((valueBytes[1] & 0xFF) << 16) +
                ((valueBytes[2] & 0xFF) << 8 ) +
                ((valueBytes[3] & 0xFF)      );
        return new NbtReturnValue(null, new LUtilsNbtFloat(Float.intBitsToFloat(value)), valueOffset+4);
    }

    @Override
    public NbtReturnValue getPureValue(LUtilsInputStream stream) {
        try {
            InputStream is = stream.getInputStream();
            byte[] valueBytes = is.readNBytes(4);
            int value = ((valueBytes[0] & 0xFF) << 24) +
                    ((valueBytes[1] & 0xFF) << 16) +
                    ((valueBytes[2] & 0xFF) << 8 ) +
                    ((valueBytes[3] & 0xFF)      );
            return new NbtReturnValue(null, new LUtilsNbtFloat(Float.intBitsToFloat(value)), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Float get() {
        return value;
    }

    @Override
    public Object __index(LuaValue n) {
        if (n.isstring() && n.checkjstring() == "value") return value;
        return null;
    }

    @Override
    public void set(Float v) {
        value = v;
    }

    @Override
    public void __newindex(LuaValue n, LuaValue value) {
        if (!n.isstring()) return;
        if (!value.isnumber()) throw new RuntimeException("Value should be number");
        switch (n.tojstring()) {
            case ("value") -> this.value = value.tofloat();
        }
    }
}
