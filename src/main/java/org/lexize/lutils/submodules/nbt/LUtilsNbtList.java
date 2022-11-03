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
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;

@LuaWhitelist
public class LUtilsNbtList extends LUtilsNbtValue<List<LUtilsNbtValue>> {

    private final static BiFunction<NbtType, NbtType, RuntimeException> WrongTypeExceptionBuilder;

    static {
        WrongTypeExceptionBuilder = new BiFunction<NbtType, NbtType, RuntimeException>() {
            @Override
            public RuntimeException apply(NbtType type, NbtType type2) {
                return new RuntimeException("Tag with type \"%s\"(%s) cant be contained in list with type \"%s\"(%s)".formatted(
                        type.typeName, type.typeId,
                        type2.typeName, type2.typeId
                ));
            }
        };
    }

    public byte containedTypeId() {
        return value.size() > 0 ? value.get(0).typeId() : 0;
    }

    public LUtilsNbtList(List<LUtilsNbtValue> value) {
        super(value);
    }

    public LUtilsNbtList() {
        super(new LinkedList<LUtilsNbtValue>());
    }

    public LUtilsNbtList(LuaTable table) {
        setList(table);
    }


    @Override
    public byte[] getPureData() {
        int length = value.size();
        byte[] prefix = new byte[] {
                containedTypeId(),
                (byte) ((length >> 24) & 0xFF), (byte) ((length >> 16) & 0xFF), (byte) ((length >> 8) & 0xFF), (byte) (length & 0xFF)
        };
        byte[][] valueArrays = new byte[length][];
        for (int i = 0; i < length; i++) {
            valueArrays[i] = value.get(i).getPureData();
        }
        byte[] valueBytes = LUtils.Utils.combineByteArrays(valueArrays);
        return LUtils.Utils.combineByteArrays(prefix, valueBytes);
    }

    @Override
    public void writePureData(LUtilsOutputStream luos) throws IOException {
        OutputStream os = luos.getOutputStream();
        int length = value.size();
        byte[] prefix = new byte[] {
                containedTypeId(),
                (byte) ((length >> 24) & 0xFF), (byte) ((length >> 16) & 0xFF), (byte) ((length >> 8) & 0xFF), (byte) (length & 0xFF)
        };
        os.write(prefix);
        for (int i = 0; i < length; i++) {
            value.get(i).writePureData(luos);
        }
    }

    @Override
    public byte typeId() {
        return 9;
    }

    @Override
    public String typeName() {
        return "TAG_List";
    }

    @Override
    public NbtReturnValue<LUtilsNbtList> getValue(byte[] bytes, int offset) {
        int nameLength = (bytes[offset + 1] << 8) + (bytes[offset + 2]);
        byte[] nameBytes = Arrays.copyOfRange(bytes, offset+3, offset+3+nameLength);
        int valueOffset = offset+3+nameLength;
        NbtType type = NbtType.getById(bytes[valueOffset]);
        int elementCount =
                (bytes[valueOffset+1] << 24) + (bytes[valueOffset+2] << 16) +
                (bytes[valueOffset+3] << 8) + (bytes[valueOffset+4]);
        LUtilsNbtList nbtList = new LUtilsNbtList();
        int elementOffset = valueOffset+5;
        for (int i = 0; i < elementCount; i++) {
            NbtReturnValue val = type.typeGetPureFunction.apply(bytes, elementOffset);
            nbtList.add(val.value());
            elementOffset = val.newOffset();
        }
        return new NbtReturnValue<>(new String(nameBytes), nbtList, elementOffset);
    }

