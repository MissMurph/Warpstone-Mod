package com.lenin.warpstonemod.client.gui.components;

import com.lenin.warpstonemod.client.gui.RawTextureResource;
import com.lenin.warpstonemod.client.gui.WSElement;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.util.SoundEvents;

public class ButtonComponent extends Component implements IClickable, IRenderable {
    protected RawTextureResource hoveredTexture;
    protected ButtonComponent.IPressable onPress;

    protected ImageComponent imageComponent;

    public ButtonComponent (ButtonComponent.IPressable function, RawTextureResource _hoveredTexture) {
        onPress = function;
        hoveredTexture = _hoveredTexture;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (parentElement.isHovered()) imageComponent.updateTexture(hoveredTexture);
        else imageComponent.updateTexture(null);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int click) {
        if (parentElement.isHovered()) this.onPress.onPress(this);
    }

    public interface IPressable {
        void onPress(ButtonComponent button);
    }

    @Override
    public void setParent(WSElement element) {
        super.setParent(element);

        imageComponent = (ImageComponent) parentElement.getComponent(ImageComponent.class);
    }
}