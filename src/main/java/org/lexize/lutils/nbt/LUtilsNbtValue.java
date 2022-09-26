package org.lexize.lutils.nbt;

import org.lexize.lutils.LUtils;
import org.luaj.vm2.LuaValue;
import org.moon.figura.lua.LuaWhitelist;

import java.nio.charset.StandardCharsets;
import java.util.function.BiFunction;

public abstract class LUtilsNbtValue <T> {

    public enum NbtType {
        Byte(LUtilsNbtByte.class),
        Short(LUtilsNbtShort.class),
        Integer(LUtilsNbtInt.class),
        Long(LUtilsNbtLong.class),
        Float(LUtilsNbtFloat.class),
        Double(LUtilsNbtDouble.class),
        ByteArray(LUtilsNbtByteArray.class),
        String(LUtilsNbtString.class),
        List(LUtilsNbtList.class),
        Compound(LUtilsNbtCompound.class),
        IntArray(LUtilsNbtIntArray.class),
        LongArray(LUtilsNbtLongArray.class)
        ;

        public final String typeName;
        public final int typeId;
        public final Class<? extends LUtilsNbtValue> typeClass;

        public final BiFunction<byte[], Integer, NbtReturnValue> typeGetFunction;
        public final BiFunction<byte[], Integer, NbtReturnValue> typeGetPureFunction;

        NbtType(Class<? extends LUtilsNbtValue> typeClass){
            try {
                this.typeClass = typeClass;

                LUtilsNbtValue v = typeClass.getConstructor().newInstance();
                typeGetFunction = v::getValue;
                typeGetPureFunction = v::getPureValue;
                this.typeName = v.typeName();
                this.typeId = v.typeId();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        public static NbtType getById(byte id) {
            for (NbtType type:
                 NbtType.values()) {
                if (type.typeId == id) return type;
            }
            return null;
        }

    }

    protected T value;

    protected static final int _baseByteCount = 65538;

    public LUtilsNbtValue() {};
    public LUtilsNbtValue(T value) {
        this.value = value;
    }

    public byte[] getBytes(String name) {
        return getBytesByValue(name, getPureData());
    }

    public abstract byte[] getPureData();

    protected byte[] getBytesByValue(String name, byte[] valueBytes) {
        byte[] nameBytes = name.getBytes(StandardCharsets.UTF_8);
        if (nameBytes.length > 0xFFFF) throw new RuntimeException("Name length (%s) more than %s".formatted(nameBytes.length, 0xffff));
        byte[] header = new byte[] {
                typeId(), (byte) ((nameBytes.length >> 8) & 0xFF), (byte) ((nameBytes.length) & 0xFF)
        };
        return LUtils.Utils.combineByteArrays(header, nameBytes, valueBytes);
    }

    public abstract byte typeId();

    public abstract String typeName();

    public abstract NbtReturnValue getValue(byte[] bytes, int offset);
    public abstract NbtReturnValue getPureValue(byte[] bytes, int offset);

    @Override
    public String toString() {
        return "%s(''): %s".formatted(typeName(), value);
    }

    public String toString(String name) {
        return "%s('%s'): %s".formatted(typeName(), name, value);
    }

    public record NbtReturnValue <T extends LUtilsNbtValue>(String name, T value, int newOffset){}

    public static interface NbtGettable<T> {

        T get();

        @LuaWhitelist
        Object __index(LuaValue n);
    }

    public static interface NbtSettable<T> {
        public void set(T v);

        @LuaWhitelist
        public void __newindex(LuaValue n, LuaValue value);
    }

    public interface NbtArray<T> {

        @LuaWhitelist
        public void __newIndex(int i, LuaValue v);
        public void set(int i, T v);

        @LuaWhitelist
        public LuaValue __index(int i);
        public T get(int i);
    }
}
