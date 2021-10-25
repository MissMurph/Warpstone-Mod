package com.lenin.warpstonemod.common.mutations.effect_mutations;

import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.mutations.MutateManager;
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

	protected Rarity rarity;

	protected Map<UUID, EffectMutationInstance> instanceMap = new Object2ObjectArrayMap<>();

	protected EffectMutation(int _id, String _mutName, String resName, String _uuid, Rarity rarity) {
		uuid = UUID.fromString(_uuid);
		mutName = _mutName;
		id = _id;

		resourceLocation = new ResourceLocation(WarpstoneMain.MOD_ID, "textures/gui/effect_mutations/" + resName);

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

		//When overriding do NOT call Super, no need to override unless differing from standard format
	public IFormattableTextComponent getMutationName() {
		return new TranslationTextComponent(mutName).mergeStyle(TextFormatting.WHITE);
	}

		//When overriding do NOT call Super, no need to override unless differing from standard format
	public IFormattableTextComponent getMutationDesc() {
		return new TranslationTextComponent(mutDescription).mergeStyle(TextFormatting.WHITE);
	}

	public abstract boolean isLegalMutation(MutateManager manager);

	public EffectMutationInstance getInstance (LivingEntity entity) {
		return getInstance(entity.getUniqueID());
	}

	public EffectMutationInstance getInstance (UUID playerUUID) {
		return instanceMap.getOrDefault(playerUUID, null);
	}

	public EffectMutationInstance putInstance (LivingEntity entity) {
		EffectMutationInstance instance = new EffectMutationInstance(this, entity);

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
}