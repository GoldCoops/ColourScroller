package net.anware.tmc.colourscroller.mixin;

import net.anware.tmc.colourscroller.ColourScroller;
import net.anware.tmc.colourscroller.ConfigurationHandler;
import net.anware.tmc.colourscroller.ScrollableHelper;
import net.anware.tmc.colourscroller.ScrollableItem;
import net.anware.tmc.colourscroller.Settings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;

import java.util.List;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(Mouse.class)
public class MixinMouse {
    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
    private void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (vertical != 0 || horizontal != 0) {
            if (this.scrollColours(Math.signum(vertical))) ci.cancel();
        }
    }

    @Unique
    private boolean scrollColours(double amount) {
        ClientPlayerEntity player = client.player;

        if (player == null) return false;
        if (!player.isCreative()) return false;
        if (!Settings.KEY_BASE.isUnbound() && !Settings.KEY_BASE.isPressed()) return false;

        int shift = -(int) Math.signum(amount);
        int selectedSlot = player.getInventory().getSelectedSlot();
        ItemStack selected = player.getInventory().getMainStacks().get(selectedSlot);

        ScrollableItem selectedScrollable = (ScrollableItem) selected.getItem();
        if (!selectedScrollable.scrollable()) {
            return false;
        }

        if (Settings.KEY_SCROLL_SINGLE.isPressed()) {
            // Single-scroll is intentionally unchanged: advances only the selected item.
            ItemStack stack = ColourScroller.getNextScrollable(selectedScrollable, selected, shift);
            if (stack.isEmpty()) return false;
            setHotbar(player, selectedSlot, stack);
        } else if (Settings.KEY_SCROLL_ROW.isPressed()) {
            scrollRow(player, shift);
        } else {
            return false;
        }
        return true;
    }

    @Unique
    private void scrollRow(ClientPlayerEntity player, int shift) {
        // Pass 1: find the longest scrollable list currently in the hotbar.
        int longestSize = 0;
        int longestCurrentIndex = 0;
        for (int slot = 0; slot < 9; slot++) {
            ItemStack target = this.getHotbar(player, slot);
            ScrollableItem ts = (ScrollableItem) target.getItem();
            if (!ts.scrollable()) continue;

            int listIdx = ts.getListIndex();
            if (listIdx < 0 || listIdx >= ScrollableHelper.SCROLLABLE_SETS.size()) continue;
            List<ScrollableHelper.ColouredEntry> list = ScrollableHelper.SCROLLABLE_SETS.get(listIdx);
            if (list == null || list.isEmpty()) continue;

            if (list.size() > longestSize) {
                longestSize = list.size();
                longestCurrentIndex = ts.getIndex();
            }
        }

        // Decide whether to force every scrollable slot to its first entry this tick.
        boolean forceZero = false;
        if (ConfigurationHandler.SYNC_ENABLED && longestSize > 0) {
            int longestNext = Math.floorMod(longestCurrentIndex + shift, longestSize);
            forceZero = (longestNext == 0);
        }

        // Pass 2: actually scroll each scrollable slot.
        for (int slot = 0; slot < 9; slot++) {
            ItemStack target = this.getHotbar(player, slot);
            ScrollableItem ts = (ScrollableItem) target.getItem();
            if (!ts.scrollable()) continue;

            ItemStack stack = ColourScroller.getNextScrollable(target, shift, forceZero);
            if (stack.isEmpty()) continue;
            setHotbar(player, slot, stack);
        }
    }

    @Unique
    ItemStack getHotbar(PlayerEntity player, int index) {
        if (index < 0 || index >= 9) return ItemStack.EMPTY;
        return player.getInventory().getMainStacks().get(index);
    }

    @Unique
    private void setHotbar(PlayerEntity player, int index, ItemStack stack) {
        player.getInventory().setStack(index, stack);
        ClientPlayNetworkHandler networkHandler = MinecraftClient.getInstance().getNetworkHandler();
        if (networkHandler != null) {
            networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(index + 36, stack));
        }
    }
}
