package com.lenin.warpstonemod.common.mutations.attributes;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.Map;
import java.util.UUID;

public abstract class WSAttribute implements IAttributeSource {

    protected float baseValue;
    protected float modValue;
    protected LivingEntity parentEntity;

    protected String name;

    protected Map<UUID, Double> resultMap = new Object2ObjectArrayMap<>();
    protected final Map<UUID, AttributeModifier> modMap = new Object2ObjectArrayMap<>();

    public WSAttribute (LivingEntity _parentEntity, String _name) {
        baseValue = 0;
        modValue = 0;
        parentEntity = _parentEntity;
        name = _name;

        this.attachListeners(MinecraftForge.EVENT_BUS);
    }

    protected abstract void attachListeners(IEventBus bus);

    @Override
    public float getAttributeValue() {
        return baseValue + modValue;
    }

    @Override
    public String getAttributeName() {
        return name;
    }

    @Override
    public void applyModifier(AttributeModifier source) {
        AttributeModifier mod = modMap.putIfAbsent(source.getID(), source);
        if (mod != null) {
            throw new IllegalArgumentException("Modifier is already applied on this attribute!");
        }

        double resultValue;
        switch (source.getOperation()){
            case ADDITION:
                resultValue = source.getAmount() / 100;
                resultMap.put(source.getID(), resultValue);
                modValue += resultValue;
                break;

            case MULTIPLY_BASE:
                resultValue = baseValue * source.getAmount();
                resultMap.put(source.getID(), resultValue);
                modValue += resultValue;
                break;

            case MULTIPLY_TOTAL:
                resultValue = (baseValue + modValue) * source.getAmount();
                resultMap.put(source.getID(), resultValue);
                modValue += resultValue;
                break;
        }

        modMap.put(source.getID(), source);
    }

    @Override
    public void removeModifier(UUID source) {
        if (resultMap.get(source) == null) return;

        modValue -= resultMap.get(source);

        modMap.remove(source);
        resultMap.remove(source);
    }
}