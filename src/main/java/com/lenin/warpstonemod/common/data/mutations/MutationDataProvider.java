package com.lenin.warpstonemod.common.data.mutations;

import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.data.WarpstoneDataProvider;
import com.lenin.warpstonemod.common.mutations.attribute_mutations.WSAttributes;
import com.lenin.warpstonemod.common.mutations.conditions.HasMutationCondition;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeMod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MutationDataProvider extends WarpstoneDataProvider {
    private static final List<MutationData> data = new ArrayList<>();

    public MutationDataProvider (DataGenerator _generator) {
        super(_generator, "mutations");
        buildMutationData();
    }

    @Override
    public void act(DirectoryCache cache) throws IOException {
        for (MutationData mut : data) {
            IDataProvider.save(GSON, cache, mut.serialize(), this.generator.getOutputFolder().resolve("data/warpstonemod/mutations/" + mut.getPath() + ".json"));
        }
    }

    private ResourceLocation key (String key) {
        return new ResourceLocation(WarpstoneMain.MOD_ID, key);
    }

    private void buildMutationData () {
        data.add(new MutationData.Builder(key("alcoholic"))
                .addModifier(Attributes.MAX_HEALTH.getRegistryName(), -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addCondition(HasMutationCondition.builder(EffectMutations.SLOW_METABOLISM.getRegistryName(), false).build())
                .addCondition(HasMutationCondition.builder(EffectMutations.WEAK_LIVER.getRegistryName(), false).build())
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new MutationData.Builder(key("archer"))
                .addModifier(WSAttributes.MELEE_DAMAGE.getKey(), -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addModifier(WSAttributes.RANGED_DAMAGE.getKey(), 0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addCondition(HasMutationCondition.builder(EffectMutations.BRAWLER.getRegistryName(), false).build())
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new MutationData.Builder(key("bad_luck"))
                .addModifier(Attributes.LUCK.getRegistryName(), -1.0, AttributeModifier.Operation.ADDITION.toString())
                .addCondition(HasMutationCondition.builder(EffectMutations.GOOD_LUCK.getRegistryName(), false).build())
                .addTag(key("negative"))
                .create()
        );

        data.add(new MutationData.Builder(key("black_lung"))
                .addTag(key("common"))
                .create()
        );

        data.add(new MutationData.Builder(key("blindness"))
                .addCondition(HasMutationCondition.builder(EffectMutations.NIGHT_VISION.getRegistryName(), false).build())
                .addTag(key("negative"))
                .create()
        );

        data.add(new MutationData.Builder(key("blood_sucking"))
                .addModifier(Attributes.MAX_HEALTH.getRegistryName(), -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addModifier(WSAttributes.LIFE_STEAL.getKey(), 0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addCondition(HasMutationCondition.builder(EffectMutations.SCALES.getRegistryName(), false).build())
                .addCondition(HasMutationCondition.builder(EffectMutations.UNDEAD.getRegistryName(), false).build())
                .addTag(key("rare"))
                .create()
        );

        data.add(new MutationData.Builder(key("brawler"))
                .addModifier(WSAttributes.MELEE_DAMAGE.getKey(), 0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addModifier(WSAttributes.RANGED_DAMAGE.getKey(), -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addCondition(HasMutationCondition.builder(EffectMutations.ARCHER.getRegistryName(), false).build())
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new MutationData.Builder(key("carnivore"))
                .addCondition(HasMutationCondition.builder(EffectMutations.HERBIVORE.getRegistryName(), false).build())
                .addTag(key("common"))
                .create()
        );

        data.add(new MutationData.Builder(key("claws"))
                .addTag(key("rare"))
                .create()
        );

        data.add(new MutationData.Builder(key("cold_blood"))
                .addCondition(HasMutationCondition.builder(EffectMutations.THICK_FUR.getRegistryName(), false).build())
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new MutationData.Builder(key("corrosive_touch"))
                .addTag(key("rare"))
                .create()
        );

        data.add(new MutationData.Builder(key("explosive"))
                .addCondition(HasMutationCondition.builder(EffectMutations.FIRE_BREATHING.getRegistryName(), false).build())
                .addTag(key("negative"))
                .create()
        );

        data.add(new MutationData.Builder(key("fast_metabolism"))
                .addCondition(HasMutationCondition.builder(EffectMutations.SLOW_METABOLISM.getRegistryName(), false).build())
                .addTag(key("common"))
                .create()
        );

        data.add(new MutationData.Builder(key("fins"))
                .addModifier(ForgeMod.SWIM_SPEED.getId(), 1.0, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addTag(key("negative"))
                .create()
        );

        data.add(new MutationData.Builder(key("fire_breathing"))
                .addCondition(HasMutationCondition.builder(EffectMutations.EXPLOSIVE.getRegistryName(), false).build())
                .addTag(key("rare"))
                .create()
        );

        data.add(new MutationData.Builder(key("fortune"))
                .addTag(key("rare"))
                .create()
        );

        data.add(new MutationData.Builder(key("frail_body"))
                .addTag(key("negative"))
                .create()
        );

        data.add(new MutationData.Builder(key("gills"))
                .addCondition(HasMutationCondition.builder(EffectMutations.WEAK_LUNGS.getRegistryName(), false).build())
                .addTag(key("common"))
                .create()
        );

        data.add(new MutationData.Builder(key("glowing"))
                .addCondition(HasMutationCondition.builder(EffectMutations.INVISIBILITY.getRegistryName(), false).build())
                .addTag(key("common"))
                .create()
        );

        data.add(new MutationData.Builder(key("good_luck"))
                .addModifier(Attributes.LUCK.getRegistryName(), 1.0, AttributeModifier.Operation.ADDITION.toString())
                .addCondition(HasMutationCondition.builder(EffectMutations.BAD_LUCK.getRegistryName(), false).build())
                .addTag(key("common"))
                .create()
        );

        data.add(new MutationData.Builder(key("thick_fur"))
                .addCondition(HasMutationCondition.builder(EffectMutations.COLD_BLOOD.getRegistryName(), false).build())
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new MutationData.Builder(key("herbivore"))
                .addCondition(HasMutationCondition.builder(EffectMutations.CARNIVORE.getRegistryName(), false).build())
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new MutationData.Builder(key("hooves"))
                .addTag(key("rare"))
                .create()
        );

        data.add(new MutationData.Builder(key("hydrophilic"))
                .addCondition(HasMutationCondition.builder(EffectMutations.POTASSIUM.getRegistryName(), false).build())
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new MutationData.Builder(key("invisibility"))
                .addCondition(HasMutationCondition.builder(EffectMutations.GLOWING.getRegistryName(), false).build())
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new MutationData.Builder(key("iron_gut"))
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new MutationData.Builder(key("jump_boost"))
                .addCondition(HasMutationCondition.builder(EffectMutations.WEAK_LEGS.getRegistryName(), false).build())
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new MutationData.Builder(key("levitation"))
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new MutationData.Builder(key("night_vision"))
                .addCondition(HasMutationCondition.builder(EffectMutations.BLINDNESS.getRegistryName(), false).build())
                .addTag(key("common"))
                .create()
        );

        data.add(new MutationData.Builder(key("potassium"))
                .addCondition(HasMutationCondition.builder(EffectMutations.HYDROPHILIC.getRegistryName(), false).build())
                .addTag(key("uncommon"))
                .addTag(key("negative"))
                .create()
        );

        data.add(new MutationData.Builder(key("scales"))
                .addModifier(Attributes.ARMOR.getRegistryName(), 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addModifier(Attributes.ARMOR_TOUGHNESS.getRegistryName(), 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addModifier(Attributes.MAX_HEALTH.getRegistryName(), -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new MutationData.Builder(key("scarring"))
                .addTag(key("rare"))
                .create()
        );

        data.add(new MutationData.Builder(key("sharp_senses"))
                .addResourcePath("textures/mob_effect/sharp_senses.png")
                .addTag(key("rare"))
                .create()
        );

        data.add(new MutationData.Builder(key("slow_falling"))
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new MutationData.Builder(key("slow_metabolism"))
                .addCondition(HasMutationCondition.builder(EffectMutations.FAST_METABOLISM.getRegistryName(), false).build())
                .addTag(key("negative"))
                .create()
        );

        data.add(new MutationData.Builder(key("strength"))
                .addTag(key("common"))
                .create()
        );

        data.add(new MutationData.Builder(key("strong_legs"))
                .addCondition(HasMutationCondition.builder(EffectMutations.WEAK_LEGS.getRegistryName(), false).build())
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new MutationData.Builder(key("thorns"))
                .addModifier(Attributes.ARMOR.getRegistryName(), -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new MutationData.Builder(key("turtle"))
                .addResourcePath("textures/mob_effect/turtle.png")
                .addTag(key("rare"))
                .create()
        );

        data.add(new MutationData.Builder(key("undead"))
                .addModifier(Attributes.MAX_HEALTH.getRegistryName(), 1, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addModifier(WSAttributes.HEALING.getKey(), -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addCondition(HasMutationCondition.builder(EffectMutations.BLOOD_SUCKING.getRegistryName(), false).build())
                .addTag(key("rare"))
                .create()
        );

        data.add(new MutationData.Builder(key("weak_legs"))
                .addCondition(HasMutationCondition.builder(EffectMutations.JUMP_BOOST.getRegistryName(), false).build())
                .addCondition(HasMutationCondition.builder(EffectMutations.STRONG_LEGS.getRegistryName(), false).build())
                .addTag(key("negative"))
                .create()
        );

        data.add(new MutationData.Builder(key("weak_liver"))
                .addCondition(HasMutationCondition.builder(EffectMutations.ALCOHOLIC.getRegistryName(), false).build())
                .addTag(key("negative"))
                .create()
        );

        data.add(new MutationData.Builder(key("weak_lungs"))
                .addCondition(HasMutationCondition.builder(EffectMutations.GILLS.getRegistryName(), false).build())
                .addTag(key("negative"))
                .create()
        );

        data.add(new MutationData.Builder(key("wool"))
                .addResourcePath("textures/mob_effect/wool.png")
                .addTag(key("rare"))
                .create()
        );
    }
}