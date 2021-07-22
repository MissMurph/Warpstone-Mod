package com.lenin.warpstonemod.common.mutations;

import com.lenin.warpstonemod.common.WarpstoneMain;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.nbt.CompoundNBT;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MutateManager {
    private final LivingEntity parentEntity;
    private final List<Mutation> attributeMutations = new ArrayList<>();

    private CompoundNBT mutData;

    private int instability;

    public MutateManager (LivingEntity _parentEntity){
        parentEntity = _parentEntity;
        instability = 0;

        //On the Manager's creation we create the Mutations list
        WarpMutations[] array = WarpMutations.values();
        for (WarpMutations warpMutations : array) {
            attributeMutations.add(warpMutations.constructAttributeMutation(warpMutations, parentEntity));
        }

        mutData = serialize();
    }

    public void mutate(){
        //Loop over every point of instablity and apply levels, no negatives if no instablity
        for (int i = 0; i < instability + 1; i++) {
            attributeMutations.get(WarpstoneMain.getRandom().nextInt(attributeMutations.size())).changeLevel(5);

            if (i > 0) { attributeMutations.get(WarpstoneMain.getRandom().nextInt(attributeMutations.size())).changeLevel(-5); }
        }

        instability++;
        mutData = serialize();

        for (Mutation m : attributeMutations) {
            if (m.getMutationType().equals(Attributes.ATTACK_SPEED.getAttributeName())) {
                System.out.println("Mutation: " + m.getMutationLevel() + " Attribute: " + parentEntity.getAttribute(Attributes.ATTACK_SPEED).getValue());
            }
        }

        if (parentEntity.getAttributeManager().hasModifier(Attributes.ATTACK_SPEED, UUID.fromString("380312ba-82a6-4b9e-b576-4c5d9e2b1a32"))) System.out.println("Modifier is present");

        MutateHelper.pushMutDataToClient(parentEntity.getUniqueID(), getMutData());
    }

    private CompoundNBT serialize (){
        CompoundNBT out = new CompoundNBT();
        out.putUniqueId("player", parentEntity.getUniqueID());
        out.putInt("instability", getInstability());

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

    public void resetMutations () {

    }

    //@OnlyIn(Dist.CLIENT)
    //private void receiveDataFromServer (SyncMutDataPacket pkt){ loadFromNBT(pkt.data); }

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