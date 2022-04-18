package com.lenin.warpstonemod.client.gui.elements;

import com.lenin.warpstonemod.client.gui.RawTextureResource;
import com.lenin.warpstonemod.client.gui.Textures;
import com.lenin.warpstonemod.client.gui.WSElement;
import com.lenin.warpstonemod.client.gui.components.ImageComponent;
import com.lenin.warpstonemod.client.gui.screens.WSScreen;
import com.lenin.warpstonemod.common.mutations.Mutation;
import com.lenin.warpstonemod.common.mutations.evolving_mutations.EvolvingMutation;
import com.lenin.warpstonemod.common.mutations.evolving_mutations.MutationTree;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;

public class MutationElement extends WSElement {

    protected Mutation mutation;

    protected MutationElement () {}

    public static class Builder extends WSElement.AbstractBuilder<MutationElement> {
        public Builder(int _x, int _y, int _width, int _height, WSScreen _parentScreen, Mutation _mutation) {
            super(_x, _y, _width, _height, _parentScreen, new MutationElement());

            element.mutation = _mutation;

            RawTextureResource texture = new RawTextureResource(_mutation.getTexture(), 18, 18, 0, 0);

            if (element.mutation instanceof EvolvingMutation) {
                List<MutationTree.Node> tree = ((EvolvingMutation)element.mutation).getChildNodes();
                List<WSElement.AbstractBuilder<? extends WSElement>> _childBuilders = new ArrayList<>();

                if (element.mutation.containsInstance(Minecraft.getInstance().player.getUniqueID())) {
                    EvolvingMutation mut = (EvolvingMutation) _mutation;
                    texture = new RawTextureResource(mut.getCurrentTexture(), 18, 18, 0, 0);
                }

                int highestX = 0;
                int highestY = 0;

                for (MutationTree.Node node : tree) {
                    int x = node.getX();
                    int y = node.getY();

                    if (x > highestX) highestX = x;
                    if (y > highestY) highestY = y;

                    x = (x * 18) + element.x + 18 + 9;
                    y = (y * 18) + element.y + 9;

                    for (MutationTree.Node next : node.getNext()) {
                        int nextX = (next.getX() * 18) + element.x + 18 + 9;
                        int nextY = (next.getY() * 18) + element.y + 9;

                        int diffX = nextX - x;
                        int diffY = nextY - y;

                        if (diffX != 0) {
                            if (diffY > diffX) {
                                _childBuilders.add(new WSElement.Builder(x + 5, nextY + 5, diffX, 4, _parentScreen)
                                        .addComponent(new ImageComponent(Textures.MUTATION_TREE_LINE_HORIZONTAL))
                                );
                            } else {
                                _childBuilders.add(new WSElement.Builder(x + 5, y + 5, diffX, 4, _parentScreen)
                                        .addComponent(new ImageComponent(Textures.MUTATION_TREE_LINE_HORIZONTAL))
                                );
                            }
                        }

                        if (diffY != 0) {
                            if (diffX > diffY) {
                                _childBuilders.add(new WSElement.Builder(nextX + 5, y + 5, 4, diffY, _parentScreen)
                                        .addComponent(new ImageComponent(Textures.MUTATION_TREE_LINE_VERTICAL))
                                );
                            } else {
                                _childBuilders.add(new WSElement.Builder(x + 5, y + 5, 4, diffY, _parentScreen)
                                        .addComponent(new ImageComponent(Textures.MUTATION_TREE_LINE_VERTICAL))
                                );
                            }
                        }
                    }

                    AbstractBuilder<MutationElement> currentBuilder = new MutationElement.Builder(x, y, 18, 18, _parentScreen, node.getParent());
                    List<ITextComponent> conditionTooltips = node.getParent().getConditionTooltips();

                    if (conditionTooltips.size() > 0)
                        currentBuilder.addTooltips(new TranslationTextComponent("generic.required.conditions").appendString(":"))
                                .addTooltips(node.getParent().getConditionTooltips().toArray(new ITextComponent[0]));

                    _childBuilders.add(currentBuilder);
                }

                addChild(new TileSheetElement.Builder(element.x + 18, element.y, (highestX + 2) * 18, (highestY + 2) * 18, _parentScreen, Textures.SHEET_CONTAINER));

                element.childBuilders.addAll(_childBuilders);
            }
            else {
                addTooltips(element.mutation.getToolTips().toArray(new ITextComponent[0]));
            }

            addComponent(new ImageComponent(texture));
        }
    }
}