package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.mob_effects.WarpEffects;
import com.lenin.warpstonemod.common.mutations.MutateManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.IMutationTick;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.Rarity;
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
    public WoolMutation(int _id) {
        super(_id,
                "wool",
                "ccb70e5e-bbc9-465e-b42f-449959295f5b",
                Rarity.RARE
        );
    }

    //RemoveList is so we dont cancel our own remove calls for the effect
    private List<UUID> removeList = new LinkedList<>();
    private Map<UUID, Integer> bonusMap = new HashMap<>();

    @Override
    public void attachListeners(IEventBus bus) {
        bus.addListener(this::onPotionRemove);
        bus.addListener(this::onPotionExpiry);
        bus.addListener(this::onItemRightClick);
    }

    @Override
    public void attachClientListeners(IEventBus bus) {

    }

    @Override
    public void mutationTick(PlayerEntity player, LogicalSide side) {
        if (side == LogicalSide.CLIENT
                || !containsInstance(player)
                || !getInstance(player).isActive()
        ) return;

        if (!player.isPotionActive(WarpEffects.WOOL.get())) {
            int bonus = 0;
            if (bonusMap.containsKey(player.getUniqueID())) bonus = bonusMap.get(player.getUniqueID());
            int duration = bonus >= 9 ? 72000 : 1200;

            player.addPotionEffect(new EffectInstance(
                    WarpEffects.WOOL.get(),
                    duration,
                    bonus,
                    false,
                    false
            ));
        }
    }

    public void onPotionRemove(PotionEvent.PotionRemoveEvent event) {
        if (event.getEntityLiving().world.isRemote()
                || event.getPotion() != WarpEffects.WOOL.get()
                || !containsInstance(event.getEntityLiving())
                || !getInstance(event.getEntityLiving()).isActive()
        ) return;

        if (!removeList.contains(event.getEntityLiving().getUniqueID())) {
            event.setCanceled(true);
        }
        else removeList.remove(event.getEntityLiving().getUniqueID());
    }

    public void onPotionExpiry (PotionEvent.PotionExpiryEvent event) {
        if (event.getEntityLiving().world.isRemote()
                || event.getPotionEffect().getPotion() != WarpEffects.WOOL.get()
                || !containsInstance(event.getEntityLiving())
                || !getInstance(event.getEntityLiving()).isActive()
        ) return;

        if (event.getPotionEffect().getAmplifier() < 9) bonusMap.put(event.getEntityLiving().getUniqueID(), event.getPotionEffect().getAmplifier() + 1);
    }

    public void onItemRightClick (PlayerInteractEvent.RightClickItem event) {
        if (event.getItemStack().getItem() != Items.SHEARS
                || !containsInstance(event.getEntityLiving())
                || !getInstance(event.getEntityLiving()).isActive()
                || event.getEntityLiving().getActivePotionEffect(WarpEffects.WOOL.get()).getAmplifier() < 1
        ) return;

        PlayerEntity player = event.getPlayer();

        if (event.getWorld().isRemote()) player.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1f, 1f);
        event.getWorld().playSound(player, player.getPosition(), SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.NEUTRAL, 1f, 1f);

        int amount = event.getEntityLiving().getActivePotionEffect(WarpEffects.WOOL.get()).getAmplifier() + 1;

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
        player.removePotionEffect(WarpEffects.WOOL.get());
    }

    @Override
    public void applyMutation(LivingEntity entity) {
        super.applyMutation(entity);

        if (entity.world.isRemote()) return;

        entity.addPotionEffect(new EffectInstance(
                WarpEffects.WOOL.get(),
                1200,
                0,
                false,
                false
        ));
    }

    @Override
    public void deactivateMutation(LivingEntity entity) {
        super.deactivateMutation(entity);

        if (entity.world.isRemote() && !entity.isPotionActive(WarpEffects.WOOL.get())) return;

        removeList.add(entity.getUniqueID());

        entity.removePotionEffect(WarpEffects.WOOL.get());
        if (bonusMap.containsKey(entity.getUniqueID())) bonusMap.remove(entity.getUniqueID());
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(WarpstoneMain.MOD_ID, "textures/mob_effect/wool.png");
    }
}