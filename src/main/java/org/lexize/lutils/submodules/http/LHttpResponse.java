package org.lexize.lutils.submodules.http;

import org.moon.figura.lua.LuaWhitelist;

import java.util.List;
import java.util.Map;

@LuaWhitelist
public class LHttpResponse<T> {
    private final T data;
    private final int code;
    private final Map<String, List<String>> headers;
    public LHttpResponse(T data, int code, Map<String, List<String>> headers) {
        this.data = data;
        this.code = code;
        this.headers = headers;
    }
    @LuaWhitelist
    public int getCode() {
        return code;
    }
    @LuaWhitelist
    public T getData() {
        return data;
    }

    @LuaWhitelist
    public Map<String, List<String>> getHeaders() {
        return headers;
    }
}
