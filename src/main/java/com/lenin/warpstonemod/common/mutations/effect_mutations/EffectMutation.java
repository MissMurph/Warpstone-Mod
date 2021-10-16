package com.lenin.warpstonemod.common.mutations.effect_mutations;

import com.lenin.warpstonemod.common.CommonProxy;
import com.lenin.warpstonemod.common.WarpstoneMain;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;

import java.util.Map;
import java.util.UUID;

public abstract class EffectMutation {
	protected final String posName, negName;
	protected final int id;
	protected ResourceLocation resourceLocation;
	protected final UUID uuid;

	protected Map<UUID, EffectMutationInstance> instanceMap = new Object2ObjectArrayMap<>();

	protected EffectMutation(int _id, String _posName, String _negName,  String resName, String _uuid) {
		uuid = UUID.fromString(_uuid);
		posName = _posName;
		negName = _negName;
		id = _id;

		resourceLocation = new ResourceLocation(WarpstoneMain.MOD_ID, "textures/gui/" + resName);

		attachListeners(MinecraftForge.EVENT_BUS);
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

	public String getMutationName(int level) {
		switch (level) {
			case -1:
				return negName;
			case 1:
				return posName;
			default:
				return "null";
		}
	}

	public EffectMutationInstance getInstance (LivingEntity entity) {
		return getInstance(entity.getUniqueID());
	}

	public EffectMutationInstance getInstance (UUID playerUUID) {
		return instanceMap.getOrDefault(playerUUID, null);
	}

	public EffectMutationInstance putInstance (LivingEntity entity, int mutationLevel) {
		EffectMutationInstance instance = new EffectMutationInstance(this, mutationLevel, entity);

		instanceMap.put(entity.getUniqueID(), instance);
		return instance;
	}

	public void putClientInstance (LivingEntity entity, int mutationLevel){}

	public ResourceLocation getTexture () {
		return resourceLocation;
	}

	public int getMutationID() {
		return id;
	}
}