package ca.emilybelnavis.mcmods.minecoloniestweaks.voiceexpanded.mixin;

import com.minecolonies.api.colony.ICivilianData;
import com.minecolonies.api.sounds.EventType;
import com.minecolonies.api.util.SoundUtils;

import ca.emilybelnavis.mcmods.minecoloniestweaks.voiceexpanded.VoiceAssignments;
import ca.emilybelnavis.mcmods.minecoloniestweaks.voiceexpanded.VoiceExpanded;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = SoundUtils.class, remap = false)
public abstract class SoundUtilsMixin {
    @ModifyVariable(
            method =
                    "playSoundAtCitizenWith(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lcom/minecolonies/api/sounds/EventType;Lcom/minecolonies/api/colony/ICivilianData;DD)V",
            at = @At("STORE"),
            ordinal = 0,
            require = 1)
    private static SoundEvent expandedVoice(
            SoundEvent original,
            Level level,
            BlockPos pos,
            EventType event,
            ICivilianData citizen,
            double chance,
            double volume) {
        if (citizen == null || citizen.isChild()) return original;
        if (!(level instanceof ServerLevel server)) return original;
        String set =
                VoiceAssignments.get(server).voice(citizen.getUUID(), citizen.isFemale(), server);
        SoundEvent replacement = VoiceExpanded.sound(set, event);
        return replacement == null ? original : replacement;
    }
}
