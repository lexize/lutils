package org.lexize.lutils.misc;

import org.lexize.lutils.annotations.LDescription;
import org.moon.figura.lua.LuaWhitelist;

@LuaWhitelist
public class LFuture<T> {
    private boolean isDone;
    private T value;
    public LFuture() {

    }

    @LuaWhitelist
    @LDescription("Is async task done")
    public boolean isDone() {
        return isDone;
    }

    @LuaWhitelist
    @LDescription("Return value of async task")
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
