package com.lenin.warpstonemod.common.mutations.effect_mutations;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lenin.warpstonemod.common.mutations.IMutationTick;
import com.lenin.warpstonemod.common.PlayerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.List;

public class PotionMutation extends EffectMutation implements IMutationTick {
    List<Effect> potions;

    public PotionMutation(ResourceLocation _key, Effect... _potions) {
        super(_key);
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
        ) return;

        for (Effect effect : potions) {
            if (player.hasEffect(effect)) {
                for (EffectInstance e : player.getActiveEffects()) {
                    if (e.getEffect() == effect && e.getDuration() < 1200) {
                        player.addEffect(new EffectInstance(effect, 3600, 0, false, false));
                    }
                }
            } else {
                player.addEffect(new EffectInstance(effect, 3600, 0, false, false));
            }
        }
    }

    @Override
    public List<ITextComponent> getToolTips() {
        List<ITextComponent> out = super.getToolTips();

        //Remove the description line
        out.remove(1);

        for (Effect potion : potions) {
            out.add(new TranslationTextComponent("generic.potion.grants")
                    .append(": ")
                    .append(potion.getDisplayName())
            );
        }

        return out;
    }

    public void onPotionApply (PotionEvent.PotionAddedEvent event) {
        if (!containsInstance(event.getEntityLiving())
                || !potions.contains(event.getPotionEffect().getEffect())
        ) return;

        event.getPotionEffect().setNoCounter(true);
    }

    @Override
    public void applyMutation(PlayerManager manager) {
        super.applyMutation(manager);

        if (manager.getParentEntity().level.isClientSide()) return;

        for (Effect effect : potions) {
            manager.getParentEntity().addEffect(new EffectInstance(effect, 3600, 0, false, false));
        }
    }

    @Override
    public void clearMutation(PlayerManager manager) {
        super.clearMutation(manager);

        if (manager.getParentEntity().level.isClientSide()) return;

        for (Effect effect : potions) {
            manager.getParentEntity().removeEffect(effect);
        }
    }

    @Override
    public void deserialize(JsonObject json) {
        super.deserialize(json);

        JsonArray effects = json.getAsJsonArray("potions");

        if (effects != null) {
            for (JsonElement jElement : effects) {
                ResourceLocation key = new ResourceLocation(jElement.getAsString());

                if (ForgeRegistries.POTIONS.containsKey(key)) {
                    potions.add(ForgeRegistries.POTIONS.getValue(key));
                }
            }
        }
    }
}