package ca.emilybelnavis.mcmods.minecoloniestweaks.voiceexpanded.mixin;

import com.ldtteam.blockui.controls.ButtonImage;
import com.ldtteam.blockui.views.BOWindow;
import com.minecolonies.api.colony.ICitizenDataView;
import com.minecolonies.core.client.gui.citizen.AbstractWindowCitizen;

import ca.emilybelnavis.mcmods.minecoloniestweaks.voiceexpanded.client.VoicePicker;

import net.minecraft.resources.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AbstractWindowCitizen.class, remap = false)
public abstract class CitizenWindowMixin {
    @Inject(method = "<init>", at = @At("RETURN"))
    private void addVoiceButton(
            ICitizenDataView citizen, ResourceLocation resource, CallbackInfo ci) {
        if (citizen.isChild()) return;
        BOWindow window = (BOWindow) (Object) this;
        // Match MineColonies' 32x26 tabs; leave eight pixels below the final tab.
        for (String name :
                new String[] {
                    "main", "request", "inventory", "happiness", "family", "job", "debug"
                }) {
            for (String suffix : new String[] {"Tab", "Icon"}) {
                var pane = window.findPaneByID(name + suffix);
                if (pane != null) pane.setPosition(pane.getX(), pane.getY() - 12);
            }
        }
        ButtonImage tab = new ButtonImage();
        tab.setImage(
                ResourceLocation.fromNamespaceAndPath(
                        "minecolonies", "textures/gui/modules/tab_left_side1.png"));
        tab.setID("voiceexpanded_choose");
        tab.setSize(32, 26);
        tab.setPosition(0, 210);
        tab.setHandler(b -> VoicePicker.open(citizen));
        window.addChild(tab, 0);
        ButtonImage icon = new ButtonImage();
        icon.setImage(
                ResourceLocation.fromNamespaceAndPath("voiceexpanded", "textures/gui/voice.png"));
        icon.setID("voiceexpanded_icon");
        icon.setSize(20, 20);
        icon.setPosition(5, 213);
        icon.setHandler(b -> VoicePicker.open(citizen));
        window.addChild(icon);
    }
}
