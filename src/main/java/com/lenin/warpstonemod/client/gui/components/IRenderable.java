package com.lenin.warpstonemod.client.gui.components;

import com.mojang.blaze3d.matrix.MatrixStack;

public interface IRenderable {

    void render (MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks);
}