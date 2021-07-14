package com.lenin.warpstonemod.common.mutations;

import net.minecraft.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class MutateHelper {

    public static List<MutateManager> managers = new ArrayList<MutateManager>();

    public static List<LivingEntity> getPlayers (){
        List<LivingEntity> list = new ArrayList<LivingEntity>();
        for (MutateManager m : managers) { list.add(m.getParentEntity()); }

        return list;
    }

    public static MutateManager getManager (LivingEntity e) {
        for (MutateManager m : managers) { if (m.getParentEntity() == e) return m; }

        return null;
    }

    public static MutateManager createManager (LivingEntity e) {
        MutateManager m = new MutateManager(e);
        managers.add(m);
        return m;
    }

    public static void register (){ }
}