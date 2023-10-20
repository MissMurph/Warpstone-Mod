package com.lenin.warpstonemod.client.renderers;

import com.lenin.warpstonemod.client.models.RatModel;
import com.lenin.warpstonemod.common.Warpstone;
import com.lenin.warpstonemod.common.entities.RatEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class RatRenderer extends MobRenderer<RatEntity, RatModel<RatEntity>> {

    protected static final ResourceLocation TEXTURE = Warpstone.key("textures/entity/rat.png");

    public RatRenderer(EntityRendererManager renderManager) {
        super(renderManager, new RatModel<>(), 0.3f);
    }

    @Override
    public ResourceLocation getTextureLocation(RatEntity entity) {
        return TEXTURE;
    }
}