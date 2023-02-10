package org.lexize.lutils.submodules.http;

import org.lexize.lutils.annotations.LDocsFuncOverload;
import org.luaj.vm2.LuaTable;
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
    @LDocsFuncOverload(
            description = "Returns response code",
            returnType = int.class
    )
    public int getCode() {
        return code;
    }
    @LuaWhitelist
    @LDocsFuncOverload(
            description = "Returns response data",
            returnType = Object.class
    )
    public T getData() {
        return data;
    }

    @LuaWhitelist
    @LDocsFuncOverload(
            description = "Returns response headers",
            returnType = LuaTable.class
    )
    public Map<String, List<String>> getHeaders() {
        return headers;
    }
}
