package net.anware.tmc.colourscroller.gui;

import net.anware.tmc.colourscroller.ConfigurationHandler;
import net.anware.tmc.colourscroller.ScrollableHelper;
import net.anware.tmc.colourscroller.ScrollableHelper.ColouredEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ScrollableEditorScreen extends Screen {

    private static final int LIST_TOP = 56;
    private static final int LIST_BOTTOM_MARGIN = 36;
    private static final int ROW_HEIGHT = 24;

    public final List<List<ColouredEntry>> workingCopy = new ArrayList<>();

    private double scrollOffset = 0.0;
    private int listBottom;

    public ScrollableEditorScreen() {
        super(Text.literal("Colour Scroller - Edit Sets"));
        loadFromCurrentState();
    }

    private void loadFromCurrentState() {
        workingCopy.clear();
        for (List<ColouredEntry> set : ScrollableHelper.SCROLLABLE_SETS) {
            workingCopy.add(new ArrayList<>(set));
        }
    }

    @Override
    protected void init() {
        listBottom = this.height - LIST_BOTTOM_MARGIN;

        this.addDrawableChild(ButtonWidget.builder(Text.literal("+ New Set"), btn -> {
            workingCopy.add(new ArrayList<>());
            MinecraftClient.getInstance().setScreen(new SetEditScreen(this, workingCopy.size() - 1));
        }).dimensions(this.width - 110, 8, 100, 20).build());

        CheckboxWidget syncToggle = CheckboxWidget.builder(
                        Text.literal("synchronization toggle"), this.textRenderer)
                .pos(10, 30)
                .checked(ConfigurationHandler.SYNC_ENABLED)
                .tooltip(Tooltip.of(Text.literal(
                        "When ON: every scrollable item in the hotbar (regardless of list name) "
                                + "snaps back to its first entry whenever the longest list wraps. "
                                + "Smaller lists may repeat several times before the longest "
                                + "finishes. When OFF: each list scrolls independently with no "
                                + "synchronisation. Only affects Scroll Row.")))
                .callback((cb, checked) -> {
                    ConfigurationHandler.SYNC_ENABLED = checked;
                    ConfigurationHandler.saveSettings();
                })
                .build();
        this.addDrawableChild(syncToggle);

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Save & Apply"), btn -> {
            ScrollableHelper.replaceAllSets(workingCopy);
            ConfigurationHandler.saveSetsToJson();
            this.close();
        }).dimensions(this.width / 2 - 202, this.height - 26, 100, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Reload File"), btn -> {
            ConfigurationHandler.loadSetsFromJson();
            loadFromCurrentState();
            scrollOffset = 0;
        }).dimensions(this.width / 2 - 102, this.height - 26, 100, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Reload Default Sets"), btn -> {
            ScrollableHelper.reloadDefaultSets();
            loadFromCurrentState();
            scrollOffset = 0;
        }).dimensions(this.width / 2 - 2, this.height - 26, 100, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Cancel"), btn -> this.close())
                .dimensions(this.width / 2 + 98, this.height - 26, 100, 20).build());

        clampScroll();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 12, 0xFFFFFFFF);
        context.drawTextWithShadow(this.textRenderer, Text.literal(workingCopy.size() + " sets"), 10, 14, 0xFFAAAAAA);

        context.enableScissor(0, LIST_TOP, this.width, listBottom);
        int rowX = 20;
        int rowW = this.width - 40;
        int top = LIST_TOP - (int) scrollOffset;

        for (int i = 0; i < workingCopy.size(); i++) {
            int y = top + i * ROW_HEIGHT;
            if (y + ROW_HEIGHT < LIST_TOP) continue;
            if (y > listBottom) break;
            renderRow(context, i, rowX, y, rowW, mouseX, mouseY);
        }
        context.disableScissor();

        if (workingCopy.isEmpty()) {
            context.drawCenteredTextWithShadow(this.textRenderer,
                    Text.literal("No sets defined. Click '+ New Set' to create one."),
                    this.width / 2, this.height / 2, 0xFF888888);
        }
    }

    private void renderRow(DrawContext context, int index, int x, int y, int w, int mouseX, int mouseY) {
        List<ColouredEntry> set = workingCopy.get(index);

        boolean rowHovered = mouseX >= x && mouseX < x + w && mouseY >= y && mouseY < y + ROW_HEIGHT - 2;
        context.fill(x, y, x + w, y + ROW_HEIGHT - 2, rowHovered ? 0xFF505050 : 0xFF2A2A2A);
        drawBorder(context, x, y, w, ROW_HEIGHT - 2, 0xFF000000);

        if (!set.isEmpty()) {
            try {
                Item first = set.get(0).item().get();
                context.drawItem(new ItemStack(first), x + 4, y + 2);
            } catch (Exception ignored) {}
        }

        String typeLabel = set.isEmpty() ? "(empty set)" : set.get(0).type();
        context.drawTextWithShadow(this.textRenderer, typeLabel, x + 26, y + 7, 0xFFFFFFFF);
        String countLabel = set.size() + " items";
        int typeWidth = this.textRenderer.getWidth(typeLabel);
        context.drawTextWithShadow(this.textRenderer, countLabel, x + 26 + typeWidth + 8, y + 7, 0xFFAAAAAA);

        int previewStart = x + 26 + typeWidth + 8 + this.textRenderer.getWidth(countLabel) + 12;
        int previewLimit = x + w - 110;
        for (int i = 1; i < set.size(); i++) {
            int px = previewStart + (i - 1) * 18;
            if (px + 16 > previewLimit) {
                context.drawTextWithShadow(this.textRenderer, Text.literal("..."), px, y + 7, 0xFFAAAAAA);
                break;
            }
            try {
                Item item = set.get(i).item().get();
                context.drawItem(new ItemStack(item), px, y + 2);
            } catch (Exception ignored) {}
        }

        drawMiniButton(context, x + w - 102, y + 2, 48, 18, "Edit", mouseX, mouseY);
        drawMiniButton(context, x + w - 52, y + 2, 48, 18, "Delete", mouseX, mouseY);
    }

    private void drawMiniButton(DrawContext context, int x, int y, int w, int h, String label, int mouseX, int mouseY) {
        boolean hover = mouseX >= x && mouseX < x + w && mouseY >= y && mouseY < y + h;
        context.fill(x, y, x + w, y + h, hover ? 0xFF707070 : 0xFF404040);
        drawBorder(context, x, y, w, h, 0xFF000000);
        int tw = this.textRenderer.getWidth(label);
        context.drawTextWithShadow(this.textRenderer, label, x + (w - tw) / 2, y + 5, 0xFFFFFFFF);
    }

    private static void drawBorder(DrawContext context, int x, int y, int w, int h, int color) {
        context.fill(x, y, x + w, y + 1, color);
        context.fill(x, y + h - 1, x + w, y + h, color);
        context.fill(x, y + 1, x + 1, y + h - 1, color);
        context.fill(x + w - 1, y + 1, x + w, y + h - 1, color);
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubleClicked) {
        if (super.mouseClicked(click, doubleClicked)) return true;
        double mouseX = click.x();
        double mouseY = click.y();
        int button = click.button();
        if (mouseY < LIST_TOP || mouseY > listBottom) return false;

        int rowX = 20;
        int rowW = this.width - 40;
        int top = LIST_TOP - (int) scrollOffset;

        for (int i = 0; i < workingCopy.size(); i++) {
            int y = top + i * ROW_HEIGHT;
            if (mouseX < rowX || mouseX >= rowX + rowW) continue;
            if (mouseY < y || mouseY >= y + ROW_HEIGHT - 2) continue;

            int editX = rowX + rowW - 102;
            int delX = rowX + rowW - 52;
            if (mouseX >= editX && mouseX < editX + 48 && mouseY >= y + 2 && mouseY < y + 20) {
                MinecraftClient.getInstance().setScreen(new SetEditScreen(this, i));
                return true;
            }
            if (mouseX >= delX && mouseX < delX + 48 && mouseY >= y + 2 && mouseY < y + 20) {
                workingCopy.remove(i);
                clampScroll();
                return true;
            }
            if (button == 0) {
                MinecraftClient.getInstance().setScreen(new SetEditScreen(this, i));
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (mouseY >= LIST_TOP && mouseY <= listBottom) {
            scrollOffset -= verticalAmount * ROW_HEIGHT;
            clampScroll();
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    private void clampScroll() {
        int contentHeight = workingCopy.size() * ROW_HEIGHT;
        int viewHeight = Math.max(0, listBottom - LIST_TOP);
        int maxOffset = Math.max(0, contentHeight - viewHeight);
        if (scrollOffset < 0) scrollOffset = 0;
        if (scrollOffset > maxOffset) scrollOffset = maxOffset;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
