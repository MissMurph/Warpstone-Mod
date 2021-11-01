package com.lenin.warpstonemod.common.mutations.effect_mutations;

import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.mutations.MutateManager;
import com.lenin.warpstonemod.common.mutations.WarpMutations;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.Map;
import java.util.UUID;

public abstract class EffectMutation {
	protected final String mutName;
	protected final int id;
	protected ResourceLocation resourceLocation;
	protected final UUID uuid;
	protected String mutDescription;

	/**
	 * {@link #rarity} by default determines required Corruption level required <br>
	 * COMMON = 1 <br>
	 * UNCOMMON = 2 <br>
	 * RARE = 3 <br>
	 * EPIC = 4 <br>
	**/
	protected Rarity rarity;

	protected Map<UUID, EffectMutationInstance> instanceMap = new Object2ObjectArrayMap<>();

	public EffectMutation(int _id, String _mutName, String _uuid, Rarity _rarity) {
		uuid = UUID.fromString(_uuid);
		mutName = WarpMutations.nameConst + "effect." +_mutName;
		id = _id;
		rarity = _rarity;

		resourceLocation = new ResourceLocation(WarpstoneMain.MOD_ID, "textures/gui/effect_mutations/" + _mutName + ".png");

		attachListeners(MinecraftForge.EVENT_BUS);

		mutDescription = mutName + ".desc";
	}

	public abstract void attachListeners(IEventBus bus);

	@OnlyIn(Dist.CLIENT)
	public abstract void attachClientListeners(IEventBus bus);

	public void applyMutation (LivingEntity entity){
		if (entity.world.isRemote) return;

		EffectMutationInstance mut = instanceMap.get(entity.getUniqueID());
		mut.setActive(true);
	}

	//This cannot clear instances as methods are overridden to deactivate mutations
	public void deactivateMutation(LivingEntity entity) {
		if (entity.world.isRemote()) return;

		instanceMap.get(entity.getUniqueID()).setActive(false);
	}

	//Different from Deactivate Mutations as will deactivate then clear the instance
	public void clearInstance (LivingEntity entity) {
		if (entity.world.isRemote()) return;

		deactivateMutation(entity);
		instanceMap.remove(entity.getUniqueID());
	}

	public IFormattableTextComponent getMutationName() {
		return new TranslationTextComponent(mutName).mergeStyle(rarity.color);
	}

	public IFormattableTextComponent getMutationDesc() {
		return new TranslationTextComponent(mutDescription).mergeStyle(TextFormatting.WHITE);
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

	public EffectMutationInstance putInstance (LivingEntity entity) {
		EffectMutationInstance instance = getInstanceType(entity);

		instanceMap.put(entity.getUniqueID(), instance);
		return instance;
	}

	public void putClientInstance (LivingEntity entity){}

	public ResourceLocation getTexture () {
		return resourceLocation;
	}

	public int getMutationID() {
		return id;
	}

	public EffectMutationInstance getInstanceType (LivingEntity entity) {
		return new EffectMutationInstance(entity);
	}
}