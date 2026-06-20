package net.anware.tmc.colourscroller.gui;

import net.anware.tmc.colourscroller.ScrollableHelper;
import net.anware.tmc.colourscroller.ScrollableHelper.ColouredEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SetEditScreen extends Screen {

    private static final int CELL_SIZE = 20;
    private static final int GRID_TOP = 72;
    private static final int GRID_BOTTOM_MARGIN = 64;

    private final ScrollableEditorScreen parent;
    private final int setIndex;

    private TextFieldWidget typeField;
    private int selectedItemIndex = -1;
    private double scrollOffset = 0.0;
    private int gridLeft, gridRight, gridBottom, columns;

    public SetEditScreen(ScrollableEditorScreen parent, int setIndex) {
        super(Text.literal("Edit Set"));
        this.parent = parent;
        this.setIndex = setIndex;
    }

    private List<ColouredEntry> set() {
        return parent.workingCopy.get(setIndex);
    }

    @Override
    protected void init() {
        gridLeft = 20;
        gridRight = this.width - 20;
        gridBottom = this.height - GRID_BOTTOM_MARGIN;
        columns = Math.max(1, (gridRight - gridLeft) / CELL_SIZE);

        String currentType = set().isEmpty() ? "custom" : set().get(0).type();
        typeField = new TextFieldWidget(this.textRenderer, 70, 36, 180, 20, Text.literal("Type"));
        typeField.setText(currentType);
        typeField.setChangedListener(this::onTypeChanged);
        this.addDrawableChild(typeField);

        this.addDrawableChild(ButtonWidget.builder(Text.literal("+ Add Item"), btn ->
                MinecraftClient.getInstance().setScreen(new ItemPickerScreen(this, collectUsedItemIds(), item -> {
                    String type = typeField.getText().isBlank() ? "custom" : typeField.getText();
                    String idPath = net.minecraft.registry.Registries.ITEM.getId(item).getPath();
                    // Defensive: refuse to add if it somehow slipped past the picker filter.
                    for (List<ColouredEntry> s : parent.workingCopy) {
                        for (ColouredEntry e : s) {
                            if (e.id().equals(idPath)) return;
                        }
                    }
                    set().add(ScrollableHelper.entryFor(type, item));
                }))
        ).dimensions(260, 36, 100, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Remove"), btn -> {
            if (selectedItemIndex >= 0 && selectedItemIndex < set().size()) {
                set().remove(selectedItemIndex);
                if (selectedItemIndex >= set().size()) selectedItemIndex = set().size() - 1;
            }
        }).dimensions(this.width / 2 - 152, this.height - 56, 70, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Move Left"), btn -> {
            if (selectedItemIndex > 0 && selectedItemIndex < set().size()) {
                ColouredEntry e = set().remove(selectedItemIndex);
                selectedItemIndex--;
                set().add(selectedItemIndex, e);
            }
        }).dimensions(this.width / 2 - 80, this.height - 56, 78, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Move Right"), btn -> {
            if (selectedItemIndex >= 0 && selectedItemIndex < set().size() - 1) {
                ColouredEntry e = set().remove(selectedItemIndex);
                selectedItemIndex++;
                set().add(selectedItemIndex, e);
            }
        }).dimensions(this.width / 2 + 2, this.height - 56, 78, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Delete Set"), btn -> {
            parent.workingCopy.remove(setIndex);
            MinecraftClient.getInstance().setScreen(parent);
        }).dimensions(this.width / 2 + 82, this.height - 56, 70, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Done"), btn ->
                MinecraftClient.getInstance().setScreen(parent)
        ).dimensions(this.width / 2 - 50, this.height - 26, 100, 20).build());
    }

    private Set<String> collectUsedItemIds() {
        Set<String> used = new HashSet<>();
        for (List<ColouredEntry> s : parent.workingCopy) {
            for (ColouredEntry e : s) {
                used.add(e.id());
            }
        }
        return used;
    }

    private void onTypeChanged(String newType) {
        String t = newType.isBlank() ? "custom" : newType;
        List<ColouredEntry> s = set();
        for (int i = 0; i < s.size(); i++) {
            ColouredEntry e = s.get(i);
            if (!e.type().equals(t)) {
                s.set(i, new ColouredEntry(t, e.id(), e.item()));
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer,
                Text.literal("Edit Set #" + (setIndex + 1) + " (" + set().size() + " items)"),
                this.width / 2, 12, 0xFFFFFFFF);
        context.drawTextWithShadow(this.textRenderer, Text.literal("Type:"), 30, 42, 0xFFFFFFFF);

        context.enableScissor(gridLeft, GRID_TOP, gridRight, gridBottom);

        List<ColouredEntry> s = set();
        int topY = GRID_TOP - (int) scrollOffset;
        Integer hoveredIdx = null;
        for (int i = 0; i < s.size(); i++) {
            int col = i % columns;
            int row = i / columns;
            int cx = gridLeft + col * CELL_SIZE;
            int cy = topY + row * CELL_SIZE;
            if (cy + CELL_SIZE < GRID_TOP) continue;
            if (cy > gridBottom) break;

            boolean hovered = mouseX >= cx && mouseX < cx + CELL_SIZE && mouseY >= cy && mouseY < cy + CELL_SIZE;
            boolean selected = i == selectedItemIndex;
            int bg = selected ? 0xFF3060A0 : (hovered ? 0xFF606060 : 0xFF2A2A2A);
            context.fill(cx, cy, cx + CELL_SIZE, cy + CELL_SIZE, bg);

            try {
                Item item = s.get(i).item().get();
                context.drawItem(new ItemStack(item), cx + 2, cy + 2);
                if (hovered) hoveredIdx = i;
            } catch (Exception ignored) {}
        }
        context.disableScissor();

        if (s.isEmpty()) {
            context.drawCenteredTextWithShadow(this.textRenderer,
                    Text.literal("Empty set. Click '+ Add Item' to add items."),
                    this.width / 2, GRID_TOP + 20, 0xFF888888);
        }

        if (selectedItemIndex >= 0 && selectedItemIndex < s.size()) {
            ColouredEntry e = s.get(selectedItemIndex);
            String idLabel = "Selected: " + e.id() + "  (index " + selectedItemIndex + ")";
            context.drawTextWithShadow(this.textRenderer, idLabel, 20, this.height - 80, 0xFFCCCCCC);
        }

        if (hoveredIdx != null) {
            try {
                Item item = s.get(hoveredIdx).item().get();
                context.drawItemTooltip(this.textRenderer, new ItemStack(item), mouseX, mouseY);
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
        if (idx >= 0 && idx < set().size()) {
            selectedItemIndex = idx;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (mouseY >= GRID_TOP && mouseY <= gridBottom) {
            scrollOffset -= verticalAmount * CELL_SIZE;
            int rows = (set().size() + columns - 1) / columns;
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
