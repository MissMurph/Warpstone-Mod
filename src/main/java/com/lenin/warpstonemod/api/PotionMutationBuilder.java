package com.lenin.warpstonemod.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lenin.warpstonemod.common.mutations.MutationSupplier;
import com.lenin.warpstonemod.common.mutations.effect_mutations.PotionMutation;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class PotionMutationBuilder extends AbstractMutationDataBuilder<PotionMutation> {

    private final List<Effect> potions = new ArrayList<>();

    public <M extends PotionMutation> PotionMutationBuilder(ResourceLocation key) {
        super(key, PotionMutation::new);
    }

    public PotionMutationBuilder addPotion (Effect _effect) {
        if (!potions.contains(_effect)) {
            potions.add(_effect);
        }

        return this;
    }

    @Override
    public JsonObject build() {
        JsonObject out = super.build();

        JsonArray array = new JsonArray();

        for (Effect potion : potions) {
            array.add(potion.getRegistryName().toString());
        }

        out.add("potions", array);

        return out;
    }
}