package com.lenin.warpstonemod.client;

import com.lenin.warpstonemod.client.gui.MutationScreenOpenButton;
import com.lenin.warpstonemod.client.gui.Textures;
import com.lenin.warpstonemod.client.renderers.RatRenderer;
import com.lenin.warpstonemod.common.CommonProxy;
import com.lenin.warpstonemod.common.entities.WSEntityTypes;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

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

	@Override
	public void attachLifeCycle(IEventBus bus) {
		super.attachLifeCycle(bus);

		bus.addListener(this::onClientSetup);
	}

	public void guiPostInit(GuiScreenEvent.InitGuiEvent.Post event) {
		if (event.getGui() instanceof InventoryScreen){
			InventoryScreen gui = (InventoryScreen) event.getGui();
			event.addWidget(new MutationScreenOpenButton(gui.getGuiLeft() + 134, gui.getGuiTop() + (gui.getYSize() / 2) - 22, 20, 18, gui));
		}
	}

	public void onClientSetup (FMLClientSetupEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(WSEntityTypes.RAT, RatRenderer::new);
	}
}