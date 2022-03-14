package com.lenin.warpstonemod.client.gui;

import com.lenin.warpstonemod.client.gui.screens.MutationScreen;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TranslationTextComponent;

public class MutationScreenOpenButton extends Button {
	protected Screen parentGui;

	public MutationScreenOpenButton(int x, int y, int width, int height, Screen _parentGui) {
		super(x, y, width, height, new TranslationTextComponent("warpstone.screen.generic.button"),
				(openScreen) -> Minecraft.getInstance().displayGuiScreen(new MutationScreen(new TranslationTextComponent("warpstonemod.mutation_screen"))));
		parentGui = _parentGui;
	}

	@Override
	public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		Minecraft minecraft = Minecraft.getInstance();
		minecraft.getTextureManager().bindTexture(Textures.MUT_OPEN_SCREEN_BUTTON.resource);

		//78 is the offset applied by the Recipe Book GUI

		if (((InventoryScreen)parentGui).getRecipeGui().isVisible() && x != getParentLeft() + 212) {
			this.setPosition(getParentLeft() + 212, getParentTop() + (166 / 2) - 22);
		}
		else if (!((InventoryScreen)parentGui).getRecipeGui().isVisible() && x != getParentLeft() + 134) {
			this.setPosition(getParentLeft() + 134, getParentTop() + (166 / 2) - 22);
		}

		int i = 0;
		if (this.isHovered()) {
			i += 19;
		}

		RenderSystem.enableDepthTest();
		blit(matrixStack, x, y, 0, (float)i, this.width, this.height, Textures.MUT_OPEN_SCREEN_BUTTON.sourceSizeX, Textures.MUT_OPEN_SCREEN_BUTTON.sourceSizeY);
	}

	protected int getParentLeft () {
		return (parentGui.width - 176) / 2;
	}

	protected int getParentTop () {
		return (parentGui.height - 166) / 2;
	}

	public void setPosition (int _x, int _y){
		this.x = _x; this.y = _y;
	}

	/*@Override
	public void onPress() {
		Minecraft.getInstance().displayGuiScreen(new MutationScreen(new TranslationTextComponent("warpstonemod.mutation_screen")));
	}*/
}