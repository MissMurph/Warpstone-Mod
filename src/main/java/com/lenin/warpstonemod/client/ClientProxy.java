package com.lenin.warpstonemod.client;

import com.lenin.warpstonemod.client.gui.MutationScreenOpenButton;
import com.lenin.warpstonemod.client.gui.Textures;
import com.lenin.warpstonemod.common.CommonProxy;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class ClientProxy extends CommonProxy {

	@Override
	public void init (){
		super.init();

		Textures.register();
	}

	@Override
	public void attachListeners(IEventBus bus) {
		super.attachListeners(bus);

		bus.addListener(this::guiPostInit);
	}

	public void guiPostInit(GuiScreenEvent.InitGuiEvent.Post event) {
		if (event.getGui() instanceof InventoryScreen){
			InventoryScreen gui = (InventoryScreen) event.getGui();
			event.addWidget(new MutationScreenOpenButton(gui.getGuiLeft() + 134, gui.getGuiTop() + (gui.getYSize() / 2) - 22, 20, 18, gui));
		}
	}
}