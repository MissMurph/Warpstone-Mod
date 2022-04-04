package com.lenin.warpstonemod.common.mutations.evolving_mutations.mutations;

import com.lenin.warpstonemod.common.Warpstone;
import com.lenin.warpstonemod.common.mutations.Mutation;
import com.lenin.warpstonemod.common.mutations.PlayerManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.GenericMutation;
import com.lenin.warpstonemod.common.mutations.evolving_mutations.EvolvingMutation;
import com.lenin.warpstonemod.common.mutations.evolving_mutations.NbtMutationInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.IntNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class NinjaCurseMutation extends EvolvingMutation {

    protected static int BLOCKS_TO_FALL = 1000;

    public NinjaCurseMutation(ResourceLocation _key) {
        super(_key);
    }

    @Override
    public void attachListeners(IEventBus bus) {
        super.attachListeners(bus);

        bus.addListener(this::onLivingFall);
    }

    @Override
    protected void fillRegistry() {
        registerChild(Warpstone.key("curse_ninja_child_1"), GenericMutation::new);
        registerChild(Warpstone.key("curse_ninja_child_2"), GenericMutation::new);
    }

    @Override
    public void applyMutation(PlayerManager manager) {
        super.applyMutation(manager);

        if (manager.getParentEntity().world.isRemote()) return;

        NbtMutationInstance instance = (NbtMutationInstance) getInstance(manager.getUniqueId());

        instance.writeIfAbsent("distance_fallen", IntNBT.valueOf(0));
    }

    private void onLivingFall (LivingFallEvent event) {
        if (event.getEntityLiving().world.isRemote()
                || !(event.getEntityLiving() instanceof PlayerEntity)
                || !containsInstance(event.getEntityLiving())
                || CHILDREN.get(Warpstone.key("curse_ninja_child_2")).containsInstance(event.getEntityLiving())
        ) return;

        NbtMutationInstance instance = (NbtMutationInstance) getInstance(event.getEntityLiving());
        int value = ((IntNBT)instance.readData("distance_fallen")).getInt() + Math.round(event.getDistance());

        if (value >= BLOCKS_TO_FALL) {
            instance.writeData("distance_fallen", IntNBT.valueOf(BLOCKS_TO_FALL));
            moveInstanceToChild(instance, CHILDREN.get(Warpstone.key("curse_ninja_child_2")));
        }
    }
}