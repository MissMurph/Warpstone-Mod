package com.lenin.warpstonemod.client.gui.components;

import com.lenin.warpstonemod.client.gui.Layer;
import com.lenin.warpstonemod.client.gui.WSElement;
import com.lenin.warpstonemod.client.gui.WSElement.*;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.IRenderable;

import java.util.HashMap;
import java.util.Map;

public class HoveredElementComponent extends Component implements IRenderable {
    protected final WSElement childElement;

    protected boolean isHovered;

    public static IFactory factory (Builder _childBuilder) {
        return (screen) -> new HoveredElementComponent(_childBuilder, screen);
    }

    protected HoveredElementComponent(Builder _childBuilder, WSElement _parentElement) {
        super(_parentElement);

        childElement = _childBuilder.build();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        isHovered = isHovered && isWithinChildBounds(mouseX, mouseY) || parentElement.isHovered();

        if (isHovered) {
            childElement.render(matrixStack, mouseX, mouseY, partialTicks);
        }
    }

    public boolean isWithinChildBounds (int mouseX, int mouseY) {
        return childElement.isWithinBounds(mouseX, mouseY) || childElement.isWithinChildBounds(mouseX, mouseY);
    }
}