package com.lenin.warpstonemod.common.mutations.effect_mutations;

import com.lenin.warpstonemod.common.CommonProxy;
import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.mutations.MutateHelper;
import com.lenin.warpstonemod.common.mutations.Mutation;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;

import java.util.Map;
import java.util.UUID;

public abstract class EffectMutation {
	protected final String posName, negName;
	protected final int id;
	protected ResourceLocation resourceLocation;
	protected final UUID uuid;

	protected Map<UUID, EffectMutationInstance> instanceMap = new Object2ObjectArrayMap<UUID, EffectMutationInstance>();

	protected EffectMutation(int _id, String _posName, String _negName,  String resName, String _uuid) {
		uuid = UUID.fromString(_uuid);
		posName = _posName;
		negName = _negName;
		id = _id;

		resourceLocation = new ResourceLocation(WarpstoneMain.MOD_ID, "textures/gui/" + resName);

		attachListeners(MinecraftForge.EVENT_BUS);
	}

	public abstract void attachListeners(IEventBus bus);

	public void applyMutation (LivingEntity entity){
		EffectMutationInstance mut = instanceMap.get(entity.getUniqueID());
		mut.setActive(true);
	}

	public void clearMutation(LivingEntity entity) {
		instanceMap.get(entity.getUniqueID()).setActive(false);
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
		EffectMutationInstance instance;

		if (CommonProxy.getSide(entity) == LogicalSide.SERVER)
			instance = new EffectMutationInstance(this, mutationLevel, entity);
		else
			instance = new EffectMutationInstance(this, mutationLevel, Minecraft.getInstance().player);

		instanceMap.put(entity.getUniqueID(), instance);
		return instance;
	}

	public void clearInstance (LivingEntity entity) {
		clearInstance(entity.getUniqueID());
	}

	public void clearInstance (UUID playerUUID) {
		instanceMap.remove(playerUUID);
	}

	public ResourceLocation getTexture () {
		return resourceLocation;
	}

	public int getMutationID() {
		return id;
	}
}