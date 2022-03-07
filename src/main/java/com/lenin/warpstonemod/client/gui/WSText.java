package com.lenin.warpstonemod.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;

public class WSText extends WSElement {

    protected ITextProperties text;

    public WSText(int _x, int _y, int _width, int _height, ITextProperties _text) {
        super(_x, _y, _width, _height);

        text = _text;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();

        fontRenderer.drawText(matrixStack, (ITextComponent) text, this.x, this.y, 1);

        blit(matrixStack, x, y, width, height, 0, 0);
    }
}