package com.lenin.warpstonemod.common.mutations.effect_mutations;

import com.google.gson.JsonObject;
import com.lenin.warpstonemod.common.Registration;
import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.mutations.Mutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.mutations.*;
import com.lenin.warpstonemod.common.mutations.evolving_mutations.mutations.NinjaCurseMutation;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;

import java.util.function.Supplier;

//@ObjectHolder("warpstonemod")
public class Mutations {

		/*	Effect Mutations	*/

	public static final Mutation INVISIBILITY = registerPotion("invisibility", Effects.INVISIBILITY);
	public static final Mutation NIGHT_VISION = registerPotion("night_vision", Effects.NIGHT_VISION);
	public static final Mutation JUMP_BOOST = registerPotion("jump_boost", Effects.JUMP_BOOST);
	public static final Mutation GOOD_LUCK = registerGeneric("good_luck");
	public static final Mutation SLOW_FALLING = registerPotion("slow_falling", Effects.SLOW_FALLING);
	public static final Mutation LEVITATION = register(LevitationMutation::new);
	public static final Mutation WEAK_LEGS = register(WeakLegsMutation::new);
	public static final Mutation BAD_LUCK = registerGeneric("bad_luck");
	public static final Mutation GLOWING = registerPotion("glowing", Effects.GLOWING);
	public static final Mutation BLINDNESS = register(BlindnessMutation::new);
	public static final Mutation FORTUNE = registerGeneric("fortune");
	public static final Mutation FAST_METABOLISM = register(FastMetabolismMutation::new);
	public static final Mutation SLOW_METABOLISM = register(SlowMetabolismMutation::new);
	public static final Mutation CORROSIVE_TOUCH = register(CorrosiveTouchMutation::new);
	public static final Mutation FRAIL_BODY = register(FrailBodyMutation::new);
	public static final Mutation STRENGTH = register(StrengthMutation::new);
	public static final Mutation GILLS = registerPotion("gills", Effects.WATER_BREATHING);
	public static final Mutation WEAK_LUNGS = register(WeakLungsMutation::new);
	public static final Mutation FIRE_BREATHING = register(FireBreathingMutation::new);
	public static final Mutation EXPLOSIVE = register(ExplosiveMutation::new);
	public static final Mutation ARCHER = registerGeneric("archer");
	public static final Mutation BRAWLER = registerGeneric("brawler");
	public static final Mutation SCALES = registerGeneric("scales");
	public static final Mutation THORNS = register(ThornsMutation::new);
	public static final Mutation FINS = registerGeneric("fins");
	public static final Mutation STRONG_LEGS = register(StrongLegsMutation::new);
	public static final Mutation IRON_GUT = register(IronGutMutation::new);
	public static final Mutation ALCOHOLIC = register(AlcoholicMutation::new);
	public static final Mutation WEAK_LIVER = register(WeakLiverMutation::new);
	public static final Mutation BLOOD_SUCKING = registerGeneric("blood_sucking");
	public static final Mutation UNDEAD = register(UndeadMutation::new);
	public static final Mutation HOOVES = register(HoovesMutation::new);
	public static final Mutation HYDROPHILIC = register(HydrophilicMutation::new);
	public static final Mutation POTASSIUM = register(PotassiumMutation::new);
	public static final Mutation CLAWS = register(ClawsMutation::new);
	public static final Mutation SHARP_SENSES = register(SharpSensesMutation::new);
	public static final Mutation TURTLE = register(TurtleMutation::new);
	public static final Mutation SCARRING = register(ScarringMutation::new);
	public static final Mutation THICK_FUR = register(FurMutation::new);
	public static final Mutation COLD_BLOOD = register(ColdBloodMutation::new);
	public static final Mutation WOOL = register(WoolMutation::new);
	public static final Mutation HERBIVORE = register(HerbivoreMutation::new);
	public static final Mutation CARNIVORE = register(CarnivoreMutation::new);
	public static final Mutation BLACK_LUNG = registerGeneric("black_lung");

		/*	Evolving Mutations	*/

	public static final Mutation NINJA = register(NinjaCurseMutation::new);

	public static Mutation register(Supplier<Mutation> mut){
		return WarpstoneMain.getProxy().getRegistration().register(mut.get());
	}

	public static Mutation registerGeneric(String key){
		return register(() -> new GenericMutation(key));
	}

	public static Mutation registerPotion(String key, Effect... potions){
		return register(() -> new PotionMutation(key, potions));
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

	public static void init() {}
}