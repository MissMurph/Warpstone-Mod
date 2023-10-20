package com.lenin.warpstonemod.client.gui.components;

import com.lenin.warpstonemod.client.gui.RawTextureResource;
import com.lenin.warpstonemod.client.gui.WSElement;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.util.text.ITextComponent;

public class TextComponent extends Component implements IRenderable {
    protected ITextComponent text;

    public static IFactory factory (ITextComponent _text) {
        return (screen) -> new TextComponent(_text, screen);
    }

    protected TextComponent(ITextComponent _text, WSElement _parentScreen) {
        super(_parentScreen);

        text = _text;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        FontRenderer fontRenderer = Minecraft.getInstance().font;
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, parentElement.getAlpha());
        RenderSystem.enableBlend();

        fontRenderer.draw(matrixStack, text, parentElement.getX(), parentElement.getY(), 1);

        blit(matrixStack, parentElement.getX(), parentElement.getY(), 0, 0, 0, 0);
    }

    public void updateText (ITextComponent _text) {
        text = _text;
    }
}