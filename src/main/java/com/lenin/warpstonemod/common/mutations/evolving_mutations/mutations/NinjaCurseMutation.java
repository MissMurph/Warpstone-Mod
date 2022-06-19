package com.lenin.warpstonemod.common.mutations.evolving_mutations.mutations;

import com.google.gson.JsonObject;
import com.lenin.warpstonemod.common.Warpstone;
import com.lenin.warpstonemod.common.mutations.Mutation;
import com.lenin.warpstonemod.common.PlayerManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.GenericMutation;
import com.lenin.warpstonemod.common.mutations.evolving_mutations.EvolvingMutation;
import com.lenin.warpstonemod.common.mutations.evolving_mutations.EvolvingMutationInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.IntNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class NinjaCurseMutation extends EvolvingMutation {

    protected static int BLOCKS_TO_FALL = 1000;

    public static final Mutation CHILD_CURSE = registerChild(Warpstone.key("curse_ninja_child_1"), GenericMutation::new);
    public static final Mutation CHILD_GIFT = registerChild(Warpstone.key("curse_ninja_child_2"), GenericMutation::new);
    public static final Mutation CHILD_TEST = registerChild(Warpstone.key("curse_ninja_child_3"), GenericMutation::new);

    public NinjaCurseMutation(ResourceLocation _key) {
        super(_key);
    }

    @Override
    public void attachListeners(IEventBus bus) {
        super.attachListeners(bus);

        bus.addListener(this::onLivingFall);
    }

    @Override
    public void applyMutation(PlayerManager manager) {
        super.applyMutation(manager);

        if (manager.getParentEntity().world.isRemote()) return;

        EvolvingMutationInstance instance = (EvolvingMutationInstance) getInstance(manager.getUniqueId());

        instance.writeIfAbsent("fall_blocks", IntNBT.valueOf(0));
    }

    private void onLivingFall (LivingFallEvent event) {
        if (event.getEntityLiving().world.isRemote()
                || !(event.getEntityLiving() instanceof PlayerEntity)
                || !containsInstance(event.getEntityLiving())
                || TREE.getCurrentNode(event.getEntityLiving().getUniqueID()).getParent().equals(CHILD_GIFT)
        ) return;

        EvolvingMutationInstance instance = (EvolvingMutationInstance) getInstance(event.getEntityLiving());

        int value = ((IntNBT)instance.readData("fall_blocks")).getInt();
        if (value < BLOCKS_TO_FALL) {
            instance.writeData("fall_blocks", IntNBT.valueOf(Math.min(BLOCKS_TO_FALL, value  + Math.round(event.getDistance()))));
        }

        checkNextConditions(instance.getParent());
    }

    @Override
    public JsonObject serializeArguments() {
        JsonObject out = super.serializeArguments();

        out.addProperty("blocks_to_fall", 1000);

        return out;
    }

    @Override
    public void deserializeArguments(JsonObject object) {
        super.deserializeArguments(object);

        //BLOCKS_TO_FALL = object.get("blocks_to_fall").getAsInt();
    }
}