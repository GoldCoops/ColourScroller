package net.anware.tmc.colourscroller;

public interface ScrollableItem {

    boolean scrollable();

    void setScrollable(boolean coloured);

    int getListIndex();

    void setListIndex(int index);

    int getIndex();

    void setIndex(int index);

    String type();

    void setType(String type);
}