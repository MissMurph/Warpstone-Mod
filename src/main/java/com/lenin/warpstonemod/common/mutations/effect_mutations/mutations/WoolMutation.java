package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mob_effects.WSEffects;
import com.lenin.warpstonemod.common.PlayerManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.IMutationTick;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;

import java.util.*;

public class WoolMutation extends EffectMutation implements IMutationTick {
    public WoolMutation(ResourceLocation _key) {
        super(_key);
    }

    //RemoveList is so we dont cancel our own remove calls for the effect
    private final List<UUID> removeList = new LinkedList<>();
    private final Map<UUID, Integer> bonusMap = new HashMap<>();

    @Override
    public void attachListeners(IEventBus bus) {
        bus.addListener(this::onPotionRemove);
        bus.addListener(this::onPotionExpiry);
        bus.addListener(this::onItemRightClick);
    }

    @Override
    public void mutationTick(PlayerEntity player, LogicalSide side) {
        if (side == LogicalSide.CLIENT
                || !containsInstance(player)
        ) return;

        if (!player.hasEffect(WSEffects.WOOL)) {
            int bonus = 0;
            if (bonusMap.containsKey(player.getUUID())) bonus = bonusMap.get(player.getUUID());
            int duration = bonus >= 9 ? 72000 : 1200;

            player.addEffect(new EffectInstance(
                    WSEffects.WOOL,
                    duration,
                    bonus,
                    false,
                    false
            ));
        }
    }

    public void onPotionRemove(PotionEvent.PotionRemoveEvent event) {
        if (event.getEntityLiving().level.isClientSide()
                || event.getPotion() != WSEffects.WOOL
                || !containsInstance(event.getEntityLiving())
        ) return;

        if (!removeList.contains(event.getEntityLiving().getUUID())) {
            event.setCanceled(true);
        }
        else removeList.remove(event.getEntityLiving().getUUID());
    }

    public void onPotionExpiry (PotionEvent.PotionExpiryEvent event) {
        if (event.getEntityLiving().level.isClientSide
                || event.getPotionEffect().getEffect() != WSEffects.WOOL
                || !containsInstance(event.getEntityLiving())
        ) return;

        if (event.getPotionEffect().getAmplifier() < 9) bonusMap.put(event.getEntityLiving().getUUID(), event.getPotionEffect().getAmplifier() + 1);
    }

    public void onItemRightClick (PlayerInteractEvent.RightClickItem event) {
        if (event.getItemStack().getItem() != Items.SHEARS
                || !containsInstance(event.getEntityLiving())
                || event.getEntityLiving().getEffect(WSEffects.WOOL).getAmplifier() < 1
        ) return;

        PlayerEntity player = event.getPlayer();

        if (event.getWorld().isClientSide()) player.playSound(SoundEvents.SHEEP_SHEAR, 1f, 1f);
        event.getWorld().playSound(player, player.blockPosition(), SoundEvents.SHEEP_SHEAR, SoundCategory.NEUTRAL, 1f, 1f);

        int amount = event.getEntityLiving().getEffect(WSEffects.WOOL).getAmplifier() + 1;

        ItemEntity entity = new ItemEntity(
                event.getWorld(),
                player.getX(),
                player.getY(),
                player.getZ(),
                new ItemStack(Items.WHITE_WOOL.getItem(), amount)
        );

        event.getWorld().addFreshEntity(entity);

        removeList.add(player.getUUID());
        bonusMap.remove(player.getUUID());
        player.removeEffect(WSEffects.WOOL);
    }

    @Override
    public void applyMutation(PlayerManager manager) {
        super.applyMutation(manager);

        if (manager.getParentEntity().level.isClientSide()) return;

        manager.getParentEntity().addEffect(new EffectInstance(
                WSEffects.WOOL,
                1200,
                0,
                false,
                false
        ));
    }

    @Override
    public void clearMutation(PlayerManager manager) {
        super.clearMutation(manager);

        if (manager.getParentEntity().level.isClientSide() && !manager.getParentEntity().hasEffect(WSEffects.WOOL)) return;

        removeList.add(manager.getUniqueId());

        manager.getParentEntity().removeEffect(WSEffects.WOOL);
        bonusMap.remove(manager.getUniqueId());
    }
}