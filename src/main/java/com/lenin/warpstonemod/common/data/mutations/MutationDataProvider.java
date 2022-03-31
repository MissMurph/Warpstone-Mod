package com.lenin.warpstonemod.common.data.mutations;

import com.lenin.warpstonemod.common.WarpstoneMain;
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
    private static final List<MutationData> data = new ArrayList<>();

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

    private ResourceLocation key (String key) {
        return new ResourceLocation(WarpstoneMain.MOD_ID, key);
    }

    private void buildMutationData () {
        data.add(new EffectMutationData.Builder(key("alcoholic"))
                .addModifier(Attributes.MAX_HEALTH.getRegistryName(), -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addCondition(HasMutationCondition.builder(Mutations.SLOW_METABOLISM.getRegistryName(), false).build())
                .addCondition(HasMutationCondition.builder(Mutations.WEAK_LIVER.getRegistryName(), false).build())
                //.addNbtNumberCondition("fall_blocks", NbtNumberCondition.Type.INT, "1000", NbtNumberCondition.Operation.EQUAL_TO, NbtNumberCondition.Operation.GREATER_THAN)
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("archer"))
                .addModifier(WSAttributes.MELEE_DAMAGE.getKey(), -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addModifier(WSAttributes.RANGED_DAMAGE.getKey(), 0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addCondition(HasMutationCondition.builder(Mutations.BRAWLER.getRegistryName(), false).build())
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("bad_luck"))
                .addModifier(Attributes.LUCK.getRegistryName(), -1.0, AttributeModifier.Operation.ADDITION.toString())
                .addCondition(HasMutationCondition.builder(Mutations.GOOD_LUCK.getRegistryName(), false).build())
                .addTag(key("negative"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("black_lung"))
                .addTag(key("common"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("blindness"))
                .addCondition(HasMutationCondition.builder(Mutations.NIGHT_VISION.getRegistryName(), false).build())
                .addTag(key("negative"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("blood_sucking"))
                .addModifier(Attributes.MAX_HEALTH.getRegistryName(), -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addModifier(WSAttributes.LIFE_STEAL.getKey(), 0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addCondition(HasMutationCondition.builder(Mutations.SCALES.getRegistryName(), false).build())
                .addCondition(HasMutationCondition.builder(Mutations.UNDEAD.getRegistryName(), false).build())
                .addTag(key("rare"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("brawler"))
                .addModifier(WSAttributes.MELEE_DAMAGE.getKey(), 0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addModifier(WSAttributes.RANGED_DAMAGE.getKey(), -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addCondition(HasMutationCondition.builder(Mutations.ARCHER.getRegistryName(), false).build())
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("carnivore"))
                .addCondition(HasMutationCondition.builder(Mutations.HERBIVORE.getRegistryName(), false).build())
                .addTag(key("common"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("claws"))
                .addTag(key("rare"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("cold_blood"))
                .addCondition(HasMutationCondition.builder(Mutations.THICK_FUR.getRegistryName(), false).build())
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("corrosive_touch"))
                .addTag(key("rare"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("explosive"))
                .addCondition(HasMutationCondition.builder(Mutations.FIRE_BREATHING.getRegistryName(), false).build())
                .addTag(key("negative"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("fast_metabolism"))
                .addCondition(HasMutationCondition.builder(Mutations.SLOW_METABOLISM.getRegistryName(), false).build())
                .addTag(key("common"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("fins"))
                .addModifier(ForgeMod.SWIM_SPEED.getId(), 1.0, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addTag(key("negative"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("fire_breathing"))
                .addCondition(HasMutationCondition.builder(Mutations.EXPLOSIVE.getRegistryName(), false).build())
                .addTag(key("rare"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("fortune"))
                .addTag(key("rare"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("frail_body"))
                .addTag(key("negative"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("gills"))
                .addCondition(HasMutationCondition.builder(Mutations.WEAK_LUNGS.getRegistryName(), false).build())
                .addTag(key("common"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("glowing"))
                .addCondition(HasMutationCondition.builder(Mutations.INVISIBILITY.getRegistryName(), false).build())
                .addTag(key("common"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("good_luck"))
                .addModifier(Attributes.LUCK.getRegistryName(), 1.0, AttributeModifier.Operation.ADDITION.toString())
                .addCondition(HasMutationCondition.builder(Mutations.BAD_LUCK.getRegistryName(), false).build())
                .addTag(key("common"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("thick_fur"))
                .addCondition(HasMutationCondition.builder(Mutations.COLD_BLOOD.getRegistryName(), false).build())
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("herbivore"))
                .addCondition(HasMutationCondition.builder(Mutations.CARNIVORE.getRegistryName(), false).build())
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("hooves"))
                .addTag(key("rare"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("hydrophilic"))
                .addCondition(HasMutationCondition.builder(Mutations.POTASSIUM.getRegistryName(), false).build())
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("invisibility"))
                .addCondition(HasMutationCondition.builder(Mutations.GLOWING.getRegistryName(), false).build())
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("iron_gut"))
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("jump_boost"))
                .addCondition(HasMutationCondition.builder(Mutations.WEAK_LEGS.getRegistryName(), false).build())
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("levitation"))
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("night_vision"))
                .addCondition(HasMutationCondition.builder(Mutations.BLINDNESS.getRegistryName(), false).build())
                .addTag(key("common"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("potassium"))
                .addCondition(HasMutationCondition.builder(Mutations.HYDROPHILIC.getRegistryName(), false).build())
                .addTag(key("uncommon"))
                .addTag(key("negative"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("scales"))
                .addModifier(Attributes.ARMOR.getRegistryName(), 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addModifier(Attributes.ARMOR_TOUGHNESS.getRegistryName(), 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addModifier(Attributes.MAX_HEALTH.getRegistryName(), -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("scarring"))
                .addTag(key("rare"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("sharp_senses"))
                .addResourcePath("textures/mob_effect/sharp_senses.png")
                .addTag(key("rare"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("slow_falling"))
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("slow_metabolism"))
                .addCondition(HasMutationCondition.builder(Mutations.FAST_METABOLISM.getRegistryName(), false).build())
                .addTag(key("negative"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("strength"))
                .addTag(key("common"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("strong_legs"))
                .addCondition(HasMutationCondition.builder(Mutations.WEAK_LEGS.getRegistryName(), false).build())
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("thorns"))
                .addModifier(Attributes.ARMOR.getRegistryName(), -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("turtle"))
                .addResourcePath("textures/mob_effect/turtle.png")
                .addTag(key("rare"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("undead"))
                .addModifier(Attributes.MAX_HEALTH.getRegistryName(), 1, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addModifier(WSAttributes.HEALING.getKey(), -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addCondition(HasMutationCondition.builder(Mutations.BLOOD_SUCKING.getRegistryName(), false).build())
                .addTag(key("rare"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("weak_legs"))
                .addCondition(HasMutationCondition.builder(Mutations.JUMP_BOOST.getRegistryName(), false).build())
                .addCondition(HasMutationCondition.builder(Mutations.STRONG_LEGS.getRegistryName(), false).build())
                .addTag(key("negative"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("weak_liver"))
                .addCondition(HasMutationCondition.builder(Mutations.ALCOHOLIC.getRegistryName(), false).build())
                .addTag(key("negative"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("weak_lungs"))
                .addCondition(HasMutationCondition.builder(Mutations.GILLS.getRegistryName(), false).build())
                .addTag(key("negative"))
                .create()
        );

        data.add(new EffectMutationData.Builder(key("wool"))
                .addResourcePath("textures/mob_effect/wool.png")
                .addTag(key("rare"))
                .create()
        );
    }

    private void buildEvolvingMutations () {
        data.add(new EvolvingMutationData.Builder(key("curse_ninja"))
                .addChildEffect(new EffectMutationData.Builder(key("curse_ninja_child_1"))
                        .addModifier(Attributes.MAX_HEALTH.getRegistryName(), -0.25f, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                        .create()
                )
                .addChildEffect(new EffectMutationData.Builder(key("curse_ninja_child_2"))
                        .addModifier(Attributes.MAX_HEALTH.getRegistryName(), 1f, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                        .addNbtNumberCondition("fall_blocks", NbtNumberCondition.Type.INT, "1000", NbtNumberCondition.Operation.EQUAL_TO, NbtNumberCondition.Operation.GREATER_THAN)
                        .create()
                )
                .addTag(key("uncommon"))
                .create()
        );
    }
}