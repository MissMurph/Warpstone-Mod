package com.lenin.warpstonemod.common;

import com.lenin.warpstonemod.client.ClientProxy;
import com.lenin.warpstonemod.common.data.loot.WSLootModifierSerializers;
import com.lenin.warpstonemod.common.items.WarpstoneItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

@Mod(Warpstone.MOD_ID)
public class Warpstone {

    public static final String MOD_ID = "warpstonemod";
    public static final ItemGroup MOD_GROUP = new WarpstoneItemGroup("warpstone");

    private static final Random random = new Random();

    private final CommonProxy proxy;

    private static Warpstone instance;

    public Warpstone() {
        instance = this;

        MinecraftForge.EVENT_BUS.register(this);

        IEventBus fmlEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        WSLootModifierSerializers.init(fmlEventBus);

        this.proxy = DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

        this.proxy.init();
        this.proxy.attachLifeCycle(fmlEventBus);
        this.proxy.attachListeners(MinecraftForge.EVENT_BUS);
    }

    public static CommonProxy getProxy () { return getInstance().proxy; }

    public static Warpstone getInstance() { return instance; }

    public static ResourceLocation key (String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    public static Random getRandom() { return random; }
}