package com.lenin.warpstonemod.client.gui.components;

import com.lenin.warpstonemod.client.gui.RawTextureResource;
import com.lenin.warpstonemod.client.gui.WSElement;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class ImageComponent extends Component implements IRenderable {

    protected RawTextureResource texture;
    protected int sourceSizeX, sourceSizeY;

    public ImageComponent (WSElement _parentElement, RawTextureResource _texture) {
        super(_parentElement);
        texture = _texture;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindTexture(texture.resource);

        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(0);
        blit(matrixStack, parentElement.getX(), parentElement.getY(), 0, 0, parentElement.getWidth(), parentElement.getHeight(), parentElement.getWidth(), parentElement.getHeight());
    }
}