package com.lenin.warpstonemod.client.gui.components;

import com.lenin.warpstonemod.client.gui.RawTextureResource;
import com.lenin.warpstonemod.client.gui.WSElement;
import com.lenin.warpstonemod.common.mutations.Mutation;
import com.lenin.warpstonemod.common.mutations.evolving_mutations.MutationTree;
import com.lenin.warpstonemod.common.mutations.evolving_mutations.MutationTree.*;
import com.lenin.warpstonemod.common.network.ChooseOptionalPacket;
import com.lenin.warpstonemod.common.network.PacketHandler;
import com.lenin.warpstonemod.common.network.SyncPlayerDataPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;

public class ChooseOptionalComponent extends ButtonComponent{
    public static IFactory factory (Mutation _parent, Mutation _target) {
        return (screen) -> new ChooseOptionalComponent(_parent, _target, screen);
    }

    protected ChooseOptionalComponent (Mutation _parent, Mutation _target, WSElement _parentElement) {
        super((button) -> ChooseOptionalComponent.choose(_parent, _target), null, _parentElement);
    }

    public static void choose (Mutation parent, Mutation target) {
        ChooseOptionalPacket pkt = new ChooseOptionalPacket(
                Minecraft.getInstance().player.getUUID(),
                parent.getRegistryName(),
                target.getRegistryName()
        );

        PacketHandler.CHANNEL.sendToServer(pkt);
    }
}