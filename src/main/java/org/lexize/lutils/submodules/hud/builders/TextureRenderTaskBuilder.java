package org.lexize.lutils.submodules.hud.builders;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.util.Identifier;
import org.lexize.lutils.submodules.LUtilsHUD;
import org.lexize.lutils.submodules.hud.TextureRenderTask;
import org.luaj.vm2.LuaError;
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
import org.moon.figura.utils.caching.CacheUtils;

@LuaWhitelist
public class TextureRenderTaskBuilder extends HUDRenderTaskBuilder<TextureRenderTaskBuilder>{

    private final static CacheUtils.Cache<TextureRenderTaskBuilder> CACHE = CacheUtils.getCache(TextureRenderTaskBuilder::new, 300);

    private final MinecraftClient mc;
    private Avatar _avatar;

    public FiguraVec3 pos = FiguraVec3.of(0,0,0);
    public FiguraVec2 size;
    public FiguraVec2 uv1;
    public FiguraVec2 uv2;
    public FiguraVec4 color = FiguraVec4.of(1,1,1,1);
    public Identifier texture;

    private TextureRenderTask task = new TextureRenderTask();

    public TextureRenderTaskBuilder() {
        mc = MinecraftClient.getInstance();
    }

    @LuaWhitelist
    public TextureRenderTaskBuilder pos(FiguraVec3 pos) {
        this.pos = pos;
        return this;
    }

    @LuaWhitelist
    public TextureRenderTaskBuilder size(FiguraVec2 size) {
        this.size = size;
        return this;
    }

    @LuaWhitelist
    public TextureRenderTaskBuilder color(FiguraVec4 color) {
        this.color = color;
        return this;
    }

    @LuaWhitelist
    public TextureRenderTaskBuilder uv1(FiguraVec2 uv1) {
        this.uv1 = uv1;
        return this;
    }

    @LuaWhitelist
    public TextureRenderTaskBuilder uv2(FiguraVec2 uv2) {
        this.uv2 = uv2;
        return this;
    }

    @LuaWhitelist
    public TextureRenderTaskBuilder texture(@LuaNotNil String type, Object texture) {
        var overrideType = FiguraTextureSet.OverrideType.valueOf(type.toUpperCase());
        Identifier texture_id = MissingSprite.getMissingSpriteId();
        switch (overrideType) {
            case RESOURCE -> {
                try {
                    Identifier id = new Identifier((String) texture);
                    System.out.println(texture);
                    texture_id = mc.getResourceManager().getResource(id).isPresent() ? id : MissingSprite.getMissingSpriteId();
                }
                catch (Exception ignored) {}
            }
            case PRIMARY -> {
                if (texture instanceof FiguraModelPart mp) {
                    if (mp.textures.size() > 0) {
                        texture_id = mp.textures.get(0).mainTex.textureID;
                    }
                }
            }
            case SECONDARY -> {
                if (texture instanceof FiguraModelPart mp) {
                    if (mp.textures.size() > 0) {
                        texture_id = mp.textures.get(0).emissiveTex.textureID;
                    }
                }
            }
            case CUSTOM -> {
                if (texture instanceof FiguraTexture t) texture_id = t.textureID;
                else if (texture instanceof String name) {
                    texture_id = new FiguraIdentifier("avatar_tex/" + _avatar.owner + "/custom/" + name);
                }
            }
            case SKIN, CAPE, ELYTRA -> {
                if (mc.player == null) break;

                PlayerListEntry info = MinecraftClient.getInstance().player.networkHandler.getPlayerListEntry(_avatar.owner);
                if (info == null) break;

                texture_id = switch (overrideType) {
                    case CAPE -> info.getCapeTexture();
                    case ELYTRA -> info.getElytraTexture() == null ? ElytraLayerAccessor.getWingsLocation() : info.getElytraTexture();
                    default -> info.getSkinTexture();
                };
            }
        }
        this.texture = texture_id;
        return this;
    }

    private TextureRenderTaskBuilder texture(Identifier texture) {
        this.texture = texture;
        return this;
    }

    @LuaWhitelist
    public FiguraVec3 getPos() {
        return pos;
    }

    @LuaWhitelist
    public FiguraVec2 getSize() {
        return size;
    }

    @LuaWhitelist
    public FiguraVec2 getUv1() {
        return uv1;
    }

    @LuaWhitelist
    public FiguraVec2 getUv2() {
        return uv2;
    }

    @LuaWhitelist
    public FiguraVec4 getColor() {
        return color;
    }

    @Override
    public void replaceNullWithDefaults() {
        if (pos == null) pos = FiguraVec3.of();
        if (color == null) color = FiguraVec4.of(1,1,1,1);
        if (uv1 == null) uv1 = FiguraVec2.of();
        if (uv2 == null) uv2 = FiguraVec2.of(1,1);
    }

    @LuaWhitelist
    @Override
    public void draw() {
        replaceNullWithDefaults();
        if (size == null || texture == null) {
            throw new LuaError("size and texture cant be null!");
        }
        task.construct(pos,size,uv1,uv2,texture,color);
        LUtilsHUD.getGuiRenderTaskStack().push(task);
    }

    @LuaWhitelist
    @Override
    public TextureRenderTaskBuilder reset() {
        pos = null;
        size = null;
        uv1 = uv2 = null;
        texture = null;
        color = null;
        replaceNullWithDefaults();
        return this;
    }

    @LuaWhitelist
    @Override
    public void free() {
        CACHE.offerOld(this);
    }

    public static TextureRenderTaskBuilder of(Avatar avatar) {
        TextureRenderTaskBuilder builder = CACHE.getFresh();
        builder._avatar = avatar;
        return builder;
    }

    @LuaWhitelist
    @Override
    protected Object clone() {
        return of(_avatar).pos(pos).size(size).uv1(uv1).uv2(uv2).texture(texture).color(color);
    }
}
