package com.lenin.warpstonemod.common;

import com.lenin.warpstonemod.client.ClientProxy;
import com.lenin.warpstonemod.common.data.loot.WarpLootModifierSerializers;
import com.lenin.warpstonemod.common.items.WarpstoneItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

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

        MinecraftForge.EVENT_BUS.register(this);

        IEventBus fmlEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        WarpLootModifierSerializers.init(fmlEventBus);

        this.proxy = DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

        this.proxy.init();
        this.proxy.attachLifeCycle(fmlEventBus);
        this.proxy.attachListeners(MinecraftForge.EVENT_BUS);
    }

    public static CommonProxy getProxy () { return getInstance().proxy; }

    public static WarpstoneMain getInstance() { return instance; }

    public static Random getRandom() { return random; }
}