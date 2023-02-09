package org.lexize.lutils.misc;

import org.moon.figura.lua.LuaWhitelist;

@LuaWhitelist
public class LFuture<T> {
    private boolean isDone;
    private T value;
    public LFuture() {

    }

    @LuaWhitelist
    public boolean isDone() {
        return isDone;
    }

    @LuaWhitelist
    public T get() {
        return value;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
