package com.lenin.warpstonemod.common.data.mutations;

import com.lenin.warpstonemod.common.Warpstone;
import com.lenin.warpstonemod.common.data.WarpstoneDataProvider;
import com.lenin.warpstonemod.common.mutations.attribute_mutations.WSAttributes;
import com.lenin.warpstonemod.common.mutations.conditions.HasMutationCondition;
import com.lenin.warpstonemod.common.mutations.conditions.nbt.NbtNumberCondition;
import com.lenin.warpstonemod.common.mutations.effect_mutations.Mutations;
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
    static final List<MutationData> data = new ArrayList<>();

    public MutationDataProvider (DataGenerator _generator) {
        super(_generator, "mutations");
        buildMutationData();
        buildEvolvingMutations();
    }

    @Override
    public void act(DirectoryCache cache) throws IOException {
        for (MutationData mut : data) {
            IDataProvider.save(GSON, cache, mut.serialize(), this.generator.getOutputFolder().resolve("data/warpstonemod/mutations/" + mut.getPath() + ".json"));
        }
    }

    private void buildMutationData () {
        data.add(new EffectMutationData.Builder(Warpstone.key("alcoholic"))
                .addModifier(Attributes.MAX_HEALTH.getRegistryName(), -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addCondition(HasMutationCondition.builder(Mutations.SLOW_METABOLISM.getRegistryName(), false).build())
                .addCondition(HasMutationCondition.builder(Mutations.WEAK_LIVER.getRegistryName(), false).build())
                .addTag(Warpstone.key("uncommon"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("archer"))
                .addModifier(WSAttributes.MELEE_DAMAGE.getKey(), -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addModifier(WSAttributes.RANGED_DAMAGE.getKey(), 0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addCondition(HasMutationCondition.builder(Mutations.BRAWLER.getRegistryName(), false).build())
                .addTag(Warpstone.key("uncommon"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("bad_luck"))
                .addModifier(Attributes.LUCK.getRegistryName(), -1.0, AttributeModifier.Operation.ADDITION.toString())
                .addCondition(HasMutationCondition.builder(Mutations.GOOD_LUCK.getRegistryName(), false).build())
                .addTag(Warpstone.key("negative"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("black_lung"))
                .addTag(Warpstone.key("common"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("blindness"))
                .addCondition(HasMutationCondition.builder(Mutations.NIGHT_VISION.getRegistryName(), false).build())
                .addTag(Warpstone.key("negative"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("blood_sucking"))
                .addModifier(Attributes.MAX_HEALTH.getRegistryName(), -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addModifier(WSAttributes.LIFE_STEAL.getKey(), 0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addCondition(HasMutationCondition.builder(Mutations.SCALES.getRegistryName(), false).build())
                .addCondition(HasMutationCondition.builder(Mutations.UNDEAD.getRegistryName(), false).build())
                .addTag(Warpstone.key("rare"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("brawler"))
                .addModifier(WSAttributes.MELEE_DAMAGE.getKey(), 0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addModifier(WSAttributes.RANGED_DAMAGE.getKey(), -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addCondition(HasMutationCondition.builder(Mutations.ARCHER.getRegistryName(), false).build())
                .addTag(Warpstone.key("uncommon"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("carnivore"))
                .addCondition(HasMutationCondition.builder(Mutations.HERBIVORE.getRegistryName(), false).build())
                .addTag(Warpstone.key("common"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("claws"))
                .addTag(Warpstone.key("rare"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("cold_blood"))
                .addCondition(HasMutationCondition.builder(Mutations.THICK_FUR.getRegistryName(), false).build())
                .addTag(Warpstone.key("uncommon"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("corrosive_touch"))
                .addTag(Warpstone.key("rare"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("explosive"))
                .addCondition(HasMutationCondition.builder(Mutations.FIRE_BREATHING.getRegistryName(), false).build())
                .addTag(Warpstone.key("negative"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("fast_metabolism"))
                .addCondition(HasMutationCondition.builder(Mutations.SLOW_METABOLISM.getRegistryName(), false).build())
                .addTag(Warpstone.key("common"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("fins"))
                .addModifier(ForgeMod.SWIM_SPEED.getId(), 1.0, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addTag(Warpstone.key("common"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("fire_breathing"))
                .addCondition(HasMutationCondition.builder(Mutations.EXPLOSIVE.getRegistryName(), false).build())
                .addTag(Warpstone.key("rare"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("fortune"))
                .addTag(Warpstone.key("rare"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("frail_body"))
                .addTag(Warpstone.key("negative"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("gills"))
                .addCondition(HasMutationCondition.builder(Mutations.WEAK_LUNGS.getRegistryName(), false).build())
                .addTag(Warpstone.key("common"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("glowing"))
                .addCondition(HasMutationCondition.builder(Mutations.INVISIBILITY.getRegistryName(), false).build())
                .addTag(Warpstone.key("common"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("good_luck"))
                .addModifier(Attributes.LUCK.getRegistryName(), 1.0, AttributeModifier.Operation.ADDITION.toString())
                .addCondition(HasMutationCondition.builder(Mutations.BAD_LUCK.getRegistryName(), false).build())
                .addTag(Warpstone.key("common"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("thick_fur"))
                .addCondition(HasMutationCondition.builder(Mutations.COLD_BLOOD.getRegistryName(), false).build())
                .addTag(Warpstone.key("uncommon"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("herbivore"))
                .addCondition(HasMutationCondition.builder(Mutations.CARNIVORE.getRegistryName(), false).build())
                .addTag(Warpstone.key("uncommon"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("hooves"))
                .addTag(Warpstone.key("rare"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("hydrophilic"))
                .addCondition(HasMutationCondition.builder(Mutations.POTASSIUM.getRegistryName(), false).build())
                .addTag(Warpstone.key("uncommon"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("invisibility"))
                .addCondition(HasMutationCondition.builder(Mutations.GLOWING.getRegistryName(), false).build())
                .addTag(Warpstone.key("uncommon"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("iron_gut"))
                .addTag(Warpstone.key("uncommon"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("jump_boost"))
                .addCondition(HasMutationCondition.builder(Mutations.WEAK_LEGS.getRegistryName(), false).build())
                .addTag(Warpstone.key("uncommon"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("levitation"))
                .addTag(Warpstone.key("uncommon"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("night_vision"))
                .addCondition(HasMutationCondition.builder(Mutations.BLINDNESS.getRegistryName(), false).build())
                .addTag(Warpstone.key("common"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("potassium"))
                .addCondition(HasMutationCondition.builder(Mutations.HYDROPHILIC.getRegistryName(), false).build())
                .addTag(Warpstone.key("uncommon"))
                .addTag(Warpstone.key("negative"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("scales"))
                .addModifier(Attributes.ARMOR.getRegistryName(), 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addModifier(Attributes.ARMOR_TOUGHNESS.getRegistryName(), 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addModifier(Attributes.MAX_HEALTH.getRegistryName(), -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addTag(Warpstone.key("uncommon"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("scarring"))
                .addTag(Warpstone.key("rare"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("sharp_senses"))
                .addResourcePath("textures/mob_effect/sharp_senses.png")
                .addTag(Warpstone.key("rare"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("slow_falling"))
                .addTag(Warpstone.key("uncommon"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("slow_metabolism"))
                .addCondition(HasMutationCondition.builder(Mutations.FAST_METABOLISM.getRegistryName(), false).build())
                .addTag(Warpstone.key("negative"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("strength"))
                .addTag(Warpstone.key("common"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("strong_legs"))
                .addCondition(HasMutationCondition.builder(Mutations.WEAK_LEGS.getRegistryName(), false).build())
                .addTag(Warpstone.key("uncommon"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("thorns"))
                .addModifier(Attributes.ARMOR.getRegistryName(), -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addTag(Warpstone.key("uncommon"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("turtle"))
                .addResourcePath("textures/mob_effect/turtle.png")
                .addTag(Warpstone.key("rare"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("undead"))
                .addModifier(Attributes.MAX_HEALTH.getRegistryName(), 1, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addModifier(WSAttributes.HEALING.getKey(), -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addCondition(HasMutationCondition.builder(Mutations.BLOOD_SUCKING.getRegistryName(), false).build())
                .addTag(Warpstone.key("rare"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("weak_legs"))
                .addCondition(HasMutationCondition.builder(Mutations.JUMP_BOOST.getRegistryName(), false).build())
                .addCondition(HasMutationCondition.builder(Mutations.STRONG_LEGS.getRegistryName(), false).build())
                .addTag(Warpstone.key("negative"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("weak_liver"))
                .addCondition(HasMutationCondition.builder(Mutations.ALCOHOLIC.getRegistryName(), false).build())
                .addTag(Warpstone.key("negative"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("weak_lungs"))
                .addCondition(HasMutationCondition.builder(Mutations.GILLS.getRegistryName(), false).build())
                .addTag(Warpstone.key("negative"))
                .create()
        );

        data.add(new EffectMutationData.Builder(Warpstone.key("wool"))
                .addResourcePath("textures/mob_effect/wool.png")
                .addTag(Warpstone.key("rare"))
                .create()
        );
    }

    private void buildEvolvingMutations () {
        data.add(new EvolvingMutationData.Builder(Warpstone.key("curse_ninja"))
                .addChildMutation(new EffectMutationData.Builder(Warpstone.key("curse_ninja_child_1"))
                        .addModifier(Attributes.MAX_HEALTH.getRegistryName(), -0.25f, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                )
                .addChildMutation(new EffectMutationData.Builder(Warpstone.key("curse_ninja_child_2"))
                        .addModifier(Attributes.MAX_HEALTH.getRegistryName(), 1f, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                        .addNbtNumberCondition("fall_blocks", NbtNumberCondition.Type.INT, "1000", NbtNumberCondition.Operation.EQUAL_TO, NbtNumberCondition.Operation.GREATER_THAN)
                )
                .addTag(Warpstone.key("uncommon"))
                .create()
        );
    }
}