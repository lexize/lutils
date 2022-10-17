package org.lexize.lutils.submodules.nbt;

import org.lexize.lutils.LUtils;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.moon.figura.lua.LuaWhitelist;

import java.util.Arrays;

@LuaWhitelist
public class LUtilsNbtLongArray extends LUtilsNbtValue<long[]> implements LUtilsNbtValue.NbtArray<Long> {

    public LUtilsNbtLongArray() {
    }

    public LUtilsNbtLongArray(long[] value) {
        super(value);
    }

    public LUtilsNbtLongArray(LuaTable table) {
        LuaValue[] keys = table.keys();
        long[] longs = new long[keys.length];
        for (int i = 0; i < keys.length; i++) {
            longs[i] = table.get(keys[i]).tolong();
        }
        value = longs;
    }

    @Override
    public byte[] getPureData() {
        byte[] prefix = new byte[] {
                (byte) ((value.length >> 24) & 0xFF),
                (byte) ((value.length >> 16) & 0xFF),
                (byte) ((value.length >> 8) & 0xFF),
                (byte) (value.length & 0xFF),
        };
        byte[][] valueArrays = new byte[value.length][4];
        for (int i = 0; i < value.length; i++) {
            long v = value[i];
            byte[] valueArray = new byte[]{
                    (byte) ((v >> 56) & 0xFF),
                    (byte) ((v >> 48) & 0xFF),
                    (byte) ((v >> 40) & 0xFF),
                    (byte) ((v >> 32) & 0xFF),
                    (byte) ((v >> 24) & 0xFF),
                    (byte) ((v >> 16) & 0xFF),
                    (byte) ((v >> 8) & 0xFF),
                    (byte) (v & 0xFF),
            };
            valueArrays[i] = valueArray;
        }
        byte[] valueBytes = LUtils.Utils.combineByteArrays(valueArrays);

        return LUtils.Utils.combineByteArrays(prefix, valueBytes);
    }

    @Override
    public byte typeId() {
        return 12;
    }

    @Override
    public String typeName() {
        return "TAG_Long_Array";
    }

    @Override
    public NbtReturnValue getValue(byte[] bytes, int offset) {
        int nameLength = (bytes[offset + 1] << 8) + (bytes[offset + 2]);
        byte[] nameBytes = Arrays.copyOfRange(bytes, 3 + offset, 3 + offset + nameLength);
        int prefixOffset = 3 + offset + nameLength;

        int length = ((bytes[prefixOffset] & 0xFF) << 24) + ((bytes[prefixOffset+1] & 0xFF) << 16) +
                ((bytes[prefixOffset+2] & 0xFF) << 8) + ((bytes[prefixOffset+3] & 0xFF));

        int valueOffset = prefixOffset + 4;
        long[] values = new long[length];

        for (int i = 0; i < length; i++) {
            byte[] valBytes = Arrays.copyOfRange(bytes, valueOffset, valueOffset + 4);
            valueOffset += 8;
            long v =((long) (valBytes[0] & 0xFF) << 56) + ((long) (valBytes[1] & 0xFF) << 48) +
                    ((long) (valBytes[2] & 0xFF) << 40) + ((long) (valBytes[3] & 0xFF) << 32) +
                    ((long) (valBytes[4] & 0xFF) << 24) + ((valBytes[5] & 0xFF) << 16) +
                    ((valBytes[6] & 0xFF) << 8) + ((valBytes[7] & 0xFF));
            values[i] = v;
        }

        return new NbtReturnValue<>(new String(nameBytes), new LUtilsNbtLongArray(values), valueOffset);
    }

    @Override
    public NbtReturnValue getPureValue(byte[] bytes, int offset) {
        int length = ((bytes[offset] & 0xFF) << 24) + ((bytes[offset+1] & 0xFF) << 16) +
                ((bytes[offset+2] & 0xFF) << 8) + ((bytes[offset+3] & 0xFF));

        int valueOffset = offset + 4;
        long[] values = new long[length];

        for (int i = 0; i < length; i++) {
            byte[] valBytes = Arrays.copyOfRange(bytes, valueOffset, valueOffset + 4);
            valueOffset += 8;
            long v =((long) (valBytes[0] & 0xFF) << 56) + ((long) (valBytes[1] & 0xFF) << 48) +
                    ((long) (valBytes[2] & 0xFF) << 40) + ((long) (valBytes[3] & 0xFF) << 32) +
                    ((long) (valBytes[4] & 0xFF) << 24) + ((valBytes[5] & 0xFF) << 16) +
                    ((valBytes[6] & 0xFF) << 8) + ((valBytes[7] & 0xFF));
            values[i] = v;
        }

        return new NbtReturnValue<>(null, new LUtilsNbtLongArray(values), valueOffset);
    }

    @Override
    public String toString() {
        return toString("");
    }

    @Override
    public String toString(String name) {
        return "%s('%s'): [%s longs]".formatted(typeName(), name, value.length);
    }

    @Override
    public void __newIndex(int i, LuaValue v) {
        long n;
        if (v.islong()) n = v.checklong();
        else throw new RuntimeException("Value should be long");
        set(i, n);
    }

    @Override
    public void set(int i, Long v) {
        value[i] = v;
    }

    @Override
    public LuaValue __index(int i) {
        return LuaValue.valueOf(get(i));
    }

    @Override
    public Long get(int i) {
        return value[i];
    }
}
