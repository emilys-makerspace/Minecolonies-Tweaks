package ca.emilybelnavis.mcmods.minecoloniestweaks.voiceexpanded;

import static org.junit.jupiter.api.Assertions.*;

import net.minecraft.nbt.CompoundTag;

import org.junit.jupiter.api.Test;

import java.util.*;

class VoiceAssignmentsTest {
    @Test
    void survivesSaveReloadAndCatalogueChanges() {
        UUID id = UUID.randomUUID();
        VoiceAssignments data = new VoiceAssignments();
        assertEquals("julie_fr", data.assign(id, List.of("julie_fr", "julie_en"), n -> 0));
        CompoundTag tag = data.save(new CompoundTag(), null);
        VoiceAssignments restored = VoiceAssignments.load(tag, null);
        assertEquals(
                "julie_fr",
                restored.assign(
                        id,
                        List.of("new_voice_en", "julie_en", "julie_fr"),
                        n -> {
                            throw new AssertionError("must not reroll");
                        }));
        assertEquals(
                "julie_fr",
                restored.assign(
                        id,
                        List.of(),
                        n -> {
                            throw new AssertionError();
                        }));
    }

    @Test
    void manualChoiceOverridesAndPersists() {
        UUID id = UUID.randomUUID();
        VoiceAssignments d = new VoiceAssignments();
        d.assign(id, List.of("julie_fr"), n -> 0);
        d.set(id, "julie_en");
        VoiceAssignments restored = VoiceAssignments.load(d.save(new CompoundTag(), null), null);
        assertEquals(
                "julie_en",
                restored.assign(
                        id,
                        List.of("julie_fr"),
                        n -> {
                            throw new AssertionError();
                        }));
    }

    @Test
    void noEligibleVoicesDoesNotPersistNullAssignment() {
        UUID id = UUID.randomUUID();
        VoiceAssignments data = new VoiceAssignments();
        assertNull(data.assign(id, List.of(), n -> 0));
        assertEquals("later_fr", data.assign(id, List.of("later_fr"), n -> 0));
    }
}
