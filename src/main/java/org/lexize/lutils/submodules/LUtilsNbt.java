package org.lexize.lutils.submodules;

import org.lexize.ldocs.annotations.LDocsDescription;
import org.lexize.ldocs.annotations.LDocsInclude;
import org.lexize.ldocs.annotations.LDocsProperty;
import org.lexize.lutils.LUtils;
import org.lexize.lutils.LVarargs;
import org.lexize.lutils.submodules.nbt.*;
import org.lexize.lutils.submodules.streams.LUtilsInputStream;
import org.lexize.lutils.submodules.streams.LUtilsOutputStream;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.moon.figura.avatar.Avatar;
import org.moon.figura.lua.LuaNotNil;
import org.moon.figura.lua.LuaTypeManager;
import org.moon.figura.lua.LuaWhitelist;

import java.io.IOException;

@LuaWhitelist
@LDocsDescription("NBT submodule of LUtils")
@LDocsProperty(name = "important", stringValue = "/lutils/important/experimental.txt", fromResource = true)
public class LUtilsNbt {

    public LUtilsNbt() {

    }

    @LuaWhitelist
    @LDocsInclude
    @LDocsDescription("Creates Byte tag by given value")
    public LUtilsNbtByte nbtByte(@LuaNotNil int b) {
        return new LUtilsNbtByte((byte) b);
    }

    @LuaWhitelist
    @LDocsInclude
    @LDocsDescription("Creates Short tag by given value")
    public LUtilsNbtShort nbtShort(@LuaNotNil int s) {
        return new LUtilsNbtShort((short) s);
    }

    @LuaWhitelist
    @LDocsInclude
    @LDocsDescription("Creates Int tag by given value")
    public LUtilsNbtInt nbtInt(@LuaNotNil int i) {
        return new LUtilsNbtInt(i);
    }

    @LuaWhitelist
    @LDocsInclude
    @LDocsDescription("Creates Long tag by given value")
    public LUtilsNbtLong nbtLong(@LuaNotNil long l) {
        return new LUtilsNbtLong(l);
    }

    @LuaWhitelist
    @LDocsInclude
    @LDocsDescription("Creates Float tag by given value")
    public LUtilsNbtFloat nbtFloat(@LuaNotNil float f) {
        return new LUtilsNbtFloat(f);
    }

    @LuaWhitelist
    @LDocsInclude
    @LDocsDescription("Creates Double tag by given value")
    public LUtilsNbtDouble nbtDouble(@LuaNotNil double d) {
        return new LUtilsNbtDouble(d);
    }

    @LuaWhitelist
    @LDocsInclude
    @LDocsDescription("Creates String tag by given value")
    public LUtilsNbtString nbtString(@LuaNotNil String s) {
        return new LUtilsNbtString(s);
    }

    @LuaWhitelist
    @LDocsInclude
    @LDocsDescription("Creates Compound tag by given table")
    public LUtilsNbtCompound nbtCompound(@LuaNotNil LuaTable table) {
        return new LUtilsNbtCompound(table);
    }

    @LuaWhitelist
    @LDocsInclude
    @LDocsDescription("Creates List tag by given table")
    public LUtilsNbtList nbtList(@LuaNotNil LuaTable table) {
        return new LUtilsNbtList(table);
    }

    @LuaWhitelist
    @LDocsInclude
    @LDocsDescription("Creates ByteArray tag by given table")
    public LUtilsNbtByteArray nbtByteArray(@LuaNotNil LuaTable table) {
        return new LUtilsNbtByteArray(table);
    }

    @LuaWhitelist
    @LDocsInclude
    @LDocsDescription("Creates IntArray tag by given table")
    public LUtilsNbtIntArray nbtIntArray(@LuaNotNil LuaTable table) {
        return new LUtilsNbtIntArray(table);
    }

    @LuaWhitelist
    @LDocsInclude
    @LDocsDescription("Creates LongArray tag by given table")
    public LUtilsNbtLongArray nbtLongArray(@LuaNotNil LuaTable table) {
        return new LUtilsNbtLongArray(table);
    }

    @LuaWhitelist
    @LDocsInclude
    @LDocsDescription("Converts given tag to bytes table")
    public LuaTable toBytes(@LuaNotNil LUtilsNbtValue value, String title) {
        return LUtils.Utils.byteArrayToTable(value.getBytes(title != null ? title : ""));
    }

    @LuaWhitelist
    @LDocsInclude
    @LDocsDescription("Converts bytes table to tag")
    @LDocsProperty(name = "use_varargs", boolValue = true)
    @LDocsProperty(name = "varargs", classValue = {LUtilsNbtValue.class, String.class, Integer.class})
    public Object fromBytes(@LuaNotNil LuaTable bytesTable, Integer offset) {
        if (offset == null) offset = 0;
        LUtilsNbtValue.NbtReturnValue val = LUtilsNbtValue.readFromBytes(bytesTable, offset);
        return new Object[] {val.value(), val.name(), val.newOffset()};
    }

    @LuaWhitelist
    @LDocsInclude
    @LDocsDescription("Writes tag data into output stream")
    public void toStream(@LuaNotNil LUtilsNbtValue value, @LuaNotNil LUtilsOutputStream luos, String title) {
        value.writeBytes(title != null ? title : "", luos);
    }

    @LuaWhitelist
    @LDocsInclude
    @LDocsDescription("Reads tag data from input stream")
    @LDocsProperty(name = "use_varargs", boolValue = true)
    @LDocsProperty(name = "varargs", classValue = {LUtilsNbtValue.class, String.class})
    public Object fromStream(@LuaNotNil LUtilsInputStream luis) throws IOException {
        LUtilsNbtValue.NbtReturnValue val = LUtilsNbtValue.readFromStream(luis);
        return new Object[] {val.value(), val.name()};
    }

}
