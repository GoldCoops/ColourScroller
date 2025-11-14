package net.anware.tmc.colourscroller.mixin;

import net.anware.tmc.colourscroller.ColourScroller;
import net.anware.tmc.colourscroller.ScrollableItem;
import net.anware.tmc.colourscroller.Settings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
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
            ItemStack stack = ColourScroller.getNextScrollable(selectedScrollable, selected, shift);
            if (stack.isEmpty()) return false;
            setHotbar(player, selectedSlot, stack);
        } else if (Settings.KEY_SCROLL_ROW.isPressed()) {
            for (int slot = 0; slot < 9; slot++) {
                ItemStack target = this.getHotbar(player, slot);
                ScrollableItem targetScrollable = (ScrollableItem) target.getItem();

                if (Objects.equals(targetScrollable.type(), selectedScrollable.type())) {
                    ItemStack stack = ColourScroller.getNextScrollable(selectedScrollable, target, shift);
                    if (stack.isEmpty()) continue;
                    setHotbar(player, slot, stack);
                }
            }
        } else {
            return false;
        }
        return true;
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
