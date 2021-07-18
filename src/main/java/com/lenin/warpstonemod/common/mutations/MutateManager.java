package com.lenin.warpstonemod.common.mutations;

import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.network.SyncMutDataPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;

public class MutateManager {
    private final LivingEntity parentEntity;
    private final List<Mutation> attributeMutations = new ArrayList<Mutation>();

    private CompoundNBT mutData;

    private int instability;

    public MutateManager (LivingEntity _parentEntity){
        parentEntity = _parentEntity;
        instability = 0;

        //On the Manager's creation we create the Mutations list
        WarpMutations[] array = WarpMutations.values();
        for (WarpMutations warpMutations : array) {
            attributeMutations.add(warpMutations.constructMutation(warpMutations, parentEntity));
        }

        mutData = serialize();
        MutateHelper.pushMutDataToClient(parentEntity.getUniqueID(), getMutData());
    }

    public void mutate(){
        //Loop over every point of instablity and apply levels, no negatives if no instablity
        for (int i = 0; i < instability; i++) {
            attributeMutations.get(WarpstoneMain.getRandom().nextInt(attributeMutations.size())).changeLevel(5);

            if (i > 0) { attributeMutations.get(WarpstoneMain.getRandom().nextInt(attributeMutations.size())).changeLevel(-5); }
        }

        instability++;
        mutData = serialize();

        if (mutData == null) System.out.println("The actual mutData is false");

        MutateHelper.pushMutDataToClient(parentEntity.getUniqueID(), getMutData());
    }

    private CompoundNBT serialize (){
        CompoundNBT out = new CompoundNBT();
        out.putUniqueId("player", parentEntity.getUniqueID());
        out.putInt("instablity", getInstability());

        for (Mutation mut : getMutations()) {
            out.putInt(mut.getMutationType(), mut.getMutationLevel());
        }
        return out;
    }

    public void loadFromNBT (CompoundNBT nbt) {
        instability = nbt.getInt("instability");

        for (Mutation mut : getMutations()) {
            mut.setLevel(nbt.getInt(mut.getMutationType()));
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void receiveDataFromServer (SyncMutDataPacket pkt){
        loadFromNBT(pkt.data);
    }

    public List<Mutation> getMutations() {
        return attributeMutations;
    }

    public LivingEntity getParentEntity (){
        return parentEntity;
    }

    public int getInstability(){
        return instability;
    }

    public CompoundNBT getMutData () { if (mutData == null) System.out.println("Getting the NBT from Manager returns null"); return mutData; }
}