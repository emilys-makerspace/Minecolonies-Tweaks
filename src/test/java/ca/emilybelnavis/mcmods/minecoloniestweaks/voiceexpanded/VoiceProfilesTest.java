package ca.emilybelnavis.mcmods.minecoloniestweaks.voiceexpanded;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.*;

class VoiceProfilesTest {
    @Test
    void noFixedProfileLimit() {
        List<String> sets = new ArrayList<>();
        for (int i = 0; i < 10000; i++) sets.add("speaker_" + i);
        assertEquals("speaker_9999", VoiceProfiles.select(sets, n -> n - 1));
    }

    @Test
    void emptyPoolFallsBack() {
        assertNull(
                VoiceProfiles.select(
                        List.of(),
                        n -> {
                            throw new AssertionError();
                        }));
    }

    @Test
    void firstAndLastEntriesReachable() {
        assertEquals("fr", VoiceProfiles.select(List.of("fr", "en"), n -> 0));
        assertEquals("en", VoiceProfiles.select(List.of("fr", "en"), n -> n - 1));
    }
}
