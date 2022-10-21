package org.lexize.lutils.submodules.nbt;

import org.luaj.vm2.LuaValue;
import org.moon.figura.lua.LuaWhitelist;

import java.util.Arrays;

@LuaWhitelist
public class LUtilsNbtLong extends LUtilsNbtValue<Long>  implements LUtilsNbtValue.NbtGettable<Long>, LUtilsNbtValue.NbtSettable<Long> {

    public LUtilsNbtLong() {};
    public LUtilsNbtLong(Long value) {
        super(value);
    }


    @Override
    public byte[] getPureData() {
        byte[] valueBytes = new byte[8];
        valueBytes[7] = (byte) (value & 0xff);
        valueBytes[6] = (byte) ((value >> 8 ) & 0xff);
        valueBytes[5] = (byte) ((value >> 16) & 0xff);
        valueBytes[4] = (byte) ((value >> 24) & 0xff);
        valueBytes[3] = (byte) ((value >> 32) & 0xff);
        valueBytes[2] = (byte) ((value >> 40) & 0xff);
        valueBytes[1] = (byte) ((value >> 48) & 0xff);
        valueBytes[0] = (byte) ((value >> 56) & 0xff);

        return valueBytes;
    }

    @Override
    public byte typeId() {
        return 4;
    }

    @Override
    public String typeName() {
        return "TAG_Long";
    }

    @Override
    public NbtReturnValue getValue(byte[] bytes, int offset) {
        int nameLength = ((bytes[offset+1] << 8) + (bytes[offset+2]));
        byte[] nameStringBytes = Arrays.copyOfRange(bytes, offset + 3, offset + 3 + nameLength);
        int valueOffset = offset + 3 + nameLength;
        byte[] valueBytes = new byte[] {
                bytes[valueOffset],
                bytes[valueOffset+1],
                bytes[valueOffset+2],
                bytes[valueOffset+3],
                bytes[valueOffset+4],
                bytes[valueOffset+5],
                bytes[valueOffset+6],
                bytes[valueOffset+7],
        };
        long value = ((long) (valueBytes[0] & 0xFF) << 56) +
                ((long) (valueBytes[1] & 0xFF) << 48) +
                ((long) (valueBytes[2] & 0xFF) << 40) +
                ((long) (valueBytes[3] & 0xFF) << 32) +
                ((long) (valueBytes[4] & 0xFF) << 24) +
                ((valueBytes[5] & 0xFF) << 16) +
                ((valueBytes[6] & 0xFF) << 8 ) +
                ((valueBytes[7] & 0xFF)      );
        return new NbtReturnValue(new String(nameStringBytes), new LUtilsNbtLong(value), valueOffset+8);
    }

    @Override
    public NbtReturnValue getPureValue(byte[] bytes, int offset) {
        int valueOffset = offset;
        byte[] valueBytes = new byte[] {
                bytes[valueOffset],
                bytes[valueOffset+1],
                bytes[valueOffset+2],
                bytes[valueOffset+3],
                bytes[valueOffset+4],
                bytes[valueOffset+5],
                bytes[valueOffset+6],
                bytes[valueOffset+7],
        };
        long value = ((long) (valueBytes[0] & 0xFF) << 56) +
                ((long) (valueBytes[1] & 0xFF) << 48) +
                ((long) (valueBytes[2] & 0xFF) << 40) +
                ((long) (valueBytes[3] & 0xFF) << 32) +
                ((long) (valueBytes[4] & 0xFF) << 24) +
                ((valueBytes[5] & 0xFF) << 16) +
                ((valueBytes[6] & 0xFF) << 8 ) +
                ((valueBytes[7] & 0xFF)      );
        return new NbtReturnValue(null, new LUtilsNbtLong(value), valueOffset+8);
    }

    @Override
    public Long get() {
        return value;
    }

    @Override
    public Object __index(LuaValue n) {
        if (n.isstring() && n.checkjstring() == "value") return value;
        return null;
    }

    @Override
    public void set(Long v) {
        value = v;
    }

    @Override
    public void __newindex(LuaValue n, LuaValue value) {
        if (!n.isstring()) return;
        if (!value.islong()) throw new RuntimeException("Value should be long");
        switch (n.tojstring()) {
            case ("value") -> this.value = value.tolong();
        }
    }
}