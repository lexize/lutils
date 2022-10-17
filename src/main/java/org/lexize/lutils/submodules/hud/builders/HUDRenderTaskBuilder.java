package org.lexize.lutils.submodules.hud.builders;

public interface HUDRenderTaskBuilder {
    void replaceNullWithDefaults();
    void draw();
}
