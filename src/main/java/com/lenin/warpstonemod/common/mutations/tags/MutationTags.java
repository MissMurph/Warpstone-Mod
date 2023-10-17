package com.lenin.warpstonemod.common.mutations.tags;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lenin.warpstonemod.common.Warpstone;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class MutationTags {

    private static final Map<ResourceLocation, MutationTag> tagMap = new HashMap<>();

    /*public static MutationTag registerTag (String key, int weight) {
        MutationTag tag = new MutationTag(key, weight);
        tagMap.put(key, tag);
        return tag;
    }

    public static MutationTag registerTag (String key, int weight, TextFormatting formatting) {
        MutationTag tag = new MutationTag(key, weight, formatting);
        tagMap.put(key, tag);
        return tag;
    }*/

        /*  RARITY  */
    public static final MutationTag COMMON = registerTag(Warpstone.key("common"));
    public static final MutationTag UNCOMMON = registerTag(Warpstone.key("uncommon"));
    public static final MutationTag RARE = registerTag(Warpstone.key("rare"));

        /*  CATEGORY    */
    public static final MutationTag NEGATIVE = registerTag(Warpstone.key("negative"));

        /*  FUNCTION    */
    public static final MutationTag PERMANENT = registerTag(Warpstone.key("permanent"));
    public static final MutationTag CHILD = registerTag(Warpstone.key("child"));

        /*  ATTRIBUTE   */
    //Vanilla attributes need generic at the start to match their naming
    public static final MutationTag MAX_HEALTH = registerTag(Warpstone.key("generic.max_health"));
    public static final MutationTag ATTACK_DAMAGE = registerTag(Warpstone.key("generic.attack_damage"));
    public static final MutationTag MOVEMENT_SPEED = registerTag(Warpstone.key("generic.movement_speed"));
    public static final MutationTag ARMOR = registerTag(Warpstone.key("generic.armor"));
    public static final MutationTag ARMOR_TOUGHNESS = registerTag(Warpstone.key("generic.armor_toughness"));
    public static final MutationTag HARVEST_SPEED = registerTag(Warpstone.key("harvest_speed"));
    public static final MutationTag BUILD_RANGE = registerTag(Warpstone.key("generic.build_range"));



    public static MutationTag registerTag(ResourceLocation key) {
        MutationTag newTag = new MutationTag(key);
        tagMap.put(key, newTag);
        return newTag;
    }

    public static List<MutationTag> getEntries () {
        return new ArrayList<>(tagMap.values());
    }

    public static MutationTag getTag (ResourceLocation key) {
        return tagMap.getOrDefault(key, null);
    }

    public static void loadTagData (JsonObject json) {
        ResourceLocation key = new ResourceLocation(json.get("key").getAsString());

        if (tagMap.containsKey(key)) {
            tagMap.get(key).deserialize(json);
        }
    }

    public static void init() {}
}