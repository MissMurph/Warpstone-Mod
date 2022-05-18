package com.lenin.warpstonemod.client.gui.elements;

import com.lenin.warpstonemod.client.gui.RawTextureResource;
import com.lenin.warpstonemod.client.gui.Textures;
import com.lenin.warpstonemod.client.gui.WSElement;
import com.lenin.warpstonemod.client.gui.components.ButtonComponent;
import com.lenin.warpstonemod.client.gui.components.ImageComponent;
import com.lenin.warpstonemod.client.gui.screens.WSScreen;
import com.lenin.warpstonemod.common.mutations.MutateHelper;
import com.lenin.warpstonemod.common.mutations.Mutation;
import com.lenin.warpstonemod.common.mutations.evolving_mutations.EvolvingMutation;
import com.lenin.warpstonemod.common.mutations.evolving_mutations.MutationTree;
import com.lenin.warpstonemod.common.network.ChooseOptionalPacket;
import com.lenin.warpstonemod.common.network.PacketHandler;
import com.lenin.warpstonemod.common.network.SyncPlayerDataPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MutationElement extends WSElement {

    protected Mutation mutation;

    protected MutationElement () {}

    public static class Builder extends WSElement.AbstractBuilder<MutationElement> {
        public Builder(int _x, int _y, int _width, int _height, WSScreen _parentScreen, Mutation _mutation) {
            super(_x, _y, _width, _height, _parentScreen, new MutationElement());

            element.mutation = _mutation;

            RawTextureResource texture = new RawTextureResource(_mutation.getTexture(), 18, 18, 0, 0);
            addTooltips(element.mutation.getToolTips().toArray(new ITextComponent[0]));

            addComponent(new ImageComponent(texture));
        }
    }
}