package net.anware.minecraft.mods.colourscroller.ui.screens;

import net.anware.minecraft.mods.colourscroller.scroll.Scroll;
import net.anware.minecraft.mods.colourscroller.scroll.ScrollLookup;
import net.anware.minecraft.mods.colourscroller.ui.Tile;
import net.anware.minecraft.mods.colourscroller.ui.TileScreen;
import net.anware.minecraft.mods.colourscroller.ui.tiles.AddScrollTile;
import net.anware.minecraft.mods.colourscroller.ui.tiles.ScrollTile;
import net.anware.minecraft.mods.colourscroller.ui.tiles.TextTile;

import java.util.ArrayList;
import java.util.List;

public class ScrollScreen extends TileScreen {
	public ScrollScreen() {
		super("Scroll Configs", 20);
		
		List<Tile> tiles = new ArrayList<>();
		tiles.add(new TextTile(this, 20, 0, 20, "Scroll Configs", 0xFFFFFFFF));
		tiles.add(ConfigScreen.getTabTile(this));
		tiles.add(new AddScrollTile(this, 30, 0, 5));
		for (Scroll scroll : ScrollLookup.SCROLL_REGISTRY.values()) {
			tiles.add(this.getScrollTile(scroll));
		}
		this.addTiles(tiles);
	}
	
	public Tile getScrollTile(Scroll scroll) {
		return new ScrollTile(this, 30, 10, 0, scroll, 250, 20);
	}
}
