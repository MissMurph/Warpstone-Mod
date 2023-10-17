package com.lenin.warpstonemod.common.mutations;

import com.google.gson.JsonObject;
import com.lenin.warpstonemod.api.*;
import com.lenin.warpstonemod.common.Registration;
import com.lenin.warpstonemod.common.Warpstone;
import com.lenin.warpstonemod.common.mutations.attribute_mutations.WSAttributes;
import com.lenin.warpstonemod.common.mutations.conditions.HasMutationCondition;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.GenericMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.PotionMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.mutations.*;
import com.lenin.warpstonemod.common.mutations.evolving_mutations.EvolvingMutation;
import com.lenin.warpstonemod.common.mutations.evolving_mutations.mutations.NinjaCurseMutation;
import com.lenin.warpstonemod.common.mutations.tags.MutationTag;
import com.lenin.warpstonemod.common.mutations.tags.MutationTags;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeMod;

import java.util.List;

public class Mutations {

		/*	Effect Mutations	*/

	public static final Mutation INVISIBILITY = register(new PotionMutationBuilder(Warpstone.key("invisibility"))
			.addPotion(Effects.INVISIBILITY)
			.addCondition(HasMutationCondition.builder(Warpstone.key("glowing"), false).build())
			.addTag(MutationTags.UNCOMMON));

	public static final Mutation NIGHT_VISION = register(new PotionMutationBuilder(Warpstone.key("night_vision"))
			.addPotion(Effects.NIGHT_VISION)
			.addCondition(HasMutationCondition.builder(Warpstone.key("blindness"), false).build())
			.addTag(MutationTags.COMMON));

	public static final Mutation JUMP_BOOST = register(new PotionMutationBuilder(Warpstone.key("jump_boost"))
			.addPotion(Effects.JUMP_BOOST)
			.addCondition(HasMutationCondition.builder(Warpstone.key("weak_legs"), false).build())
			.addTag(MutationTags.UNCOMMON));

	public static final Mutation GOOD_LUCK = register(
			new EffectMutationBuilder(Warpstone.key("good_luck"))
					.addModifier(Attributes.LUCK, 1.0, AttributeModifier.Operation.ADDITION)
					.addCondition(HasMutationCondition.builder(Warpstone.key("bad_luck"), false).build())
					.addTag(MutationTags.UNCOMMON));

	public static final Mutation SLOW_FALLING = register(new PotionMutationBuilder(Warpstone.key("slow_falling"))
			.addPotion(Effects.SLOW_FALLING)
			.addTag(MutationTags.UNCOMMON));

	public static final Mutation LEVITATION = register(
			new GenericMutationBuilder<>(Warpstone.key("levitation"), LevitationMutation::new)
					.addTag(MutationTags.UNCOMMON));

	public static final Mutation WEAK_LEGS = register(
			new GenericMutationBuilder<>(Warpstone.key("weak_legs"), WeakLegsMutation::new)
					.addCondition(HasMutationCondition.builder(Mutations.JUMP_BOOST.getRegistryName(), false).build())
					.addCondition(HasMutationCondition.builder(Warpstone.key("strong_legs"), false).build())
					.addTag(MutationTags.NEGATIVE));

	public static final Mutation BAD_LUCK = register(new EffectMutationBuilder(Warpstone.key("bad_luck"))
			.addModifier(Attributes.LUCK.getRegistryName(), -1.0, AttributeModifier.Operation.ADDITION.toString())
			.addCondition(HasMutationCondition.builder(Mutations.GOOD_LUCK.getRegistryName(), false).build())
			.addTag(MutationTags.NEGATIVE));

	public static final Mutation GLOWING = register(new PotionMutationBuilder(Warpstone.key("glowing"))
			.addPotion(Effects.GLOWING)
			.addCondition(HasMutationCondition.builder(Mutations.INVISIBILITY.getRegistryName(), false).build())
			.addTag(MutationTags.COMMON));

	public static final Mutation BLINDNESS = register(new GenericMutationBuilder<>(Warpstone.key("blindness"), BlindnessMutation::new)
			.addTag(MutationTags.COMMON));

	public static final Mutation FORTUNE = register(new GenericMutationBuilder<>(Warpstone.key("fortune"), GenericMutation::new)
			.addTag(MutationTags.RARE));

