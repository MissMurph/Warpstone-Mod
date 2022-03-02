package com.lenin.warpstonemod.common.mutations;

import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.items.IWarpstoneConsumable;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvents;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MutateManager {
    protected final LivingEntity parentEntity;
    protected final List<AttributeMutation> attributeMutations = new ArrayList<>();
    protected List<Integer> effectMutations = new ArrayList<>();

    protected CompoundNBT mutData;

    protected int instability;
    protected int corruption;

    public MutateManager (LivingEntity _parentEntity){
        parentEntity = _parentEntity;
        instability = 0;
        corruption = 0;

        if (_parentEntity == null) return;

        WarpMutations[] array = WarpMutations.values();
        for (WarpMutations warpMutations : array) {
            attributeMutations.add(WarpMutations.constructAttributeMutation(warpMutations, this));
        }

        mutData = serialize();
    }

    public void mutate(IWarpstoneConsumable item){
        boolean hasEffectBeenCreated = false;

        //Loop over every point of instablity and apply levels, no negatives if no instablity
        for (int i = 0; i < getInstabilityLevel() + 1; i++) {
            if (effectMutations.size() < 14 && !hasEffectBeenCreated && WarpstoneMain.getRandom().nextInt(100) > 90) {
                EffectMutation mut = getRandomEffectMut();

                if (mut != null) {
                    effectMutations.add(mut.getMutationID());
                    mut.applyMutation(parentEntity);

                    continue;
                }

                //If no effect mutations can be added then there's no point checking this each iteration so we stop it here
                hasEffectBeenCreated = true;
            }

            List<AttributeMutation> legal = attributeMutations
                    .stream()
                    .filter(attr -> attr.canMutate(this))
                    .collect(Collectors.toList());

            int change = getCorruptionLevel() > 0 ? WarpstoneMain.getRandom().nextInt(getCorruptionLevel()) + 1 : 1;

            if (i > 0) {
                int index = WarpstoneMain.getRandom().nextInt(legal.size());
                legal.get(index).changeLevel(-change);
                legal.remove(index);
            }

            if (i >= 8) change = WarpstoneMain.getRandom().nextInt(100) > 100 - (5 * (getInstabilityLevel() - getCorruptionLevel())) ? change * -1 : change;

            legal.get(WarpstoneMain.getRandom().nextInt(legal.size()))
                    .changeLevel(change);
        }

        double witherRisk = getWitherRisk(item.getCorruptionValue());
        if (Math.random() > 1f - witherRisk) {
            int duration = WarpstoneMain.getRandom().nextInt((int) Math.round(2400 * witherRisk));
            parentEntity.addPotionEffect(new EffectInstance(Effects.WITHER, duration));
        }

        int instabilityValue = item.getCorruptionValue() + (int) Math.round(item.getCorruptionValue() * (
                (double)getInstability() / 100) * (double)(WarpstoneMain.getRandom().nextInt((getCorruptionLevel() + 2) * 10) / 100)
        );
        int corruptionValue = Math.round(instabilityValue * (getInstabilityLevel() /10f));

        int currentLevel = getCorruptionLevel();

        instability += instabilityValue;
        corruption += corruptionValue;
        mutData = serialize();

        if (currentLevel != getCorruptionLevel()) {
            parentEntity.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 1f, 1f);
        }

        MutateHelper.pushMutDataToClient(parentEntity.getUniqueID(), getMutData());
    }

    protected EffectMutation getRandomEffectMut () {
        if (effectMutations.size() >= EffectMutations.getMapSize()) return null;
        List<EffectMutation> legalList = new ArrayList<>();

        for (EffectMutation e : EffectMutations.getMap().values()) {
            if (!containsEffect(e) && e.isLegalMutation(this)) legalList.add(e);
        }

        if (legalList.isEmpty()) return null;

        int i = WarpstoneMain.getRandom().nextInt(legalList.size());
        return EffectMutations.constructInstance(legalList.get(i).getMutationID(), parentEntity);
    }

    protected CompoundNBT serialize (){
        CompoundNBT out = new CompoundNBT();
        out.putUniqueId("player", parentEntity.getUniqueID());
        out.putInt("instability", getInstability());
        out.putInt("corruption", getCorruption());

        for (AttributeMutation mut : getAttributeMutations()) {
            out.putInt(mut.getMutationType(), mut.getMutationLevel());
        }

        List<Integer> classList = new ArrayList<>(effectMutations);

        out.putIntArray("effect_mutations", classList);

        return out;
    }

    public void loadFromNBT (CompoundNBT nbt) {
        int currentLevel = getCorruptionLevel();
        instability = nbt.getInt("instability");
        corruption = nbt.getInt("corruption");
        mutData = nbt;

        if (getCorruptionLevel() > currentLevel) {
            parentEntity.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 1f, 1f);
        }

        for (AttributeMutation mut : getAttributeMutations()) {
            mut.setLevel(nbt.getInt(mut.getMutationType()));
        }

        int[] array = nbt.getIntArray("effect_mutations");
        List<Integer> deletion = new ArrayList<>(effectMutations);

        for (int i : array) {
            if (containsEffect(i)) { deletion.remove((Integer) i); continue; }
            effectMutations.add(i);

            EffectMutation mut = EffectMutations.constructInstance(i, parentEntity);

            mut.applyMutation(parentEntity);
        }

        for (int i : deletion) {
            effectMutations.remove((Integer) i);
        }
    }

    public void resetMutations (boolean death) {
        for (AttributeMutation m : attributeMutations) { m.setLevel(0); }

        for (int i : effectMutations) { getEffect(i).clearInstance(this.parentEntity); }
        effectMutations.clear();

        if (!death) corruption = 0;
        instability = 0;
        mutData = serialize();

        MutateHelper.pushMutDataToClient(parentEntity.getUniqueID(), getMutData());
    }

    public List<AttributeMutation> getAttributeMutations (){
        return attributeMutations;
    }

    public List<Integer> getEffectMutations (){
            return new ArrayList<>(effectMutations);
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

    public int getCorruption () {
        return corruption;
    }

    public int getCorruptionLevel (){
        int threshold = 0;

        for (int i = 0; i < 10; i++) {
            threshold += i * (125 * (i + 1)) + 750;

            if (threshold > corruption) {
                //if (i < 1) return 0;
                return i;
            }
        }

        return 0;
    }

    public int getCorruptionToNextLevel () {
        int target = getCorruptionLevel() + 1;
        int threshold = 0;

        for (int i = 0; i < target; i++) {
            threshold += i * (125 * (i + 1)) + 750;

            if (threshold > corruption) {
                return threshold;
            }
        }

        return threshold - getCorruption();
    }

    public double getWitherRisk (int corruptionValue) {
        double value =  (((double) corruptionValue / 100f) * (((double) getInstabilityLevel() / 10 - 0.3) - (double) getCorruptionLevel() / 10));

        if (value < 0) return 0;
        return value;
    }

    public CompoundNBT getMutData () {
        if (mutData == null) System.out.println("Getting the NBT from Manager returns null");
        return mutData;
    }

    public void unload() {
        saveData();

        for (int i : effectMutations) {
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
        return EffectMutations.getEffectMutation(id);
    }

    public boolean containsEffect (EffectMutation mut) {
        return containsEffect(mut.getMutationID());
    }

    public boolean containsEffect (int id) {
        for (int i : effectMutations) {
            if (i == id) return true;
        }
        return false;
    }
}