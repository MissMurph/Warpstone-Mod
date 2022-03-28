package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;

public class CarnivoreMutation extends HerbivoreMutation {

    public CarnivoreMutation() {
        super(
                "carnivore"
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
}
