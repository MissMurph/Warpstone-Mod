package com.lenin.warpstonemod.client.gui.elements;

import com.lenin.warpstonemod.client.gui.RawTextureResource;
import com.lenin.warpstonemod.client.gui.Textures;
import com.lenin.warpstonemod.client.gui.WSElement;
import com.lenin.warpstonemod.client.gui.components.Component;
import com.lenin.warpstonemod.client.gui.components.ImageComponent;
import com.lenin.warpstonemod.client.gui.screens.WSScreen;
import com.lenin.warpstonemod.common.mutations.Mutation;
import com.lenin.warpstonemod.common.mutations.evolving_mutations.EvolvingMutation;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.util.text.ITextComponent;

public class MutationElement extends WSElement {

    protected Mutation mutation;

    protected MutationElement () {}

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.isHovered = isChildHovered() || (mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height);

        if (this.isHovered) {
            renderToolTip(matrixStack, mouseX, mouseY);

            for (WSElement element : childElements) {
                element.render(matrixStack, mouseX, mouseY, partialTicks);
            }
        }

        for (Component c : components) {
            if (c instanceof IRenderable) {
                ((IRenderable) c).render(matrixStack, mouseX, mouseY, partialTicks);
            }
        }
    }

    private boolean isChildHovered () {
        for (WSElement element : childElements) {
            if (element.isHovered()) return true;
        }

        return false;
    }

    public static class Builder extends WSElement.Builder<MutationElement> {
        public Builder(int _x, int _y, int _width, int _height, WSScreen _parentScreen, Mutation _mutation) {
            super(_x, _y, _width, _height, _parentScreen, new MutationElement());

            element.mutation = _mutation;

            addComponent(new ImageComponent(new RawTextureResource(_mutation.getTexture(), 18, 18, 0, 0)));

            if (element.mutation instanceof EvolvingMutation) {
                addChild(new TileSheetElement.Builder(element.x, element.y - 100, 100, 100, _parentScreen, Textures.SHEET_CONTAINER));
            }
            else {
                addTooltips(element.mutation.getToolTips().toArray(new ITextComponent[0]));
            }
        }
    }
}