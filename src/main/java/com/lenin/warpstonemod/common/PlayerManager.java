package com.lenin.warpstonemod.common;

import com.ibm.icu.impl.Pair;
import com.lenin.warpstonemod.common.events.MutateWeightCollectEvent;
import com.lenin.warpstonemod.common.items.MutateItem;
import com.lenin.warpstonemod.common.items.MutateItem.*;
import com.lenin.warpstonemod.common.mutations.MutateHelper;
import com.lenin.warpstonemod.common.mutations.Mutation;
import com.lenin.warpstonemod.common.mutations.Mutations;
import com.lenin.warpstonemod.common.mutations.attribute_mutations.*;
import com.lenin.warpstonemod.common.mutations.attribute_mutations.WSAttributes;
import com.lenin.warpstonemod.common.mutations.tags.MutationTag;
import com.lenin.warpstonemod.common.weighted_random.WeightEntry;
import com.lenin.warpstonemod.common.weighted_random.WeightModifier;
import com.lenin.warpstonemod.common.weighted_random.WeightedRange;
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

    public void mutate(MutateItem item){
        MutateProperties properties = item.getMutateProperties();

        if (mutations.size() < 14 && Warpstone.getRandom().nextInt(100) > 90) {
            List<Mutation> legalMuts = Registration.MUTATIONS.getValues()
                    .stream()
                    .filter(mut -> mut.isLegalMutation(this))
                    .collect(Collectors.toList());

            if (legalMuts.size() > 0) {

                MutateWeightCollectEvent event = new MutateWeightCollectEvent((PlayerEntity) getParentEntity());
                MinecraftForge.EVENT_BUS.post(event);

                Map<ResourceLocation, List<WeightModifier>> modMap = event.getModifiers();
                WeightedRange<Mutation> range = new WeightedRange<>(legalMuts.toArray(new Mutation[0]));

                for (WeightEntry<Mutation> weight : range.getEntries()) {
                    Mutation mut = weight.get();

                    if (modMap.containsKey(mut.getRegistryName())) {
                        modMap.get(mut.getRegistryName()).forEach(weight::applyModifier);
                    }

                    for (MutationTag tag : mut.getTags()) {
                        if (modMap.containsKey(tag.getKey())) {
                            modMap.get(tag.getKey()).forEach(weight::applyModifier);
                        }
                    }
                }

                addMutation(range.getResult().get());
            }

            //Can only mutate either a mutation OR attr changes
            return;
        }

        //Loop over every point of instability and apply levels, no negatives if no instablity
        //This is low key kind of fucked lol
        for (int i = 0; i < getInstabilityLevel() + 1; i++) {
            MutateWeightCollectEvent event = new MutateWeightCollectEvent((PlayerEntity) getParentEntity());
            MinecraftForge.EVENT_BUS.post(event);

            Map<ResourceLocation, List<WeightModifier>> modMap = event.getModifiers();
            WeightedRange<AttributeMutation> range = new WeightedRange<>();

            Map<AttributeMutation, Pair<UUID, UUID>> uuidMap = new HashMap<>();
            Map<UUID, WeightEntry<AttributeMutation>> posNegMap = new HashMap<>();

            for (AttributeMutation attr : attributeMutations) {
                uuidMap.computeIfAbsent(attr, key -> Pair.of(UUID.randomUUID(), UUID.randomUUID()));

                WeightEntry<AttributeMutation> posWeight = attr.getWeight(true);
                WeightEntry<AttributeMutation> negWeight = attr.getWeight(false);

                if (posWeight != null) {
                    posWeight.applyModifier(new WeightModifier((Math.max(0, Math.min(10, 20 - (getInstabilityLevel() - getCorruptionLevel())))) / 10f, WeightModifier.Operation.MULTIPLY_TOTAL));
                    range.entry(posWeight);
                }

                if (negWeight != null) {
                    negWeight.applyModifier(new WeightModifier((-10 + (Math.max(0, getInstabilityLevel() - getCorruptionLevel()))) / 10f, WeightModifier.Operation.MULTIPLY_TOTAL));
                    range.entry(negWeight);
                }

                /*if (attr.canMutate(true)) {
                    //After Instability 10 we want positive weights to gradually degrade
                    WeightEntry<AttributeMutation> weight = posNegMap.put(uuidMap.get(attr).first, range.entry(attr, attr.getWeight()));
                    weight.applyModifier(new WeightModifier((Math.max(0, Math.min(10, 20 - (getInstabilityLevel() - getCorruptionLevel())))) / 10f, WeightModifier.Operation.MULTIPLY_TOTAL));
                }
                if (attr.canMutate(false)) {
                    WeightEntry<AttributeMutation> weight = posNegMap.put(uuidMap.get(attr).first, range.entry(attr, attr.getWeight()));
                    weight.applyModifier(new WeightModifier((-10 + (Math.max(0, getInstabilityLevel() - getCorruptionLevel()))) / 10f, WeightModifier.Operation.MULTIPLY_TOTAL));
                }*/
            }

            for (WeightEntry<AttributeMutation> weight : range.getEntries()) {
                AttributeMutation mut = weight.get();
                if (modMap.containsKey(mut.getTag().getKey())) {
                    modMap.get(mut.getTag().getKey()).forEach(weight::applyModifier);
                }
            }

            WeightEntry<AttributeMutation> result = range.getResult();
            result.get().mutate(result);
        }

        double witherRisk = getWitherRisk(properties.corruption);
        if (Math.random() > 1f - witherRisk) {
            int duration = Warpstone.getRandom().nextInt((int) Math.round(2400 * witherRisk));
            parentEntity.addEffect(new EffectInstance(Effects.WITHER, duration));
        }

        int instabilityValue = properties.instability + (int) Math.round(properties.instability * (
                (double)getInstability() / 100) * (double)(Warpstone.getRandom().nextInt((getCorruptionLevel() + 2) * 10) / 100)
        );
        int corruptionValue = Math.round(instabilityValue * (getInstabilityLevel() /10f));

        int currentLevel = getCorruptionLevel();

        instability += instabilityValue;
        corruption += corruptionValue;
        mutData = serialize();
        MutateHelper.pushPlayerDataToClient(getUniqueId(), getMutData());

        if (currentLevel != getCorruptionLevel()) {
            parentEntity.playSound(SoundEvents.PLAYER_LEVELUP, 1f, 1f);
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
        out.putUUID("player", parentEntity.getUUID());
        out.putInt("instability", getInstability());
        out.putInt("corruption", getCorruption());

        for (AttributeMutation mut : getAttributeMutations()) {
            out.putInt(mut.getName(), mut.getMutationLevel());
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
            parentEntity.playSound(SoundEvents.PLAYER_LEVELUP, 1f, 1f);
        }

        for (AttributeMutation mut : getAttributeMutations()) {
            mut.setLevel(nbt.get(mut.getName()));
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
        for (AttributeMutation m : attributeMutations) { m.reset(); }

        for (Mutation mut : mutations.values()) {
            removeMutation(mut);
        }
        mutations.clear();

        if (!death) corruption = 0;
        instability = 0;
        mutData = serialize();

        MutateHelper.pushPlayerDataToClient(parentEntity.getUUID(), getMutData());
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

        toolTips.add(new TranslationTextComponent("mutation.screen.corruption").withStyle(TextFormatting.WHITE));
        toolTips.add(new TranslationTextComponent("warpstone.screen.generic.level")
                .append(new StringTextComponent(" "))
                .append(new StringTextComponent(String.valueOf(getCorruptionLevel())))
                .withStyle(TextFormatting.WHITE));
        toolTips.add(new TranslationTextComponent("warpstone.screen.generic.total")
                .append(new StringTextComponent(" "))
                .append(new StringTextComponent(String.valueOf(getCorruption())))
                .withStyle(TextFormatting.WHITE));

        toolTips.add(new TranslationTextComponent("warpstone.screen.generic.next_level")
                .withStyle(TextFormatting.GRAY)
                .withStyle(TextFormatting.ITALIC)
                .append(new StringTextComponent(" "))
                .append(new StringTextComponent(String.valueOf(getCorruptionToNextLevel())).withStyle(TextFormatting.WHITE))
        );

        if (witherEffect > 0) {
            toolTips.add(new TranslationTextComponent("warpstone.consumable.wither_risk")
                    .append(new StringTextComponent(" "))
                    .append(new StringTextComponent("-" + witherEffect + "%")
                            .withStyle(TextFormatting.GREEN))
            );
        }

        return toolTips;
    }

    public List<ITextComponent> getInstabilityTooltips () {
        List<ITextComponent> toolTips = new ArrayList<>();
        TextFormatting color = getInstabilityLevel() > 5 ? TextFormatting.RED : TextFormatting.WHITE;

        int witherEffect = getInstabilityLevel() * 10 - 30;

        toolTips.add(new TranslationTextComponent("mutation.screen.instability").withStyle(TextFormatting.WHITE));
        toolTips.add(new TranslationTextComponent("warpstone.screen.generic.level")
                .append(new StringTextComponent(" "))
                .append(new StringTextComponent(String.valueOf(getInstabilityLevel()))
                        .withStyle(color)));
        toolTips.add(new TranslationTextComponent("warpstone.screen.generic.total")
                .append(new StringTextComponent(" "))
                .append(new StringTextComponent(String.valueOf(getInstability())))
                .withStyle(TextFormatting.WHITE));
        if (witherEffect > 0) {
            toolTips.add(new TranslationTextComponent("warpstone.consumable.wither_risk")
                    .append(new StringTextComponent(" "))
                    .append(new StringTextComponent("+" + witherEffect + "%").withStyle(TextFormatting.RED))
            );
        }

        return toolTips;
    }

    public UUID getUniqueId () {
        return getParentEntity().getUUID();
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
    }

    public void saveData (){
        mutData = serialize();
        MutateHelper.savePlayerData(parentEntity.getUUID(), getMutData());
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