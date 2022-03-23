package com.lenin.warpstonemod.client.gui.screens;

import com.lenin.warpstonemod.client.gui.RawTextureResource;
import com.lenin.warpstonemod.client.gui.Textures;
import com.lenin.warpstonemod.client.gui.WSElement;
import com.lenin.warpstonemod.client.gui.components.ButtonComponent;
import com.lenin.warpstonemod.client.gui.components.ImageComponent;
import com.lenin.warpstonemod.client.gui.components.TextComponent;
import com.lenin.warpstonemod.common.mutations.attribute_mutations.AttributeMutation;
import com.lenin.warpstonemod.common.mutations.MutateHelper;
import com.lenin.warpstonemod.common.mutations.PlayerManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.text.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class MutationScreen extends WSScreen {
	//okay you can let me write UI code again I've learnt my lesson

	public MutationScreen(ITextComponent titleIn) {
		super(titleIn, Textures.MUT_SCREEN, 176, 166);
	}

	@Override
	protected void init(){
		super.init();

		PlayerManager clientManager = MutateHelper.getClientManager();

		elements.add(new WSElement.Builder(this.guiLeft + 132, guiTop + 144, 20, 18, this)
				.addComponent(new ImageComponent(Textures.MUT_OPEN_SCREEN_BUTTON))
				.addComponent(
						new ButtonComponent((onPress) -> Minecraft.getInstance().displayGuiScreen(
								new InventoryScreen(Minecraft.getInstance().player)),
								Textures.MUT_OPEN_SCREEN_BUTTON_HVRD))
				.build()
		);

			/*	Instability	*/
		//title above the widget
		elements.add(new WSElement.Builder(this.guiLeft + 121, guiTop + 64, 25, 25, this)
				.addComponent(new TextComponent(new TranslationTextComponent("mutation.screen.instability")))
				.build());

		//widget
		elements.add(new WSElement.Builder(this.guiLeft + 137, this.guiTop + 80, 25, 25, this)
				.addComponent(new TextComponent(new StringTextComponent(String.valueOf(clientManager.getInstabilityLevel()))))
				.addTooltips(clientManager.getInstabilityTooltips().toArray(new ITextComponent[0]))
				.build());

			/* Corruption	*/
		//title above the widget
		elements.add(new WSElement.Builder(this.guiLeft + 117, guiTop + 102, 25, 25, this)
				.addComponent(new TextComponent(new TranslationTextComponent("mutation.screen.corruption")))
				.build());

		//widget
		elements.add(new WSElement.Builder(this.guiLeft + 136, this.guiTop + 119, 25, 25, this)
				.addComponent(new TextComponent(new StringTextComponent(String.valueOf(clientManager.getCorruptionLevel()))))
				.addTooltips(clientManager.getCorruptionTooltips().toArray(new ITextComponent[0]))
				.build()
		);

		List<AttributeMutation> muts = clientManager.getAttributeMutations();
		List<String> effectMuts = clientManager.getEffectMutations();

		for (int i = 0; i < muts.size(); i++) {
			int level = muts.get(i).getMutationLevel();

			int levelMultiple = level < 0 ? 5 : 10;

			float maxLevel = levelMultiple * Math.min(5, clientManager.getCorruptionLevel() + 1);

			float maxFrame = levelMultiple * 5;

			int frame = Math.round(((float)(level) / maxLevel) * maxFrame) + 25;

			List<ITextComponent> attrTooltips = new ArrayList<>();

			attrTooltips.add((new TranslationTextComponent(muts.get(i).getMutationName())).mergeStyle(TextFormatting.WHITE));
			String levelText = "+";
			TextFormatting color = TextFormatting.GREEN;
			if (level < 0) { levelText = ""; color = TextFormatting.RED; }

			String strMaxLevel = (level < 0 ? "-" + 5 * clientManager.getCorruptionLevel() : "+" + 10 * clientManager.getCorruptionLevel()) + "%";

			attrTooltips.add((new StringTextComponent(levelText + (level) + "%")).mergeStyle(color));
			attrTooltips.add(new StringTextComponent("")
					.appendSibling(new TranslationTextComponent("warpstone.screen.generic.max_level"))
					.appendSibling(new StringTextComponent(" " + strMaxLevel)
							.mergeStyle(color)
					)
			);

			WSElement attrBar = new WSElement.Builder(getGuiLeft() + 13 + (17 * i), getGuiTop() + 60, 7, 78, this)
					.addComponent(new ImageComponent(Textures.SHEET_MUT_ATTR_BAR.resolve(frame)))
					.addTooltips(attrTooltips.toArray(new ITextComponent[0]))
					.build();

			elements.add(attrBar);
		}

		for (int i = 0; i < effectMuts.size(); i++) {
			int y = getGuiTop() + 10;
			int x = getGuiLeft() + 10 + (23 * i);
			if (i >= 7) {
				y += 23;
				x = getGuiLeft() + 10 + (23 * (i - 7));
			}

			elements.add(new WSElement.Builder(x, y, 18, 18, this)
					.addComponent(new ImageComponent(
							new RawTextureResource(EffectMutations.getMutation(effectMuts.get(i)).getTexture(), 18, 18, 0, 0)))
					.addTooltips(EffectMutations.getMutation(effectMuts.get(i)).getToolTips().toArray(new ITextComponent[0]))
					.build()
			);
		}
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		InputMappings.Input mouseKey = InputMappings.getInputByCode(keyCode, scanCode);
		if (super.keyPressed(keyCode, scanCode, modifiers)) {
			return true;
		} else if (this.minecraft.gameSettings.keyBindInventory.isActiveAndMatches(mouseKey)) {
			this.closeScreen();
			return true;
		}
		return true;
	}


}