	public static final Mutation FAST_METABOLISM = register(new GenericMutationBuilder<>(Warpstone.key("fast_metabolism"), FastMetabolismMutation::new)
			.addCondition(HasMutationCondition.builder(Warpstone.key("slow_metabolism"), false).build())
			.addTag(MutationTags.COMMON));

	public static final Mutation SLOW_METABOLISM = register(new GenericMutationBuilder<>(Warpstone.key("slow_metabolism"), SlowMetabolismMutation::new)
			.addCondition(HasMutationCondition.builder(Mutations.FAST_METABOLISM.getRegistryName(), false).build())
			.addTag(MutationTags.NEGATIVE));

	public static final Mutation CORROSIVE_TOUCH = register(new GenericMutationBuilder<>(Warpstone.key("corrosive_touch"), CorrosiveTouchMutation::new)
			.addTag(MutationTags.RARE));

	public static final Mutation FRAIL_BODY = register(new GenericMutationBuilder<>(Warpstone.key("frail_body"), FrailBodyMutation::new)
			.addTag(MutationTags.NEGATIVE));

	public static final Mutation STRENGTH = register(new GenericMutationBuilder<>(Warpstone.key("strength"), StrengthMutation::new)
			.addTag(MutationTags.COMMON));

	public static final Mutation GILLS = register(new PotionMutationBuilder(Warpstone.key("gills"))
			.addPotion(Effects.WATER_BREATHING)
			.addTag(MutationTags.NEGATIVE));

	public static final Mutation WEAK_LUNGS = register(new GenericMutationBuilder<>(Warpstone.key("weak_lungs"), WeakLungsMutation::new)
			.addCondition(HasMutationCondition.builder(Mutations.GILLS.getRegistryName(), false).build())
			.addTag(MutationTags.NEGATIVE));

	public static final Mutation FIRE_BREATHING = register(new GenericMutationBuilder<>(Warpstone.key("fire_breathing"), FireBreathingMutation::new)
			.addCondition(HasMutationCondition.builder(Warpstone.key("explosive"), false).build())
			.addTag(MutationTags.RARE));

	public static final Mutation EXPLOSIVE = register(new GenericMutationBuilder<>(Warpstone.key("explosive"), ExplosiveMutation::new)
			.addCondition(HasMutationCondition.builder(Mutations.FIRE_BREATHING.getRegistryName(), false).build())
			.addTag(MutationTags.NEGATIVE));

