package com.lenin.warpstonemod.common.mutations.effect_mutations;

import com.google.gson.JsonObject;
import com.lenin.warpstonemod.common.Registration;
import com.lenin.warpstonemod.common.Warpstone;
import com.lenin.warpstonemod.common.mutations.Mutation;
import com.lenin.warpstonemod.common.mutations.MutationSupplier;
import com.lenin.warpstonemod.common.mutations.effect_mutations.mutations.*;
import com.lenin.warpstonemod.common.mutations.evolving_mutations.EvolvingMutation;
import com.lenin.warpstonemod.common.mutations.evolving_mutations.mutations.NinjaCurseMutation;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;

//@ObjectHolder("warpstonemod")
public class Mutations {

		/*	Effect Mutations	*/

	public static final Mutation INVISIBILITY = registerPotion("invisibility", Effects.INVISIBILITY);
	public static final Mutation NIGHT_VISION = registerPotion("night_vision", Effects.NIGHT_VISION);
	public static final Mutation JUMP_BOOST = registerPotion("jump_boost", Effects.JUMP_BOOST);
	public static final Mutation GOOD_LUCK = registerGeneric("good_luck");
	public static final Mutation SLOW_FALLING = registerPotion("slow_falling", Effects.SLOW_FALLING);
	public static final Mutation LEVITATION = register("levitation", LevitationMutation::new);
	public static final Mutation WEAK_LEGS = register("weak_legs", WeakLegsMutation::new);
	public static final Mutation BAD_LUCK = registerGeneric("bad_luck");
	public static final Mutation GLOWING = registerPotion("glowing", Effects.GLOWING);
	public static final Mutation BLINDNESS = register("blindness", BlindnessMutation::new);
	public static final Mutation FORTUNE = registerGeneric("fortune");
	public static final Mutation FAST_METABOLISM = register("fast_metabolism", FastMetabolismMutation::new);
	public static final Mutation SLOW_METABOLISM = register("slow_metabolism", SlowMetabolismMutation::new);
	public static final Mutation CORROSIVE_TOUCH = register("corrosive_touch", CorrosiveTouchMutation::new);
	public static final Mutation FRAIL_BODY = register("frail_body", FrailBodyMutation::new);
	public static final Mutation STRENGTH = register("strength", StrengthMutation::new);
	public static final Mutation GILLS = registerPotion("gills", Effects.WATER_BREATHING);
	public static final Mutation WEAK_LUNGS = register("weak_lungs", WeakLungsMutation::new);
	public static final Mutation FIRE_BREATHING = register("fire_breathing", FireBreathingMutation::new);
	public static final Mutation EXPLOSIVE = register("explosive", ExplosiveMutation::new);
	public static final Mutation ARCHER = registerGeneric("archer");
	public static final Mutation BRAWLER = registerGeneric("brawler");
	public static final Mutation SCALES = registerGeneric("scales");
	public static final Mutation THORNS = register("thorns", ThornsMutation::new);
	public static final Mutation FINS = registerGeneric("fins");
	public static final Mutation STRONG_LEGS = register("strong_legs", StrongLegsMutation::new);
	public static final Mutation IRON_GUT = register("iron_gut", IronGutMutation::new);
	public static final Mutation ALCOHOLIC = register("alcoholic", AlcoholicMutation::new);
	public static final Mutation WEAK_LIVER = register("weak_liver", WeakLiverMutation::new);
	public static final Mutation BLOOD_SUCKING = registerGeneric("blood_sucking");
	public static final Mutation UNDEAD = register("undead", UndeadMutation::new);
	public static final Mutation HOOVES = register("hooves", HoovesMutation::new);
	public static final Mutation HYDROPHILIC = register("hydrophilic", HydrophilicMutation::new);
	public static final Mutation POTASSIUM = register("potassium", PotassiumMutation::new);
	public static final Mutation CLAWS = register("claws", ClawsMutation::new);
	public static final Mutation SHARP_SENSES = register("sharp_senses", SharpSensesMutation::new);
	public static final Mutation TURTLE = register("turtle", TurtleMutation::new);
	public static final Mutation SCARRING = register("scarring", ScarringMutation::new);
	public static final Mutation THICK_FUR = register("thick_fur", FurMutation::new);
	public static final Mutation COLD_BLOOD = register("cold_blood", ColdBloodMutation::new);
	public static final Mutation WOOL = register("wool", WoolMutation::new);
	public static final Mutation HERBIVORE = register("herbivore", HerbivoreMutation::new);
	public static final Mutation CARNIVORE = register("carnivore", CarnivoreMutation::new);
	public static final Mutation BLACK_LUNG = registerGeneric("black_lung");

		/*	Evolving Mutations	*/

	public static final Mutation NINJA = register("curse_ninja", NinjaCurseMutation::new);

	public static Mutation register(ResourceLocation key, MutationSupplier<Mutation> mut){
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
	}

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
		return Registration.EFFECT_MUTATIONS.getValue(key);
	}

	public static void loadMutationData (JsonObject json) {
		ResourceLocation key = new ResourceLocation(json.get("key").getAsString());

		if (Registration.EFFECT_MUTATIONS.containsKey(key)) {
			Registration.EFFECT_MUTATIONS.getValue(key).deserialize(json);
		}
	}

	public static void loadMutationTree (JsonObject json) {
		ResourceLocation key = new ResourceLocation(json.get("key").getAsString());

		Mutation mut = Registration.EFFECT_MUTATIONS.getValue(key);

		if (mut instanceof EvolvingMutation) {
			((EvolvingMutation) mut).loadTreeData(json);
		}
	}

	public static void init() {}
}