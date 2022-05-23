package com.lenin.warpstonemod.client.gui.elements;

import com.lenin.warpstonemod.client.gui.RawTextureResource;
import com.lenin.warpstonemod.client.gui.WSElement;
import com.lenin.warpstonemod.client.gui.WSElement.*;
import com.lenin.warpstonemod.client.gui.components.ImageComponent;
import com.lenin.warpstonemod.client.gui.screens.WSScreen;
import com.lenin.warpstonemod.common.mutations.Mutation;
import net.minecraft.util.text.ITextComponent;

public class MutationElement extends Builder {

    public MutationElement(int _x, int _y, int _width, int _height, WSScreen _parentScreen, Mutation mutation) {
        super(_x, _y, _width, _height, _parentScreen);

        RawTextureResource texture = new RawTextureResource(mutation.getTexture(), 18, 18, 0, 0);
        addTooltips(mutation.getToolTips().toArray(new ITextComponent[0]));

        addComponent(ImageComponent.factory(texture));
    }
}