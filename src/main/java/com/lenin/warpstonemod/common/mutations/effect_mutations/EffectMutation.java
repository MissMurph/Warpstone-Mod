package com.lenin.warpstonemod.common.mutations.effect_mutations;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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

	protected EffectMutation(ResourceLocation _key) {
		super(_key);

		translateKeyConstant = translateKeyConstant + "effect.";
	}

	public static Builder builder (ResourceLocation key) {
		return new Builder(key, EffectMutation::new);
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

	public static class Builder extends AbstractBuilder<EffectMutation> {
		protected final List<AttrModifierData> modifiers = new ArrayList<>();

		protected Builder(ResourceLocation _resource) {
			super(_resource, EffectMutation::new);
		}

			//used for custom implementation of this concrete class, substituting the usual construction with custom implementation
		protected <T extends EffectMutation> Builder(ResourceLocation _resource, MutationSupplier<T> supplier) {
			super(_resource, supplier);
		}

		public Builder addModifier (ResourceLocation target, double value, String operation) {
			return addModifier(target, mutation.getRegistryName().getPath(), value, operation);
		}

		public Builder addModifier (ResourceLocation target, String name, double value, String operation) {
			modifiers.add(new AttrModifierData(target.toString(), name, value, operation));
			return this;
		}

		@Override
		public JsonObject build() {
			JsonObject out = super.build();

			JsonArray jsonMods = new JsonArray();

			for (AttrModifierData modifier : modifiers) {
				jsonMods.add(modifier.serialize(mutation));
			}

			out.add("modifiers", jsonMods);

			return out;
		}

		private static class AttrModifierData {
			private final String target;
			private final String name;
			private final double value;
			private final String operation;

			private AttrModifierData (String _target, String _name, double _value, String _operation) {
				target = _target;
				name = _name;
				value = _value;
				operation = _operation;
			}

			private JsonObject serialize (Mutation mutation) {
				JsonObject out = new JsonObject();

				out.addProperty("target", target);
				if (!Objects.equals(mutation.getRegistryName().getPath(), this.name)) out.addProperty("name", name);
				out.addProperty("value", value);
				out.addProperty("operation", operation);

				return out;
			}
		}
	}
}