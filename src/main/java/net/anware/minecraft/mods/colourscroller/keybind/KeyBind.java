package net.anware.minecraft.mods.colourscroller.keybind;

import org.lwjgl.glfw.GLFW;

import java.util.LinkedHashMap;
import java.util.Map;

public class KeyBind {
    public KeyBind(String id, KeySequence defaultKey) {
        this.id = id;
        this.defaultKeySequence = defaultKey;
        this.setKeySeq(defaultKey);
        KeyBindLookup.register(this);
    }

    public final String id;
    protected KeySequence defaultKeySequence, keySequence = null;
    protected int pressedStage = -1;
    protected boolean isPressed = false;

    public void setKeySeq(KeySequence keySequence) {
        KeySequence old = this.keySequence;
        this.keySequence = keySequence;
        KeyBindLookup.updateKeybindLookup(this, old);
    }
    
    public void resetKeySeq() {
        this.setKeySeq(this.defaultKeySequence);
    }

    public void onKeyEvent(int key, int action) {
        int id = this.keySequence.indexOf(key);
        if (action == GLFW.GLFW_PRESS) {
            if (id == this.pressedStage + 1) {
                this.pressedStage++;
                if (this.pressedStage == this.keySequence.length() - 1) {
                    this.setPressed(true);
                }
            }
        } else if (action == GLFW.GLFW_RELEASE) {
            if (this.isPressed) {
                this.setPressed(false);
            }
            this.pressedStage = Math.min(this.pressedStage, id - 1);
        }
    }

    protected void setPressed(boolean pressed) {
        this.isPressed = pressed;
        if (pressed) {
            this.onPress();
        } else {
            this.onRelease();
        }
    }

    public void onPress() {}

    public void onRelease() {}

    public boolean isPressed() {
        return this.isPressed;
    }

    @Override
    public String toString() {
        return this.keySequence.toString();
    }
    
    public Object serialize() {
        Map<String, Object> e = new LinkedHashMap<>();
        e.put("id", id);
        e.put("key", this.keySequence.get());
        return e;
    }
    
    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
}