package com.lenin.warpstonemod.common.mutations;

import com.lenin.warpstonemod.common.WarpstoneMain;
import net.minecraft.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class MutateManager {
    private final LivingEntity parentEntity;
    private final List<Mutation> attributeMutations = new ArrayList<Mutation>();

    private int instability;

    private static final float MULTIPLIER = 0.05f;

    public MutateManager (LivingEntity _parentEntity){
        parentEntity = _parentEntity;
        instability = 0;

        //On the Manager's creation we create the Mutations list
        WarpMutations[] array = WarpMutations.values();
        for (WarpMutations warpMutations : array) {
            attributeMutations.add(warpMutations.constructMutation(warpMutations, parentEntity));
        }

        //As the manager is only being created when a mutation is needed to be made we can do the first mutation upon cration
        mutate();
    }

    public void mutate(){
        //Loop over every point of instablity and apply levels, no negatives if no instablity
        for (int i = 0; i < instability; i++) {
            attributeMutations.get(WarpstoneMain.getRandom().nextInt(attributeMutations.size())).changeLevel(5);

            if (i > 0) { attributeMutations.get(WarpstoneMain.getRandom().nextInt(attributeMutations.size())).changeLevel(-5); }
        }

        instability++;
    }

    public List<Mutation> getMutations() {
        return attributeMutations;
    }

    public LivingEntity getParentEntity (){
        return parentEntity;
    }

    public int getInstability(){
        return instability;
    }
}