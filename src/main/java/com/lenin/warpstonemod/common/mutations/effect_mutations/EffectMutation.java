package com.lenin.warpstonemod.common.mutations.effect_mutations;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.mutations.MutateManager;
import com.lenin.warpstonemod.common.mutations.tags.MutationTag;
import com.lenin.warpstonemod.common.mutations.tags.MutationTags;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.*;

public abstract class EffectMutation extends ForgeRegistryEntry<EffectMutation> {
	protected final String mutName;
	protected UUID uuid;
	protected final String translateKeyConstant = "mutation.effect.";

	public List<MutationTag> tags;

	protected ResourceLocation textureResource;

	/**
	 * rarity by default determines required Corruption level required <br>
	 * COMMON = 1 <br>
	 * UNCOMMON = 2 <br>
	 * RARE = 3 <br>
	 * EPIC = 4 <br>
	**/

	protected Map<UUID, EffectMutationInstance> instanceMap = new Object2ObjectArrayMap<>();

	public EffectMutation(String _mutName, String _uuid, MutationTag... _tags) {
		uuid = UUID.fromString(_uuid);
		mutName = _mutName;
		tags = Arrays.asList(_tags);

		textureResource = new ResourceLocation(WarpstoneMain.MOD_ID, "textures/gui/effect_mutations/" + _mutName + ".png");

		setRegistryName(WarpstoneMain.MOD_ID, mutName);

		attachListeners(MinecraftForge.EVENT_BUS);
		attachClientListeners(MinecraftForge.EVENT_BUS);
	}

	public abstract void attachListeners(IEventBus bus);

	@OnlyIn(Dist.CLIENT)
	public abstract void attachClientListeners(IEventBus bus);

	public void applyMutation (LivingEntity entity){
		if (!containsInstance(entity.getUniqueID())) constructInstance(entity);

		EffectMutationInstance mut = instanceMap.get(entity.getUniqueID());
		mut.setActive(true);
	}

	//This cannot clear instances as methods are overridden to deactivate mutations
	public void deactivateMutation(LivingEntity entity) {
		if (!containsInstance(entity.getUniqueID())) return;

		instanceMap.get(entity.getUniqueID()).setActive(false);
	}

	//Different from Deactivate Mutations as will deactivate then clear the instance
	public void clearInstance (LivingEntity entity) {
		if (entity.world.isRemote()) {
			clearClientInstance();
			return;
		}

		deactivateMutation(entity);
		instanceMap.remove(entity.getUniqueID());
	}

	public IFormattableTextComponent getMutationName() {
		TranslationTextComponent text = new TranslationTextComponent(translateKeyConstant + mutName);

		for (MutationTag tag : tags) {
			if (tag.formatting != null) {
				tag.formatting.forEach(text::mergeStyle);
			}
		}

		return text;
	}

	public IFormattableTextComponent getMutationDesc() {
		return new TranslationTextComponent(translateKeyConstant + mutName + ".desc").mergeStyle(TextFormatting.WHITE);
	}

	public List<ITextComponent> getToolTips () {
		List<ITextComponent> list = new ArrayList<>();
		list.add(getMutationName());
		list.add(getMutationDesc());
		return list;
	}

	public boolean isLegalMutation(MutateManager manager){
		MutationTag tag = getTagOfType(MutationTag.Type.RARITY);

		if (tag != null && tag.getType() != null) {
			switch (tag.getResource().getPath()) {
				case "UNCOMMON":
					return manager.getCorruptionLevel() >= 2;
				case "RARE":
					return manager.getCorruptionLevel() >= 3;
				case "EPIC":
					return manager.getCorruptionLevel() >= 4;
				default:
					return manager.getCorruptionLevel() >= 1;
			}
		}

		return manager.getCorruptionLevel() >= 1;
	}

	public JsonObject serialize () {
		JsonObject json = new JsonObject();

		json.addProperty("name", mutName);
		json.addProperty("uuid", String.valueOf(uuid));
		json.addProperty("resource_path", getTexture().getPath());

		JsonArray jsonTags = new JsonArray();

		for (MutationTag tag : tags) {
			jsonTags.add(tag.getResource().getPath());
		}

		json.add("tags", jsonTags);

		return json;
	}

	public void deserialize (JsonObject json) {
		if (json == null) {
			System.out.println(getKey() + " JSON is null");
			return;
		}

		uuid = UUID.fromString(json.get("uuid").getAsString());
		textureResource = new ResourceLocation(WarpstoneMain.MOD_ID, json.get("resource_path").getAsString());

		List<String> newTags = new ArrayList<>();
		json.getAsJsonArray("tags").forEach(jsonElement -> newTags.add(jsonElement.getAsString()));

		for (String key : newTags) {
			MutationTag tag = MutationTags.getTag(key);
			if (tag != null && !tags.contains(tag)) tags.add(tag);
		}
	}

	public EffectMutationInstance getInstance (LivingEntity entity) {
		return getInstance(entity.getUniqueID());
	}

	public EffectMutationInstance getInstance (UUID playerUUID) {
		return instanceMap.getOrDefault(playerUUID, null);
	}

	public boolean containsInstance (LivingEntity entity) {
		return containsInstance(entity.getUniqueID());
	}

	public boolean containsInstance (UUID playerUUID) {
		if (playerUUID == null) return false;
		return instanceMap.containsKey(playerUUID);
	}

	public void constructInstance(LivingEntity entity) {
		EffectMutationInstance instance = entity.world.isRemote() ? putClientInstance() : getInstanceType(entity);

		if (instance != null) instanceMap.put(entity.getUniqueID(), instance);
	}

	public MutationTag getTagOfType (MutationTag.Type type) {
		for (MutationTag tag : tags) {
			if (tag.getType() == type) return tag;
		}

		return null;
	}

	@OnlyIn(Dist.CLIENT)
	public EffectMutationInstance putClientInstance() {
		return null;
	}

	@OnlyIn(Dist.CLIENT)
	public void clearClientInstance () {
		instanceMap.clear();
	}

	public ResourceLocation getTexture () {
		return textureResource;
	}

	public String getKey () {
		return mutName;
	}

	public EffectMutationInstance getInstanceType (LivingEntity entity) {
		return new EffectMutationInstance(entity);
	}
}