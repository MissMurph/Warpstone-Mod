package com.lenin.warpstonemod.client.gui.screens;

import com.lenin.warpstonemod.client.gui.RawTextureResource;
import com.lenin.warpstonemod.client.gui.WSElement;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

public class WSScreen extends Screen {

    protected final RawTextureResource screenResource;

    protected static final int MAX_LAYERS = 10;
    protected List<WSElement> elements = new ArrayList<>();
    protected final List<List<WSElement.AbstractBuilder<? extends WSElement>>> layers = new ArrayList<>(MAX_LAYERS);

    protected final int sizeX, sizeY;
    protected int guiLeft, guiTop;

    protected WSElement currentlyHovered;

    protected WSScreen(ITextComponent title, RawTextureResource _screenResource, int _sizeX, int _sizeY) {
        super(title);
        sizeX = _sizeX;
        sizeY = _sizeY;
        screenResource = _screenResource;

        for (int i = 0; i < MAX_LAYERS; i++) {
            layers.add(i, new ArrayList<>());
        }
    }

    @Override
    protected void init() {
        super.init();

        this.guiLeft = (this.width - this.sizeX) / 2;
        this.guiTop = (this.height - this.sizeY) / 2;

        elements.forEach(WSElement::clearComponents);
        elements.clear();

        loadLayers();
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

        if (currentlyHovered != null) currentlyHovered.onHover(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        InputMappings.Input mouseKey = InputMappings.getInputByCode(keyCode, scanCode);
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        } else if (this.minecraft.gameSettings.keyBindInventory.isActiveAndMatches(mouseKey)) {
            this.closeScreen();
            return true;
        }
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (WSElement element : this.elements) {
            element.onClick(mouseX, mouseY, button);
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    public void setHovered (WSElement element) {
        if (currentlyHovered == null) currentlyHovered = element;
    }

    public void clearHovered (WSElement element) {
        if (currentlyHovered == element) currentlyHovered = null;
    }

    protected void layer (WSElement.AbstractBuilder<? extends WSElement> builder) {
        layers.get(0).add(builder);
    }

    protected void layer (WSElement.AbstractBuilder<? extends WSElement> builder, int layer) {
        int index = Math.max(Math.min(MAX_LAYERS - 1, layer), 0);

        layers.get(index).add(builder);
    }

    protected void loadLayers () {
        for (List<WSElement.AbstractBuilder<? extends WSElement>> layer : layers) {
            for (WSElement.AbstractBuilder<? extends WSElement> builder : layer) {
                elements.add(builder.build());
            }
        }
    }

    public int getGuiLeft () { return guiLeft; }

    public int getGuiTop () { return guiTop; }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}