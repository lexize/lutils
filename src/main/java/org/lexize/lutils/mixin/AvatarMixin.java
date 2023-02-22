package org.lexize.lutils.mixin;

import org.lexize.lutils.submodules.socket.LSocket;
import org.moon.figura.avatar.Avatar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(Avatar.class)
public class AvatarMixin {
    @Inject(method = "clean", at = @At("RETURN"), remap = false)
    public void onClean(CallbackInfo ci) throws IOException {
        LSocket.clearAvatarSockets(((Avatar) (Object) this));
    }
}
