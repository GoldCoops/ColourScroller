package net.anware.tmc.colourscroller.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Consumer;

public class ItemPickerScreen extends Screen {

    private static final int CELL_SIZE = 20;
    private static final int GRID_TOP = 60;
    private static final int GRID_BOTTOM_MARGIN = 32;

    private static List<Item> ALL_ITEMS_CACHE;

    private final Screen parent;
    private final Consumer<Item> onSelect;
    private final Set<String> excludedItemIds;

    private final List<Item> filtered = new ArrayList<>();
    private TextFieldWidget searchField;
    private double scrollOffset = 0.0;
    private int gridLeft, gridRight, gridBottom, columns;

    public ItemPickerScreen(Screen parent, Set<String> excludedItemIds, Consumer<Item> onSelect) {
        super(Text.literal("Pick an Item"));
        this.parent = parent;
        this.onSelect = onSelect;
        this.excludedItemIds = excludedItemIds == null ? Collections.emptySet() : excludedItemIds;
    }

    public ItemPickerScreen(Screen parent, Consumer<Item> onSelect) {
        this(parent, Collections.emptySet(), onSelect);
    }

    private static List<Item> getAllItems() {
        if (ALL_ITEMS_CACHE == null) {
            List<Item> list = new ArrayList<>();
            for (Item item : Registries.ITEM) {
                if (item == Items.AIR) continue;
                list.add(item);
            }
            list.sort((a, b) -> Registries.ITEM.getId(a).toString().compareTo(Registries.ITEM.getId(b).toString()));
            ALL_ITEMS_CACHE = list;
        }
        return ALL_ITEMS_CACHE;
    }

    @Override
    protected void init() {
        gridLeft = 20;
        gridRight = this.width - 20;
        gridBottom = this.height - GRID_BOTTOM_MARGIN;
        columns = Math.max(1, (gridRight - gridLeft) / CELL_SIZE);

        searchField = new TextFieldWidget(this.textRenderer, this.width / 2 - 150, 32, 240, 20, Text.literal("Search"));
        searchField.setPlaceholder(Text.literal("Search items..."));
        searchField.setChangedListener(s -> {
            updateFilter();
            scrollOffset = 0;
        });
        this.addDrawableChild(searchField);
        this.setInitialFocus(searchField);

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Cancel"), btn ->
                MinecraftClient.getInstance().setScreen(parent)
        ).dimensions(this.width / 2 + 96, 32, 54, 20).build());

        updateFilter();
    }

    private void updateFilter() {
        filtered.clear();
        String q = searchField == null ? "" : searchField.getText().toLowerCase(Locale.ROOT).trim();
        for (Item item : getAllItems()) {
            String idPath = Registries.ITEM.getId(item).getPath();
            if (excludedItemIds.contains(idPath)) continue;
            if (q.isEmpty()) {
                filtered.add(item);
                continue;
            }
            String id = Registries.ITEM.getId(item).toString();
            if (id.toLowerCase(Locale.ROOT).contains(q)) {
                filtered.add(item);
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 12, 0xFFFFFFFF);
        String hint = excludedItemIds.isEmpty()
                ? filtered.size() + " matches"
                : filtered.size() + " matches  (" + excludedItemIds.size() + " hidden — already in a set)";
        context.drawTextWithShadow(this.textRenderer, Text.literal(hint), 20, 16, 0xFFAAAAAA);

        context.enableScissor(gridLeft, GRID_TOP, gridRight, gridBottom);

        int topY = GRID_TOP - (int) scrollOffset;
        Integer hoveredIdx = null;
        for (int i = 0; i < filtered.size(); i++) {
            int col = i % columns;
            int row = i / columns;
            int cx = gridLeft + col * CELL_SIZE;
            int cy = topY + row * CELL_SIZE;
            if (cy + CELL_SIZE < GRID_TOP) continue;
            if (cy > gridBottom) break;

            boolean hovered = mouseX >= cx && mouseX < cx + CELL_SIZE && mouseY >= cy && mouseY < cy + CELL_SIZE;
            context.fill(cx, cy, cx + CELL_SIZE, cy + CELL_SIZE, hovered ? 0xFF606060 : 0xFF2A2A2A);
            try {
                context.drawItem(new ItemStack(filtered.get(i)), cx + 2, cy + 2);
                if (hovered) hoveredIdx = i;
            } catch (Exception ignored) {}
        }
        context.disableScissor();

        if (hoveredIdx != null) {
            try {
                context.drawItemTooltip(this.textRenderer, new ItemStack(filtered.get(hoveredIdx)), mouseX, mouseY);
            } catch (Exception ignored) {}
        }
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubleClicked) {
        if (super.mouseClicked(click, doubleClicked)) return true;
        double mouseX = click.x();
        double mouseY = click.y();
        if (mouseX < gridLeft || mouseX >= gridLeft + columns * CELL_SIZE) return false;
        if (mouseY < GRID_TOP || mouseY > gridBottom) return false;

        int topY = GRID_TOP - (int) scrollOffset;
        int col = (int) ((mouseX - gridLeft) / CELL_SIZE);
        int row = (int) ((mouseY - topY) / CELL_SIZE);
        int idx = row * columns + col;
        if (idx >= 0 && idx < filtered.size()) {
            onSelect.accept(filtered.get(idx));
            boolean shiftHeld = (click.modifiers() & GLFW.GLFW_MOD_SHIFT) != 0;
            if (!shiftHeld) {
                MinecraftClient.getInstance().setScreen(parent);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (mouseY >= GRID_TOP && mouseY <= gridBottom) {
            scrollOffset -= verticalAmount * CELL_SIZE * 2;
            int rows = (filtered.size() + columns - 1) / columns;
            int contentHeight = rows * CELL_SIZE;
            int viewHeight = Math.max(0, gridBottom - GRID_TOP);
            int maxOffset = Math.max(0, contentHeight - viewHeight);
            if (scrollOffset < 0) scrollOffset = 0;
            if (scrollOffset > maxOffset) scrollOffset = maxOffset;
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
