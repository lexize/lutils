package org.lexize.lutils.nbt;

import org.lexize.lutils.LUtils;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.moon.figura.lua.LuaWhitelist;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@LuaWhitelist
public class LUtilsNbtCompound extends LUtilsNbtValue<Map<String, LUtilsNbtValue>>{

    public LUtilsNbtCompound() {
        value = new HashMap<>();
    }

    public LUtilsNbtCompound(Map<String, LUtilsNbtValue> value) {
        super(value);
    }

    public LUtilsNbtCompound(LuaTable compoundTable) {
        setCompound(compoundTable);
    }

    @Override
    public byte[] getPureData() {
        byte[][] valueArrays = new byte[value.size()+1][];

        int i = 0;
        for (Map.Entry<String, LUtilsNbtValue> entry:
                value.entrySet()) {
            valueArrays[i] = entry.getValue().getBytes(entry.getKey());
            i++;
        }
        valueArrays[value.size()] = new byte[] {0};

        byte[] valueBytes = LUtils.Utils.combineByteArrays(valueArrays);

        return valueBytes;
    }

    @Override
    public byte typeId() {
        return 10;
    }

    @Override
    public String typeName() {
        return "TAG_Compound";
    }

    @Override
    public NbtReturnValue getValue(byte[] bytes, int offset) {
        int nameLength = (bytes[offset + 1] << 8) + (bytes[offset + 2]);
        byte[] nameBytes = Arrays.copyOfRange(bytes, offset+3, offset+3+nameLength);
        int valueOffset = offset+3+nameLength;

        HashMap<String, LUtilsNbtValue> compoundMap = new HashMap<>();

        NbtType type;
        while ((type = NbtType.getById(bytes[valueOffset])) != null) {
            NbtReturnValue v = type.typeGetFunction.apply(bytes,valueOffset);
            compoundMap.put(v.name(), v.value());
            valueOffset = v.newOffset();
        }
        return new NbtReturnValue<>(new String(nameBytes), new LUtilsNbtCompound(compoundMap), valueOffset+1);
    }

    @Override
    public NbtReturnValue getPureValue(byte[] bytes, int offset) {
        int valueOffset = offset;

        HashMap<String, LUtilsNbtValue> compoundMap = new HashMap<>();

        NbtType type;
        while ((type = NbtType.getById(bytes[valueOffset])) != null) {
            NbtReturnValue v = type.typeGetFunction.apply(bytes,valueOffset);
            compoundMap.put(v.name(), v.value());
            valueOffset = v.newOffset();
        }
        return new NbtReturnValue<>(null, new LUtilsNbtCompound(compoundMap), valueOffset+1);
    }

    @LuaWhitelist
    public LUtilsNbtValue put(String k, LUtilsNbtValue v) {
        return value.put(k,v);
    }

    @LuaWhitelist
    public LUtilsNbtValue get(String k) {
        return value.get(k);
    }

    @LuaWhitelist
    public LUtilsNbtValue remove(String k) {
        return value.remove(k);
    }

    @LuaWhitelist
    public void setCompound(LuaTable table) {
        HashMap<String, LUtilsNbtValue> values = new HashMap<>();
        for (LuaValue k:
                table.keys()) {
            if (k.isnil()) throw new RuntimeException("List cant contain nulls");
            if(!(k.checkuserdata() instanceof LUtilsNbtValue<?> value)) throw new RuntimeException("List can contain only NBT tags");
            String key = k.isstring() ? k.checkjstring() : k.toString();
            values.put(key, value);
        }
        setCompound(values);
    }

    public void setCompound(Map<String, LUtilsNbtValue> compound) {
        value = compound;
    }

    @LuaWhitelist
    public void clear() {
        value.clear();
    }

    @LuaWhitelist
    public boolean containsKey(String k) {
        return value.containsKey(k);
    }

    @LuaWhitelist
    public LUtilsNbtValue __index(LuaValue index) {
        String k;
        if (index.isstring()) k = index.checkjstring();
        else return null;
        return get(k);
    }

    @LuaWhitelist
    public void __newindex(LuaValue index, LUtilsNbtValue v) {
        String k;
        if (index.isstring()) k = index.checkjstring();
        else return;
        put(k, v);
    }

    @Override
    public String toString() {
        return toString("");
    }

    @Override
    public String toString(String name) {
        StringBuilder listStringBuilder = new StringBuilder();
        listStringBuilder.append("%s('%s'): %s entries\n".formatted(typeName(),name, value.size()));
        listStringBuilder.append("{\n");
        for (Map.Entry<String, LUtilsNbtValue> v:
                value.entrySet()) {
            String[] splittedStrings = v.getValue().toString(v.getKey()).split("\n");
            for (String s:
                    splittedStrings) {
                listStringBuilder.append("\t"+s+"\n");
            }
        }
        listStringBuilder.append("}");
        return listStringBuilder.toString();
    }
}
