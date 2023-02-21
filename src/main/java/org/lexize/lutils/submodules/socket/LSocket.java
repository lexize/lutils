package org.lexize.lutils.submodules.socket;

import org.lexize.lutils.LUtilsTrust;
import org.lexize.lutils.annotations.LDescription;
import org.lexize.lutils.annotations.LDocsFuncOverload;
import org.luaj.vm2.LuaError;
import org.moon.figura.avatar.Avatar;
import org.moon.figura.lua.LuaWhitelist;

import java.io.IOException;
import java.net.Socket;

@LuaWhitelist
@LDescription("Socket submodule")
public class LSocket {
    private final Avatar avatar;
    private static final LuaError NO_PERMISSION = new LuaError("This avatar don't have permission to open Socket connection");
    public LSocket(Avatar avatar) {
        this.avatar = avatar;
    }
    @LuaWhitelist
    @LDocsFuncOverload(
            argumentTypes = {String.class, int.class},
            argumentNames = {"ip", "port"},
            returnType = LSocketClient.class,
            description = "Opens connection by specified IP and port"
    )
    public LSocketClient connect(String ip, int port) throws IOException {
        permissionCheck();
        return new LSocketClient(new Socket(ip, port));
    }
    private void permissionCheck() {
        if (!canOpenSocketConnection()) throw NO_PERMISSION;
    }
    @LuaWhitelist
    public boolean canOpenSocketConnection() {
        return avatar.permissions.get(LUtilsTrust.SOCKET_PERMISSION) > 0;
    }
}
