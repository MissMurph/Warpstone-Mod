package com.lenin.warpstonemod.common;

import com.lenin.warpstonemod.client.ClientProxy;
import com.lenin.warpstonemod.common.items.WarpstoneItemGroup;
import com.lenin.warpstonemod.common.mutations.EffectsMap;
import com.lenin.warpstonemod.common.mutations.MutateHelper;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutationRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;
import java.util.UUID;

@Mod(WarpstoneMain.MOD_ID)
public class WarpstoneMain {

    public static final String MOD_ID = "warpstonemod";
    public static final ItemGroup MOD_GROUP = new WarpstoneItemGroup("warpstone");

    private static final Random random = new Random();

    private final CommonProxy proxy;
    private final EffectsMap map;

    private static WarpstoneMain instance;

    public WarpstoneMain() {
        instance = this;

        //CommonProxy.register();

        //Register the mod
       // MinecraftForge.EVENT_BUS.register(this);

        this.map = new EffectsMap();
        map.init();

        //Creates Proxies and assigns for Minecraft to use, refs client THEN server
        this.proxy = DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

        this.proxy.init();
        this.proxy.attachListeners(MinecraftForge.EVENT_BUS);
    }

    public static CommonProxy getProxy () { return getInstance().proxy; }

    public static EffectsMap getEffectsMap () { return getInstance().map; }

    public static WarpstoneMain getInstance() { return instance; }

    public static Random getRandom() { return random; }
}