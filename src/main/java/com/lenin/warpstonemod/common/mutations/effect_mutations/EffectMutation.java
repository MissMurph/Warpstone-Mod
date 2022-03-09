package com.lenin.warpstonemod.common.mutations.effect_mutations;

import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.mutations.MutateManager;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Rarity;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class EffectMutation extends ForgeRegistryEntry<EffectMutation> {
	protected final String mutName;
	protected final UUID uuid;
	protected final String translateKeyConstant = "mutation.effect.";

	/**
	 * {@link #rarity} by default determines required Corruption level required <br>
	 * COMMON = 1 <br>
	 * UNCOMMON = 2 <br>
	 * RARE = 3 <br>
	 * EPIC = 4 <br>
	**/

	protected Rarity rarity;

	protected Map<UUID, EffectMutationInstance> instanceMap = new Object2ObjectArrayMap<>();

	public EffectMutation(String _mutName, String _uuid, Rarity _rarity) {
		uuid = UUID.fromString(_uuid);
		mutName = _mutName;
		rarity = _rarity;

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
		return new TranslationTextComponent(translateKeyConstant + mutName).mergeStyle(rarity.color);
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
		switch (rarity){
			case UNCOMMON:
				return manager.getCorruptionLevel() >= 2;
			case RARE:
				return manager.getCorruptionLevel() >= 3;
			case EPIC:
				return manager.getCorruptionLevel() >= 4;
			default:
				return manager.getCorruptionLevel() >= 1;
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

	@OnlyIn(Dist.CLIENT)
	public EffectMutationInstance putClientInstance() {
		return null;
	}

	@OnlyIn(Dist.CLIENT)
	public void clearClientInstance () {
		instanceMap.clear();
	}

	public ResourceLocation getTexture () {
		return new ResourceLocation(WarpstoneMain.MOD_ID, "textures/gui/effect_mutations/" + getKey() + ".png");
	}

	public String getKey () {
		return mutName;
	}

	public EffectMutationInstance getInstanceType (LivingEntity entity) {
		return new EffectMutationInstance(entity);
	}
}