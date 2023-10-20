package com.lenin.warpstonemod.common.entities;

import com.lenin.warpstonemod.common.items.WSItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public class RatEntity extends CreatureEntity {

    public RatEntity(EntityType<? extends CreatureEntity> entity, World worldIn) {
        super(entity, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return RatEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 2.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.FOLLOW_RANGE, 10.0f);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.5D));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.1D, Ingredient.of(WSItems.HUMAN_MEAT), true));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomWalkingGoal(this, 1.8D));
        this.goalSelector.addGoal(4, new LookRandomlyGoal(this));
    }

    @Override
    protected int getExperienceReward(PlayerEntity player) {
        return 1 + this.level.random.nextInt(2);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.BAT_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.BAT_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.BAT_HURT;
    }

    @Override
    protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {
        this.playSound(SoundEvents.SILVERFISH_STEP, 0.15f, 1.0f);
    }
}