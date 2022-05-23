package com.lenin.warpstonemod.client.gui.elements;

import com.lenin.warpstonemod.client.gui.RawTextureResource;
import com.lenin.warpstonemod.client.gui.Textures;
import com.lenin.warpstonemod.client.gui.WSElement;
import com.lenin.warpstonemod.client.gui.WSElement.Builder;
import com.lenin.warpstonemod.client.gui.components.ChooseOptionalComponent;
import com.lenin.warpstonemod.client.gui.components.ImageComponent;
import com.lenin.warpstonemod.client.gui.screens.WSScreen;
import com.lenin.warpstonemod.common.mutations.MutateHelper;
import com.lenin.warpstonemod.common.mutations.Mutation;
import com.lenin.warpstonemod.common.mutations.evolving_mutations.EvolvingMutation;
import com.lenin.warpstonemod.common.mutations.evolving_mutations.MutationTree;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EvoElement extends Builder {
    public EvoElement(int _x, int _y, int _width, int _height, WSScreen _parentScreen, EvolvingMutation _mutation) {
        super(_x, _y, _width, _height, _parentScreen);

        List<MutationTree.Node> tree = _mutation.getChildNodes();
        RawTextureResource texture = new RawTextureResource(_mutation.getTexture(), 18, 18, 0, 0);
        MutationTree.Node currentNode = null;
        List<ResourceLocation> legalOptionals = new ArrayList<>();

        if (_mutation.containsInstance(Minecraft.getInstance().player.getUniqueID())) {
            texture = new RawTextureResource(_mutation.getCurrentTexture(), 18, 18, 0, 0);

            currentNode = _mutation.getCurrentNode(Minecraft.getInstance().player.getUniqueID());

            legalOptionals = currentNode.getOptional()
                    .stream()
                    .map(MutationTree.Node::getParent)
                    .filter(parent -> parent.isLegalMutation(MutateHelper.getClientManager()))
                    .map(Mutation::getRegistryName)
                    .collect(Collectors.toList());
        }

        int highestX = 0;
        int highestY = 0;

        for (MutationTree.Node node : tree) {
            int x = node.getX();
            int y = node.getY();

            if (x > highestX) highestX = x;
            if (y > highestY) highestY = y;

            x = (x * 18) + x + 18 + 9;
            y = (y * 18) + y + 9;

            List<MutationTree.Node> allNodes = node.getNext();
            allNodes.addAll(node.getOptional());

            for (MutationTree.Node next : allNodes) {
                int nextX = (next.getX() * 18) + x + 18 + 9;
                int nextY = (next.getY() * 18) + y + 9;

                int diffX = nextX - x;
                int diffY = nextY - y;

                if (diffX != 0) {
                    if (diffY > diffX) {
                        addChild(WSElement.builder(x + 5, nextY + 5, diffX, 4, _parentScreen)
                                .addComponent(ImageComponent.factory(Textures.MUTATION_TREE_LINE_HORIZONTAL)),
                                1
                        );
                    } else {
                        addChild(WSElement.builder(x + 5, y + 5, diffX, 4, _parentScreen)
                                .addComponent(ImageComponent.factory(Textures.MUTATION_TREE_LINE_HORIZONTAL)),
                                1
                        );
                    }
                }

                if (diffY != 0) {
                    if (diffX > diffY) {
                        addChild(WSElement.builder(nextX + 5, y + 5, 4, diffY, _parentScreen)
                                .addComponent(ImageComponent.factory(Textures.MUTATION_TREE_LINE_VERTICAL)),
                                1
                        );
                    } else {
                        addChild(WSElement.builder(x + 5, y + 5, 4, diffY, _parentScreen)
                                .addComponent(ImageComponent.factory(Textures.MUTATION_TREE_LINE_VERTICAL)),
                                1
                        );
                    }
                }
            }

            Builder currentBuilder = new MutationElement(x, y, 18, 18, _parentScreen, node.getParent());
            List<ITextComponent> conditionTooltips = node.getParent().getConditionTooltips();

            if (conditionTooltips.size() > 0)
                currentBuilder.addTooltips(new TranslationTextComponent("generic.required.conditions").appendString(":"))
                        .addTooltips(node.getParent().getConditionTooltips().toArray(new ITextComponent[0]));

            if (currentNode != null && legalOptionals.contains(node.getRegistryKey())) {
                currentBuilder.addComponent(ChooseOptionalComponent.factory(_mutation, currentNode.getParent()));
            }

            addChild(currentBuilder, 2);
        }

        addChild(new TileSheetElement.Builder(x + 18, y, (highestX + 2) * 18, (highestY + 2) * 18, _parentScreen, Textures.SHEET_CONTAINER));

        addComponent(ImageComponent.factory(texture));
    }
}