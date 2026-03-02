package net.anware.minecraft.mods.colourscroller.scroll.mixin;

import net.anware.minecraft.mods.colourscroller.keybind.KeyBindLookup;
import net.anware.minecraft.mods.colourscroller.scroll.ScrollLookup;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {
    
    @Shadow @Final public PlayerEntity player;
    @Shadow public int selectedSlot;
    
    @Inject(method = "scrollInHotbar", at = @At("HEAD"), cancellable = true)
    private void wrapScrollColour(double scrollAmount, CallbackInfo ci) {
        if (this.scrollColour(scrollAmount)) ci.cancel();
    }

    @Unique
    private boolean scrollColour(double amonut) {
        if (!this.player.isCreative()) {
            return false;
        }
        if (!(this.player instanceof ClientPlayerEntity)) {
            return false;
        }
        int shift = -(int) Math.signum(amonut);
        if (KeyBindLookup.SCROLL_SINGLE.isPressed()) {
            ItemStack stack = this.getSelectedStack();
            Item item = ScrollLookup.getShifted(stack.getItem(), shift);
            if (item == null) {
                return false;
            }
            ItemStack shiftedStack = new ItemStack(item);
            this.setHotbar(this.selectedSlot, shiftedStack);
            return true;
        }
        if (KeyBindLookup.SCROLL_HOTBAR.isPressed()) {
            return true;
        }
        return false;
    }
    
    @Unique
    private ItemStack getSelectedStack() {
        return this.player.getInventory().main.get(this.selectedSlot);
    }
    
    @Unique
    ItemStack getHotbar(int index) {
        if (index < 0 || index >= 9) return ItemStack.EMPTY;
        return this.player.getInventory().main.get(index);
    }
    
    @SuppressWarnings("ConstantConditions")
    @Unique
    private void setHotbar(int index, ItemStack stack) {
        this.player.getInventory().main.set(index, stack);
        MinecraftClient.getInstance().getNetworkHandler().sendPacket(new CreativeInventoryActionC2SPacket(index + 36, stack));
    }
}
