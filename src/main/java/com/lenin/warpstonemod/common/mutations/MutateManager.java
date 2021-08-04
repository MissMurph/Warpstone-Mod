package com.lenin.warpstonemod.common.mutations;

import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;

import java.util.ArrayList;
import java.util.List;

public class MutateManager {
    protected final LivingEntity parentEntity;
    protected final List<AttributeMutation> attributeMutations = new ArrayList<>();
    protected List<EffectMutation> effectMutations = new ArrayList<>();

    protected CompoundNBT mutData;

    protected int instability;

    public MutateManager (LivingEntity _parentEntity){
        parentEntity = _parentEntity;
        instability = 0;

        if (_parentEntity == null) return;

        //On the Manager's creation we create the Attribute Mutations classList
        WarpMutations[] array = WarpMutations.values();
        for (WarpMutations warpMutations : array) {
            attributeMutations.add(WarpMutations.constructAttributeMutation(warpMutations, parentEntity));
        }

        mutData = serialize();
    }

    public void mutate(){
        boolean hasEffectBeenCreated = false;

        //Loop over every point of instablity and apply levels, no negatives if no instablity
        for (int i = 0; i < instability + 1; i++) {
            if (!hasEffectBeenCreated && effectMutations.size() < EffectsMap.getMapSize() && WarpstoneMain.getRandom().nextInt(100) > 75) {
                EffectMutation mut = getRandomEffectMut();

                effectMutations.add(mut);
                mut.applyMutation(parentEntity);

                hasEffectBeenCreated = true;
            } else {
                attributeMutations.get(WarpstoneMain.getRandom().nextInt(attributeMutations.size())).changeLevel(5);
            }

            if (i > 0) {
                attributeMutations.get(WarpstoneMain.getRandom().nextInt(attributeMutations.size())).changeLevel(-5);
            }
        }

        instability++;
        mutData = serialize();

        MutateHelper.pushMutDataToClient(parentEntity.getUniqueID(), getMutData());
    }

    protected EffectMutation getRandomEffectMut () {
        if (effectMutations.size() >= EffectsMap.getMapSize()) return null;

        while (true) {
            int i = WarpstoneMain.getRandom().nextInt(EffectsMap.getMapSize());

            if (!containsEffect(i)) {
                int i2 = WarpstoneMain.getRandom().nextBoolean() ? 1 : -1;

                return EffectsMap.constructInstance(i, parentEntity, i2);
            }
        }
    }

    protected CompoundNBT serialize (){
        CompoundNBT out = new CompoundNBT();
        out.putUniqueId("player", parentEntity.getUniqueID());
        out.putInt("instability", getInstability());

        for (AttributeMutation mut : getAttributeMutations()) {
            out.putInt(mut.getMutationType(), mut.getMutationLevel());
        }

        List<Integer> classList = new ArrayList<>();

        for (EffectMutation m : effectMutations) {
            classList.add(m.getMutationID());
            out.putInt(String.valueOf(m.getMutationID()), m.getInstance(parentEntity).getMutationLevel());
        }

        if (!classList.isEmpty()) out.putIntArray("effect_mutations", classList);

        return out;
    }

    public void loadFromNBT (CompoundNBT nbt) {
        instability = nbt.getInt("instability");
        mutData = nbt;

        for (AttributeMutation mut : getAttributeMutations()) {
            mut.setLevel(nbt.getInt(mut.getMutationType()));
        }

       if (nbt.contains("effect_mutations")) {
            int[] array = nbt.getIntArray("effect_mutations");

            for (int i : array) {
                if (containsEffect(i)) continue;
                EffectMutation mut = EffectsMap.constructInstance(i, parentEntity, nbt.getInt(String.valueOf(i)));
                System.out.println(mut.getMutationName(mut.getInstance(parentEntity).getMutationLevel()) + " created");
                effectMutations.add(mut);
                mut.applyMutation(parentEntity);
            }
        }
    }

    public void resetMutations () {
        //for (Mutation m : getMutations()) { m.clearMutation(); }

        for (AttributeMutation m : attributeMutations) { m.setLevel(0); }

        effectMutations.clear();
    }

    public List<AttributeMutation> getAttributeMutations (){
        return attributeMutations;
    }

    public List<EffectMutation> getEffectMutations (){
        return effectMutations;
    }

    public LivingEntity getParentEntity (){
        return parentEntity;
    }

    public int getInstability(){
        return instability;
    }

    public CompoundNBT getMutData () {
        if (mutData == null) System.out.println("Getting the NBT from Manager returns null");
        return mutData;
    }

    public void unload() {
        MutateHelper.savePlayerData(parentEntity.getUniqueID(), getMutData());

        for (EffectMutation m : effectMutations) {
            m.clearInstance(this.parentEntity);
        }

        effectMutations.clear();
        attributeMutations.clear();
        MutateHelper.managers.remove(this);
    }

    public boolean containsEffect (int id) {
        for (EffectMutation m : effectMutations) {
            if (m.getMutationID() == id) return true;
        }
        return false;
    }
}