package com.lenin.warpstonemod.common.mutations.effect_mutations;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lenin.warpstonemod.common.mutations.PlayerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PotionMutation extends EffectMutation implements IMutationTick {
    List<Effect> potions;

    public PotionMutation(String _mutName, Effect... _potions) {
        super(_mutName);
        potions = Arrays.asList(_potions);
    }

    @Override
    public void attachClientListeners(IEventBus bus) {
        super.attachClientListeners(bus);

        bus.addListener(this::onPotionApply);
    }

    @Override
    public void mutationTick(PlayerEntity player, LogicalSide side) {
        if (side == LogicalSide.CLIENT
                || !containsInstance(player)
                || !getInstance(player).isActive()
        ) return;

        for (Effect effect : potions) {
            if (player.isPotionActive(effect)) {
                for (EffectInstance e : player.getActivePotionEffects()) {
                    if (e.getPotion() == effect && e.getDuration() < 1200) {
                        player.addPotionEffect(new EffectInstance(effect, 3600, 0, false, false));
                    }
                }
            } else {
                player.addPotionEffect(new EffectInstance(effect, 3600, 0, false, false));
            }
        }
    }

    public void onPotionApply (PotionEvent.PotionAddedEvent event) {
        if (!containsInstance(event.getEntityLiving())
                || !getInstance(event.getEntityLiving()).isActive()
                || !potions.contains(event.getPotionEffect().getPotion())
        ) return;

        event.getPotionEffect().setPotionDurationMax(true);
    }

    @Override
    public void applyMutation(PlayerManager manager) {
        super.applyMutation(manager);

        if (manager.getParentEntity().world.isRemote) return;

        for (Effect effect : potions) {
            manager.getParentEntity().addPotionEffect(new EffectInstance(effect, 3600, 0, false, false));
        }
    }

    @Override
    public void deactivateMutation(PlayerManager manager) {
        super.deactivateMutation(manager);

        if (manager.getParentEntity().world.isRemote) return;

        for (Effect effect : potions) {
            manager.getParentEntity().removePotionEffect(effect);
        }
    }

    @Override
    public JsonObject serializeArguments() {
        JsonObject json = super.serializeArguments();
        JsonArray array = new JsonArray();

        for (Effect potion : potions) {
            array.add(potion.getRegistryName().toString());
        }

        json.add("potions", array);
        return json;
    }

    @Override
    public void deserializeArguments(JsonObject object) {
        super.deserializeArguments(object);

        //Registry.EFFECTS
    }
}