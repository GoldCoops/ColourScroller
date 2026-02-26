package net.anware.tmc.colourscroller.mixin;

import net.anware.tmc.colourscroller.ScrollableItem;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Item.class)
public abstract class MixinItem implements ScrollableItem {
    @Unique
    private int listIndex, index;
    @Unique
    private boolean coloured = false;
    @Unique
    private String type = "";

    @Override
    public boolean scrollable() {
        return this.coloured;
    }

    @Override
    public void setScrollable(boolean coloured) {
        this.coloured = coloured;
    }

    @Override
    public int getListIndex() {
        return this.listIndex;
    }

    @Override
    public void setListIndex(int index) {
        this.listIndex = index;
    }

    @Override
    public int getIndex() {
        return this.index;
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String type() {
        return this.type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }
}