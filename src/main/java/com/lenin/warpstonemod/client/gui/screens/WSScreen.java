package com.lenin.warpstonemod.client.gui.screens;

import com.lenin.warpstonemod.client.gui.RawTextureResource;
import com.lenin.warpstonemod.client.gui.WSElement;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

public class WSScreen extends Screen {

    protected final RawTextureResource screenResource;

    protected List<WSElement> elements = new ArrayList<>();

    protected final int sizeX, sizeY;

    protected int guiLeft, guiTop;

    protected WSScreen(ITextComponent title, RawTextureResource _screenResource, int _sizeX, int _sizeY) {
        super(title);
        sizeX = _sizeX;
        sizeY = _sizeY;
        screenResource = _screenResource;
    }

    @Override
    protected void init() {
        super.init();

        elements.forEach(WSElement::clearComponents);
        elements.clear();

        this.guiLeft = (this.width - this.sizeX) / 2;
        this.guiTop = (this.height - this.sizeY) / 2;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(screenResource.resource);
        int i = this.guiLeft;
        int j = this.guiTop;
        this.blit(matrixStack, i, j, 0, 0, this.sizeX, this.sizeY);

        for (WSElement w : elements) {
            w.render(matrixStack, mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (WSElement element : this.elements) {
            element.onClick(mouseX, mouseY, button);
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    public int getGuiLeft () { return guiLeft; }

    public int getGuiTop () { return guiTop; }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}