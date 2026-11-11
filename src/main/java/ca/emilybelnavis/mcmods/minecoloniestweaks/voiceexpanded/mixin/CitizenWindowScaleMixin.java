package ca.emilybelnavis.mcmods.minecoloniestweaks.voiceexpanded.mixin;

import com.ldtteam.blockui.views.BOWindow;
import com.minecolonies.core.client.gui.citizen.AbstractWindowCitizen;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BOWindow.class, remap = false)
public abstract class CitizenWindowScaleMixin {
    @Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
    private void fitCitizenWindow(CallbackInfoReturnable<BOWindow.WindowRenderType> ci) {
        if ((Object) this instanceof AbstractWindowCitizen)
            ci.setReturnValue(BOWindow.WindowRenderType.FULLSCREEN_VANILLA);
    }
}
