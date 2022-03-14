package com.lenin.warpstonemod.common.mutations.effect_mutations;

import com.lenin.warpstonemod.common.Registration;
import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.mutations.effect_mutations.mutations.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ObjectHolder;

import java.util.function.Supplier;

@ObjectHolder("warpstonemod")
public class EffectMutations {
	public static final EffectMutation INVISIBILITY = registerEffect(InvisibilityMutation::new);
	public static final EffectMutation NIGHT_VISION = registerEffect(NightVisionMutation::new);
	public static final EffectMutation JUMP_BOOST = registerEffect(JumpBoostMutation::new);
	public static final EffectMutation GOOD_LUCK = registerEffect(GoodLuckMutation::new);
	public static final EffectMutation SLOW_FALLING = registerEffect(SlowFallMutation::new);
	public static final EffectMutation LEVITATION = registerEffect(LevitationMutation::new);
	public static final EffectMutation WEAK_LEGS = registerEffect(WeakLegsMutation::new);
	public static final EffectMutation BAD_LUCK = registerEffect(BadLuckMutation::new);
	public static final EffectMutation GLOWING = registerEffect(GlowingMutation::new);
	public static final EffectMutation BLINDNESS = registerEffect(BlindnessMutation::new);
	public static final EffectMutation FORTUNE = registerEffect(FortuneMutation::new);
	public static final EffectMutation FAST_METABOLISM = registerEffect(FastMetabolismMutation::new);
	public static final EffectMutation SLOW_METABOLISM = registerEffect(SlowMetabolismMutation::new);
	public static final EffectMutation CORROSIVE_TOUCH = registerEffect(CorrosiveTouchMutation::new);
	public static final EffectMutation FRAIL_BODY = registerEffect(FrailBodyMutation::new);
	public static final EffectMutation STRENGTH = registerEffect(StrengthMutation::new);
	public static final EffectMutation GILLS = registerEffect(GillsMutation::new);
	public static final EffectMutation WEAK_LUNGS = registerEffect(WeakLungsMutation::new);
	public static final EffectMutation FIRE_BREATHING = registerEffect(FireBreathingMutation::new);
	public static final EffectMutation EXPLOSIVE = registerEffect(ExplosiveMutation::new);
	public static final EffectMutation ARCHER = registerEffect(ArcherMutation::new);
	public static final EffectMutation BRAWLER = registerEffect(BrawlerMutation::new);
	public static final EffectMutation SCALES = registerEffect(ScalesMutation::new);
	public static final EffectMutation THORNS = registerEffect(ThornsMutation::new);
	public static final EffectMutation FINS = registerEffect(FinsMutation::new);
	public static final EffectMutation STRONG_LEGS = registerEffect(StrongLegsMutation::new);
	public static final EffectMutation IRON_GUT = registerEffect(IronGutMutation::new);
	public static final EffectMutation ALCOHOLIC = registerEffect(AlcoholicMutation::new);
	public static final EffectMutation WEAK_LIVER = registerEffect(WeakLiverMutation::new);
	public static final EffectMutation BLOOD_SUCKING = registerEffect(BloodSuckingMutation::new);
	public static final EffectMutation UNDEAD = registerEffect(UndeadMutation::new);
	public static final EffectMutation HOOVES = registerEffect(HoovesMutation::new);
	public static final EffectMutation HYDROPHILIC = registerEffect(HydrophilicMutation::new);
	public static final EffectMutation POTASSIUM = registerEffect(PotassiumMutation::new);
	public static final EffectMutation CLAWS = registerEffect(ClawsMutation::new);
	public static final EffectMutation SHARP_SENSES = registerEffect(SharpSensesMutation::new);
	public static final EffectMutation TURTLE = registerEffect(TurtleMutation::new);
	public static final EffectMutation SCARRING = registerEffect(ScarringMutation::new);
	public static final EffectMutation THICK_FUR = registerEffect(FurMutation::new);
	public static final EffectMutation COLD_BLOOD = registerEffect(ColdBloodMutation::new);
	public static final EffectMutation WOOL = registerEffect(WoolMutation::new);
	public static final EffectMutation HERBIVORE = registerEffect(HerbivoreMutation::new);
	public static final EffectMutation CARNIVORE = registerEffect(CarnivoreMutation::new);
	public static final EffectMutation BLACK_LUNG = registerEffect(BlackLungMutation::new);


	public static EffectMutation registerEffect(Supplier<EffectMutation> mut){
		return WarpstoneMain.getProxy().getRegistration().register(mut.get());
	}

	public static EffectMutation getMutation (String key) {
		return Registration.EFFECT_MUTATIONS.getValue(new ResourceLocation(WarpstoneMain.MOD_ID, key));
	}

	public static EffectMutation getMutation (EffectMutation mutation) {
		return Registration.EFFECT_MUTATIONS.getValue(new ResourceLocation(WarpstoneMain.MOD_ID, mutation.getKey()));
	}

	public static void register() {}
}