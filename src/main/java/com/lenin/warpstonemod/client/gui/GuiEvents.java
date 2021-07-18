package com.lenin.warpstonemod.client.gui;

import com.lenin.warpstonemod.common.WarpstoneMain;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = WarpstoneMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class GuiEvents {
	/*
	This class serves as a HOOK into the InventoryScreen. THe event we're calling has a function to add a widget to the inspected screen,
	we're using this to add our custom button to be next to the recipe book
	 */

	@SubscribeEvent
	public static void guiPostInit(GuiScreenEvent.InitGuiEvent.Post event) {
		if (event.getGui() instanceof InventoryScreen){
			InventoryScreen gui = (InventoryScreen) event.getGui();
			event.addWidget(new WarpButton(gui.getGuiLeft() + 134, gui.getGuiTop() + (gui.getYSize() / 2) - 22, 20, 18, gui));
		}
	}

	//@SubscribeEvent
	//public static void onInvKey(GuiScreenEvent.KeyboardKeyEvent.KeyboardKeyPressedEvent.Post event){
	//	if (event.getGui() instanceof MutationScreen && event.getKeyCode() == 69) {
	//		event.getGui().closeScreen();
	//	}
	//}
}