	public static final Mutation ARCHER = register(new EffectMutationBuilder(Warpstone.key("archer"))
			.addModifier(WSAttributes.MELEE_DAMAGE.getKey(), -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
			.addModifier(WSAttributes.RANGED_DAMAGE.getKey(), 0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
			.addCondition(HasMutationCondition.builder(Warpstone.key("brawler"), false).build())
			.addTag(MutationTags.UNCOMMON));

	public static final Mutation BRAWLER = register(new EffectMutationBuilder(Warpstone.key("brawler"))
			.addModifier(WSAttributes.MELEE_DAMAGE.getKey(), 0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
			.addModifier(WSAttributes.RANGED_DAMAGE.getKey(), -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
			.addCondition(HasMutationCondition.builder(Mutations.ARCHER.getRegistryName(), false).build())
			.addTag(MutationTags.UNCOMMON));

	public static final Mutation SCALES = register(new EffectMutationBuilder(Warpstone.key("scales"))
			.addModifier(Attributes.ARMOR.getRegistryName(), 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
			.addModifier(Attributes.ARMOR_TOUGHNESS.getRegistryName(), 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
			.addModifier(Attributes.MAX_HEALTH.getRegistryName(), -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
			.addTag(MutationTags.UNCOMMON));

	public static final Mutation THORNS = register(new EffectMutationBuilder(Warpstone.key("thorns"), ThornsMutation::new)
			.addModifier(Attributes.ARMOR.getRegistryName(), -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
			.addTag(MutationTags.UNCOMMON));

	public static final Mutation FINS = register(new EffectMutationBuilder(Warpstone.key("fins"))
			.addModifier(ForgeMod.SWIM_SPEED.getId(), 1.0, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
			.addTag(MutationTags.COMMON));

	public static final Mutation STRONG_LEGS = register(new GenericMutationBuilder<>(Warpstone.key("strong_legs"), StrongLegsMutation::new)
			.addCondition(HasMutationCondition.builder(Mutations.WEAK_LEGS.getRegistryName(), false).build())
			.addTag(MutationTags.UNCOMMON));

	public static final Mutation IRON_GUT = register(new GenericMutationBuilder<>(Warpstone.key("iron_gut"), IronGutMutation::new)
			.addTag(MutationTags.UNCOMMON));

	public static final Mutation ALCOHOLIC = register(new EffectMutationBuilder(Warpstone.key("alcoholic"), AlcoholicMutation::new)
			.addModifier(Attributes.MAX_HEALTH.getRegistryName(), -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
			.addCondition(HasMutationCondition.builder(Mutations.SLOW_METABOLISM.getRegistryName(), false).build())
			.addCondition(HasMutationCondition.builder(Warpstone.key("weak_liver"), false).build())
			.addTag(MutationTags.UNCOMMON));

	public static final Mutation WEAK_LIVER = register(new EffectMutationBuilder(Warpstone.key("weak_liver"), WeakLiverMutation::new)
			.addCondition(HasMutationCondition.builder(Mutations.ALCOHOLIC.getRegistryName(), false).build())
			.addTag(MutationTags.NEGATIVE));

	public static final Mutation BLOOD_SUCKING = register(new EffectMutationBuilder(Warpstone.key("blood_sucking"))
			.addModifier(Attributes.MAX_HEALTH, -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL)
			.addModifier(WSAttributes.LIFE_STEAL.getKey(), 0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
			.addCondition(HasMutationCondition.builder(Mutations.SCALES.getRegistryName(), false).build())
			.addCondition(HasMutationCondition.builder(Warpstone.key("undead"), false).build())
			.addTag(MutationTags.RARE));

	public static final Mutation UNDEAD = register(new EffectMutationBuilder(Warpstone.key("undead"), UndeadMutation::new)
			.addModifier(Attributes.MAX_HEALTH, 1, AttributeModifier.Operation.MULTIPLY_TOTAL)
			.addModifier(WSAttributes.HEALING.getKey(), -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
			.addCondition(HasMutationCondition.builder(Mutations.BLOOD_SUCKING.getRegistryName(), false).build())
			.addTag(MutationTags.RARE));

	public static final Mutation HOOVES = register(new GenericMutationBuilder<>(Warpstone.key("hooves"), HoovesMutation::new)
			.addTag(MutationTags.RARE));

	public static final Mutation HYDROPHILIC = register(new GenericMutationBuilder<>(Warpstone.key("hydrophilic"), HydrophilicMutation::new)
			.addCondition(HasMutationCondition.builder(Warpstone.key("potassium"), false).build())
			.addTag(MutationTags.UNCOMMON));

	public static final Mutation POTASSIUM = register(new GenericMutationBuilder<>(Warpstone.key("potassium"), PotassiumMutation::new)
			.addCondition(HasMutationCondition.builder(Mutations.HYDROPHILIC.getRegistryName(), false).build())
			.addTag(MutationTags.UNCOMMON)
			.addTag(MutationTags.NEGATIVE));

	public static final Mutation CLAWS = register(new GenericMutationBuilder<>(Warpstone.key("claws"), ClawsMutation::new)
			.addTag(MutationTags.RARE));

	public static final Mutation SHARP_SENSES = register(new GenericMutationBuilder<>(Warpstone.key("sharp_senses"), SharpSensesMutation::new)
			.setResourcePath("textures/mob_effect/sharp_senses.png")
			.addTag(MutationTags.RARE));

	public static final Mutation TURTLE = register(new EffectMutationBuilder(Warpstone.key("turtle"), TurtleMutation::new)
			.setResourcePath("textures/mob_effect/turtle.png")
			.addTag(MutationTags.RARE));

	public static final Mutation SCARRING = register(new GenericMutationBuilder<>(Warpstone.key("scarring"), ScarringMutation::new)
			.addTag(MutationTags.RARE));

	public static final Mutation THICK_FUR = register(new GenericMutationBuilder<>(Warpstone.key("thick_fur"), FurMutation::new)
			.addCondition(HasMutationCondition.builder(Warpstone.key("cold_blood"), false).build())
			.addTag(MutationTags.UNCOMMON));

	public static final Mutation COLD_BLOOD = register(new GenericMutationBuilder<>(Warpstone.key("cold_blood"), ColdBloodMutation::new)
			.addCondition(HasMutationCondition.builder(Mutations.THICK_FUR.getRegistryName(), false).build())
			.addTag(MutationTags.UNCOMMON));

	public static final Mutation WOOL = register(new EffectMutationBuilder(Warpstone.key("wool"), WoolMutation::new)
			.setResourcePath("textures/mob_effect/wool.png")
			.addTag(MutationTags.RARE));

	public static final Mutation HERBIVORE = register(new GenericMutationBuilder<>(Warpstone.key("herbivore"), HerbivoreMutation::new)
			.addCondition(HasMutationCondition.builder(Warpstone.key("carnivore"), false).build())
			.addTag(MutationTags.COMMON));

	public static final Mutation CARNIVORE = register(new GenericMutationBuilder<>(Warpstone.key("carnivore"), CarnivoreMutation::new)
			.addCondition(HasMutationCondition.builder(Mutations.HERBIVORE.getRegistryName(), false).build())
			.addTag(Warpstone.key("common")));

	public static final Mutation BLACK_LUNG = register(new GenericMutationBuilder<>(Warpstone.key("black_lung"), GenericMutation::new));

		/*	Evolving Mutations	*/

	public static final Mutation NINJA = register(new GenericMutationBuilder<>(Warpstone.key("curse_ninja"), NinjaCurseMutation::new));

		/* Registration Functions	*/

	public static <M extends Mutation, B extends AbstractMutationDataBuilder<M>> M register (B builder) {
		return WarpstoneAPI.registerMutation(builder);
	}

	/*private static PotionMutation registerPotion (String name, Effect... potions) {
		PotionMutationBuilder builder = new PotionMutationBuilder(Warpstone.key(name));

		for (Effect potion : potions) {
			builder.addPotion(potion);
		}

		return WarpstoneAPI.registerMutation(builder);
	}*/


		//OLD
	/*public static Mutation register(ResourceLocation key, MutationSupplier<Mutation> mut){
		return Warpstone.getProxy().getRegistration().register(mut.get(key));
	}

	public static Mutation register(String name, MutationSupplier<Mutation> mut){
		return Warpstone.getProxy().getRegistration().register(mut.get(key(name)));
	}

	public static Mutation registerGeneric(ResourceLocation key){
		return register(key, GenericMutation::new);
	}

	public static Mutation registerGeneric(String name){
		return register(key(name), GenericMutation::new);
	}

	public static Mutation registerPotion(ResourceLocation key, Effect... potions){
		return Warpstone.getProxy().getRegistration().register(new PotionMutation(key, potions));
	}

	public static Mutation registerPotion(String name, Effect... potions){
		return Warpstone.getProxy().getRegistration().register(new PotionMutation(key(name), potions));
	}*/

	protected static ResourceLocation key (String _key) {
		return new ResourceLocation(Warpstone.MOD_ID, _key);
	}

	public static Mutation getMutation (String key) {
		return getMutation(new ResourceLocation(key));
	}

	public static Mutation getMutation (Mutation mutation) {
		return getMutation(mutation.getRegistryName());
	}

	public static Mutation getMutation (ResourceLocation key) {
		return Registration.MUTATIONS.getValue(key);
	}

	public static void loadMutationData (JsonObject json) {
		ResourceLocation key = new ResourceLocation(json.get("key").getAsString());

		if (Registration.MUTATIONS.containsKey(key)) {
			Registration.MUTATIONS.getValue(key).deserialize(json);
		}
	}

	public static void loadMutationTree (JsonObject json) {
		ResourceLocation key = new ResourceLocation(json.get("key").getAsString());

		Mutation mut = Registration.MUTATIONS.getValue(key);

		if (mut instanceof EvolvingMutation) {
			((EvolvingMutation) mut).loadTreeData(json);
		}
	}

	public static void init() {}

	private static void accept (List<JsonObject> read) {

	}
}