package com.lenin.warpstonemod.client.gui.screens;

import com.lenin.warpstonemod.client.gui.RawTextureResource;
import com.lenin.warpstonemod.client.gui.Textures;
import com.lenin.warpstonemod.client.gui.WSElement;
import com.lenin.warpstonemod.client.gui.components.ImageComponent;
import com.lenin.warpstonemod.common.Registration;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import com.lenin.warpstonemod.common.mutations.tags.MutationTag;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CorruptedTomeScreen extends WSScreen{
    public CorruptedTomeScreen() {
        super(new TranslationTextComponent("warpstone.screen.corrupted_tome"), Textures.CORRUPTED_TOME_SCREEN, 256, 180);
    }

    @Override
    protected void init() {
        super.init();

        List<EffectMutation> muts = new ArrayList<>(Registration.EFFECT_MUTATIONS.getValues());

        muts.sort(new TagComporator());

        for (int i = 0; i < muts.size(); i++) {
                //Casting to int always rounds down
            int row = ((int) ((float)i / 10));

            int y = getGuiTop() + 10 + (23 * row);
            int x = getGuiLeft() + 15 + (23 * (i - (10 * row)));

            elements.add(new WSElement.Builder(x, y, 18, 18, this)
                    .addComponent(new ImageComponent(
                            new RawTextureResource(EffectMutations.getMutation(muts.get(i)).getTexture(), 18, 18, 0, 0)))
                    .addTooltips(EffectMutations.getMutation(muts.get(i)).getToolTips().toArray(new ITextComponent[0]))
                    .build()
            );
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    /**Comparator sorts mutations so negatives are first sorted by rarity with the rest coming after sorted by rarity.
     * Only needed for Tome Screen hence this isn't a part of the core Mutation Code
     */

    public static class TagComporator implements Comparator<EffectMutation> {

        @Override
        public int compare(EffectMutation o1, EffectMutation o2) {
            int o1Weight = 0;
            int o2Weight = 0;

            for (MutationTag tag : o1.tags) {
                o1Weight += tag.getWeight();
            }
            for (MutationTag tag : o2.tags) {
                o2Weight += tag.getWeight();
            }

            return o1Weight - o2Weight;
        }
    }
}