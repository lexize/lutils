package org.lexize.lutils;

import org.luaj.vm2.LuaNil;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.moon.figura.lua.LuaWhitelist;

import java.util.Arrays;

@LuaWhitelist
public class LVarargs extends LuaValue {

    private LuaValue _firstValue;
    private LuaValue[] _values;

    public LVarargs(LuaValue... values) {
        _values = values;
    }

    @Override
    public int type() {
        return LuaValue.TVALUE;
    }

    @Override
    public String typename() {
        return "Varargs";
    }

    @Override
    public LuaValue arg(int i) {
        try {
            return _values[i-1];
        }
        catch (Exception e) {
            return LuaValue.NIL;
        }
    }

    @Override
    public int narg() {
        return _values.length;
    }

    @Override
    public LuaValue arg1() {
        return _values[0];
    }

    @Override
    public Varargs subargs(int start) {
        return new LVarargs(Arrays.copyOfRange(_values, start-1, _values.length));
    }
}
