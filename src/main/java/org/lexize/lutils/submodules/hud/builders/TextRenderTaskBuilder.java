package org.lexize.lutils.submodules.hud.builders;

import net.minecraft.client.util.math.MatrixStack;
import org.lexize.lutils.submodules.LUtilsHUD;
import org.lexize.lutils.submodules.hud.TextRenderTask;
import org.luaj.vm2.LuaError;
import org.moon.figura.lua.LuaWhitelist;
import org.moon.figura.math.vector.FiguraVec2;
import org.moon.figura.math.vector.FiguraVec3;
import org.moon.figura.math.vector.FiguraVec4;
import org.moon.figura.utils.caching.CacheUtils;

@LuaWhitelist
public class TextRenderTaskBuilder extends HUDRenderTaskBuilder{
    public String text;
    public FiguraVec3 pos;
    public FiguraVec4 color = FiguraVec4.of(1,1,1,1);
    public Boolean shadow = false;
    public Boolean mirror = false;
    public Float size = 1f;

    public TextRenderTaskBuilder() {
    }


    @LuaWhitelist
    public TextRenderTaskBuilder text(String text) {
        this.text = text;
        return this;
    }

    @LuaWhitelist
    public TextRenderTaskBuilder pos(FiguraVec3 pos) {
        this.pos = pos;
        return this;
    }

    @LuaWhitelist
    public TextRenderTaskBuilder color(FiguraVec4 color) {
        this.color = color;
        return this;
    }

    @LuaWhitelist
    public TextRenderTaskBuilder shadow(Boolean shadow) {
        this.shadow = shadow;
        return this;
    }

    @LuaWhitelist
    public TextRenderTaskBuilder mirror(Boolean mirror) {
        this.mirror = mirror;
        return this;
    }

    @LuaWhitelist
    public TextRenderTaskBuilder size(Float size) {
        this.size = size;
        return this;
    }

    @LuaWhitelist
    public String getText() {
        return text;
    }

    @LuaWhitelist
    public FiguraVec3 getPos() {
        return pos;
    }

    @LuaWhitelist
    public FiguraVec4 getColor() {
        return color;
    }

    @LuaWhitelist
    public Boolean getShadow() {
        return shadow;
    }

    @LuaWhitelist
    public Boolean getMirror() {
        return mirror;
    }

    @LuaWhitelist
    public Float getSize() {
        return size;
    }


    @Override
    public void replaceNullWithDefaults() {
        if (pos == null) pos = FiguraVec3.of();
        if (color == null) color = FiguraVec4.of(1,1,1,1);
        if (size == null) size = 1f;
        if (shadow == null) shadow = false;
        if (mirror == null) mirror = false;
    }

    @Override
    @LuaWhitelist
    public void draw() {
        replaceNullWithDefaults();
        if (text == null) throw new LuaError("text cant be null!");
        TextRenderTask task = TextRenderTask.of();
        task.construct(text, pos, color, shadow,mirror, size);
        LUtilsHUD.getGuiRenderTaskStack().push(task);
    }

    @LuaWhitelist
    public TextRenderTaskBuilder copy() {
        replaceNullWithDefaults();
        return new TextRenderTaskBuilder()
                .text( text != null ? String.copyValueOf(text.toCharArray()) : null)
                .color(color != null ? color.copy() : null)
                .pos(pos != null ? pos.copy() : null)
                .size(size)
                .shadow(shadow)
                .mirror(mirror);
    }
}
