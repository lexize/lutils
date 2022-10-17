package org.lexize.lutils.submodules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.util.Identifier;
import org.lexize.lutils.LUtils;
import org.lexize.lutils.submodules.hud.FillRenderTask;
import org.lexize.lutils.submodules.hud.HUDRenderTask;
import org.lexize.lutils.submodules.hud.TextRenderTask;
import org.lexize.lutils.submodules.hud.TextureRenderTask;
import org.lexize.lutils.submodules.hud.builders.FillRenderTaskBuilder;
import org.lexize.lutils.submodules.hud.builders.TextRenderTaskBuilder;
import org.lexize.lutils.submodules.hud.builders.TextureRenderTaskBuilder;
import org.moon.figura.avatar.Avatar;
import org.moon.figura.lua.LuaNotNil;
import org.moon.figura.lua.LuaWhitelist;
import org.moon.figura.math.vector.FiguraVec2;
import org.moon.figura.math.vector.FiguraVec3;
import org.moon.figura.math.vector.FiguraVec4;
import org.moon.figura.mixin.render.layers.elytra.ElytraLayerAccessor;
import org.moon.figura.model.FiguraModelPart;
import org.moon.figura.model.rendering.texture.FiguraTexture;
import org.moon.figura.model.rendering.texture.FiguraTextureSet;
import org.moon.figura.utils.FiguraIdentifier;

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
