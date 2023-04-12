package org.lexize.lutils.submodules.http;

import org.lexize.lutils.LUtilsTrust;
import org.lexize.lutils.annotations.LDescription;
import org.lexize.lutils.annotations.LDocsFuncOverload;
import org.lexize.lutils.misc.LFuture;
import org.lexize.lutils.providers.LProvider;
import org.lexize.lutils.readers.LReader;
import org.lexize.lutils.streams.LJavaInputStream;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.moon.figura.FiguraMod;
import org.moon.figura.avatar.Avatar;
import org.moon.figura.lua.LuaWhitelist;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@LuaWhitelist
public class LHttp {
    private static final LuaError NO_PERMISSION = new LuaError("This avatar don't have permission to use HTTP requests");
    private final HttpClient httpClient;
    private final Avatar avatar;
    public LHttp(Avatar avatar) {
        this.avatar = avatar;
        httpClient = HttpClient.newBuilder().build();
    }
    private void permissionCheck() {
        if (!canSendHTTPRequests()) throw NO_PERMISSION;
    }
    private HttpRequest.Builder requestBuilder(String uri, HashMap<String, String> headers) {
        HttpRequest.Builder builder = HttpRequest.newBuilder().uri(URI.create(uri));
        if (headers != null) {
            for (Map.Entry<String, String> kv :
                    headers.entrySet()) {
                builder.header(kv.getKey(), kv.getValue());
            }
        }
        return builder;
    }

    @LuaWhitelist
    public boolean canSendHTTPRequests() {
        return avatar.permissions.get(LUtilsTrust.HTTP_PERMISSION) > 0;
    }

