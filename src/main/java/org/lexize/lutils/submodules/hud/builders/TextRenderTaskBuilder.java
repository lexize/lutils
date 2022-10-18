package org.lexize.lutils.submodules.hud.builders;

import org.lexize.lutils.submodules.LUtilsHUD;
import org.lexize.lutils.submodules.hud.TextRenderTask;
import org.luaj.vm2.LuaError;
import org.moon.figura.lua.LuaWhitelist;
import org.moon.figura.math.vector.FiguraVec2;
import org.moon.figura.math.vector.FiguraVec3;
import org.moon.figura.utils.caching.CacheUtils;

@LuaWhitelist
public class TextRenderTaskBuilder extends HUDRenderTaskBuilder<TextRenderTaskBuilder>{

    private final static CacheUtils.Cache<TextRenderTaskBuilder> CACHE = CacheUtils.getCache(TextRenderTaskBuilder::new, 300);

    public String text;
    public FiguraVec2 pos;
    public FiguraVec3 color = FiguraVec3.of(1,1,1);
    public Boolean shadow = false;
    public Boolean mirror = false;
    public Float size = 1f;

    private TextRenderTask task = new TextRenderTask();


    @LuaWhitelist
    public TextRenderTaskBuilder text(String text) {
        this.text = text;
        return this;
    }

    @LuaWhitelist
    public TextRenderTaskBuilder pos(FiguraVec2 pos) {
        this.pos = pos;
        return this;
    }

    @LuaWhitelist
    public TextRenderTaskBuilder color(FiguraVec3 color) {
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
    public FiguraVec2 getPos() {
        return pos;
    }

    @LuaWhitelist
    public FiguraVec3 getColor() {
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
        if (pos == null) pos = FiguraVec2.of();
        if (color == null) color = FiguraVec3.of(1,1,1);
        if (size == null) size = 1f;
        if (shadow == null) shadow = false;
        if (mirror == null) mirror = false;
    }

    @Override
    @LuaWhitelist
    public void draw() {
        replaceNullWithDefaults();
        if (text == null) throw new LuaError("text cant be null!");
        task.construct(text, pos, color, shadow,mirror, size);
        LUtilsHUD.getGuiRenderTaskStack().push(task);
    }


    @LuaWhitelist
    @Override
    public TextRenderTaskBuilder reset() {
        text = null;
        pos = null;
        color = null;
        size = null;
        shadow = null;
        mirror = null;
        replaceNullWithDefaults();
        return this;
    }

    @LuaWhitelist
    @Override
    public void free() {
        CACHE.offerOld(this);
    }

    @LuaWhitelist
    @Override
    protected Object clone() {
        return of().text(text).color(color).size(size).shadow(shadow).mirror(mirror);
    }

    public static TextRenderTaskBuilder of() {
        return CACHE.getFresh();
    }
}
