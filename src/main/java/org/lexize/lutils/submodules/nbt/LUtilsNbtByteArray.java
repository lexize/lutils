package org.lexize.lutils.submodules.nbt;

import org.lexize.lutils.LUtils;
import org.lexize.lutils.submodules.streams.LUtilsInputStream;
import org.lexize.lutils.submodules.streams.LUtilsOutputStream;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.moon.figura.lua.LuaWhitelist;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

@LuaWhitelist
public class LUtilsNbtByteArray extends LUtilsNbtValue<byte[]> implements LUtilsNbtValue.NbtArray<Byte> {

    public LUtilsNbtByteArray() {
    }

    public LUtilsNbtByteArray(byte[] value) {
        super(value);
    }
    public LUtilsNbtByteArray(LuaTable table) {
        LuaValue[] keys = table.keys();
        byte[] bytes = new byte[keys.length];
        for (int i = 0; i < keys.length; i++) {
            bytes[i] = table.get(keys[i]).tobyte();
        }
        value = bytes;
    }

    @Override
    public byte[] getPureData() {
        byte[] prefix = new byte[] {
                (byte) ((value.length >> 24) & 0xFF),
                (byte) ((value.length >> 16) & 0xFF),
                (byte) ((value.length >> 8) & 0xFF),
                (byte) (value.length & 0xFF)
        };
        return LUtils.Utils.combineByteArrays(prefix, value);
    }

    @Override
    public void writePureData(LUtilsOutputStream luos) throws IOException {
        OutputStream os = luos.getOutputStream();
        byte[] prefix = new byte[] {
                (byte) ((value.length >> 24) & 0xFF),
                (byte) ((value.length >> 16) & 0xFF),
                (byte) ((value.length >> 8) & 0xFF),
                (byte) (value.length & 0xFF)
        };
        os.write(prefix);
        os.write(value);
    }

    @Override
    public byte typeId() {
        return 7;
    }

    @Override
    public String typeName() {
        return "TAG_Byte_Array";
    }

    @Override
    public NbtReturnValue getValue(byte[] bytes, int offset) {
        int nameLength = (bytes[offset + 1] << 8) + (bytes[offset + 2]);
        byte[] nameBytes = Arrays.copyOfRange(bytes, 3 + offset, 3 + offset + nameLength);
        int prefixOffset = 3 + offset + nameLength;
        byte[] arrayLengthBytes = Arrays.copyOfRange(bytes, prefixOffset, prefixOffset+4);
        int arrayLength = ((arrayLengthBytes[0] & 0xFF) << 24) + ((arrayLengthBytes[1] & 0xFF) << 16) +
                ((arrayLengthBytes[2] & 0xFF) << 8) + ((arrayLengthBytes[3] & 0xFF));
        int valueOffset = prefixOffset + 4;
        byte[] array = Arrays.copyOfRange(bytes, valueOffset, valueOffset+arrayLength);
        return new NbtReturnValue<>(new String(nameBytes), new LUtilsNbtByteArray(array), valueOffset+arrayLength);
    }

    @Override
    public NbtReturnValue getValue(LUtilsInputStream stream) {
        try {
            InputStream is = stream.getInputStream();
            byte[] nameLengthBytes = is.readNBytes(2);
            int nameLength = ((nameLengthBytes[0]) << 8) + (nameLengthBytes[1]);
            byte[] nameBytes = is.readNBytes(nameLength);
            byte[] arrayLengthBytes = is.readNBytes(4);
            int arrayLength = ((arrayLengthBytes[0] & 0xFF) << 24) + ((arrayLengthBytes[1] & 0xFF) << 16) +
                    ((arrayLengthBytes[2] & 0xFF) << 8) + ((arrayLengthBytes[3] & 0xFF));
            byte[] array = is.readNBytes(arrayLength);
            return new NbtReturnValue<>(new String(nameBytes), new LUtilsNbtByteArray(array), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public NbtReturnValue getPureValue(byte[] bytes, int offset) {
        int prefixOffset = offset;
        byte[] arrayLengthBytes = Arrays.copyOfRange(bytes, prefixOffset, prefixOffset+4);
        int arrayLength = ((arrayLengthBytes[0] & 0xFF) << 24) + ((arrayLengthBytes[1] & 0xFF) << 16) +
                ((arrayLengthBytes[2] & 0xFF) << 8) + ((arrayLengthBytes[3] & 0xFF));
        int valueOffset = prefixOffset + 4;
        byte[] array = Arrays.copyOfRange(bytes, valueOffset, valueOffset+arrayLength);
        return new NbtReturnValue<>(null, new LUtilsNbtByteArray(array), valueOffset+arrayLength);
    }

    @Override
    public NbtReturnValue getPureValue(LUtilsInputStream stream) {
        try {
            InputStream is = stream.getInputStream();
            byte[] arrayLengthBytes = is.readNBytes(4);
            int arrayLength = ((arrayLengthBytes[0] & 0xFF) << 24) + ((arrayLengthBytes[1] & 0xFF) << 16) +
                    ((arrayLengthBytes[2] & 0xFF) << 8) + ((arrayLengthBytes[3] & 0xFF));
            byte[] array = is.readNBytes(arrayLength);
            return new NbtReturnValue<>(null, new LUtilsNbtByteArray(array), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return toString("");
    }

    @Override
    public String toString(String name) {
        return "%s('%s'): [%s bytes]".formatted(typeName(), name, value.length);
    }

    @Override
    public void __newIndex(int i, LuaValue v) {
        int n;
        if (v.isint()) n = v.checkint();
        else throw new RuntimeException("Value should be integer");
        if (n >= Byte.MIN_VALUE && n <= Byte.MAX_VALUE) set(i, (byte) n);
        else throw new RuntimeException("Value (%s) isn't in range from -128 to 127".formatted(n));
    }

    @Override
    public void set(int i, Byte v) {
        value[i] = v;
    }

    @Override
    public LuaValue __index(int i) {
        return LuaValue.valueOf(get(i));
    }

    @Override
    public Byte get(int i) {
        return value[i];
    }
}
