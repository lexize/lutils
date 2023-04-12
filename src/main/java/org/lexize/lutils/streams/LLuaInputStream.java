package org.lexize.lutils.streams;

import org.lexize.lutils.annotations.LField;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.moon.figura.lua.LuaWhitelist;

import java.io.IOException;

@LuaWhitelist
@LField(value = "onRead", type = LuaFunction.class, description = "Function called when read() method is called. Should return int")
@LField(value = "onSkip", type = LuaFunction.class, description = "Function called when skip() method is called. Should return long")
@LField(value = "availableCount", type = LuaFunction.class, description = "Function that should return amount of bytes available to read")
@LField(value = "onClose", type = LuaFunction.class, description = "Function called when close() method is called.")
@LField(value = "onMark", type = LuaFunction.class, description = "Function called when mark() method is called.")
@LField(value = "onReset", type = LuaFunction.class, description = "Function called when reset() method is called.")
@LField(value = "isMarkSupported", type = LuaFunction.class, description = "Function that should return true if mark supported for this stream, false otherwise")
public class LLuaInputStream extends LInputStream{
    private LuaFunction onRead, onSkip, availableCount, onClose, onMark, onReset, isMarkSupported;
    @Override
    public int read() throws IOException {
        return onRead.invoke().arg1().toint();
    }
    @Override
    public long skip(long n) throws IOException {
        return onSkip.invoke(LuaValue.valueOf(n)).arg1().tolong();
    }
    @Override
    public int available() throws IOException {
        return availableCount.invoke().arg1().toint();
    }
    @Override
    public void close() throws IOException {
        onClose.invoke();
    }
    @Override
    public void mark(int readlimit) {
        onMark.invoke(LuaValue.valueOf(readlimit));
    }
    @Override
    public void reset() throws IOException {
        onReset.invoke();
    }
    @Override
    public boolean markSupported() {
        return isMarkSupported.invoke().arg1().toboolean();
    }
    @LuaWhitelist
    public Object __index(LuaValue key) {
        if (!key.isstring()) return null;
        return switch (key.tojstring()) {
            case "onRead" -> onRead;
            case "onSkip" -> onSkip;
            case "availableCount" -> availableCount;
            case "onClose" -> onClose;
            case "onMark" -> onMark;
            case "onReset" -> onReset;
            case "isMarkSupported" -> isMarkSupported;
            default -> null;
        };
    }
    @LuaWhitelist
    public void __newindex(LuaValue key, LuaValue value) {
        if (!key.isstring()) return;
        switch (key.tojstring()) {
            case "onRead" -> onRead = value.checkfunction();
            case "onSkip" -> onSkip = value.checkfunction();
            case "availableCount" -> availableCount = value.checkfunction();
            case "onClose" -> onClose = value.checkfunction();
            case "onMark" -> onMark = value.checkfunction();
            case "onReset" -> onReset = value.checkfunction();
            case "isMarkSupported" -> isMarkSupported = value.checkfunction();
        }
    }

}
