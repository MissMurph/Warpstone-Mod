package com.lenin.warpstonemod.common.mutations;

import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.items.IWarpstoneConsumable;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;

import java.util.*;

public class MutateManager {
    protected final LivingEntity parentEntity;
    protected final List<AttributeMutation> attributeMutations = new ArrayList<>();
    protected Map<Integer, Integer> effectMutations = new HashMap<>();

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

    public void mutate(IWarpstoneConsumable item){
        boolean hasEffectBeenCreated = false;

        int instabilityValue = item.getCorruptionValue() + (int) Math.floor(item.getCorruptionValue() * ((double)getInstability() / 100));

        //Loop over every point of instablity and apply levels, no negatives if no instablity
        for (int i = 0; i < getInstabilityLevel() + 1; i++) {
            if (!hasEffectBeenCreated && effectMutations.size() < WarpstoneMain.getEffectsMap().getMapSize() && WarpstoneMain.getRandom().nextInt(100) > 75) {
                EffectMutation mut = getRandomEffectMut();

                effectMutations.put(mut.getMutationID(), mut.getInstance(this.parentEntity).getMutationLevel());
                mut.applyMutation(parentEntity);

                hasEffectBeenCreated = true;
            } else {
                attributeMutations.get(WarpstoneMain.getRandom().nextInt(attributeMutations.size())).changeLevel(5);
            }

            if (i > 0) {
                attributeMutations.get(WarpstoneMain.getRandom().nextInt(attributeMutations.size())).changeLevel(-5);
            }
        }

        instability += instabilityValue;
        mutData = serialize();

        MutateHelper.pushMutDataToClient(parentEntity.getUniqueID(), getMutData());
    }

    protected EffectMutation getRandomEffectMut () {
        if (effectMutations.size() >= WarpstoneMain.getEffectsMap().getMapSize()) return null;

        while (true) {
            int i = WarpstoneMain.getRandom().nextInt(WarpstoneMain.getEffectsMap().getMapSize());

            if (!containsEffect(i)) {
                int i2 = WarpstoneMain.getRandom().nextBoolean() ? 1 : -1;

                return WarpstoneMain.getEffectsMap().constructInstance(i, parentEntity, i2);
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

        for (int i : effectMutations.keySet()) {
            classList.add(i);
            out.putInt(String.valueOf(i), getEffect(i).getInstance(parentEntity).getMutationLevel());
        }

        out.putIntArray("effect_mutations", classList);

        return out;
    }

    public void loadFromNBT (CompoundNBT nbt) {
        instability = nbt.getInt("instability");
        mutData = nbt;

        for (AttributeMutation mut : getAttributeMutations()) {
            mut.setLevel(nbt.getInt(mut.getMutationType()));
        }

        int[] array = nbt.getIntArray("effect_mutations");
        List<Integer> deletion = new ArrayList<>(effectMutations.keySet());

        for (int i : array) {
            if (containsEffect(i)) { deletion.remove((Integer) i); continue; }
            effectMutations.put(i, nbt.getInt(String.valueOf(i)));

            if (!parentEntity.world.isRemote()) {
                EffectMutation mut = WarpstoneMain.getEffectsMap().constructInstance(i, parentEntity, nbt.getInt(String.valueOf(i)));
                mut.applyMutation(parentEntity);
            }
        }

        for (int i : deletion) {
            effectMutations.remove((Integer) i);
        }
    }

    public void resetMutations () {
        for (AttributeMutation m : attributeMutations) { m.setLevel(0); }

        for (int i : effectMutations.keySet()) { getEffect(i).clearInstance(this.parentEntity); }
        effectMutations.clear();

        instability = 0;
        mutData = serialize();

        MutateHelper.pushMutDataToClient(parentEntity.getUniqueID(), getMutData());
    }

    public List<AttributeMutation> getAttributeMutations (){
        return attributeMutations;
    }

    public List<Integer> getEffectMutations (){
            return new ArrayList<>(effectMutations.keySet());
    }

    public Map<Integer, Integer> getEffectLevelsMap (){
        return effectMutations;
    }

    public LivingEntity getParentEntity (){
        return this.parentEntity;
    }

    public int getInstability(){
        return instability;
    }

    public int getInstabilityLevel (){
        return (int) Math.floor((double) (instability) / 100);
    }

    public CompoundNBT getMutData () {
        if (mutData == null) System.out.println("Getting the NBT from Manager returns null");
        return mutData;
    }

    public void unload() {
        saveData();

        for (int i : effectMutations.keySet()) {
            getEffect(i).clearInstance(this.parentEntity);
        }

        effectMutations.clear();
        attributeMutations.clear();
        MutateHelper.managers.remove(this);
    }

    public void saveData (){
        mutData = serialize();
        MutateHelper.savePlayerData(parentEntity.getUniqueID(), getMutData());
    }

    private EffectMutation getEffect (int id) {
        return WarpstoneMain.getEffectsMap().getMap().get(id);
    }

    public boolean containsEffect (int id) {
        for (int i : effectMutations.keySet()) {
            if (i == id) return true;
        }
        return false;
    }
}