package com.lenin.warpstonemod.client.gui.screens;

import com.lenin.warpstonemod.client.gui.Layer;
import com.lenin.warpstonemod.client.gui.RawTextureResource;
import com.lenin.warpstonemod.client.gui.WSElement;
import com.lenin.warpstonemod.client.gui.WSElement.*;
import com.lenin.warpstonemod.client.gui.elements.EvoElement;
import com.lenin.warpstonemod.client.gui.elements.MutationElement;
import com.lenin.warpstonemod.common.mutations.Mutation;
import com.lenin.warpstonemod.common.mutations.evolving_mutations.EvolvingMutation;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WSScreen extends Screen {

    protected final RawTextureResource screenResource;

    protected List<WSElement> elements = new ArrayList<>();

    protected Map<Integer, Layer> layers = new LinkedHashMap<>();

    protected final int sizeX, sizeY;
    protected int guiLeft, guiTop;

    protected WSElement currentlyHovered;

    protected WSScreen(ITextComponent title, RawTextureResource _screenResource, int _sizeX, int _sizeY) {
        super(title);
        sizeX = _sizeX;
        sizeY = _sizeY;
        screenResource = _screenResource;
    }

    @Override
    protected void init() {
        super.init();

        this.guiLeft = (this.width - this.sizeX) / 2;
        this.guiTop = (this.height - this.sizeY) / 2;

        elements.forEach(WSElement::clearComponents);
        elements.clear();

        List<Layer> sortedLayers = layers.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

        for (Layer sortedLayer : sortedLayers) {
            elements.addAll(sortedLayer.build());
        }
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

    protected void layer (Builder builder) {
        layer(builder, 0);
    }

    protected void layer (Builder builder, int index) {
        layers.computeIfAbsent(index, Layer::new);
        layers.get(index).addBuilder(builder);
    }

    public int getGuiLeft () { return guiLeft; }

    public int getGuiTop () { return guiTop; }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    protected Builder mutationElement (int x, int y, int width, int height, Mutation mut) {
        if (mut instanceof EvolvingMutation) return new EvoElement(x, y, width, height, this, (EvolvingMutation) mut);

        else return new MutationElement(x, y, width, height, this, mut);
    }
}