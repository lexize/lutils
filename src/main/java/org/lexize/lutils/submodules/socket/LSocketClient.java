package org.lexize.lutils.submodules.socket;

import org.lexize.lutils.annotations.LField;
import org.lexize.lutils.streams.LJavaInputStream;
import org.lexize.lutils.streams.LJavaOutputStream;
import org.luaj.vm2.LuaValue;
import org.moon.figura.lua.LuaWhitelist;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

@LuaWhitelist
@LField(value = "input", type = LJavaInputStream.class)
@LField(value = "output", type = LJavaOutputStream.class)
public class LSocketClient {
    private final Socket socket;
    private final LJavaInputStream inputStream;
    private final LJavaOutputStream outputStream;
    public LSocketClient(Socket socket) throws IOException {
        this.socket = socket;
        inputStream = new LJavaInputStream(socket.getInputStream());
        outputStream = new LJavaOutputStream(socket.getOutputStream());
    }

    @LuaWhitelist
    public Object __index(LuaValue key) {
        if(!key.isstring()) return null;
        return switch (key.tojstring()) {
            case "input" -> inputStream;
            case "output" -> outputStream;
            default -> null;
        };
    }

    @LuaWhitelist
    public void close() throws IOException {
        socket.close();
    }

    @LuaWhitelist
    public void shutdownInput() throws IOException {
        socket.shutdownInput();
    }

    @LuaWhitelist
    public void shutdownOutput() throws IOException {
        socket.shutdownOutput();
    }

    @LuaWhitelist
    public boolean isConnected() {
        return socket.isConnected();
    }

    @LuaWhitelist
    public boolean isBound() {
        return socket.isBound();
    }

    @LuaWhitelist
    public boolean isClosed() {
        return socket.isClosed();
    }

    @LuaWhitelist
    public boolean isInputShutdown() {
        return socket.isInputShutdown();
    }

    @LuaWhitelist
    public boolean isOutputShutdown() {
        return socket.isOutputShutdown();
    }
    @LuaWhitelist
    public void setSendBufferSize(int size) throws SocketException {
        socket.setSendBufferSize(size);
    }

    @LuaWhitelist
    public int getSendBufferSize() throws SocketException {
        return socket.getSendBufferSize();
    }

    @LuaWhitelist
    public void setReceiveBufferSize(int size) throws SocketException {
        socket.setReceiveBufferSize(size);
    }

    @LuaWhitelist
    public int getReceiveBufferSize() throws SocketException {
        return socket.getReceiveBufferSize();
    }

    @LuaWhitelist
    public void setKeepAlive(boolean on) throws SocketException {
        socket.setKeepAlive(on);
    }

    @LuaWhitelist
    public boolean getKeepAlive() throws SocketException {
        return socket.getKeepAlive();
    }
}
