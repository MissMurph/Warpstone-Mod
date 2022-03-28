package com.lenin.warpstonemod.common.mutations.effect_mutations;

import com.google.gson.JsonObject;
import com.lenin.warpstonemod.common.Registration;
import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.mutations.effect_mutations.mutations.*;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;

import java.util.function.Supplier;

//@ObjectHolder("warpstonemod")
public class EffectMutations {
	public static final EffectMutation INVISIBILITY = registerPotion("invisibility", Effects.INVISIBILITY);
	public static final EffectMutation NIGHT_VISION = registerPotion("night_vision", Effects.NIGHT_VISION);
	public static final EffectMutation JUMP_BOOST = registerPotion("jump_boost", Effects.JUMP_BOOST);
	public static final EffectMutation GOOD_LUCK = registerGeneric("good_luck");
	public static final EffectMutation SLOW_FALLING = registerPotion("slow_falling", Effects.SLOW_FALLING);
	public static final EffectMutation LEVITATION = register(LevitationMutation::new);
	public static final EffectMutation WEAK_LEGS = register(WeakLegsMutation::new);
	public static final EffectMutation BAD_LUCK = registerGeneric("bad_luck");
	public static final EffectMutation GLOWING = registerPotion("glowing", Effects.GLOWING);
	public static final EffectMutation BLINDNESS = register(BlindnessMutation::new);
	public static final EffectMutation FORTUNE = registerGeneric("fortune");
	public static final EffectMutation FAST_METABOLISM = register(FastMetabolismMutation::new);
	public static final EffectMutation SLOW_METABOLISM = register(SlowMetabolismMutation::new);
	public static final EffectMutation CORROSIVE_TOUCH = register(CorrosiveTouchMutation::new);
	public static final EffectMutation FRAIL_BODY = register(FrailBodyMutation::new);
	public static final EffectMutation STRENGTH = register(StrengthMutation::new);
	public static final EffectMutation GILLS = registerPotion("gills", Effects.WATER_BREATHING);
	public static final EffectMutation WEAK_LUNGS = register(WeakLungsMutation::new);
	public static final EffectMutation FIRE_BREATHING = register(FireBreathingMutation::new);
	public static final EffectMutation EXPLOSIVE = register(ExplosiveMutation::new);
	public static final EffectMutation ARCHER = registerGeneric("archer");
	public static final EffectMutation BRAWLER = registerGeneric("brawler");
	public static final EffectMutation SCALES = registerGeneric("scales");
	public static final EffectMutation THORNS = register(ThornsMutation::new);
	public static final EffectMutation FINS = registerGeneric("fins");
	public static final EffectMutation STRONG_LEGS = register(StrongLegsMutation::new);
	public static final EffectMutation IRON_GUT = register(IronGutMutation::new);
	public static final EffectMutation ALCOHOLIC = register(AlcoholicMutation::new);
	public static final EffectMutation WEAK_LIVER = register(WeakLiverMutation::new);
	public static final EffectMutation BLOOD_SUCKING = registerGeneric("blood_sucking");
	public static final EffectMutation UNDEAD = register(UndeadMutation::new);
	public static final EffectMutation HOOVES = register(HoovesMutation::new);
	public static final EffectMutation HYDROPHILIC = register(HydrophilicMutation::new);
	public static final EffectMutation POTASSIUM = register(PotassiumMutation::new);
	public static final EffectMutation CLAWS = register(ClawsMutation::new);
	public static final EffectMutation SHARP_SENSES = register(SharpSensesMutation::new);
	public static final EffectMutation TURTLE = register(TurtleMutation::new);
	public static final EffectMutation SCARRING = register(ScarringMutation::new);
	public static final EffectMutation THICK_FUR = register(FurMutation::new);
	public static final EffectMutation COLD_BLOOD = register(ColdBloodMutation::new);
	public static final EffectMutation WOOL = register(WoolMutation::new);
	public static final EffectMutation HERBIVORE = register(HerbivoreMutation::new);
	public static final EffectMutation CARNIVORE = register(CarnivoreMutation::new);
	public static final EffectMutation BLACK_LUNG = registerGeneric("black_lung");

	public static EffectMutation register(Supplier<EffectMutation> mut){
		return WarpstoneMain.getProxy().getRegistration().register(mut.get());
	}

	public static EffectMutation registerGeneric(String key){
		return register(() -> new GenericMutation(key));
	}

	public static EffectMutation registerPotion(String key, Effect... potions){
		return register(() -> new PotionMutation(key, potions));
	}

	public static EffectMutation getMutation (String key) {
		return getMutation(new ResourceLocation(key));
	}

	public static EffectMutation getMutation (EffectMutation mutation) {
		return getMutation(mutation.getRegistryName());
	}

	public static EffectMutation getMutation (ResourceLocation key) {
		return Registration.EFFECT_MUTATIONS.getValue(key);
	}

	public static void loadMutationData (JsonObject json) {
		ResourceLocation key = new ResourceLocation(json.get("key").getAsString());

		if (Registration.EFFECT_MUTATIONS.containsKey(key)) {
			Registration.EFFECT_MUTATIONS.getValue(key).deserialize(json);
		}
	}

	public static void init() {}
}