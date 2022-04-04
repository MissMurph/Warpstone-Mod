package com.lenin.warpstonemod.common.mutations.effect_mutations;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lenin.warpstonemod.common.mutations.Mutation;
import com.lenin.warpstonemod.common.mutations.PlayerManager;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.ResourceLocation;

import java.util.*;

public abstract class EffectMutation extends Mutation {
	//Key is target attribute, this way we can have multiple modifiers on an attribute
	protected Map<ResourceLocation, AttributeModifier> modifiers = new HashMap<>();

	public EffectMutation(ResourceLocation _key) {
		super(_key);

		translateKeyConstant = translateKeyConstant + "effect.";
	}

	@Override
	public void applyMutation (PlayerManager manager){
		if (!containsInstance(manager.getUniqueId())) constructInstance(manager);

		if (manager.getParentEntity().world.isRemote()) return;

		for (ResourceLocation target : modifiers.keySet()) {
			manager.getAttribute(target).applyModifier(modifiers.get(target));
		}
	}

	//This cannot clear instances as methods are overridden to deactivate mutations
	@Override
	public void deactivateMutation(PlayerManager manager) {
		super.deactivateMutation(manager);

		if (manager.getParentEntity().world.isRemote()) return;

		for (ResourceLocation target : modifiers.keySet()) {
			manager.getAttribute(target).removeModifier(modifiers.get(target).getID());
		}
	}

	@Override
	public void deserialize(JsonObject json) {
		super.deserialize(json);

		JsonArray mods = json.getAsJsonArray("modifiers");
		mods.forEach(element -> {
			JsonObject object = element.getAsJsonObject();
			modifiers.put(new ResourceLocation(object.get("target").getAsString()), new AttributeModifier(
					object.has("name") ? object.get("name").getAsString() : name,
					object.get("value").getAsDouble(),
					AttributeModifier.Operation.valueOf(object.get("operation").getAsString())
			));
		});
	}
}