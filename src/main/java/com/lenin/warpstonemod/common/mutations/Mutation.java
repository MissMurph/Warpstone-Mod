package com.lenin.warpstonemod.common.mutations;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lenin.warpstonemod.common.PlayerManager;
import com.lenin.warpstonemod.common.Registration;
import com.lenin.warpstonemod.common.Warpstone;
import com.lenin.warpstonemod.common.data.mutations.MutationData;
import com.lenin.warpstonemod.common.mutations.conditions.IMutationCondition;
import com.lenin.warpstonemod.common.mutations.conditions.MutationConditions;
import com.lenin.warpstonemod.common.mutations.conditions.nbt.NbtMatchesStringCondition;
import com.lenin.warpstonemod.common.mutations.conditions.nbt.NbtNumberCondition;
import com.lenin.warpstonemod.common.mutations.tags.MutationTag;
import com.lenin.warpstonemod.common.mutations.tags.MutationTags;
import com.lenin.warpstonemod.common.weighted_random.IWeightable;
import com.lenin.warpstonemod.common.weighted_random.WeightEntry;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.*;

public abstract class Mutation extends ForgeRegistryEntry<Mutation> implements IWeightable<Mutation> {
    protected String translateKeyConstant = "mutation.";
    protected Map<UUID, MutationInstance> instanceMap = new Object2ObjectArrayMap<>();

    protected final String name;
    protected final UUID uuid;
    protected ResourceLocation textureResource;

    protected List<MutationTag> tags = new ArrayList<>();
    protected Map<ResourceLocation, IMutationCondition> conditions = new HashMap<>();

    protected int weight = 100;

    protected Mutation (ResourceLocation _key) {
        name = _key.getPath();
        uuid = UUID.randomUUID();

        textureResource = new ResourceLocation(_key.getNamespace(), "textures/gui/effect_mutations/" + _key.getPath() + ".png");

        setRegistryName(_key);

        attachListeners(MinecraftForge.EVENT_BUS);
        attachClientListeners(MinecraftForge.EVENT_BUS);
    }

    public void attachListeners (IEventBus bus) {}

    public void attachClientListeners (IEventBus bus) {}

    public void applyMutation (PlayerManager manager){
        if (!containsInstance(manager.getUniqueId())) constructInstance(manager);
    }

    public void applyMutation (MutationInstance instance) {
        if (!containsInstance(instance.getParent().getUniqueId())) {
            if (instance.getParent().getParentEntity().level.isClientSide()) {
                MutationInstance clientInstance = putClientInstance();
                if (clientInstance != null) instanceMap.put(clientInstance.getParent().getUniqueId(), clientInstance);
            }

            instanceMap.put(instance.getParent().getUniqueId(), instance);
            applyMutation(instance.getParent());
        }
    }


    //Different from Deactivate Mutations as will deactivate then clear the instance
    public void clearMutation(PlayerManager manager) {
        if (containsInstance(manager.getUniqueId())) {
            if (manager.getParentEntity().level.isClientSide()) {
                clearClientInstance();
                return;
            }

            instanceMap.remove(manager.getUniqueId());
        }
    }

    public IFormattableTextComponent getMutationName() {
        TranslationTextComponent text = new TranslationTextComponent(translateKeyConstant + name);

        for (MutationTag tag : tags) {
            if (tag.getFormatting() != null) {
                tag.getFormatting().forEach(text::withStyle);
            }
        }

        return text;
    }

    public List<ITextComponent> getToolTips () {
        List<ITextComponent> list = new ArrayList<>();
        list.add(getMutationName());
        list.add(new TranslationTextComponent(translateKeyConstant + name + ".desc").withStyle(TextFormatting.WHITE));
        return list;
    }

    public List<ITextComponent> getConditionTooltips () {
        List<ITextComponent> out = new ArrayList<>();

        for (IMutationCondition condition : conditions.values()) {
            out.add(condition.getTooltip());
        }

        return out;
    }

