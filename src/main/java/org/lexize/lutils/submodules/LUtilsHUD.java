package org.lexize.lutils.submodules;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import org.lexize.lutils.LUtils;
import org.lexize.lutils.submodules.hud.*;
import org.lexize.lutils.submodules.hud.builders.FillRenderTaskBuilder;
import org.lexize.lutils.submodules.hud.builders.TextRenderTaskBuilder;
import org.lexize.lutils.submodules.hud.builders.TextureRenderTaskBuilder;
import org.moon.figura.avatar.Avatar;
import org.moon.figura.lua.LuaWhitelist;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@LuaWhitelist
public class LUtilsHUD {

    private Avatar _avatar;
    private MinecraftClient mc;
    private TextRenderer textRenderer;

    private static Stack<HUDRenderTask> _hudRenderTaskStack = new Stack<>();

    public static Stack<HUDRenderTask> getGuiRenderTaskStack() {
        return _hudRenderTaskStack;
    }

    public LUtilsHUD(Avatar avatar) {
        _avatar = avatar;
        mc = MinecraftClient.getInstance();
        textRenderer = mc.textRenderer;
    }

    @LuaWhitelist
    public FillRenderTaskBuilder fill() {
        return new FillRenderTaskBuilder();
    }

    @LuaWhitelist
    public TextRenderTaskBuilder text() {
        return new TextRenderTaskBuilder();
    }

    @LuaWhitelist
    public TextureRenderTaskBuilder texture() {
        return new TextureRenderTaskBuilder(_avatar);
    }

    @LuaWhitelist
    public void enableScissor(float x, float y, float width, float height) {
        _hudRenderTaskStack.push(EnableScissorTask.of(x,y,width,height));
    }

    @LuaWhitelist
    public void disableScissor() {
        _hudRenderTaskStack.push(DisableScissorTask.of());
    }

    @LuaWhitelist
    public int getScaledStringWidth(String text, float scale) {
        return (int) (textRenderer.getWidth(text) * scale);
    }

    @LuaWhitelist
    public String getWrappedScaledString(String text, float scale, int maxWidth) {
        var s = StringVisitable.plain(text);
        List<OrderedText> t = textRenderer.wrapLines(s, (int) (maxWidth/scale));
        List<String> strings = new ArrayList<>();
        t.forEach(o -> strings.add(LUtils.Utils.fromOrderedText(o)));
        return String.join("\n", strings);
    }

    @LuaWhitelist
    public float getWrappedScaledHeight(String text, float scale, int maxWidth){
        return textRenderer.getWrappedLinesHeight(text, (int) (maxWidth / scale))*scale;
    }
}
