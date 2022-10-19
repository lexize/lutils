package org.lexize.lutils.submodules.hud.builders;

import net.minecraft.client.util.math.MatrixStack;
import org.lexize.lutils.submodules.LUtilsHUD;
import org.lexize.lutils.submodules.hud.FillRenderTask;
import org.luaj.vm2.LuaError;
import org.moon.figura.lua.LuaWhitelist;
import org.moon.figura.math.vector.FiguraVec2;
import org.moon.figura.math.vector.FiguraVec3;
import org.moon.figura.math.vector.FiguraVec4;
import org.moon.figura.utils.caching.CacheUtils;
import org.moon.figura.utils.caching.CachedType;

@LuaWhitelist
public class FillRenderTaskBuilder extends HUDRenderTaskBuilder {

    public FillRenderTaskBuilder() {}


    public FiguraVec4 color = FiguraVec4.of(1,1,1,1);
    public FiguraVec3 pos = FiguraVec3.of();
    public FiguraVec2 size = FiguraVec2.of();

    @LuaWhitelist
    public FillRenderTaskBuilder color(FiguraVec4 color) {
        this.color = color;
        return this;
    }

    @LuaWhitelist
    public FillRenderTaskBuilder pos(FiguraVec3 pos) {
        this.pos = pos;
        return this;
    }

    @LuaWhitelist
    public FillRenderTaskBuilder size(FiguraVec2 size) {
        this.size = size;
        return this;
    }

    @LuaWhitelist
    public FiguraVec4 getColor() {
        return color;
    }

    @LuaWhitelist
    public FiguraVec3 getPos() {
        return pos;
    }

    @LuaWhitelist
    public FiguraVec2 getSize() {
        return size;
    }

    @Override
    public void replaceNullWithDefaults() {
        if (pos == null) pos = FiguraVec3.of();
    }

    @LuaWhitelist
    @Override
    public void draw() {
        replaceNullWithDefaults();
        if (size == null || color == null) throw new LuaError("size and color cant be null!");
        FillRenderTask task = FillRenderTask.of();
        task.construct(pos, size, color);
        LUtilsHUD.getGuiRenderTaskStack().push(task);
    }

    @LuaWhitelist
    public FillRenderTaskBuilder copy() {
        return new FillRenderTaskBuilder()
                .color(color != null ? color.copy() : null)
                .pos(pos != null ? pos.copy() : null)
                .size(size != null ? size.copy() : null);
    }

}