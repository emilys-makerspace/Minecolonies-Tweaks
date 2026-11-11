package ca.emilybelnavis.mcmods.minecoloniestweaks.voiceexpanded;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.*;

public final class VoiceAssignments extends SavedData {
    private final Map<UUID, String> assignments = new HashMap<>();
    private static final Factory<VoiceAssignments> FACTORY =
            new Factory<>(VoiceAssignments::new, VoiceAssignments::load, null);

    public static VoiceAssignments get(ServerLevel level) {
        return level.getServer()
                .overworld()
                .getDataStorage()
                .computeIfAbsent(FACTORY, "voiceexpanded_assignments");
    }

    static VoiceAssignments load(CompoundTag tag, HolderLookup.Provider lookup) {
        VoiceAssignments data = new VoiceAssignments();
        CompoundTag entries = tag.getCompound("assignments");
        for (String key : entries.getAllKeys()) {
            try {
                data.assignments.put(UUID.fromString(key), entries.getString(key));
            } catch (IllegalArgumentException ignored) {
            }
        }
        return data;
    }

    public String voice(UUID citizen, boolean female, ServerLevel level) {
        return assign(citizen, VoiceExpanded.eligible(female), level.random::nextInt);
    }

    String assign(UUID citizen, List<String> eligible, java.util.function.IntUnaryOperator random) {
        String existing = assignments.get(citizen);
        if (existing != null) return existing;
        String selected = VoiceProfiles.select(eligible, random);
        if (selected != null) {
            assignments.put(citizen, selected);
            setDirty();
        }
        return selected;
    }

    public void set(UUID citizen, String voice) {
        assignments.put(citizen, voice);
        setDirty();
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider lookup) {
        CompoundTag entries = new CompoundTag();
        assignments.forEach((id, voice) -> entries.putString(id.toString(), voice));
        tag.put("assignments", entries);
        return tag;
    }
}
