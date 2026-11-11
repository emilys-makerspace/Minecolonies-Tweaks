package ca.emilybelnavis.mcmods.minecoloniestweaks.voiceexpanded;

import com.minecolonies.api.colony.permissions.Action;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.UuidArgument;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.UUID;

public final class VoiceControls {
    private static final SimpleCommandExceptionType DENIED =
            new SimpleCommandExceptionType(
                    Component.literal(
                            "Stand near an adult colonist you have permission to manage."));

    private VoiceControls() {}

    public static void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher()
                .register(
                        Commands.literal("colonistvoice")
                                .then(
                                        Commands.argument("citizen", UuidArgument.uuid())
                                                .executes(
                                                        c ->
                                                                choose(
                                                                        c.getSource(),
                                                                        UuidArgument.getUuid(
                                                                                c, "citizen"),
                                                                        null))
                                                .then(
                                                        Commands.argument(
                                                                        "voice",
                                                                        StringArgumentType.word())
                                                                .suggests(
                                                                        (c, b) ->
                                                                                SharedSuggestionProvider
                                                                                        .suggest(
                                                                                                java
                                                                                                        .util
                                                                                                        .stream
                                                                                                        .Stream
                                                                                                        .concat(
                                                                                                                VoiceExpanded
                                                                                                                        .LABELS
                                                                                                                        .keySet()
                                                                                                                        .stream(),
                                                                                                                java
                                                                                                                        .util
                                                                                                                        .stream
                                                                                                                        .Stream
                                                                                                                        .of(
                                                                                                                                "random")),
                                                                                                b))
                                                                .executes(
                                                                        c ->
                                                                                choose(
                                                                                        c
                                                                                                .getSource(),
                                                                                        UuidArgument
                                                                                                .getUuid(
                                                                                                        c,
                                                                                                        "citizen"),
                                                                                        StringArgumentType
                                                                                                .getString(
                                                                                                        c,
                                                                                                        "voice"))))));
    }

    private static AbstractEntityCitizen target(ServerPlayer player, UUID uuid)
            throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        if (!(player.serverLevel().getEntity(uuid) instanceof AbstractEntityCitizen citizen)
                || citizen.distanceToSqr(player) > 256
                || citizen.getCitizenData() == null
                || citizen.getCitizenData().isChild()) throw DENIED.create();
        if (!player.hasPermissions(2)
                && !citizen.getCitizenData()
                        .getColony()
                        .getPermissions()
                        .hasPermission(player, Action.MANAGE_HUTS)) throw DENIED.create();
        return citizen;
    }

    private static int choose(CommandSourceStack source, UUID uuid, String set)
            throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        AbstractEntityCitizen citizen = target(player, uuid);
        VoiceAssignments data = VoiceAssignments.get(player.serverLevel());
        UUID identity = citizen.getCitizenData().getUUID();
        if (set != null) {
            if (set.equals("random"))
                set =
                        VoiceProfiles.select(
                                VoiceExpanded.eligible(citizen.isFemale()),
                                player.getRandom()::nextInt);
            if (set == null || !VoiceExpanded.LABELS.containsKey(set))
                throw new SimpleCommandExceptionType(
                                Component.literal("That voice set is unavailable."))
                        .create();
            data.set(identity, set);
            player.sendSystemMessage(
                    Component.literal(
                            citizen.getName().getString() + ": " + VoiceExpanded.LABELS.get(set)));
            return 1;
        }
        String current = data.voice(identity, citizen.isFemale(), player.serverLevel());
        player.sendSystemMessage(
                Component.literal(
                        "Voice for "
                                + citizen.getName().getString()
                                + " — current: "
                                + VoiceExpanded.LABELS.getOrDefault(current, "Original")));
        player.sendSystemMessage(button("[Random voice]", uuid, "random"));
        VoiceExpanded.LABELS.forEach(
                (id, label) -> player.sendSystemMessage(button("[" + label + "]", uuid, id)));
        return 1;
    }

    private static Component button(String label, UUID citizen, String set) {
        return Component.literal(label)
                .withStyle(
                        s ->
                                s.withColor(net.minecraft.ChatFormatting.AQUA)
                                        .withClickEvent(
                                                new ClickEvent(
                                                        ClickEvent.Action.RUN_COMMAND,
                                                        "/colonistvoice " + citizen + " " + set))
                                        .withHoverEvent(
                                                new HoverEvent(
                                                        HoverEvent.Action.SHOW_TEXT,
                                                        Component.literal(
                                                                "Assign this voice and"
                                                                    + " language"))));
    }
}
