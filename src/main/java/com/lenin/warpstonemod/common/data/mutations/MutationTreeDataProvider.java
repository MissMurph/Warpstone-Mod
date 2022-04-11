package com.lenin.warpstonemod.common.data.mutations;

import com.lenin.warpstonemod.common.Warpstone;
import com.lenin.warpstonemod.common.data.WarpstoneDataProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MutationTreeDataProvider extends WarpstoneDataProvider {
    private static final List<MutationTreeData> data = new ArrayList<>();

    public MutationTreeDataProvider(DataGenerator _generator) {
        super(_generator, "mutation_trees");
        buildTrees();
    }

    @Override
    public void act(DirectoryCache cache) throws IOException {
        for (MutationTreeData tree : data) {
            IDataProvider.save(GSON, cache, tree.serialize(), this.generator.getOutputFolder().resolve("data/warpstonemod/mutations/trees/" + tree.getKey().getPath() + ".json"));
        }
    }

    private void buildTrees () {
        data.add(new MutationTreeData.Builder(Warpstone.key("curse_ninja"),
                newNode(Warpstone.key("curse_ninja_child_1"), 1, 0)
                        .addNext(Warpstone.key("curse_ninja_child_2")))
                .addNode(newNode(Warpstone.key("curse_ninja_child_2"), 1, 2))
                .create()
        );
    }

    private MutationTreeData.NodeData.Builder newNode (ResourceLocation _nodeKey, int _x, int _y) {
        return new MutationTreeData.NodeData.Builder(_nodeKey, _x, _y);
    }
}