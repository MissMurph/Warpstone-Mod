package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.MutateManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import com.lenin.warpstonemod.common.mutations.tags.MutationTags;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Rarity;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;

public class CarnivoreMutation extends HerbivoreMutation {

    public CarnivoreMutation() {
        super(
                "carnivore",
                "b9de45cc-6dd2-468d-980e-3da40521b10a"
        );
    }

    @Override
    public void onItemUseFinish (LivingEntityUseItemEvent.Finish event) {
        if (event.getEntityLiving().world.isRemote()
                || !(event.getEntityLiving() instanceof PlayerEntity)
                || !event.getItem().isFood()
                || !containsInstance(event.getEntityLiving())
                || !getInstance(event.getEntityLiving()).isActive()
        ) return;

        Food food = event.getItem().getItem().getFood();

        if (MEAT_FOOD.contains(food) || food.isMeat()) {
            float hunger = food.getHealing() * 0.25f;
            float saturation = food.getSaturation() * 0.25f;

            PlayerEntity player = (PlayerEntity)event.getEntityLiving();
            player.getFoodStats().addStats(Math.round(hunger), saturation);
        }

        if (VEGE_FOOD.contains(food) || !food.isMeat()) {
            float hunger = food.getHealing() * 0.25f;
            float saturation = food.getSaturation() * 0.25f;

            PlayerEntity player = (PlayerEntity)event.getEntityLiving();
            player.getFoodStats().addStats(-Math.round(hunger), -saturation);
        }
    }

    @Override
    public boolean isLegalMutation(MutateManager manager) {
        return super.isLegalMutation(manager) && !manager.containsEffect(EffectMutations.HERBIVORE);
    }
}
