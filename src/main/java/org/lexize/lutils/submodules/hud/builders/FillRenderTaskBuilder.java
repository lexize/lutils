package org.lexize.lutils.submodules.hud.builders;

import org.lexize.lutils.submodules.LUtilsHUD;
import org.lexize.lutils.submodules.hud.FillRenderTask;
import org.luaj.vm2.LuaError;
import org.moon.figura.lua.LuaWhitelist;
import org.moon.figura.math.vector.FiguraVec2;
import org.moon.figura.math.vector.FiguraVec3;
import org.moon.figura.math.vector.FiguraVec4;

@LuaWhitelist
public class FillRenderTaskBuilder implements HUDRenderTaskBuilder {
    public FiguraVec4 color = FiguraVec4.of(1,1,1,1);
    public FiguraVec3 pos = FiguraVec3.of();
    public FiguraVec2 size = FiguraVec2.of();

    private FillRenderTask task = new FillRenderTask();

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
        if (size == null || color == null) throw new LuaError("size and color cant be null!");
        task.construct(pos, size, color);
        LUtilsHUD.getGuiRenderTaskStack().push(task);
    }
}