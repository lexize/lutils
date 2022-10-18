package org.lexize.lutils.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.lexize.lutils.submodules.hud.DisableScissorTask;
import org.lexize.lutils.submodules.hud.EnableScissorTask;
import org.lexize.lutils.submodules.hud.HUDRenderTask;
import org.lexize.lutils.submodules.LUtilsHUD;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Stack;

@Mixin(InGameHud.class)
public class HudRenderer {
    @Inject(method = "render", at = @At("RETURN"))
    public void onRender(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        Stack<HUDRenderTask> taskStack = LUtilsHUD.getGuiRenderTaskStack();
        boolean scissorsActivated = false;
        while (!taskStack.empty()) {
            HUDRenderTask task = taskStack.remove(0);
            task.render(matrices);
            if (task instanceof EnableScissorTask) {
                scissorsActivated = true;
            }
            else if (task instanceof DisableScissorTask) {
                scissorsActivated = false;
            }
        }

        if (scissorsActivated) RenderSystem.disableScissor();
    }
}
