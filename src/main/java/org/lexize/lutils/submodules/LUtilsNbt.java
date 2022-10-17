package org.lexize.lutils.submodules;

import org.lexize.lutils.LUtils;
import org.lexize.lutils.LVarargs;
import org.lexize.lutils.submodules.nbt.*;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.moon.figura.avatar.Avatar;
import org.moon.figura.lua.LuaNotNil;
import org.moon.figura.lua.LuaTypeManager;
import org.moon.figura.lua.LuaWhitelist;

@LuaWhitelist
public class LUtilsNbt {

    private final LuaTypeManager _ltm;

    public LUtilsNbt(Avatar avatar) {
        _ltm = avatar.luaRuntime.typeManager;
    }

    @LuaWhitelist
    public LUtilsNbtByte nbtByte(@LuaNotNil int b) {
        return new LUtilsNbtByte((byte) b);
    }

    @LuaWhitelist
    public LUtilsNbtShort nbtShort(@LuaNotNil int s) {
        return new LUtilsNbtShort((short) s);
    }

    @LuaWhitelist
    public LUtilsNbtInt nbtInt(@LuaNotNil int i) {
        return new LUtilsNbtInt(i);
    }

    @LuaWhitelist
    public LUtilsNbtLong nbtLong(@LuaNotNil long l) {
        return new LUtilsNbtLong(l);
    }

    @LuaWhitelist
    public LUtilsNbtFloat nbtFloat(@LuaNotNil float f) {
        return new LUtilsNbtFloat(f);
    }

    @LuaWhitelist
    public LUtilsNbtDouble nbtDouble(@LuaNotNil double d) {
        return new LUtilsNbtDouble(d);
    }

    @LuaWhitelist
    public LUtilsNbtString nbtString(@LuaNotNil String s) {
        return new LUtilsNbtString(s);
    }

    @LuaWhitelist
    public LUtilsNbtCompound nbtCompound(@LuaNotNil LuaTable table) {
        return new LUtilsNbtCompound(table);
    }

    @LuaWhitelist
    public LUtilsNbtList nbtList(@LuaNotNil LuaTable table) {
        return new LUtilsNbtList(table);
    }

    @LuaWhitelist
    public LUtilsNbtByteArray nbtByteArray(@LuaNotNil LuaTable table) {
        return new LUtilsNbtByteArray(table);
    }

    @LuaWhitelist
    public LUtilsNbtIntArray nbtIntArray(@LuaNotNil LuaTable table) {
        return new LUtilsNbtIntArray(table);
    }

    @LuaWhitelist
    public LUtilsNbtLongArray nbtLongArray(@LuaNotNil LuaTable table) {
        return new LUtilsNbtLongArray(table);
    }

    @LuaWhitelist
    public LuaTable toBytes(@LuaNotNil LUtilsNbtCompound compound, String title) {
        return LUtils.Utils.byteArrayToTable(compound.getBytes(title != null ? title : ""));
    }

    @LuaWhitelist
    public Varargs fromBytes(@LuaNotNil LuaTable bytesTable) {
        byte[] bytes = LUtils.Utils.tableToByteArray(bytesTable);
        LUtilsNbtValue.NbtReturnValue val = LUtilsNbtValue.NbtType.Compound.typeGetFunction.apply(bytes, 0);
        return LuaValue.varargsOf(LuaValue.valueOf(val.name()), _ltm.javaToLua(val.value()));
    }

}
