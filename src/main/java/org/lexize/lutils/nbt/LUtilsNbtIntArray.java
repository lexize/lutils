package org.lexize.lutils.nbt;

import org.lexize.lutils.LUtils;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.moon.figura.lua.LuaWhitelist;

import java.util.Arrays;

@LuaWhitelist
public class LUtilsNbtIntArray extends LUtilsNbtValue<int[]> implements LUtilsNbtValue.NbtArray<Integer> {

    public LUtilsNbtIntArray() {
    }

    public LUtilsNbtIntArray(int[] value) {
        super(value);
    }

    public LUtilsNbtIntArray(LuaTable table) {
        LuaValue[] keys = table.keys();
        int[] ints = new int[keys.length];
        for (int i = 0; i < keys.length; i++) {
            ints[i] = table.get(keys[i]).toint();
        }
        value = ints;
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
            int v = value[i];
            byte[] valueArray = new byte[]{
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
        return 11;
    }

    @Override
    public String typeName() {
        return "TAG_Int_Array";
    }

    @Override
    public NbtReturnValue getValue(byte[] bytes, int offset) {
        int nameLength = (bytes[offset + 1] << 8) + (bytes[offset + 2]);
        byte[] nameBytes = Arrays.copyOfRange(bytes, 3 + offset, 3 + offset + nameLength);
        int prefixOffset = 3 + offset + nameLength;

        int length = ((bytes[prefixOffset] & 0xFF) << 24) + ((bytes[prefixOffset+1] & 0xFF) << 16) +
                ((bytes[prefixOffset+2] & 0xFF) << 8) + ((bytes[prefixOffset+3] & 0xFF));

        int valueOffset = prefixOffset + 4;
        int[] values = new int[length];

        for (int i = 0; i < length; i++) {
            byte[] valBytes = Arrays.copyOfRange(bytes, valueOffset, valueOffset + 4);
            valueOffset += 4;
            int v = ((valBytes[0] & 0xFF) << 24) + ((valBytes[1] & 0xFF) << 16) +
                    ((valBytes[2] & 0xFF) << 8) + ((valBytes[3] & 0xFF));
            values[i] = v;
        }

        return new NbtReturnValue<>(new String(nameBytes), new LUtilsNbtIntArray(values), valueOffset);
    }

    @Override
    public NbtReturnValue getPureValue(byte[] bytes, int offset) {
        int length = ((bytes[offset] & 0xFF) << 24) + ((bytes[offset+1] & 0xFF) << 16) +
                ((bytes[offset+2] & 0xFF) << 8) + ((bytes[offset+3] & 0xFF));

        int valueOffset = offset + 4;
        int[] values = new int[length];

        for (int i = 0; i < length; i++) {
            byte[] valBytes = Arrays.copyOfRange(bytes, valueOffset, valueOffset + 4);
            valueOffset += 4;
            int v = ((valBytes[0] & 0xFF) << 24) + ((valBytes[1] & 0xFF) << 16) +
                    ((valBytes[2] & 0xFF) << 8) + ((valBytes[3] & 0xFF));
            values[i] = v;
        }

        return new NbtReturnValue<>(null, new LUtilsNbtIntArray(values), valueOffset);
    }

    @Override
    public String toString() {
        return toString("");
    }

    @Override
    public String toString(String name) {
        return "%s('%s'): [%s integers]".formatted(typeName(), name, value.length);
    }

    @Override
    public void __newIndex(int i, LuaValue v) {
        int n;
        if (v.isint()) n = v.checkint();
        else throw new RuntimeException("Value should be integer");
        set(i, n);
    }

    @Override
    public void set(int i, Integer v) {
        value[i] = v;
    }

    @Override
    public LuaValue __index(int i) {
        return LuaValue.valueOf(get(i));
    }

    @Override
    public Integer get(int i) {
        return value[i];
    }
}
