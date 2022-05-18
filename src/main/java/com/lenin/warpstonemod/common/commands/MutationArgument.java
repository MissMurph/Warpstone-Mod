package com.lenin.warpstonemod.common.commands;

import com.lenin.warpstonemod.common.Registration;
import com.lenin.warpstonemod.common.mutations.Mutation;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class MutationArgument implements ArgumentType<Mutation> {

    public static final SimpleCommandExceptionType MUTATION_NOT_FOUND = new SimpleCommandExceptionType(new TranslationTextComponent("warpstonemod.commmand.mutation.not_found"));

    @Override
    public Mutation parse(StringReader reader) throws CommandSyntaxException {
        ResourceLocation key = ResourceLocation.read(reader);

        if (Registration.MUTATIONS.containsKey(key)) {
            return Registration.MUTATIONS.getValue(key);
        }
        throw MUTATION_NOT_FOUND.createWithContext(reader);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return ISuggestionProvider.suggestIterable(
                Registration.MUTATIONS.getValues().stream()
                        .map(Mutation::getRegistryName)
                        .collect(Collectors.toList()),
                builder);
    }

    @Override
    public Collection<String> getExamples() {
        return ArgumentType.super.getExamples();
    }
}