package com.lenin.warpstonemod.client.gui.components;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.util.text.ITextComponent;

public class TextComponent extends Component implements IRenderable {
    protected ITextComponent text;

    public TextComponent(ITextComponent _text) {
        super();

        text = _text;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, parentElement.getAlpha());
        RenderSystem.enableBlend();

        fontRenderer.drawText(matrixStack, text, parentElement.getY(), parentElement.getWidth(), 1);

        blit(matrixStack, parentElement.getX(), parentElement.getY(), parentElement.getWidth(), parentElement.getHeight(), 0, 0);
    }

    public void updateText (ITextComponent _text) {
        text = _text;
    }
}