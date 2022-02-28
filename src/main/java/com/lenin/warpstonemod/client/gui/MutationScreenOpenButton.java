package com.lenin.warpstonemod.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.util.text.TranslationTextComponent;

public class MutationScreenOpenButton extends WarpButton {
	public MutationScreenOpenButton(int x, int y, int width, int height, Screen _parentGui) {
		super(x, y, width, height, _parentGui);
	}

	@Override
	public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		Minecraft minecraft = Minecraft.getInstance();
		minecraft.getTextureManager().bindTexture(RESOURCE_LOCATION);

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
		blit(matrixStack, x, y, 0, (float)i, this.width, this.height, 20, 37);
	}

	@Override
	public void onPress() {
		Minecraft.getInstance().displayGuiScreen(new MutationScreen(new TranslationTextComponent("warpstonemod.mutation_screen")));
	}
}