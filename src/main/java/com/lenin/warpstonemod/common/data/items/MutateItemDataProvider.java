package com.lenin.warpstonemod.common.data.items;

import com.lenin.warpstonemod.common.data.WarpstoneDataProvider;
import com.lenin.warpstonemod.common.data.mutations.MutationData;
import com.lenin.warpstonemod.common.items.WSItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MutateItemDataProvider extends WarpstoneDataProvider {
    private static final List<MutateItemData> data = new ArrayList<>();

    public MutateItemDataProvider (DataGenerator _generator) {
        super(_generator, "mutate_items");
        buildItemData();
    }

    @Override
    public void run(DirectoryCache cache) throws IOException {
        for (MutateItemData item : data) {
            IDataProvider.save(GSON, cache, item.serialize(), this.generator.getOutputFolder().resolve("data/warpstonemod/mutate_items/" + item.getKey() + ".json"));
        }
    }

    private void buildItemData () {
        data.add(new MutateItemData.Builder(WSItems.WARPSTONE_DUST.getRegistryName(), 20, 20, 20).build());
    }
}