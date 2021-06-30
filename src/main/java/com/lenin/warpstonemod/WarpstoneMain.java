package com.lenin.warpstonemod;

import com.lenin.warpstonemod.items.WarpstoneItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(WarpstoneMain.MOD_ID)
public class WarpstoneMain {

    public static final String MOD_ID = "warpstonemod";
    public static final ItemGroup MOD_GROUP = new WarpstoneItemGroup("warpstone");

    public WarpstoneMain() {
        Registration.register();

        //Event Listener for mod loading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        //Register the mod
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup (final FMLCommonSetupEvent event) {

    }
}