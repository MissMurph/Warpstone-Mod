package com.lenin.warpstonemod.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;

public class WSImage extends WSElement {
    protected ResourceLocation texture;

    public WSImage(int _x, int _y, int _width, int _height, ResourceLocation _texture) {
        super(_x, _y, _width, _height);

        texture = _texture;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);


    }
}