package com.lenin.warpstonemod.common.data.mutations;

import com.lenin.warpstonemod.common.Registration;
import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.data.WarpstoneDataProvider;
import com.lenin.warpstonemod.common.mutations.attribute_mutations.WSAttributes;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
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
        data.add(new MutationData.Builder(key("alcoholic"), "3fe06fe0-6a8d-403a-b4da-4ed1a9d822fb")
                .addModifier(Attributes.MAX_HEALTH.getRegistryName(), -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new MutationData.Builder(key("archer"), "d35e4fe7-73bc-4fb4-97fe-b47a0e6cf62c")
                .addModifier(WSAttributes.MELEE_DAMAGE.getKey(), -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addModifier(WSAttributes.RANGED_DAMAGE.getKey(), 0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new MutationData.Builder(key("bad_luck"), "0942e8e9-295a-430f-9988-5537e4010648")
                .addModifier(Attributes.LUCK.getRegistryName(), -1.0, AttributeModifier.Operation.ADDITION.toString())
                .addTag(key("negative"))
                .create()
        );

        data.add(new MutationData.Builder(key("black_lung"), "24d24ca3-4072-45d6-a7f0-29085b4f77fd")
                .addTag(key("common"))
                .create()
        );

        data.add(new MutationData.Builder(key("blindness"), "0d988324-bfef-4dd4-87a7-647364829c44")
                .addTag(key("negative"))
                .create()
        );

        data.add(new MutationData.Builder(key("blood_sucking"), "0c3e6ecf-34ef-4ad6-8440-d06573f15fd3")
                .addModifier(Attributes.MAX_HEALTH.getRegistryName(), -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addModifier(WSAttributes.HEALING.getKey(), 0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addTag(key("rare"))
                .create()
        );

        data.add(new MutationData.Builder(key("brawler"), "af1bc073-2f9f-471e-a165-9681cfe4700c")
                .addModifier(WSAttributes.MELEE_DAMAGE.getKey(), 0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addModifier(WSAttributes.RANGED_DAMAGE.getKey(), -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new MutationData.Builder(key("carnivore"), "b9de45cc-6dd2-468d-980e-3da40521b10a")
                .addTag(key("common"))
                .create()
        );

        data.add(new MutationData.Builder(key("claws"), "dde7cc2c-20b4-4fec-af34-0e41044b2587")
                .addTag(key("rare"))
                .create()
        );

        data.add(new MutationData.Builder(key("cold_blood"), "ecf187d9-7a7d-4732-9a20-f44bfe64a615")
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new MutationData.Builder(key("corrosive_touch"), "8ee3692f-f855-43e3-8a9f-dffb37381995")
                .addTag(key("rare"))
                .create()
        );

        data.add(new MutationData.Builder(key("explosive"), "7332e11c-ff66-439f-8808-4de93e9cf355")
                .addTag(key("negative"))
                .create()
        );

        data.add(new MutationData.Builder(key("fast_metabolism"), "1ce59983-cba5-4586-9186-3f69bd0487ce")
                .addTag(key("common"))
                .create()
        );

        data.add(new MutationData.Builder(key("fins"), "26d0153a-08da-4c90-9287-44f1e6920e7d")
                .addModifier(ForgeMod.SWIM_SPEED.getId(), 1.0, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addTag(key("negative"))
                .create()
        );

        data.add(new MutationData.Builder(key("fire_breathing"), "9970d2cf-e6ba-4025-acf6-fc23ca0c3668")
                .addTag(key("rare"))
                .create()
        );

        data.add(new MutationData.Builder(key("fortune"), "9b0a8faa-1888-409f-a2a4-b0aab39cc065")
                .addTag(key("rare"))
                .create()
        );

        data.add(new MutationData.Builder(key("frail_body"), "b09d14ec-beda-4ea9-bf80-2055535c1b99")
                .addTag(key("negative"))
                .create()
        );

        data.add(new MutationData.Builder(key("gills"), "bf69604d-0669-41d2-92e4-aafa8fa0acdc")
                .addTag(key("common"))
                .create()
        );

        data.add(new MutationData.Builder(key("glowing"), "0d988324-bfef-4dd4-87a7-647364829c44")
                .addTag(key("common"))
                .create()
        );

        data.add(new MutationData.Builder(key("good_luck"), "a2361e8f-1be0-478f-9742-a873400e9b6d")
                .addModifier(Attributes.LUCK.getRegistryName(), 1.0, AttributeModifier.Operation.ADDITION.toString())
                .addTag(key("common"))
                .create()
        );

        data.add(new MutationData.Builder(key("thick_fur"), "d20a2481-f5b9-4ad5-8557-3833b983673a")
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new MutationData.Builder(key("herbivore"), "a605523f-b225-44f1-a598-4d6dc5958337")
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new MutationData.Builder(key("hooves"), "eeb67c1e-ef30-4d9a-b4ed-5b4ea156274a")
                .addTag(key("rare"))
                .create()
        );

        data.add(new MutationData.Builder(key("hydrophilic"), "17782c2e-2438-4c81-b05b-507cb3c576b0")
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new MutationData.Builder(key("invisibility"), "a2361e8f-1be0-478f-9742-a873400e9b6d")
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new MutationData.Builder(key("iron_gut"), "487b1027-3643-41ef-b3f5-e5f71abf503f")
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new MutationData.Builder(key("jump_boost"), "1020d46e-68db-45f4-9721-b14608ade167")
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new MutationData.Builder(key("levitation"), "45c87f74-844f-410c-8de2-d9e8cf1cac2c")
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new MutationData.Builder(key("potassium"), "f74dfa9a-2104-403b-85a3-2a3f0c08e8c5")
                .addTag(key("uncommon"))
                .addTag(key("negative"))
                .create()
        );

        data.add(new MutationData.Builder(key("scales"), "265aebfe-d019-4fed-b1a7-a3311ffc7562")
                .addModifier(Attributes.ARMOR.getRegistryName(), 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addModifier(Attributes.ARMOR_TOUGHNESS.getRegistryName(), 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addModifier(Attributes.MAX_HEALTH.getRegistryName(), -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new MutationData.Builder(key("scarring"), "50cc914e-dbfb-4d26-8be3-03de8151932a")
                .addTag(key("rare"))
                .create()
        );

        data.add(new MutationData.Builder(key("sharp_senses"), "6ba9291c-f067-410b-9579-9f11169ea0fd")
                .addResourcePath("textures/mob_effect/sharp_senses.png")
                .addTag(key("rare"))
                .create()
        );

        data.add(new MutationData.Builder(key("slow_falling"), "4e80c5c4-07ef-4ddb-85f9-e1901ba17103")
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new MutationData.Builder(key("slow_metabolism"), "4e0ded20-d669-46e2-98c7-c70d023f4fc6")
                .addTag(key("negative"))
                .create()
        );

        data.add(new MutationData.Builder(key("strength"), "0c0bbd77-a45b-4e92-8f95-ebdd8a565e02")
                .addTag(key("common"))
                .create()
        );

        data.add(new MutationData.Builder(key("strong_legs"), "88a8026d-7ce3-4a21-8436-f3cce8840080")
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new MutationData.Builder(key("thorns"), "cd74624b-9d68-4017-b4ab-eb326f45dd72")
                .addModifier(Attributes.ARMOR.getRegistryName(), -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addTag(key("uncommon"))
                .create()
        );

        data.add(new MutationData.Builder(key("turtle"), "21fdd2d1-a7d3-44fd-a033-d155775e5d95")
                .addResourcePath("textures/mob_effect/turtle.png")
                .addTag(key("rare"))
                .create()
        );

        data.add(new MutationData.Builder(key("undead"), "36588dba-9d9e-45be-b572-c0c571370054")
                .addModifier(Attributes.MAX_HEALTH.getRegistryName(), 1, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addModifier(WSAttributes.HEALING.getKey(), -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL.toString())
                .addTag(key("rare"))
                .create()
        );

        data.add(new MutationData.Builder(key("weak_legs"), "d198bf46-f9aa-4950-b0de-6f80d6396853")
                .addTag(key("negative"))
                .create()
        );

        data.add(new MutationData.Builder(key("weak_liver"), "76635d17-d433-4464-bf8e-dc1a60d55229")
                .addTag(key("negative"))
                .create()
        );

        data.add(new MutationData.Builder(key("weak_lungs"), "9216454f-c64d-4dcd-95f3-339df891aeef")
                .addTag(key("negative"))
                .create()
        );

        data.add(new MutationData.Builder(key("wool"), "ccb70e5e-bbc9-465e-b42f-449959295f5b")
                .addResourcePath("textures/mob_effect/wool.png")
                .addTag(key("rare"))
                .create()
        );
    }
}