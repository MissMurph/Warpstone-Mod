package com.lenin.warpstonemod.common.data.loot;

import com.lenin.warpstonemod.common.Warpstone;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class WarpLootModifierSerializers {
	private static final DeferredRegister<GlobalLootModifierSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, Warpstone.MOD_ID);

	public static final RegistryObject<LootModifierFortuneMutation.Serializer> LOOT_FORTUNE = SERIALIZERS.register(
			"loot_fortune",
			LootModifierFortuneMutation.Serializer::new
	);

	public static final RegistryObject<LootModifierBlackLungMutation.Serializer> LOOT_BLACKLUNG = SERIALIZERS.register(
			"loot_blacklung",
			LootModifierBlackLungMutation.Serializer::new
	);

	public static void init (IEventBus bus){
		SERIALIZERS.register(bus);
	}
}