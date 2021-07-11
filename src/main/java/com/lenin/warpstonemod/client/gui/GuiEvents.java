package com.lenin.warpstonemod.client.gui;

import com.lenin.warpstonemod.common.WarpstoneMain;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = WarpstoneMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class GuiEvents {

	@SubscribeEvent
	public static void guiPostInit(GuiScreenEvent.InitGuiEvent.Post event) {
		if (event.getGui() instanceof InventoryScreen){
			InventoryScreen gui = (InventoryScreen) event.getGui();
			//event.getWidgetList().add(new WarpButton(64, 9, 10, 10, gui));
			System.out.println("Inventory Screen Post Initialized");

			event.addWidget(new WarpButton(200, 20, 20, 20, gui));
		}
	}
}