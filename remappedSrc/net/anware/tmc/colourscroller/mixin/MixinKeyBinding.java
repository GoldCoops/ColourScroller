package net.anware.tmc.colourscroller.mixin;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil.Key;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(KeyBinding.class)
public abstract class MixinKeyBinding {
    @Shadow private int timesPressed;
    @Shadow @Final private static Map<String, KeyBinding> KEYS_BY_ID;
    @Shadow private Key boundKey;
    @Unique
    private static final Map<Key, List<KeyBinding>> KEY_DIC = new HashMap<>();

    @Unique
    private void addTime() {
        this.timesPressed++;
    }

    @Unique
    private Key getKey() {
        return this.boundKey;
    }

    @Inject(method = "setKeyPressed", at = @At("HEAD"), cancellable = true)
    private static void setKeyPressedRepl(Key key, boolean pressed, CallbackInfo ci) {
        setPressed(key, pressed);
        ci.cancel();
    }

    @Inject(method = "onKeyPressed", at = @At("HEAD"), cancellable = true)
    private static void onKeyPressedRepl(Key key, CallbackInfo ci) {
        onPressed(key);
        ci.cancel();
    }

    @Inject(method = "updateKeysByCode", at = @At("HEAD"), cancellable = true)
    private static void updateKeysByCodeRepl(CallbackInfo ci) {
        updateState();
        ci.cancel();
    }

    @Unique
    private static void setPressed(Key key, boolean pressed) {
        List<KeyBinding> list = KEY_DIC.get(key);
        if (list == null || list.isEmpty()) return;
        for (KeyBinding keybinding : list) keybinding.setPressed(pressed);
    }

    @Unique
    private static void onPressed(Key key) {
        List<KeyBinding> list = KEY_DIC.get(key);
        if (list == null || list.isEmpty()) return;
        for (KeyBinding keybinding : list) ((MixinKeyBinding) (Object)keybinding).addTime();
    }

    @Unique
    private static void updateState() {
        KEY_DIC.clear();
        for (KeyBinding keybinding : KEYS_BY_ID.values()) {
            KEY_DIC.merge(((MixinKeyBinding) (Object) keybinding).getKey(), new ArrayList<>(){{add(keybinding);}}, (list, listadd) -> {
                if (list.isEmpty()) return listadd;
                return new ArrayList<>(){{addAll(list);addAll(listadd);}};
            });
        }
    }
}