package ca.emilybelnavis.mcmods.minecoloniestweaks.voiceexpanded;

import java.util.List;
import java.util.function.IntUnaryOperator;

public final class VoiceProfiles {
    private VoiceProfiles() {}

    public static String select(List<String> pool, IntUnaryOperator random) {
        if (pool.isEmpty()) return null;
        return pool.get(random.applyAsInt(pool.size()));
    }
}