    @Override
    public NbtReturnValue getValue(LUtilsInputStream stream) {
        try {
            InputStream is = stream.getInputStream();
            byte[] nameLengthBytes = is.readNBytes(2);
            int nameLength = ((nameLengthBytes[0]) << 8) + (nameLengthBytes[1]);
            byte[] nameBytes = is.readNBytes(nameLength);
            NbtType type = NbtType.getById(is.read());

            byte[] elementCountBytes = is.readNBytes(4);
            int elementCount = ((elementCountBytes[0] & 0xFF) << 24) + ((elementCountBytes[1] & 0xFF) << 16) +
                    ((elementCountBytes[2] & 0xFF) << 8) + ((elementCountBytes[3] & 0xFF));
            LUtilsNbtList nbtList = new LUtilsNbtList();
            for (int i = 0; i < elementCount; i++) {
                NbtReturnValue val = type.typeGetPureFromStreamFunction.apply(stream);
                nbtList.add(val.value());
            }
            return new NbtReturnValue<>(new String(nameBytes), nbtList, 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public NbtReturnValue getPureValue(byte[] bytes, int offset) {
        int valueOffset = offset;
        NbtType type = NbtType.getById(bytes[valueOffset]);
        int elementCount =
                (bytes[valueOffset+1] << 24) + (bytes[valueOffset+2] << 16) +
                        (bytes[valueOffset+3] << 8) + (bytes[valueOffset+4]);
        LUtilsNbtList nbtList = new LUtilsNbtList();
        int elementOffset = valueOffset+5;
        for (int i = 0; i < elementCount; i++) {
            NbtReturnValue val = type.typeGetFunction.apply(bytes, elementOffset);
            nbtList.add(val.value());
            elementOffset = val.newOffset();
        }
        return new NbtReturnValue<>(null, nbtList, elementOffset);
    }

    @Override
    public NbtReturnValue getPureValue(LUtilsInputStream stream) {
        try {
            InputStream is = stream.getInputStream();
            NbtType type = NbtType.getById(is.read());

            byte[] elementCountBytes = is.readNBytes(4);
            int elementCount = ((elementCountBytes[0] & 0xFF) << 24) + ((elementCountBytes[1] & 0xFF) << 16) +
                    ((elementCountBytes[2] & 0xFF) << 8) + ((elementCountBytes[3] & 0xFF));
            LUtilsNbtList nbtList = new LUtilsNbtList();
            for (int i = 0; i < elementCount; i++) {
                NbtReturnValue val = type.typeGetPureFromStreamFunction.apply(stream);
                nbtList.add(val.value());
            }
            return new NbtReturnValue<>(null, nbtList, 0);
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
        NbtType containedType = NbtType.getById(containedTypeId());
        StringBuilder listStringBuilder = new StringBuilder();
        listStringBuilder.append("%s('%s'[%s]): %s entries\n".formatted(typeName(),name,containedType != null ? containedType.typeName : "NONE", value.size()));
        listStringBuilder.append("{\n");
        for (LUtilsNbtValue v:
                value) {
            String[] splittedStrings = v.toString().split("\n");
            for (String s:
                 splittedStrings) {
                listStringBuilder.append("\t"+s+"\n");
            }
        }
        listStringBuilder.append("}");
        return listStringBuilder.toString();
    }

    @LuaWhitelist
    public boolean add(LUtilsNbtValue element) {
        if(containedTypeId() != 0 && element.typeId() != containedTypeId()) {
            NbtType firstType = NbtType.getById(containedTypeId());
            NbtType secondType = NbtType.getById(element.typeId());
            throw WrongTypeExceptionBuilder.apply(firstType,secondType);
        };
        if (element == null) return false;
        return this.value.add(element);
    }

    @LuaWhitelist
    public LUtilsNbtValue set(int i, LUtilsNbtValue v) {
        if(containedTypeId() != 0 && v.typeId() != containedTypeId()) {
            NbtType firstType = NbtType.getById(containedTypeId());
            NbtType secondType = NbtType.getById(v.typeId());
            throw WrongTypeExceptionBuilder.apply(firstType,secondType);
        };
        if (v == null) return remove(i);
        return value.set(i,v);
    }

    @LuaWhitelist
    public void setList(LuaTable table) {
        List<LUtilsNbtValue> values = new LinkedList<>();
        for (LuaValue k:
             table.keys()) {
            if (k.isnil()) throw new RuntimeException("List cant contain nulls");
            if(!(k.checkuserdata() instanceof LUtilsNbtValue<?> value)) throw new RuntimeException("List can contain only NBT tags");
            values.add(value);
        }
        setList(values);
    }

    public void setList(List<LUtilsNbtValue> replacementList) {
        byte t = replacementList.get(0).typeId();
        for (LUtilsNbtValue v:
             replacementList) {
            if (v.typeId() != t) throw new RuntimeException("List should contain tags with same type");
        }
        value = replacementList;
    }

    @LuaWhitelist
    public LUtilsNbtValue get(int i) {
        return value.get(i);
    }

    @LuaWhitelist
    public LUtilsNbtValue remove(int i) {
        return value.remove(i);
    }

    @LuaWhitelist
    public void clear() {
        value.clear();
    }


    @LuaWhitelist
    public LUtilsNbtValue __index(LuaValue index) {
        int i;
        if (index.isint()) i = index.checkint();
        else return null;
        return get(i);
    }

    @LuaWhitelist
    public void __newindex(LuaValue index, LUtilsNbtValue v) {
        int i;
        if (index.isint()) i = index.checkint();
        else return;
        set(i, v);
    }
}
