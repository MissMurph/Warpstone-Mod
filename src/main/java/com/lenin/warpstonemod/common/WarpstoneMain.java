package com.lenin.warpstonemod.common;

import com.lenin.warpstonemod.client.ClientProxy;
import com.lenin.warpstonemod.common.items.WarpstoneItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(WarpstoneMain.MOD_ID)
public class WarpstoneMain {

    public static final String MOD_ID = "warpstonemod";
    public static final ItemGroup MOD_GROUP = new WarpstoneItemGroup("warpstone");

    private final CommonProxy proxy;

    private static WarpstoneMain instance;

    public WarpstoneMain() {
        instance = this;
        Registration.register();

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
}