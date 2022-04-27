package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mob_effects.WSEffects;
import com.lenin.warpstonemod.common.mutations.PlayerManager;
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

        if (!player.isPotionActive(WSEffects.WOOL)) {
            int bonus = 0;
            if (bonusMap.containsKey(player.getUniqueID())) bonus = bonusMap.get(player.getUniqueID());
            int duration = bonus >= 9 ? 72000 : 1200;

            player.addPotionEffect(new EffectInstance(
                    WSEffects.WOOL,
                    duration,
                    bonus,
                    false,
                    false
            ));
        }
    }

    public void onPotionRemove(PotionEvent.PotionRemoveEvent event) {
        if (event.getEntityLiving().world.isRemote()
                || event.getPotion() != WSEffects.WOOL
                || !containsInstance(event.getEntityLiving())
        ) return;

        if (!removeList.contains(event.getEntityLiving().getUniqueID())) {
            event.setCanceled(true);
        }
        else removeList.remove(event.getEntityLiving().getUniqueID());
    }

    public void onPotionExpiry (PotionEvent.PotionExpiryEvent event) {
        if (event.getEntityLiving().world.isRemote()
                || event.getPotionEffect().getPotion() != WSEffects.WOOL
                || !containsInstance(event.getEntityLiving())
        ) return;

        if (event.getPotionEffect().getAmplifier() < 9) bonusMap.put(event.getEntityLiving().getUniqueID(), event.getPotionEffect().getAmplifier() + 1);
    }

    public void onItemRightClick (PlayerInteractEvent.RightClickItem event) {
        if (event.getItemStack().getItem() != Items.SHEARS
                || !containsInstance(event.getEntityLiving())
                || event.getEntityLiving().getActivePotionEffect(WSEffects.WOOL).getAmplifier() < 1
        ) return;

        PlayerEntity player = event.getPlayer();

        if (event.getWorld().isRemote()) player.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1f, 1f);
        event.getWorld().playSound(player, player.getPosition(), SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.NEUTRAL, 1f, 1f);

        int amount = event.getEntityLiving().getActivePotionEffect(WSEffects.WOOL).getAmplifier() + 1;

        ItemEntity entity = new ItemEntity(
                event.getWorld(),
                player.getPosX(),
                player.getPosY(),
                player.getPosZ(),
                new ItemStack(Items.WHITE_WOOL.getItem(), amount)
        );

        event.getWorld().addEntity(entity);

        removeList.add(player.getUniqueID());
        bonusMap.remove(player.getUniqueID());
        player.removePotionEffect(WSEffects.WOOL);
    }

    @Override
    public void applyMutation(PlayerManager manager) {
        super.applyMutation(manager);

        if (manager.getParentEntity().world.isRemote()) return;

        manager.getParentEntity().addPotionEffect(new EffectInstance(
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

        if (manager.getParentEntity().world.isRemote() && !manager.getParentEntity().isPotionActive(WSEffects.WOOL)) return;

        removeList.add(manager.getUniqueId());

        manager.getParentEntity().removePotionEffect(WSEffects.WOOL);
        bonusMap.remove(manager.getUniqueId());
    }
}