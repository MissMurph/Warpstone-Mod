package com.lenin.warpstonemod.common.mutations.effect_mutations;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lenin.warpstonemod.api.AbstractMutationDataBuilder;
import com.lenin.warpstonemod.common.data.mutations.EffectMutationData;
import com.lenin.warpstonemod.common.data.mutations.MutationData;
import com.lenin.warpstonemod.common.mutations.Mutation;
import com.lenin.warpstonemod.common.PlayerManager;
import com.lenin.warpstonemod.common.mutations.MutationSupplier;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.*;

public class EffectMutation extends Mutation {
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

	@Override
	public void clearMutation(PlayerManager manager) {
		super.clearMutation(manager);

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

	@Override
	public List<ITextComponent> getToolTips() {
		List<ITextComponent> tooltips = super.getToolTips();

		for (Map.Entry<ResourceLocation, AttributeModifier> entry : modifiers.entrySet()) {
			double value = entry.getValue().getAmount();
			String prefix = "+";
			String suffix = "%";
			TextFormatting formatting = TextFormatting.GREEN;

			if (value < 0) {
				prefix = "";
				formatting = TextFormatting.RED;
			}

			if (entry.getValue().getOperation() == AttributeModifier.Operation.ADDITION) {
				suffix = "";
			} else {
				value *= 100;
			}

			tooltips.add(
					new StringTextComponent(prefix + ((int)value) + suffix)
							.mergeStyle(formatting)
							.appendString(" ")
							.appendSibling(new TranslationTextComponent("attribute." + entry.getKey().getPath())
									.mergeStyle(TextFormatting.WHITE)
							)
			);
		}

		return tooltips;
	}
}