package com.lenin.warpstonemod.client.gui.components;

import com.lenin.warpstonemod.client.gui.IClickable;
import com.lenin.warpstonemod.client.gui.RawTextureResource;
import com.lenin.warpstonemod.client.gui.WSElement;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.IRenderable;

public class ButtonComponent extends Component implements IClickable, IRenderable {
    protected RawTextureResource hoveredTexture;
    protected ButtonComponent.IPressable onPress;

    protected ImageComponent imageComponent;

    public static IFactory factory (ButtonComponent.IPressable function, RawTextureResource _hoveredTexture) {
        return (screen) -> new ButtonComponent(function, _hoveredTexture, screen);
    }

    protected ButtonComponent (ButtonComponent.IPressable function, RawTextureResource _hoveredTexture, WSElement _parentScreen) {
        super(_parentScreen);
        onPress = function;
        hoveredTexture = _hoveredTexture;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (hoveredTexture == null || imageComponent == null) return;

        if (parentElement.isHovered()) {
            imageComponent.updateTexture(hoveredTexture);
        } else imageComponent.updateTexture(null);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int click) {
        if (parentElement.isHovered()) this.onPress.onPress(this);
    }

    public interface IPressable {
        void onPress(ButtonComponent button);
    }
}