package org.lexize.lutils.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.lexize.lutils.submodules.hud.HUDRenderTask;
import org.lexize.lutils.submodules.LUtilsHUD;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Stack;

@Mixin(InGameHud.class)
public class HudRenderer {
    @Inject(method = "render", at = @At(value = "RETURN"))
    public void onRender(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        Stack<HUDRenderTask> taskStack = LUtilsHUD.getGuiRenderTaskStack();
        MatrixStack matrix = new MatrixStack();
        while (!taskStack.empty()) {
            HUDRenderTask task = taskStack.remove(0);
            task.render(matrix);
            task.free();
        }
        RenderSystem.disableScissor();
    }
}
