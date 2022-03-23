package com.lenin.warpstonemod.common.mutations;

import com.lenin.warpstonemod.common.Registration;
import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.items.IWarpstoneConsumable;
import com.lenin.warpstonemod.common.mutations.attribute_mutations.*;
import com.lenin.warpstonemod.common.mutations.attribute_mutations.attributes.AttributeMutationUUIDs;
import com.lenin.warpstonemod.common.mutations.attribute_mutations.WSAttributes;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayerManager {
    protected final LivingEntity parentEntity;
    protected final List<AttributeMutation> attributeMutations = new ArrayList<>();
    protected List<String> effectMutations = new ArrayList<>();

    protected List<IAttributeSource> attributes = new ArrayList<>();

    protected CompoundNBT mutData;

    protected int instability = 0;
    protected int corruption = 0;

    public PlayerManager(LivingEntity _parentEntity){
        parentEntity = _parentEntity;

        attributes.add(new VanillaAttribute(Attributes.MAX_HEALTH, getParentEntity()));

        attributeMutations.add(new AttributeMutation(getAttribute(new ResourceLocation("minecraft", "generic.max_health")), this, AttributeMutationUUIDs.MAX_HEALTH_UUID));
        attributeMutations.add(new AttributeMutation(getAttribute(new ResourceLocation("minecraft", "generic.attack_damage")), this, AttributeMutationUUIDs.ATTACK_DAMAGE_UUID));
        attributeMutations.add(new AttributeMutation(getAttribute(new ResourceLocation("minecraft", "generic.movement_speed")), this, AttributeMutationUUIDs.SPEED_UUID));
        attributeMutations.add(new AttributeMutation(getAttribute(new ResourceLocation("minecraft", "generic.armor")), this, AttributeMutationUUIDs.AMOUR_UUID));
        attributeMutations.add(new AttributeMutation(getAttribute(new ResourceLocation("minecraft", "generic.armor_toughness")), this, AttributeMutationUUIDs.ARMOUR_TOUGHNESS_UUID));
        attributeMutations.add(new AttributeMutation(getAttribute(new ResourceLocation(WarpstoneMain.MOD_ID, "harvest_speed")), this, AttributeMutationUUIDs.MINING_SPEED_UUID));

        mutData = serialize();
    }

    protected PlayerManager () {
        parentEntity = null;
    }

    public void mutate(IWarpstoneConsumable item){
        boolean hasEffectBeenCreated = false;

        //Loop over every point of instablity and apply levels, no negatives if no instablity
        for (int i = 0; i < getInstabilityLevel() + 1; i++) {
                /*  Effect Mutation Creation    */
            if (effectMutations.size() < 14 && !hasEffectBeenCreated && WarpstoneMain.getRandom().nextInt(100) > 90) {
                List<EffectMutation> legalMutations = Registration.EFFECT_MUTATIONS.getValues()
                        .stream()
                        .filter(mut -> !this.containsEffect(mut))
                        .filter(mut -> mut.isLegalMutation(this))
                        .collect(Collectors.toList());

                if (legalMutations.size() > 0) {
                    EffectMutation mut = legalMutations.get(WarpstoneMain.getRandom().nextInt(legalMutations.size()));

                    effectMutations.add(mut.getKey());
                    mut.applyMutation(this);

                    hasEffectBeenCreated = true;
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

    protected CompoundNBT serialize (){
        CompoundNBT out = new CompoundNBT();
        out.putUniqueId("player", parentEntity.getUniqueID());
        out.putInt("instability", getInstability());
        out.putInt("corruption", getCorruption());

        for (AttributeMutation mut : getAttributeMutations()) {
            out.putInt(mut.getMutationType(), mut.getMutationLevel());
        }

        ListNBT tagList = new ListNBT();

        for (int i = 0; i < effectMutations.size(); i++) {
            CompoundNBT mut = new CompoundNBT();
            mut.putString("effect_mutations" + i, EffectMutations.getMutation(effectMutations.get(i)).getKey());
            tagList.add(mut);
        }

        out.put("effect_mutations", tagList);

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

        ListNBT list = (ListNBT) nbt.get("effect_mutations");

        List<String> deletion = new ArrayList<>(effectMutations);

        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                CompoundNBT tag = list.getCompound(i);
                String mutKey = tag.getString("effect_mutations" + i);

                if (containsEffect(mutKey)) { deletion.remove(mutKey); continue; }
                effectMutations.add(mutKey);

                EffectMutation mut = getEffect(mutKey);
                mut.applyMutation(this);
            }
        }

        for (String mut : deletion) {
            getEffect(mut).clearInstance(this);
            effectMutations.remove(mut);
        }
    }

    public void resetMutations (boolean death) {
        for (AttributeMutation m : attributeMutations) { m.setLevel(0); }

        for (String key : effectMutations) { getEffect(key).clearInstance(this); }
        effectMutations.clear();

        if (!death) corruption = 0;
        instability = 0;
        mutData = serialize();

        MutateHelper.pushMutDataToClient(parentEntity.getUniqueID(), getMutData());
    }

    public List<AttributeMutation> getAttributeMutations (){
        return attributeMutations;
    }

    public List<String> getEffectMutations (){
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

    @OnlyIn(Dist.CLIENT)
    public List<ITextComponent> getCorruptionTooltips () {
        List<ITextComponent> toolTips = new ArrayList<>();
        int witherEffect = getCorruptionLevel() * 10;

        toolTips.add(new TranslationTextComponent("mutation.screen.corruption").mergeStyle(TextFormatting.WHITE));
        toolTips.add(new TranslationTextComponent("warpstone.screen.generic.level")
                .appendSibling(new StringTextComponent(" "))
                .appendSibling(new StringTextComponent(String.valueOf(getCorruptionLevel())))
                .mergeStyle(TextFormatting.WHITE));
        toolTips.add(new TranslationTextComponent("warpstone.screen.generic.total")
                .appendSibling(new StringTextComponent(" "))
                .appendSibling(new StringTextComponent(String.valueOf(getCorruption())))
                .mergeStyle(TextFormatting.WHITE));

        toolTips.add(new TranslationTextComponent("warpstone.screen.generic.next_level")
                .mergeStyle(TextFormatting.GRAY)
                .mergeStyle(TextFormatting.ITALIC)
                .appendSibling(new StringTextComponent(" "))
                .appendSibling(new StringTextComponent(String.valueOf(getCorruptionToNextLevel())).mergeStyle(TextFormatting.WHITE))
        );

        if (witherEffect > 0) {
            toolTips.add(new TranslationTextComponent("warpstone.consumable.wither_risk")
                    .appendSibling(new StringTextComponent(" "))
                    .appendSibling(new StringTextComponent("-" + witherEffect + "%").mergeStyle(TextFormatting.GREEN))
            );
        }

        return toolTips;
    }

    public List<ITextComponent> getInstabilityTooltips () {
        List<ITextComponent> toolTips = new ArrayList<>();
        TextFormatting color = getInstabilityLevel() > 5 ? TextFormatting.RED : TextFormatting.WHITE;

        int witherEffect = getInstabilityLevel() * 10 - 30;

        toolTips.add(new TranslationTextComponent("mutation.screen.instability").mergeStyle(TextFormatting.WHITE));
        toolTips.add(new TranslationTextComponent("warpstone.screen.generic.level")
                .appendSibling(new StringTextComponent(" "))
                .appendSibling(new StringTextComponent(String.valueOf(getInstabilityLevel()))
                        .mergeStyle(color)));
        toolTips.add(new TranslationTextComponent("warpstone.screen.generic.total")
                .appendSibling(new StringTextComponent(" "))
                .appendSibling(new StringTextComponent(String.valueOf(getInstability())))
                .mergeStyle(TextFormatting.WHITE));
        if (witherEffect > 0) {
            toolTips.add(new TranslationTextComponent("warpstone.consumable.wither_risk")
                    .appendSibling(new StringTextComponent(" "))
                    .appendSibling(new StringTextComponent("+" + witherEffect + "%").mergeStyle(TextFormatting.RED))
            );
        }

        return toolTips;
    }

    public UUID getUniqueId () {
        return getParentEntity().getUniqueID();
    }

    public IAttributeSource getAttribute (ResourceLocation key) {
        for (IAttributeSource attribute : attributes) {
            if (attribute.getAttributeName().equals(key)) return attribute;
        }

        IAttributeSource newAttribute;

        if (ForgeRegistries.ATTRIBUTES.containsKey(key)) newAttribute = new VanillaAttribute(ForgeRegistries.ATTRIBUTES.getValue(key), getParentEntity());
        //else if (Registry.ATTRIBUTE.containsKey(key)) newAttribute = new VanillaAttribute(Registry.ATTRIBUTE.getOrDefault(key), getParentEntity());
        else newAttribute = WSAttributes.createAttribute(key, getParentEntity());

        attributes.add(newAttribute);
        return newAttribute;
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

        for (String key : effectMutations) {
            getEffect(key).clearInstance(this);
        }

        effectMutations.clear();
        attributeMutations.clear();
        MutateHelper.managers.remove(this);
    }

    public void saveData (){
        mutData = serialize();
        MutateHelper.savePlayerData(parentEntity.getUniqueID(), getMutData());
    }

    private EffectMutation getEffect (String key) {
        return EffectMutations.getMutation(key);
    }

    public boolean containsEffect (EffectMutation mut) {
        return containsEffect(mut.getKey());
    }

    public boolean containsEffect (String key) {
        if (effectMutations.contains(key)) return true;
        return false;
    }
}