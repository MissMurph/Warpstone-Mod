package com.lenin.warpstonemod.common.mutations.effect_mutations;

import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.mutations.effect_mutations.mutations.*;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.entity.LivingEntity;

import java.util.Map;

public class EffectMutations {
	public static Map<Integer, EffectMutation> EFFFECT_MAP = new Object2ObjectArrayMap<>();

	public final static EffectMutation INVISIBILITY = registerEffect(new InvisibilityMutation(0));
	public final static EffectMutation NIGHT_VISION = registerEffect(new NightVisionMutation(1));
	public final static EffectMutation JUMP_BOOST = registerEffect(new JumpBoostMutation(2));
	public final static EffectMutation GOOD_LUCK = registerEffect(new GoodLuckMutation(3));
	public final static EffectMutation SLOW_FALLING = registerEffect(new SlowFallMutation(4));
	public final static EffectMutation LEVITATION = registerEffect(new LevitationMutation(5));
	public final static EffectMutation WEAK_LEGS = registerEffect(new WeakLegsMutation(6));
	public final static EffectMutation BAD_LUCK = registerEffect(new BadLuckMutation(7));
	public final static EffectMutation GLOWING = registerEffect(new GlowingMutation(8));
	public final static EffectMutation BLINDNESS = registerEffect(new BlindnessMutation(9));
	public final static EffectMutation FORTUNE = registerEffect(new FortuneMutation(10));
	public final static EffectMutation FAST_METABOLISM = registerEffect(new FastMetabolismMutation(11));
	public final static EffectMutation SLOW_METABOLISM = registerEffect(new SlowMetabolismMutation(12));
	public final static EffectMutation CORROSIVE_TOUCH = registerEffect(new CorrosiveTouchMutation(13));
	public final static EffectMutation FRAIL_BODY = registerEffect(new FrailBodyMutation(14));
	public final static EffectMutation STRENGTH = registerEffect(new StrengthMutation(15));
	public final static EffectMutation GILLS = registerEffect(new GillsMutation(16));
	public final static EffectMutation WEAK_LUNGS = registerEffect(new WeakLungsMutation(17));
	public final static EffectMutation FIRE_BREATHING = registerEffect(new FireBreathingMutation(18));
	public final static EffectMutation EXPLOSIVE = registerEffect(new ExplosiveMutation(19));
	public final static EffectMutation ARCHER = registerEffect(new ArcherMutation(20));
	public final static EffectMutation BRAWLER = registerEffect(new BrawlerMutation(21));
	public final static EffectMutation SCALES = registerEffect(new ScalesMutation(22));
	public final static EffectMutation THORNS = registerEffect(new ThornsMutation(23));
	public final static EffectMutation FINS = registerEffect(new FinsMutation(24));
	public final static EffectMutation STRONG_LEGS = registerEffect(new StrongLegsMutation(25));
	public final static EffectMutation IRON_GUT = registerEffect(new IronGutMutation(26));
	public final static EffectMutation ALCOHOLIC = registerEffect(new AlcoholicMutation(27));
	public final static EffectMutation WEAK_LIVER = registerEffect(new WeakLiverMutation(28));
	public final static EffectMutation BLOOD_SUCKING = registerEffect(new BloodSuckingMutation(29));
	public final static EffectMutation UNDEAD = registerEffect(new UndeadMutation(30));
	public final static EffectMutation HOOVES = registerEffect(new HoovesMutation(31));
	public final static EffectMutation HYDROPHILIC = registerEffect(new HydrophilicMutation(32));
	public final static EffectMutation POTASSIUM = registerEffect(new PotassiumMutation(33));
	public final static EffectMutation CLAWS = registerEffect(new ClawsMutation(34));
	public final static EffectMutation SHARP_SENSES = registerEffect(new SharpSensesMutation(35));
	public final static EffectMutation TURTLE = registerEffect(new TurtleMutation(36));
	public final static EffectMutation SCARRING = registerEffect(new ScarringMutation(37));
	public final static EffectMutation THICK_FUR = registerEffect(new FurMutation(38));

	public static EffectMutation constructInstance (int key, LivingEntity parent){
		EffectMutation mut = EFFFECT_MAP.get(key);
		mut.putInstance(parent);
		return mut;
	}

	public static EffectMutation registerEffect (EffectMutation mut){
		if (EFFFECT_MAP.containsKey(mut.getMutationID())) throw new IllegalArgumentException("ID Already Registered");
		int key = EFFFECT_MAP.size();
		EFFFECT_MAP.put(key, mut);
		return mut;
	}

	public static int getMapSize () {
		return EFFFECT_MAP.size();
	}

	public static Map<Integer, EffectMutation> getMap () {
		return EFFFECT_MAP;
	}

	public static EffectMutation getEffectMutation (EffectMutation mut) {
		return getEffectMutation(mut.getMutationID());
	}

	public static EffectMutation getEffectMutation(int id) {
		return EFFFECT_MAP.get(id);
	}

	public static void init() {}
}