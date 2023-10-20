package com.lenin.warpstonemod.common.mutations.attribute_mutations;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public abstract class WSAttribute implements IAttributeSource {

    protected float baseValue;
    protected float modValue;
    protected LivingEntity parentEntity;

    protected ResourceLocation key;

    protected Map<UUID, Double> resultMap = new Object2ObjectArrayMap<>();
    protected final Map<UUID, AttributeModifier> modMap = new Object2ObjectArrayMap<>();

    public WSAttribute (LivingEntity _parentEntity, ResourceLocation _key) {
        baseValue = 0;
        modValue = 0;
        parentEntity = _parentEntity;
        key = _key;

        if (parentEntity != null) this.attachListeners(MinecraftForge.EVENT_BUS);
    }

    protected abstract void attachListeners(IEventBus bus);

    @Override
    public float getAttributeValue() {
        return baseValue + modValue;
    }

    @Override
    public ResourceLocation getAttributeName() {
        return key;
    }

    @Override
    public void applyModifier(AttributeModifier source) {
        AttributeModifier mod = modMap.putIfAbsent(source.getId(), source);
        if (mod != null) {
            throw new IllegalArgumentException("Modifier is already applied on this attribute!");
        }

        double resultValue;
        switch (source.getOperation()){
            case ADDITION:
                resultValue = source.getAmount() / 100;
                resultMap.put(source.getId(), resultValue);
                modValue += resultValue;
                break;

            case MULTIPLY_BASE:
                resultValue = baseValue * source.getAmount();
                resultMap.put(source.getId(), resultValue);
                modValue += resultValue;
                break;

            case MULTIPLY_TOTAL:
                resultValue = (baseValue + modValue) * source.getAmount();
                resultMap.put(source.getId(), resultValue);
                modValue += resultValue;
                break;
        }

        modMap.put(source.getId(), source);
    }

    @Override
    public void removeModifier(UUID source) {
        if (resultMap.get(source) == null) return;

        modValue -= resultMap.get(source);

        modMap.remove(source);
        resultMap.remove(source);
    }

    public interface AttributeSupplier<T extends WSAttribute> {
        T get(LivingEntity entity);
    }

    public static class AttributeFactory<T extends WSAttribute> {
        AttributeSupplier<T> supplier;
        ResourceLocation key;

        public AttributeFactory (AttributeSupplier<T> _supplier, ResourceLocation _key) {
            supplier = _supplier;
            key = _key;
        }

        public T get(LivingEntity entity) {
            return supplier.get(entity);
        }

        public ResourceLocation getKey () {
            return key;
        }
    }
}