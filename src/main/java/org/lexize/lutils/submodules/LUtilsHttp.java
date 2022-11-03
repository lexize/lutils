package org.lexize.lutils.submodules;

import org.lexize.ldocs.annotations.LDocsDescription;
import org.lexize.ldocs.annotations.LDocsInclude;
import org.lexize.ldocs.annotations.LDocsProperty;
import org.lexize.lutils.LUtils;
import org.lexize.lutils.LVarargs;
import org.lexize.lutils.submodules.nbt.LUtilsNbtValue;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.moon.figura.avatar.Avatar;
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
@LDocsDescription("HTTP submodule of LUtils")
@LDocsProperty(name = "important", stringValue = "/lutils/important/experimental.txt", fromResource = true)
public class LUtilsHttp {
    private HttpClient _client = HttpClient.newBuilder().build();

    @LuaWhitelist
    @LDocsInclude
    @LDocsDescription("Requests data from given URI via GET method")
    @LDocsProperty(name = "use_varargs", boolValue = true)
    @LDocsProperty(name = "varargs", classValue = {LuaTable.class, Integer.class, LuaTable.class})
    @LDocsProperty(name = "varargs_descs", stringValue = {
            "Byte table of response data",
            "Status code of response",
            "Headers of response"
    })
    public Object[] get(@LuaNotNil String uri, LuaTable headers) throws IOException, InterruptedException {
        if (headers == null) headers = new LuaTable();

        HttpRequest.Builder requestBuilder = requestBuilderByUriAndHeaders(uri, headers);
        requestBuilder.GET();

        HttpResponse<byte[]> response = _client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofByteArray());

        return lVarargsByResponse(response);
    }

    @LuaWhitelist
    @LDocsInclude
    @LDocsDescription("Requests data from given URI via DELETE method")
    @LDocsProperty(name = "use_varargs", boolValue = true)
    @LDocsProperty(name = "varargs", classValue = {LuaTable.class, Integer.class, LuaTable.class})
    @LDocsProperty(name = "varargs_descs", stringValue = {
            "Byte table of response data",
            "Status code of response",
            "Headers of response"
    })
    public Object[] delete(@LuaNotNil String uri, LuaTable headers) throws IOException, InterruptedException {
        if (headers == null) headers = new LuaTable();

        HttpRequest.Builder requestBuilder = requestBuilderByUriAndHeaders(uri, headers);
        requestBuilder.DELETE();

        HttpResponse<byte[]> response = _client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofByteArray());

        return lVarargsByResponse(response);
    }

    @LuaWhitelist
    @LDocsInclude
    @LDocsDescription("Requests data from given URI via POST method")
    @LDocsProperty(name = "use_varargs", boolValue = true)
    @LDocsProperty(name = "varargs", classValue = {LuaTable.class, Integer.class, LuaTable.class})
    @LDocsProperty(name = "varargs_descs", stringValue = {
            "Byte table of response data",
            "Status code of response",
            "Headers of response"
    })
    public Object[] post(@LuaNotNil String uri, @LuaNotNil LuaValue data, LuaTable headers) throws IOException, InterruptedException {
        if (headers == null) headers = new LuaTable();
        byte[] bytes = luaValueToBytes(data);


        HttpRequest.Builder requestBuilder = requestBuilderByUriAndHeaders(uri, headers);
        requestBuilder.POST(HttpRequest.BodyPublishers.ofByteArray(bytes));

        HttpResponse<byte[]> response = _client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofByteArray());

        return lVarargsByResponse(response);
    }

    @LuaWhitelist
    @LDocsInclude
    @LDocsDescription("Requests data from given URI via PUT method")
    @LDocsProperty(name = "use_varargs", boolValue = true)
    @LDocsProperty(name = "varargs", classValue = {LuaTable.class, Integer.class, LuaTable.class})
    @LDocsProperty(name = "varargs_descs", stringValue = {
            "Byte table of response data",
            "Status code of response",
            "Headers of response"
    })
    public Object[] put(@LuaNotNil String uri, @LuaNotNil LuaValue data, LuaTable headers) throws IOException, InterruptedException {
        if (headers == null) headers = new LuaTable();
        byte[] bytes = luaValueToBytes(data);

        HttpRequest.Builder requestBuilder = requestBuilderByUriAndHeaders(uri, headers);
        requestBuilder.PUT(HttpRequest.BodyPublishers.ofByteArray(bytes));

        HttpResponse<byte[]> response = _client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofByteArray());

        return lVarargsByResponse(response);
    }

    @LuaWhitelist
    @LDocsInclude
    @LDocsDescription("Requests data from given URI via specified method")
    @LDocsProperty(name = "use_varargs", boolValue = true)
    @LDocsProperty(name = "varargs", classValue = {LuaTable.class, Integer.class, LuaTable.class})
    @LDocsProperty(name = "varargs_descs", stringValue = {
            "Byte table of response data",
            "Status code of response",
            "Headers of response"
    })
    public Object[] method(@LuaNotNil String uri, @LuaNotNil String methodName, @LuaNotNil LuaValue data, LuaTable headers) throws IOException, InterruptedException {
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

    private Object[] lVarargsByResponse(HttpResponse<byte[]> response) {
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

        return new Object[] {
                LUtils.Utils.byteArrayToTable(response.body()), response.statusCode(), headers
        };
    }

    private byte[] luaValueToBytes(LuaValue data) {
        return switch (data.type()) {
            case (LuaValue.TTABLE) -> LUtils.Utils.tableToByteArray(data.checktable());
            case (LuaValue.TSTRING) -> data.checkjstring().getBytes(StandardCharsets.UTF_8);
            default -> throw new LuaError("Data should be either string or byte array.");
        };
    }
}