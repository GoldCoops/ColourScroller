package net.anware.tmc.colourscroller;

import net.anware.tmc.colourscroller.gui.ScrollableEditorScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

@Environment(EnvType.CLIENT)
public class ColourScroller implements ClientModInitializer {

    public static final String ID = "colourscroller";

    @Override
    public void onInitializeClient() {
        Settings.register();
        ScrollableHelper.initialize();
        ConfigurationHandler.init();


        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (Settings.KEY_OPEN_EDITOR.wasPressed()) {
                if (client.currentScreen == null) {
                    MinecraftClient.getInstance().setScreen(new ScrollableEditorScreen());
                }
            }
        });
    }

    public static ItemStack getNextScrollable(ScrollableItem indexScrollable, ItemStack currentItemStack, int shift) {
        return getNextScrollable(currentItemStack, shift, false);
    }


    public static ItemStack getNextScrollable(ItemStack currentItemStack, int shift, boolean forceZero) {
        ScrollableItem scrollable = (ScrollableItem) currentItemStack.getItem();

        List<ScrollableHelper.ColouredEntry> list = ScrollableHelper.getSet(scrollable.getListIndex());
        if (list.isEmpty()) return ItemStack.EMPTY;

        int nextIndex = forceZero ? 0 : Math.floorMod(scrollable.getIndex() + shift, list.size());
        ScrollableHelper.ColouredEntry entry = list.get(nextIndex);
        return createStack(entry.item().get(), currentItemStack);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static ItemStack createStack(Item item, ItemStack stack) {
        ItemStack newStack = new ItemStack(item, stack.getCount());

        stack.getComponents().forEach((component) -> {
            ComponentType<?> type = component.type();
            if (type == DataComponentTypes.BLOCK_STATE) return;
            if (type == DataComponentTypes.MAP_ID) return;
            if (type == DataComponentTypes.ITEM_NAME) return;
            if (type == DataComponentTypes.BREAK_SOUND) return;
            if (type == DataComponentTypes.ITEM_MODEL) return;
            if (type == DataComponentTypes.TOOLTIP_DISPLAY) return;

            newStack.set((ComponentType) type, component.value());  // unchecked cast is safe here
        });

        return newStack;
    }
}