    /**
     * rarity by default determines required Corruption level required using tag weight <br>
     * COMMON = 1 <br>
     * UNCOMMON = 2 <br>
     * RARE = 3 <br>
     * EPIC = 4 <br>
     **/

    public boolean isLegalMutation(PlayerManager manager){
        if (manager.containsMutation(this)) return false;

        for (IMutationCondition condition : conditions.values()) {
            if (!condition.act(manager)) return false;
        }

        MutationTag tag = getTagOfType(MutationTag.Type.RARITY);

        if (tag != null && tag.getType() != null) {
            return manager.getCorruptionLevel() >= tag.getWeight();
        }

        return true;
    }

    public JsonObject serializeArguments () {
        return new JsonObject();
    }


    public void deserializeArguments (JsonObject object) {
        //do nothing
    }

    public void deserialize (JsonObject json) {
        if (json == null) {
            System.out.println(getRegistryName() + " JSON is null");
            return;
        }

        textureResource = new ResourceLocation(Warpstone.MOD_ID, json.get("resource_path").getAsString());

        weight = json.get("weight").getAsInt();

        List<String> newTags = new ArrayList<>();
        json.getAsJsonArray("tags").forEach(jsonElement -> newTags.add(jsonElement.getAsString()));

        for (String key : newTags) {
            MutationTag tag = MutationTags.getTag(new ResourceLocation(key));
            if (tag != null && !tags.contains(tag)) tags.add(tag);
        }

        json.getAsJsonArray("conditions").forEach(jsonElement -> {
            JsonObject object = jsonElement.getAsJsonObject();
            IMutationCondition condition = MutationConditions.getCondition(new ResourceLocation(object.get("key").getAsString())).deserialize(object);
            conditions.put(condition.getKey(), condition);
        });

        deserializeArguments(json);
    }

    public CompoundNBT saveData (PlayerManager manager) {
        return null;
    }

    public void loadData (PlayerManager manager, CompoundNBT nbt) {
        if (!instanceMap.containsKey(manager.getUniqueId())) {
            applyMutation(manager);
        }
    }

    public MutationInstance getInstance (LivingEntity entity) {
        return getInstance(entity.getUUID());
    }

    public MutationInstance getInstance (PlayerManager manager) {
        //return getInstance(manager.getUniqueId());
        return instanceMap.computeIfAbsent(manager.getUniqueId(), instance -> constructInstance(manager));
    }

    public MutationInstance getInstance (UUID playerUUID) {
        return instanceMap.getOrDefault(playerUUID, null);
    }

    public boolean containsInstance (LivingEntity entity) {
        return containsInstance(entity.getUUID());
    }

    public boolean containsInstance (UUID playerUUID) {
        if (playerUUID == null) return false;
        return instanceMap.containsKey(playerUUID);
    }

    public MutationInstance constructInstance(PlayerManager manager) {
        MutationInstance instance = manager.getParentEntity().level.isClientSide() ? putClientInstance() : getInstanceType(manager);

        if (instance != null) return instanceMap.put(manager.getParentEntity().getUUID(), instance);
        return null;
    }

    public List<MutationTag> getTags () {
        return tags;
    }

    public boolean hasTag (ResourceLocation key) {
        for (MutationTag tag : tags) {
            if (tag.getKey().equals(key)) return true;
        }

        return false;
    }

    public MutationTag getTagOfType (MutationTag.Type type) {
        for (MutationTag tag : tags) {
            if (tag.getType() == type) return tag;
        }

        return null;
    }

    @OnlyIn(Dist.CLIENT)
    public MutationInstance putClientInstance() {
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    public void clearClientInstance () {
        instanceMap.clear();
    }

    public ResourceLocation getTexture () {
        return textureResource;
    }

    public MutationInstance getInstanceType (PlayerManager manager) {
        return new MutationInstance(manager);
    }

    public WeightEntry<Mutation> getWeight () {
        return new WeightEntry<>(this, 100);
    }

    @Override
    public Mutation get() {
        return this;
    }


}