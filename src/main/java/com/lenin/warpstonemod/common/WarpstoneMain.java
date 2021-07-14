package com.lenin.warpstonemod.common;

import com.lenin.warpstonemod.client.ClientProxy;
import com.lenin.warpstonemod.common.items.WarpstoneItemGroup;
import com.lenin.warpstonemod.common.mutations.MutateHelper;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.Random;
import java.util.UUID;

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
        MutateHelper.register();

        //Event Listener for mod loading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        //Register the mod
        MinecraftForge.EVENT_BUS.register(this);

        //Creates Proxies and assigns for Minecraft to use, refs client THEN server
        this.proxy = DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    }

    private void setup (final FMLCommonSetupEvent event) {

    }

    public static CommonProxy getProxy () { return getInstance().proxy; }

    public static WarpstoneMain getInstance() { return instance; }

    public static Random getRandom() { return random; }
}