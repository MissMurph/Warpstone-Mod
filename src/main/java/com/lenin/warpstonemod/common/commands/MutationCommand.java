package com.lenin.warpstonemod.common.commands;

import com.lenin.warpstonemod.common.Registration;
import com.lenin.warpstonemod.common.mutations.MutateHelper;
import com.lenin.warpstonemod.common.PlayerManager;
import com.lenin.warpstonemod.common.mutations.Mutation;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;

public class MutationCommand {
    public static ArgumentBuilder<CommandSource, ?> register () {
        return Commands.literal("mutation").requires(source -> source.hasPermission(2))
                .then(Commands.literal("add")
                        .then(Commands.argument("mutation", new MutationArgument())
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(context -> {
                                            PlayerEntity player = EntityArgument.getPlayer(context, "player");
                                            Mutation mutation = context.getArgument("mutation", Mutation.class);
                                            return addMutation(context.getSource(), player, mutation);
                                        }))
                                .executes(context -> {
                                    Mutation mutation = context.getArgument("mutation", Mutation.class);
                                    return addMutation(context.getSource(), null, mutation);
                                })))
                .then(Commands.literal("remove")
                        .then(Commands.argument("mutation", new MutationArgument())
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(context -> {
                                            PlayerEntity player = EntityArgument.getPlayer(context, "player");
                                            Mutation mutation = context.getArgument("mutation", Mutation.class);
                                            return removeMutation(context.getSource(), player, mutation);
                                        }))
                                .executes(context -> {
                                    Mutation mutation = context.getArgument("mutation", Mutation.class);
                                    return removeMutation(context.getSource(), null, mutation);
                                })));
    }

    private static int addMutation (CommandSource source, @Nullable PlayerEntity target, Mutation mutation) throws CommandSyntaxException {
        PlayerEntity player = target != null ? target : source.getPlayerOrException();
        PlayerManager manager = MutateHelper.getManager(player);

        if (manager.containsMutation(mutation)) {
            player.sendMessage(new StringTextComponent("Error: ")
                    .append(player.getDisplayName())
                    .append(" Already Has ")
                    .append(mutation.getMutationName())
                    .withStyle(TextFormatting.RED),
                    Util.NIL_UUID
            );

            return 0;
        }

        if (!Registration.MUTATIONS.containsValue(mutation)) {
            player.sendMessage(new StringTextComponent("Error: ")
                    .append(mutation.getMutationName())
                    .append(" Not Found ")
                    .withStyle(TextFormatting.RED),
                    Util.NIL_UUID
            );

            return 0;
        }

        manager.addMutationCommand(mutation);
        return 0;
    }

    private static int removeMutation (CommandSource source, @Nullable PlayerEntity target, Mutation mutation) throws CommandSyntaxException {
        PlayerEntity player = target != null ? target : source.getPlayerOrException();
        PlayerManager manager = MutateHelper.getManager(player);

        if (!manager.containsMutation(mutation)) {
            player.sendMessage(new StringTextComponent("Error: ")
                            .append(player.getDisplayName())
                            .append(" Doesn't have ")
                            .append(mutation.getMutationName())
                            .withStyle(TextFormatting.RED),
                    Util.NIL_UUID
            );

            return 0;
        }

        manager.removeMutationCommand(mutation);
        return 0;
    }
}