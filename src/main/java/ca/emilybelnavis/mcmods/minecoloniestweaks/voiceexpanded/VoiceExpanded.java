package ca.emilybelnavis.mcmods.minecoloniestweaks.voiceexpanded;

import com.google.gson.*;
import com.minecolonies.api.sounds.EventType;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.Supplier;

public final class VoiceExpanded {
    public static final String ID = "voiceexpanded";
    private static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(Registries.SOUND_EVENT, ID);
    private static final Map<String, Supplier<SoundEvent>> EVENTS = new HashMap<>();
    public static final Map<String, String> LABELS = new LinkedHashMap<>();
    public static final Map<String, String> GENDERS = new LinkedHashMap<>();

    public VoiceExpanded(IEventBus bus) {
        Path custom = FMLPaths.CONFIGDIR.get().resolve("voiceexpanded/catalogue.json");
        try (Reader reader =
                Files.exists(custom)
                        ? Files.newBufferedReader(custom)
                        : new InputStreamReader(
                                Objects.requireNonNull(
                                        getClass()
                                                .getResourceAsStream(
                                                        "/voiceexpanded-catalogue.json")),
                                java.nio.charset.StandardCharsets.UTF_8)) {
            JsonArray sets = JsonParser.parseReader(reader).getAsJsonArray();
            for (JsonElement item : sets) {
                JsonObject set = item.getAsJsonObject();
                String id = set.get("id").getAsString();
                String gender = set.get("gender").getAsString();
                if (!id.matches("[a-z0-9_/-]+")
                        || !Set.of("male", "female", "neutral").contains(gender)
                        || GENDERS.putIfAbsent(id, gender) != null)
                    throw new IllegalArgumentException("Invalid or duplicate voice set: " + id);
                LABELS.put(
                        id,
                        set.has("display_label")
                                ? set.get("display_label").getAsString()
                                : (set.has("accent") ? set.get("accent").getAsString() : "Voice")
                                        + " — "
                                        + set.get("language")
                                                .getAsString()
                                                .toUpperCase(java.util.Locale.ROOT));
                for (EventType event : EventType.values()) {
                    String key = id + "." + event.getId();
                    EVENTS.put(
                            key,
                            SOUNDS.register(
                                    key,
                                    () ->
                                            SoundEvent.createVariableRangeEvent(
                                                    ResourceLocation.fromNamespaceAndPath(
                                                            ID, key))));
                }
            }
        } catch (IOException e) {
            throw new java.io.UncheckedIOException("Cannot read voice catalogue", e);
        }
        SOUNDS.register(bus);
        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.addListener(
                VoiceControls::registerCommands);
    }

    public static List<String> eligible(boolean female) {
        return GENDERS.entrySet().stream()
                .filter(
                        e ->
                                e.getValue().equals("neutral")
                                        || e.getValue().equals(female ? "female" : "male"))
                .map(Map.Entry::getKey)
                .toList();
    }

    public static SoundEvent sound(String set, EventType event) {
        Supplier<SoundEvent> holder = EVENTS.get(set + "." + event.getId());
        return holder == null ? null : holder.get();
    }
}
