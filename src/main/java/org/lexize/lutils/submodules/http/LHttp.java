package org.lexize.lutils.submodules.http;

import org.lexize.lutils.LUtilsTrust;
import org.lexize.lutils.misc.LFuture;
import org.lexize.lutils.providers.LProvider;
import org.lexize.lutils.readers.LReader;
import org.lexize.lutils.streams.LJavaInputStream;
import org.luaj.vm2.LuaError;
import org.moon.figura.FiguraMod;
import org.moon.figura.avatar.Avatar;
import org.moon.figura.lua.LuaWhitelist;
import org.moon.figura.permissions.FiguraPermissions;
import org.moon.figura.permissions.Permissions;

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
        if (avatar.permissions.get(LUtilsTrust.HTTP_PERMISSION) == 0) throw NO_PERMISSION;
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
    public LHttpResponse<LJavaInputStream> getStream(String uri, HashMap<String, String> headers) throws IOException, InterruptedException {
        permissionCheck();
        var builder = requestBuilder(uri, headers).GET();
        var d = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofInputStream());
        InputStream stream = d.body();
        return new LHttpResponse<>(new LJavaInputStream(stream), d.statusCode(), d.headers().map());
    }
    @LuaWhitelist
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
    public <P> LHttpResponse<LJavaInputStream> postStream(String uri, P data, LProvider<P> provider, HashMap<String, String> headers) throws IOException, InterruptedException {
        permissionCheck();
        var builder = postBuilder(uri, data, provider, headers);
        var d = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofInputStream());
        InputStream stream = d.body();
        return new LHttpResponse<>(new LJavaInputStream(stream), d.statusCode(), d.headers().map());
    }
    @LuaWhitelist
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
    public <P> LHttpResponse<LJavaInputStream> putStream(String uri, P data, LProvider<P> provider, HashMap<String, String> headers) throws IOException, InterruptedException {
        permissionCheck();
        var builder = putBuilder(uri, data, provider, headers);
        var d = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofInputStream());
        InputStream stream = d.body();
        return new LHttpResponse<>(new LJavaInputStream(stream), d.statusCode(), d.headers().map());
    }
    @LuaWhitelist
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
    public <P,R> LFuture<LHttpResponse<LJavaInputStream>> putStreamAsync(String uri, P data, LProvider<P> provider, HashMap<String, String> headers) {
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
    public LHttpResponse<LJavaInputStream> deleteStream(String uri, HashMap<String, String> headers) throws IOException, InterruptedException {
        permissionCheck();
        var builder = requestBuilder(uri, headers).DELETE();
        var d = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofInputStream());
        InputStream stream = d.body();
        return new LHttpResponse<>(new LJavaInputStream(stream), d.statusCode(), d.headers().map());
    }
    @LuaWhitelist
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
    public <P> LHttpResponse<LJavaInputStream> methodStream(String uri, String method, P data, LProvider<P> provider, HashMap<String, String> headers) throws IOException, InterruptedException {
        permissionCheck();
        var builder = methodBuilder(uri, method, data, provider, headers);
        var d = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofInputStream());
        InputStream stream = d.body();
        return new LHttpResponse<>(new LJavaInputStream(stream), d.statusCode(), d.headers().map());
    }
    @LuaWhitelist
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
    public <P,R> LFuture<LHttpResponse<LJavaInputStream>> methodStreamAsync(String uri, String method, P data, LProvider<P> provider, HashMap<String, String> headers) {
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