    @LuaWhitelist
    @LDocsFuncOverload(
            description = "Sends HTTP GET request by specified URI, and returns response data with reader",
            argumentNames = {"uri", "reader", "headers"},
            argumentTypes = {String.class, LReader.class, LuaTable.class},
            returnType = LHttpResponse.class
    )
    public <R> LHttpResponse<R> get(String uri, LReader<R> reader, HashMap<String, String> headers) throws IOException, InterruptedException {
        permissionCheck();
        var builder = requestBuilder(uri, headers).GET();
        var d = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofInputStream());
        InputStream stream = d.body();
        R readData = reader.readFrom(stream);
        stream.close();
        return new LHttpResponse<>(readData, d.statusCode(), d.headers().map());
    }
    @LuaWhitelist
    @LDocsFuncOverload(
            description = "Sends HTTP GET request by specified URI, and returns response with data stream",
            argumentNames = {"uri", "headers"},
            argumentTypes = {String.class, LuaTable.class},
            returnType = LHttpResponse.class
    )
    public LHttpResponse<LJavaInputStream> getStream(String uri, HashMap<String, String> headers) throws IOException, InterruptedException {
        permissionCheck();
        var builder = requestBuilder(uri, headers).GET();
        var d = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofInputStream());
        InputStream stream = d.body();
        return new LHttpResponse<>(new LJavaInputStream(stream), d.statusCode(), d.headers().map());
    }
    @LuaWhitelist
    @LDocsFuncOverload(
            description = "Sends async HTTP GET request by specified URI, and returns response data with reader",
            argumentNames = {"uri", "reader", "headers"},
            argumentTypes = {String.class, LReader.class, LuaTable.class},
            returnType = LFuture.class
    )
    public <R> LFuture<LHttpResponse<R>> getAsync(String uri, LReader<R> reader, HashMap<String, String> headers) {
        permissionCheck();
        var builder = requestBuilder(uri, headers).GET();
        var f = httpClient.sendAsync(builder.build(), HttpResponse.BodyHandlers.ofInputStream());
        LFuture<LHttpResponse<R>> future = new LFuture<>();
        f.thenRun(() -> {
            try {
                var d = f.join();
                InputStream stream = d.body();
                R readData = reader.readFrom(stream);
                stream.close();
                future.setDone(true);
                future.setValue(new LHttpResponse<>(readData, d.statusCode(), d.headers().map()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return future;
    }
    @LuaWhitelist
    @LDocsFuncOverload(
            description = "Sends async HTTP GET request by specified URI, and returns response with data stream",
            argumentNames = {"uri", "headers"},
            argumentTypes = {String.class, LuaTable.class},
            returnType = LFuture.class
    )
    public LFuture<LHttpResponse<LJavaInputStream>> getStreamAsync(String uri, HashMap<String, String> headers) {
        permissionCheck();
        var builder = requestBuilder(uri, headers).GET();
        var f = httpClient.sendAsync(builder.build(), HttpResponse.BodyHandlers.ofInputStream());
        LFuture<LHttpResponse<LJavaInputStream>> future = new LFuture<>();
        f.thenRun(() -> {
            var d = f.join();
            InputStream stream = d.body();
            future.setDone(true);
            future.setValue(new LHttpResponse<>(new LJavaInputStream(stream), d.statusCode(), d.headers().map()));
        });
        return future;
    }

    private <T> HttpRequest.Builder postBuilder(String uri, T data, LProvider<T> provider, HashMap<String, String> headers){
        var builder = requestBuilder(uri, headers);
        if (data != null) builder.POST(HttpRequest.BodyPublishers.ofInputStream(() -> provider.getStream(data)));
        else builder.POST(HttpRequest.BodyPublishers.noBody());
        return builder;
    }
    @LuaWhitelist
    @LDocsFuncOverload(
            description = "Sends HTTP POST request by specified URI, and returns response data with reader",
            argumentNames = {"uri", "data", "provider", "reader", "headers"},
            argumentTypes = {String.class, Object.class, LProvider.class, LReader.class, LuaTable.class},
            returnType = LHttpResponse.class
    )
    public <P,R> LHttpResponse<R> post(String uri, P data, LProvider<P> provider, LReader<R> reader, HashMap<String, String> headers) throws IOException, InterruptedException {
        permissionCheck();
        var builder = postBuilder(uri, data, provider, headers);
        var d = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofInputStream());
        InputStream stream = d.body();
        R readData = reader.readFrom(stream);
        stream.close();
        return new LHttpResponse<>(readData, d.statusCode(), d.headers().map());
    }
    @LuaWhitelist
    @LDocsFuncOverload(
            description = "Sends HTTP POST request by specified URI, and returns response with data stream",
            argumentNames = {"uri", "data", "provider", "headers"},
            argumentTypes = {String.class, Object.class, LProvider.class, LuaTable.class},
            returnType = LHttpResponse.class
    )
    public <P> LHttpResponse<LJavaInputStream> postStream(String uri, P data, LProvider<P> provider, HashMap<String, String> headers) throws IOException, InterruptedException {
        permissionCheck();
        var builder = postBuilder(uri, data, provider, headers);
        var d = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofInputStream());
        InputStream stream = d.body();
        return new LHttpResponse<>(new LJavaInputStream(stream), d.statusCode(), d.headers().map());
    }
    @LuaWhitelist
    @LDocsFuncOverload(
            description = "Sends async HTTP POST request by specified URI, and returns response data with reader",
            argumentNames = {"uri", "data", "provider", "reader", "headers"},
            argumentTypes = {String.class, Object.class, LProvider.class, LReader.class, LuaTable.class},
            returnType = LFuture.class
    )
    public <P,R> LFuture<LHttpResponse<R>> postAsync(String uri, P data, LProvider<P> provider, LReader<R> reader, HashMap<String, String> headers) {
        permissionCheck();
        var builder = postBuilder(uri, data, provider, headers);
        var f = httpClient.sendAsync(builder.build(), HttpResponse.BodyHandlers.ofInputStream());
        LFuture<LHttpResponse<R>> future = new LFuture<>();
        f.thenRun(() -> {
            try {
                var d = f.join();
                InputStream stream = d.body();
                R readData = reader.readFrom(stream);
                stream.close();
                future.setDone(true);
                future.setValue(new LHttpResponse<>(readData, d.statusCode(), d.headers().map()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return future;
    }
    @LuaWhitelist
    @LDocsFuncOverload(
            description = "Sends async HTTP POST request by specified URI, and returns response with data stream",
            argumentNames = {"uri", "data", "provider", "headers"},
            argumentTypes = {String.class, Object.class, LProvider.class, LuaTable.class},
            returnType = LFuture.class
    )
    public <P,R> LFuture<LHttpResponse<LJavaInputStream>> postStreamAsync(String uri, P data, LProvider<P> provider, HashMap<String, String> headers) {
        permissionCheck();
        var builder = postBuilder(uri, data, provider, headers);
        var f = httpClient.sendAsync(builder.build(), HttpResponse.BodyHandlers.ofInputStream());
        LFuture<LHttpResponse<LJavaInputStream>> future = new LFuture<>();
        f.thenRun(() -> {
            var d = f.join();
            InputStream stream = d.body();
            future.setDone(true);
            future.setValue(new LHttpResponse<>(new LJavaInputStream(stream), d.statusCode(), d.headers().map()));
        });
        return future;
    }

    private <T> HttpRequest.Builder putBuilder(String uri, T data, LProvider<T> provider, HashMap<String, String> headers){
        var builder = requestBuilder(uri, headers);
        if (data != null) builder.PUT(HttpRequest.BodyPublishers.ofInputStream(() -> provider.getStream(data)));
        else builder.PUT(HttpRequest.BodyPublishers.noBody());
        return builder;
    }
    @LuaWhitelist
    @LDocsFuncOverload(
            argumentTypes = {String.class, Object.class, LProvider.class, LReader.class, LuaTable.class},
            argumentNames = {"uri", "data", "provider", "reader", "headers"},
            description = "Sends HTTP PUT request by specified URI, and returns response data with reader",
            returnType = LHttpResponse.class
    )
    public <P,R> LHttpResponse<R> put(String uri, P data, LProvider<P> provider, LReader<R> reader, HashMap<String, String> headers) throws IOException, InterruptedException {
        permissionCheck();
        var builder = putBuilder(uri, data, provider, headers);
        var d = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofInputStream());
        InputStream stream = d.body();
        R readData = reader.readFrom(stream);
        stream.close();
        return new LHttpResponse<>(readData, d.statusCode(), d.headers().map());
    }
    @LuaWhitelist
    @LDocsFuncOverload(
            argumentTypes = {String.class, Object.class, LProvider.class, LuaTable.class},
            argumentNames = {"uri", "data", "provider", "headers"},
            description = "Sends HTTP PUT request by specified URI, and returns response with data stream",
            returnType = LHttpResponse.class
    )
    public <P> LHttpResponse<LJavaInputStream> putStream(String uri, P data, LProvider<P> provider, HashMap<String, String> headers) throws IOException, InterruptedException {
        permissionCheck();
        var builder = putBuilder(uri, data, provider, headers);
        var d = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofInputStream());
        InputStream stream = d.body();
        return new LHttpResponse<>(new LJavaInputStream(stream), d.statusCode(), d.headers().map());
    }
    @LuaWhitelist
    @LDocsFuncOverload(
            argumentTypes = {String.class, Object.class, LProvider.class, LReader.class, LuaTable.class},
            argumentNames = {"uri", "data", "provider", "reader", "headers"},
            description = "Sends async HTTP PUT request by specified URI, and returns response data with reader",
            returnType = LFuture.class
    )
    public <P,R> LFuture<LHttpResponse<R>> putAsync(String uri, P data, LProvider<P> provider, LReader<R> reader, HashMap<String, String> headers) {
        permissionCheck();
        var builder = putBuilder(uri, data, provider, headers);
        var f = httpClient.sendAsync(builder.build(), HttpResponse.BodyHandlers.ofInputStream());
        LFuture<LHttpResponse<R>> future = new LFuture<>();
        f.thenRun(() -> {
            try {
                var d = f.join();
                InputStream stream = d.body();
                R readData = reader.readFrom(stream);
                stream.close();
                future.setDone(true);
                future.setValue(new LHttpResponse<>(readData, d.statusCode(), d.headers().map()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return future;
    }
    @LuaWhitelist
    @LDocsFuncOverload(
            argumentTypes = {String.class, Object.class, LProvider.class, LuaTable.class},
            argumentNames = {"uri", "data", "provider", "headers"},
            description = "Sends async HTTP PUT request by specified URI, and returns response with data stream",
            returnType = LFuture.class
    )
    public <P> LFuture<LHttpResponse<LJavaInputStream>> putStreamAsync(String uri, P data, LProvider<P> provider, HashMap<String, String> headers) {
        permissionCheck();
        var builder = putBuilder(uri, data, provider, headers);
        var f = httpClient.sendAsync(builder.build(), HttpResponse.BodyHandlers.ofInputStream());
        LFuture<LHttpResponse<LJavaInputStream>> future = new LFuture<>();
        f.thenRun(() -> {
            var d = f.join();
            InputStream stream = d.body();
            future.setDone(true);
            future.setValue(new LHttpResponse<>(new LJavaInputStream(stream), d.statusCode(), d.headers().map()));
        });
        return future;
    }

    @LuaWhitelist
    @LDocsFuncOverload(
            argumentTypes = {String.class, LReader.class, LuaTable.class},
            argumentNames = {"uri", "reader", "headers"},
            description = "Sends HTTP DELETE request by specified URI, and returns response data with reader",
            returnType = LHttpResponse.class
    )
    public <R> LHttpResponse<R> delete(String uri, LReader<R> reader, HashMap<String, String> headers) throws IOException, InterruptedException {
        permissionCheck();
        var builder = requestBuilder(uri, headers).DELETE();
        var d = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofInputStream());
        InputStream stream = d.body();
        R readData = reader.readFrom(stream);
        stream.close();
        return new LHttpResponse<>(readData, d.statusCode(), d.headers().map());
    }
    @LuaWhitelist
    @LDocsFuncOverload(
            argumentTypes = {String.class, LuaTable.class},
            argumentNames = {"uri", "headers"},
            description = "Sends HTTP DELETE request by specified URI, and returns response with data stream",
            returnType = LHttpResponse.class
    )
    public LHttpResponse<LJavaInputStream> deleteStream(String uri, HashMap<String, String> headers) throws IOException, InterruptedException {
        permissionCheck();
        var builder = requestBuilder(uri, headers).DELETE();
        var d = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofInputStream());
        InputStream stream = d.body();
        return new LHttpResponse<>(new LJavaInputStream(stream), d.statusCode(), d.headers().map());
    }
    @LuaWhitelist
    @LDocsFuncOverload(
            argumentTypes = {String.class, LReader.class, LuaTable.class},
            argumentNames = {"uri", "reader", "headers"},
            description = "Sends async HTTP DELETE request by specified URI, and returns response data with reader",
            returnType = LFuture.class
    )
    public <R> LFuture<LHttpResponse<R>> deleteAsync(String uri, LReader<R> reader, HashMap<String, String> headers) {
        permissionCheck();
        var builder = requestBuilder(uri, headers).DELETE();
        var f = httpClient.sendAsync(builder.build(), HttpResponse.BodyHandlers.ofInputStream());
        LFuture<LHttpResponse<R>> future = new LFuture<>();
        f.thenRun(() -> {
            try {
                var d = f.join();
                InputStream stream = d.body();
                R readData = reader.readFrom(stream);
                stream.close();
                future.setDone(true);
                future.setValue(new LHttpResponse<>(readData, d.statusCode(), d.headers().map()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return future;
    }
    @LuaWhitelist
    @LDocsFuncOverload(
            argumentTypes = {String.class, LuaTable.class},
            argumentNames = {"uri", "headers"},
            description = "Sends async HTTP DELETE request by specified URI, and returns response with data stream",
            returnType = LFuture.class
    )
    public LFuture<LHttpResponse<LJavaInputStream>> deleteStreamAsync(String uri, HashMap<String, String> headers) {
        permissionCheck();
        var builder = requestBuilder(uri, headers).DELETE();
        var f = httpClient.sendAsync(builder.build(), HttpResponse.BodyHandlers.ofInputStream());
        LFuture<LHttpResponse<LJavaInputStream>> future = new LFuture<>();
        f.thenRun(() -> {
            var d = f.join();
            InputStream stream = d.body();
            future.setDone(true);
            future.setValue(new LHttpResponse<>(new LJavaInputStream(stream), d.statusCode(), d.headers().map()));
        });
        return future;
    }

    private <T> HttpRequest.Builder methodBuilder(String uri, String method, T data, LProvider<T> provider, HashMap<String, String> headers){
        var builder = requestBuilder(uri, headers);
        if (data != null) builder.method(method, HttpRequest.BodyPublishers.ofInputStream(() -> provider.getStream(data)));
        else builder.method(method, HttpRequest.BodyPublishers.noBody());
        return builder;
    }
    @LuaWhitelist
    @LDocsFuncOverload(
            argumentTypes = {String.class, String.class, Object.class, LProvider.class, LReader.class, LuaTable.class},
            argumentNames = {"uri", "method", "data", "provider", "reader", "headers"},
            description = "Sends HTTP request by specified URI with specified method, and returns response data with reader",
            returnType = LHttpResponse.class
    )
    public <P,R> LHttpResponse<R> method(String uri, String method, P data, LProvider<P> provider, LReader<R> reader, HashMap<String, String> headers) throws IOException, InterruptedException {
        permissionCheck();
        var builder = methodBuilder(uri, method, data, provider, headers);
        var d = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofInputStream());
        InputStream stream = d.body();
        R readData = reader.readFrom(stream);
        stream.close();
        return new LHttpResponse<>(readData, d.statusCode(), d.headers().map());
    }
    @LuaWhitelist
    @LDocsFuncOverload(
            argumentTypes = {String.class, String.class, Object.class, LProvider.class, LuaTable.class},
            argumentNames = {"uri", "method", "data", "provider","headers"},
            description = "Sends HTTP request by specified URI with specified method, and returns stream with response data",
            returnType = LHttpResponse.class
    )
    public <P> LHttpResponse<LJavaInputStream> methodStream(String uri, String method, P data, LProvider<P> provider, HashMap<String, String> headers) throws IOException, InterruptedException {
        permissionCheck();
        var builder = methodBuilder(uri, method, data, provider, headers);
        var d = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofInputStream());
        InputStream stream = d.body();
        return new LHttpResponse<>(new LJavaInputStream(stream), d.statusCode(), d.headers().map());
    }
    @LuaWhitelist
    @LDocsFuncOverload(
            argumentTypes = {String.class, String.class, Object.class, LProvider.class, LReader.class, LuaTable.class},
            argumentNames = {"uri", "method", "data", "provider", "reader", "headers"},
            description = "Sends async HTTP request by specified URI with specified method, and returns response data with reader",
            returnType = LFuture.class
    )
    public <P,R> LFuture<LHttpResponse<R>> methodAsync(String uri, String method, P data, LProvider<P> provider, LReader<R> reader, HashMap<String, String> headers) {
        permissionCheck();
        var builder = methodBuilder(uri, method, data, provider, headers);
        var f = httpClient.sendAsync(builder.build(), HttpResponse.BodyHandlers.ofInputStream());
        LFuture<LHttpResponse<R>> future = new LFuture<>();
        f.thenRun(() -> {
            try {
                var d = f.join();
                InputStream stream = d.body();
                R readData = reader.readFrom(stream);
                stream.close();
                future.setDone(true);
                future.setValue(new LHttpResponse<>(readData, d.statusCode(), d.headers().map()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return future;
    }
    @LuaWhitelist
    @LDocsFuncOverload(
            argumentTypes = {String.class, String.class, Object.class, LProvider.class, LuaTable.class},
            argumentNames = {"uri", "method", "data", "provider","headers"},
            description = "Sends async HTTP request by specified URI with specified method, and returns stream with response data",
            returnType = LFuture.class
    )
    public <P> LFuture<LHttpResponse<LJavaInputStream>> methodStreamAsync(String uri, String method, P data, LProvider<P> provider, HashMap<String, String> headers) {
        permissionCheck();
        var builder = methodBuilder(uri, method, data, provider, headers);
        var f = httpClient.sendAsync(builder.build(), HttpResponse.BodyHandlers.ofInputStream());
        LFuture<LHttpResponse<LJavaInputStream>> future = new LFuture<>();
        f.thenRun(() -> {
            var d = f.join();
            InputStream stream = d.body();
            future.setDone(true);
            future.setValue(new LHttpResponse<>(new LJavaInputStream(stream), d.statusCode(), d.headers().map()));
        });
        return future;
    }
}
