package com.lenin.warpstonemod.common;

import com.lenin.warpstonemod.client.ClientProxy;
import com.lenin.warpstonemod.common.items.WarpstoneItemGroup;
import com.lenin.warpstonemod.common.mutations.MutateHelper;
import com.lenin.warpstonemod.common.mutations.EffectsMap;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutationRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod(WarpstoneMain.MOD_ID)
public class WarpstoneMain {

    public static final String MOD_ID = "warpstonemod";
    public static final ItemGroup MOD_GROUP = new WarpstoneItemGroup("warpstone");

    private static final Random random = new Random();

    private final CommonProxy proxy;

    private static WarpstoneMain instance;

    public WarpstoneMain() {
        instance = this;
        Registration.register();
        MutateHelper.init();
        EffectsMap.init();
        EffectMutationRegistry.init();
        //CommonProxy.register();

        //Register the mod
        MinecraftForge.EVENT_BUS.register(this);

        //Creates Proxies and assigns for Minecraft to use, refs client THEN server
        this.proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

        this.proxy.init();
    }

    public static CommonProxy getProxy () { return getInstance().proxy; }

    public static WarpstoneMain getInstance() { return instance; }

    public static Random getRandom() { return random; }
}