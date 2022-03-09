package com.lenin.warpstonemod.client.gui.components;

import com.lenin.warpstonemod.client.gui.RawTextureResource;
import com.lenin.warpstonemod.client.gui.WSElement;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.util.ResourceLocation;

public class ImageComponent extends Component implements IRenderable {

    protected RawTextureResource baseTexture;
    protected RawTextureResource activeTexture;

    public ImageComponent (RawTextureResource _texture) {
        baseTexture = _texture;
        activeTexture = baseTexture;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindTexture(activeTexture.resource);

        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(0);
        blit(matrixStack, parentElement.getX(), parentElement.getY(),
                0, 0,
                parentElement.getWidth(), parentElement.getHeight(),
                activeTexture.sourceSizeX, activeTexture.sourceSizeY);
    }

    public void updateTexture (RawTextureResource texture) {
        if (texture == null) activeTexture = baseTexture;
        else activeTexture = texture;
    }
}