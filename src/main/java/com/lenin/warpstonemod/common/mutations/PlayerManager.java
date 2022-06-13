package com.lenin.warpstonemod.common.mutations;

import com.lenin.warpstonemod.common.Registration;
import com.lenin.warpstonemod.common.Warpstone;
import com.lenin.warpstonemod.common.events.MutateWeightCollectEvent;
import com.lenin.warpstonemod.common.items.warpstone_consumables.IWarpstoneConsumable;
import com.lenin.warpstonemod.common.mutations.attribute_mutations.*;
import com.lenin.warpstonemod.common.mutations.attribute_mutations.WSAttributes;
import com.lenin.warpstonemod.common.mutations.tags.MutationTag;
import com.lenin.warpstonemod.common.mutations.weights.MutateModifier;
import com.lenin.warpstonemod.common.mutations.weights.MutateWeight;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerManager {
    protected final LivingEntity parentEntity;
    protected final List<AttributeMutation> attributeMutations = new ArrayList<>();
    protected Map<ResourceLocation, Mutation> mutations = new HashMap<>();

    protected List<IAttributeSource> attributes = new ArrayList<>();

    protected CompoundNBT mutData;

    protected int instability = 0;
    protected int corruption = 0;

    public PlayerManager(LivingEntity _parentEntity){
        parentEntity = _parentEntity;

        attributeMutations.add(new AttributeMutation(new VanillaAttribute(Attributes.MAX_HEALTH, _parentEntity), this));
        attributeMutations.add(new AttributeMutation(new VanillaAttribute(Attributes.ATTACK_DAMAGE, _parentEntity), this));
        attributeMutations.add(new AttributeMutation(new VanillaAttribute(Attributes.MOVEMENT_SPEED, _parentEntity), this));
        attributeMutations.add(new AttributeMutation(new VanillaAttribute(Attributes.ARMOR, _parentEntity), this));
        attributeMutations.add(new AttributeMutation(new VanillaAttribute(Attributes.ARMOR_TOUGHNESS, _parentEntity), this));
        attributeMutations.add(new AttributeMutation(getAttribute(new ResourceLocation(Warpstone.MOD_ID, "harvest_speed")), this));

        mutData = serialize();
    }

    protected PlayerManager () {
        parentEntity = null;
    }

    public void mutate(IWarpstoneConsumable item){
        boolean hasEffectBeenCreated = false;

        int totalWeight = 0;

        //Loop over every point of instability and apply levels, no negatives if no instablity
        for (int i = 0; i < getInstabilityLevel() + 1; i++) {
                /*  Effect Mutation Creation    */
            if (mutations.size() < 14 && !hasEffectBeenCreated && Warpstone.getRandom().nextInt(100) > 90) {
                List<MutateWeight> legalWeights = Registration.MUTATIONS.getValues()
                        .stream()
                        .filter(mut -> mut.isLegalMutation(this))
                        .map(Mutation::getWeight)
                        .collect(Collectors.toList());

                if (legalWeights.size() > 0) {

                    MutateWeightCollectEvent event = new MutateWeightCollectEvent((PlayerEntity) getParentEntity());

                    MinecraftForge.EVENT_BUS.post(event);

                    Map<ResourceLocation, List<MutateModifier>> modMap = event.getModifiers();

                    for (MutateWeight weight : legalWeights) {
                        if (modMap.containsKey(weight.getParent().getRegistryName())) {
                            modMap.get(weight.getParent().getRegistryName()).forEach(weight::applyModifier);
                        }

                        for (MutationTag tag : weight.getParent().getTags()) {
                            if (modMap.containsKey(tag.getKey())) {
                                modMap.get(tag.getKey()).forEach(weight::applyModifier);
                            }
                        }

                        totalWeight += weight.getCombinedWeight();
                    }

                    int result = Warpstone.getRandom().nextInt(totalWeight);
                    MutateWeight resultWeight = null;

                    for (int l = legalWeights.size() - 1; l > 0; l--) {
                        totalWeight -= legalWeights.get(l).getCombinedWeight();

                        if (result >= totalWeight) {
                            resultWeight = legalWeights.get(l);
                            break;
                        }
                    }

                    addMutation(resultWeight.getParent());

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

            int change = getCorruptionLevel() > 0 ? Warpstone.getRandom().nextInt(getCorruptionLevel()) + 1 : 1;

            if (i > 0) {
                int index = Warpstone.getRandom().nextInt(legal.size());
                legal.get(index).changeLevel(-change);
                legal.remove(index);
            }

            if (i >= 8) change = Warpstone.getRandom().nextInt(100) > 100 - (5 * (getInstabilityLevel() - getCorruptionLevel())) ? change * -1 : change;

            legal.get(Warpstone.getRandom().nextInt(legal.size()))
                    .changeLevel(change);
        }

        double witherRisk = getWitherRisk(item.getCorruption());
        if (Math.random() > 1f - witherRisk) {
            int duration = Warpstone.getRandom().nextInt((int) Math.round(2400 * witherRisk));
            parentEntity.addPotionEffect(new EffectInstance(Effects.WITHER, duration));
        }

        int instabilityValue = item.getCorruption() + (int) Math.round(item.getCorruption() * (
                (double)getInstability() / 100) * (double)(Warpstone.getRandom().nextInt((getCorruptionLevel() + 2) * 10) / 100)
        );
        int corruptionValue = Math.round(instabilityValue * (getInstabilityLevel() /10f));

        int currentLevel = getCorruptionLevel();

        instability += instabilityValue;
        corruption += corruptionValue;
        mutData = serialize();
        MutateHelper.pushPlayerDataToClient(getUniqueId(), getMutData());

        if (currentLevel != getCorruptionLevel()) {
            parentEntity.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 1f, 1f);
        }
    }

    protected void addMutation (Mutation mutation) {
        if (mutations.containsValue(mutation)
                || !Registration.MUTATIONS.containsValue(mutation)) return;

        mutation.applyMutation(this);
        mutations.put(mutation.getRegistryName(),mutation);
    }

    protected void removeMutation (Mutation mutation) {
        if (!mutations.containsValue(mutation)
                || !Registration.MUTATIONS.containsValue(mutation)) return;

        mutation.clearMutation(this);
        mutations.remove(mutation.getRegistryName());
    }

    public void addMutationCommand (Mutation mutation) {
        if (mutations.containsKey(mutation.getRegistryName())
                || !Registration.MUTATIONS.containsKey(mutation.getRegistryName())) return;

        addMutation(mutation);
        mutData = serialize();
        MutateHelper.pushPlayerDataToClient(getUniqueId(), getMutData());
    }

    public void removeMutationCommand (Mutation mutation) {
        if (!mutations.containsKey(mutation.getRegistryName())
                || !Registration.MUTATIONS.containsKey(mutation.getRegistryName())) return;

        removeMutation(mutation);
        mutData = serialize();
        MutateHelper.pushPlayerDataToClient(getUniqueId(), getMutData());
    }

    protected CompoundNBT serialize (){
        CompoundNBT out = new CompoundNBT();
        out.putUniqueId("player", parentEntity.getUniqueID());
        out.putInt("instability", getInstability());
        out.putInt("corruption", getCorruption());

        for (AttributeMutation mut : getAttributeMutations()) {
            out.putInt(mut.getMutationType(), mut.getMutationLevel());
        }

        ListNBT serializedMutations = new ListNBT();

        for (ResourceLocation key : mutations.keySet()) {
            CompoundNBT mut = new CompoundNBT();

            mut.putString("key", key.toString());
            mut.put("mutation_data", mutations.get(key).saveData(this));

            serializedMutations.add(mut);
        }

        out.put("mutations", serializedMutations);

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

        ListNBT list = (ListNBT) nbt.get("mutations");

        List<ResourceLocation> deletion = new ArrayList<>(mutations.keySet());

        if (list != null) {
            for (INBT nbt2 : list) {
                CompoundNBT compound = (CompoundNBT) nbt2;
                ResourceLocation key = new ResourceLocation(compound.getString("key"));

                if (containsMutation(key)) {
                    deletion.remove(key);
                    continue;
                }

                getMutation(key).loadData(this, compound.getCompound("mutation_data"));
                mutations.put(key, getMutation(key));
            }
        }

        for (ResourceLocation mut : deletion) {
            removeMutation(getMutation(mut));
        }
    }

    public void resetMutations (boolean death) {
        for (AttributeMutation m : attributeMutations) { m.setLevel(0); }

        for (Mutation mut : mutations.values()) {
            removeMutation(mut);
        }
        mutations.clear();

        if (!death) corruption = 0;
        instability = 0;
        mutData = serialize();

        MutateHelper.pushPlayerDataToClient(parentEntity.getUniqueID(), getMutData());
    }

    public List<AttributeMutation> getAttributeMutations (){
        return attributeMutations;
    }

    public List<ResourceLocation> getEffectMutations (){
            return new ArrayList<>(mutations.keySet());
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

        for (Mutation mut : mutations.values()) {
            removeMutation(mut);
        }

        mutations.clear();
        attributeMutations.clear();
        MutateHelper.MANAGERS.remove(this);
    }

    public void saveData (){
        mutData = serialize();
        MutateHelper.savePlayerData(parentEntity.getUniqueID(), getMutData());
    }

    private Mutation getMutation(ResourceLocation key) {
        return Mutations.getMutation(key);
    }

    public boolean containsMutation(Mutation mut) {
        return containsMutation(mut.getRegistryName());
    }

    public boolean containsMutation(ResourceLocation key) {
        return mutations.containsKey(key);
    }
}