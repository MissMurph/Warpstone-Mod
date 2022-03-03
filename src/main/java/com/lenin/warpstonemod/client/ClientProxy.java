package com.lenin.warpstonemod.client;

import com.lenin.warpstonemod.client.gui.MutationScreenOpenButton;
import com.lenin.warpstonemod.common.CommonProxy;
import com.lenin.warpstonemod.common.Registration;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;

import java.util.stream.Collectors;

public class ClientProxy extends CommonProxy {

	@Override
	public void init (){
		super.init();
	}

	@Override
	public void attachListeners(IEventBus bus) {
		super.attachListeners(bus);

		bus.addListener(this::guiPostInit);

		/*for (EffectMutation mut : Registration.EFFECT_MUTATIONS.getEntries()
				.stream()
				.map(RegistryObject::get)
				.collect(Collectors.toList())) {
			mut.attachClientListeners(bus);
		}*/
	}

	public void guiPostInit(GuiScreenEvent.InitGuiEvent.Post event) {
		if (event.getGui() instanceof InventoryScreen){
			InventoryScreen gui = (InventoryScreen) event.getGui();
			event.addWidget(new MutationScreenOpenButton(gui.getGuiLeft() + 134, gui.getGuiTop() + (gui.getYSize() / 2) - 22, 20, 18, gui));
		}
	}
}