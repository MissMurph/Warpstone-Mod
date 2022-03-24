package com.lenin.warpstonemod.common.data.mutations;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MutationData {
    private ResourceLocation resource;
    private String uuid;
    private String resourcePath;
    private final List<String> tags = new ArrayList<>();
    private final List<AttrModifierData> modifiers = new ArrayList<>();

    private MutationData () {}

    public JsonObject serialize () {
        JsonObject out = new JsonObject();

        out.addProperty("key", resource.toString());
        out.addProperty("uuid", uuid);
        out.addProperty("resource_path", resourcePath);

        JsonArray jsonTags = new JsonArray();

        for (String tag : tags) {
            jsonTags.add(tag);
        }

        out.add("tags", jsonTags);

        JsonArray jsonMods = new JsonArray();

        for (AttrModifierData modifier : modifiers) {
            jsonMods.add(modifier.serialize(this));
        }

        out.add("modifiers", jsonMods);

        return out;
    }

    public String getPath () {
        return resource.getPath();
    }

    public static class Builder {
        private final MutationData data;

        public Builder(ResourceLocation _resource, String _uuid) {
            data = new MutationData();

            data.resource = _resource;
            data.uuid = _uuid;
            data.resourcePath = "textures/gui/effect_mutations/" + _resource.getPath() + ".png";
        }

        public Builder addResourcePath (String _path) {
            data.resourcePath = _path;
            return this;
        }

        public Builder addTag (ResourceLocation tag) {
            data.tags.add(tag.toString());
            return this;
        }

        public Builder addModifier (ResourceLocation target, double value, String operation) {
            return addModifier(target, data.resource.getPath(), data.uuid, value, operation);
        }

            //If you have to define a unique name you also need a new UUID to match
        public Builder addModifier (ResourceLocation target, String name, String uuid, double value, String operation) {
            data.modifiers.add(new AttrModifierData(target.toString(), name, uuid, value, operation));
            return this;
        }

        public MutationData create () {
            return data;
        }
    }

    private static class AttrModifierData {
        private final String target;
        private final String name;
        private final String uuid;
        private final double value;
        private final String operation;

        private AttrModifierData (String _target, String _name, String _uuid, double _value, String _operation) {
            target = _target;
            name = _name;
            uuid = _uuid;
            value = _value;
            operation = _operation;
        }

        private JsonObject serialize (MutationData data) {
            JsonObject out = new JsonObject();

            out.addProperty("target", target);
            if (!Objects.equals(data.resource.getPath(), this.name)) out.addProperty("name", name);
            if (!Objects.equals(data.uuid, this.uuid)) out.addProperty("UUID", uuid);
            out.addProperty("value", value);
            out.addProperty("operation", operation);

            return out;
        }
    }
}