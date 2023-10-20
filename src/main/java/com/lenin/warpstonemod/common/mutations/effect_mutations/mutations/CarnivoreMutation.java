package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;

public class CarnivoreMutation extends HerbivoreMutation {

    public CarnivoreMutation(ResourceLocation _key) {
        super(_key);
    }

    @Override
    public void onItemUseFinish (LivingEntityUseItemEvent.Finish event) {
        if (event.getEntityLiving().level.isClientSide()
                || !(event.getEntityLiving() instanceof PlayerEntity)
                || !event.getItem().isEdible()
                || !containsInstance(event.getEntityLiving())
        ) return;

        Food food = event.getItem().getItem().getFoodProperties();

        if (MEAT_FOOD.contains(food) || food.isMeat()) {
            float hunger = food.getNutrition() * 0.25f;
            float saturation = food.getSaturationModifier() * 0.25f;

            PlayerEntity player = (PlayerEntity)event.getEntityLiving();
            player.getFoodData().eat(Math.round(hunger), saturation);
        }

        if (VEGE_FOOD.contains(food) || !food.isMeat()) {
            float hunger = food.getNutrition() * 0.25f;
            float saturation = food.getSaturationModifier() * 0.25f;

            PlayerEntity player = (PlayerEntity)event.getEntityLiving();
            player.getFoodData().eat(-Math.round(hunger), -saturation);
        }
    }
}
