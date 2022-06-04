package com.lenin.warpstonemod.client.gui.screens;

import com.lenin.warpstonemod.client.gui.Textures;
import com.lenin.warpstonemod.client.gui.WSElement;
import com.lenin.warpstonemod.client.gui.components.ButtonComponent;
import com.lenin.warpstonemod.client.gui.components.ImageComponent;
import com.lenin.warpstonemod.client.gui.components.TextComponent;
import com.lenin.warpstonemod.client.gui.components.ToolTipComponent;
import com.lenin.warpstonemod.common.Registration;
import com.lenin.warpstonemod.common.mutations.attribute_mutations.AttributeMutation;
import com.lenin.warpstonemod.common.mutations.MutateHelper;
import com.lenin.warpstonemod.common.mutations.PlayerManager;
import com.lenin.warpstonemod.common.mutations.Mutations;
import com.lenin.warpstonemod.common.mutations.tags.MutationTag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.util.ResourceLocation;
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

		PlayerManager clientManager = MutateHelper.getClientManager();

		layer(WSElement.builder(132, 144, 20, 18, this)
				.addComponent(ImageComponent.factory(Textures.MUT_OPEN_SCREEN_BUTTON))
				.addComponent(
						ButtonComponent.factory((onPress) -> Minecraft.getInstance().displayGuiScreen(
								new InventoryScreen(Minecraft.getInstance().player)),
								Textures.MUT_OPEN_SCREEN_BUTTON_HVRD))
		);

		/*	Instability	*/
		//title above the widget
		layer(WSElement.builder(121, 64, 25, 25, this)
				.addComponent(TextComponent.factory(new TranslationTextComponent("mutation.screen.instability")))
		);

		//widget
		layer(WSElement.builder(137, 80, 25, 25, this)
				.addComponent(TextComponent.factory(new StringTextComponent(String.valueOf(clientManager.getInstabilityLevel()))))
				.addComponent(ToolTipComponent.factory(clientManager.getInstabilityTooltips().toArray(new ITextComponent[0])))
		);

		/* Corruption	*/
		//title above the widget
		layer(WSElement.builder(117, 102, 25, 25, this)
				.addComponent(TextComponent.factory(new TranslationTextComponent("mutation.screen.corruption")))
		);

		//widget
		layer(WSElement.builder(136, 119, 25, 25, this)
				.addComponent(TextComponent.factory(new StringTextComponent(String.valueOf(clientManager.getCorruptionLevel()))))
				.addComponent(ToolTipComponent.factory(clientManager.getCorruptionTooltips().toArray(new ITextComponent[0])))
		);

		List<AttributeMutation> muts = clientManager.getAttributeMutations();
		List<ResourceLocation> effectMuts = clientManager.getEffectMutations();

		for (int i = 0; i < muts.size(); i++) {
			int level = muts.get(i).getMutationLevel();

			int levelMultiple = level < 0 ? 5 : 10;

			float maxLevel = levelMultiple * Math.min(5, clientManager.getCorruptionLevel() + 1);

			float maxFrame = levelMultiple * 5;

			int frame = Math.round(((float)(level) / maxLevel) * maxFrame) + 25;

			List<ITextComponent> attrTooltips = new ArrayList();

			attrTooltips.add(muts.get(i).getMutationName().mergeStyle(TextFormatting.WHITE));
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

			layer(WSElement.builder(13 + (17 * i), 60, 7, 78, this)
					.addComponent(ImageComponent.factory(Textures.SHEET_MUT_ATTR_BAR.resolve(frame)))
					.addComponent(ToolTipComponent.factory(attrTooltips.toArray(new ITextComponent[0])))
			);
		}

		for (int i = 0; i < effectMuts.size(); i++) {
			int y = 9;
			int x = 9 + (24 * i);
			if (i >= 7) {
				y += 24;
				x = 10 + (24 * (i - 7));
			}

			layer(mutationElement(x, y, Mutations.getMutation(effectMuts.get(i))), 2);
		}
	}
}