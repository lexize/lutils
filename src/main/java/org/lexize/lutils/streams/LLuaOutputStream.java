package org.lexize.lutils.streams;

import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.moon.figura.lua.LuaWhitelist;

import java.io.IOException;

@LuaWhitelist
public class LLuaOutputStream extends LOutputStream {
    private LuaFunction onWrite, onFlush, onClose;
    @Override
    public void write(int i) throws IOException {
        onWrite.invoke(LuaValue.valueOf(i));
    }

    @Override
    public void flush() throws IOException {
        onFlush.invoke();
    }

    @Override
    public void close() throws IOException {
        onClose.invoke();
    }

    @LuaWhitelist
    public Object __index(LuaValue key) {
        if (!key.isstring()) return null;
        return switch (key.tojstring()) {
            case "onWrite" -> onWrite;
            case "onFlush" -> onFlush;
            case "onClose" -> onClose;
            default -> null;
        };
    }

    @LuaWhitelist
    public void __newindex(LuaValue key, LuaValue value) {
        if (!key.isstring()) return;
        switch (key.tojstring()) {
            case "onWrite" -> onWrite = value.checkfunction();
            case "onFlush" -> onFlush = value.checkfunction();
            case "onClose" -> onClose = value.checkfunction();
        }
    }
}
