package ca.emilybelnavis.mcmods.minecoloniestweaks.voiceexpanded.client;

import com.minecolonies.api.colony.ICitizenDataView;
import ca.emilybelnavis.mcmods.minecoloniestweaks.voiceexpanded.VoiceExpanded;
import ca.emilybelnavis.mcmods.minecoloniestweaks.client.citizen.Gui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** Vanilla controls and container panel, with the existing server-authorized voice command. */
public final class VoicePicker extends Screen
{
    private final Screen parent;
    private final UUID entity;
    private final String citizenName;
    private final List<Map.Entry<String, String>> entries = new ArrayList<>(VoiceExpanded.LABELS.entrySet());
    private int page;
    private int pageSize;
    private int left;
    private int top;
    private int panelWidth;
    private int panelHeight;

    private VoicePicker(Screen parent, UUID entity, String citizenName)
    {
        super(Component.translatable("minecoloniestweaks.citizen.voice"));
        this.parent = parent;
        this.entity = entity;
        this.citizenName = citizenName;
    }

    public static void open(ICitizenDataView citizen)
    {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        var entity = mc.level.getEntity(citizen.getEntityId());
        if (entity != null) mc.setScreen(new VoicePicker(mc.screen, entity.getUUID(), citizen.getName()));
    }

    @Override
    protected void init()
    {
        panelWidth = Math.min(340, width - 16);
        pageSize = Math.max(1, Math.min(6, (height - 120) / 24));
        panelHeight = 108 + pageSize * 24;
        left = (width - panelWidth) / 2;
        top = (height - panelHeight) / 2;
        page = Math.max(0, Math.min(page, pages() - 1));
        addRenderableWidget(Button.builder(Component.translatable("minecoloniestweaks.voice.random"), b -> select("random"))
          .bounds(left + 8, top + 34, panelWidth - 16, 20).build());
        for (int row = 0; row < pageSize; row++)
        {
            int index = page * pageSize + row;
            if (index >= entries.size()) break;
            var entry = entries.get(index);
            Component label = Component.literal(entry.getValue());
            addRenderableWidget(Button.builder(Component.literal(font.plainSubstrByWidth(entry.getValue(), panelWidth - 32)),
              b -> select(entry.getKey())).bounds(left + 8, top + 60 + row * 24, panelWidth - 16, 20)
              .tooltip(Tooltip.create(label)).build());
        }
        int footer = top + 64 + pageSize * 24;
        Button previous = addRenderableWidget(Button.builder(Component.literal("<"), b -> changePage(-1))
          .bounds(left + 8, footer, 24, 20).build());
        previous.setTooltip(Tooltip.create(Component.translatable("minecoloniestweaks.voice.previous")));
        previous.active = page > 0;
        Button next = addRenderableWidget(Button.builder(Component.literal(">"), b -> changePage(1))
          .bounds(left + panelWidth - 32, footer, 24, 20).build());
        next.setTooltip(Tooltip.create(Component.translatable("minecoloniestweaks.voice.next")));
        next.active = page + 1 < pages();
        addRenderableWidget(Button.builder(Component.translatable("gui.back"), b -> onClose())
          .bounds(left + panelWidth / 2 - 45, footer + 0, 90, 20).build());
    }

    private int pages()
    {
        return Math.max(1, (entries.size() + pageSize - 1) / pageSize);
    }

    private void changePage(int direction)
    {
        page = Math.max(0, Math.min(page + direction, pages() - 1));
        rebuildWidgets();
    }

    private void select(String set)
    {
        if (minecraft != null && minecraft.player != null)
            minecraft.player.connection.sendCommand("colonistvoice " + entity + " " + set);
        onClose();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick)
    {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        Gui.panel(graphics, left, top, panelWidth, panelHeight);
        graphics.drawString(font, title, left + 8, top + 8, Gui.LABEL, false);
        graphics.drawString(font, font.plainSubstrByWidth(citizenName, panelWidth - 16), left + 8, top + 21, Gui.LABEL_SOFT, false);
        Component count = Component.literal((page + 1) + " / " + pages());
        graphics.drawString(font, count, left + panelWidth - 8 - font.width(count), top + 8, Gui.LABEL_SOFT, false);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose()
    {
        if (minecraft != null) minecraft.setScreen(parent);
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }
}
