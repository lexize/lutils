package org.lexize.lutils.submodules;

import org.lexize.lutils.LUtils;
import org.lexize.lutils.LVarargs;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.moon.figura.lua.LuaNotNil;
import org.moon.figura.lua.LuaWhitelist;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@LuaWhitelist
public class LUtilsHttp {

    private HttpClient _client = HttpClient.newBuilder().build();

    @LuaWhitelist
    public LVarargs get(@LuaNotNil String uri, LuaTable headers) throws IOException, InterruptedException {
        if (headers == null) headers = new LuaTable();

        HttpRequest.Builder requestBuilder = requestBuilderByUriAndHeaders(uri, headers);
        requestBuilder.GET();

        HttpResponse<byte[]> response = _client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofByteArray());

        return lVarargsByResponse(response);
    }

    @LuaWhitelist
    public LVarargs delete(@LuaNotNil String uri, LuaTable headers) throws IOException, InterruptedException {
        if (headers == null) headers = new LuaTable();

        HttpRequest.Builder requestBuilder = requestBuilderByUriAndHeaders(uri, headers);
        requestBuilder.DELETE();

        HttpResponse<byte[]> response = _client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofByteArray());

        return lVarargsByResponse(response);
    }

    @LuaWhitelist
    public LVarargs post(@LuaNotNil String uri, @LuaNotNil LuaValue data, LuaTable headers) throws IOException, InterruptedException {
        if (headers == null) headers = new LuaTable();
        byte[] bytes = luaValueToBytes(data);


        HttpRequest.Builder requestBuilder = requestBuilderByUriAndHeaders(uri, headers);
        requestBuilder.POST(HttpRequest.BodyPublishers.ofByteArray(bytes));

        HttpResponse<byte[]> response = _client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofByteArray());

        return lVarargsByResponse(response);
    }

    @LuaWhitelist
    public LVarargs put(@LuaNotNil String uri, @LuaNotNil LuaValue data, LuaTable headers) throws IOException, InterruptedException {
        if (headers == null) headers = new LuaTable();
        byte[] bytes = luaValueToBytes(data);

        HttpRequest.Builder requestBuilder = requestBuilderByUriAndHeaders(uri, headers);
        requestBuilder.PUT(HttpRequest.BodyPublishers.ofByteArray(bytes));

        HttpResponse<byte[]> response = _client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofByteArray());

        return lVarargsByResponse(response);
    }

    @LuaWhitelist
    public LVarargs method(@LuaNotNil String uri, @LuaNotNil String methodName, @LuaNotNil LuaValue data, LuaTable headers) throws IOException, InterruptedException {
        if (headers == null) headers = new LuaTable();
        byte[] bytes = luaValueToBytes(data);

        HttpRequest.Builder requestBuilder = requestBuilderByUriAndHeaders(uri, headers);
        requestBuilder.method(methodName, HttpRequest.BodyPublishers.ofByteArray(bytes));
        HttpResponse<byte[]> response = _client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofByteArray());

        return lVarargsByResponse(response);
    }

    private HttpRequest.Builder requestBuilderByUriAndHeaders(String uri, LuaTable headers) {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        requestBuilder.uri(URI.create(uri));
        for (LuaValue k:
                headers.keys()) {
            requestBuilder.setHeader(k.checkjstring(), headers.get(k).checkjstring());
        }
        return requestBuilder;
    }

    private LVarargs lVarargsByResponse(HttpResponse<byte[]> response) {
        LuaTable headers = new LuaTable();

        for (Map.Entry<String, List<String>> header:
             response.headers().map().entrySet()) {
            LuaTable headerValues = new LuaTable();
            for (String v:
                    header.getValue()) {
                headerValues.set(headerValues.length()+1, v);
            }
            headers.set(header.getKey(), headerValues);
        }

        return new LVarargs(
                LUtils.Utils.byteArrayToTable(response.body()),
                LuaValue.valueOf(response.statusCode()),
                headers
        );
    }

    private byte[] luaValueToBytes(LuaValue data) {
        switch (data.type()) {
            case (LuaValue.TTABLE):
                return LUtils.Utils.tableToByteArray(data.checktable());
            case (LuaValue.TSTRING):
                return data.checkjstring().getBytes(StandardCharsets.UTF_8);
            default:
                throw new LuaError("Data should be either string or byte array.");
        }
    }
}