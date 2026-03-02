package net.anware.minecraft.mods.colourscroller.ui.screens;

import net.anware.minecraft.mods.colourscroller.keybind.KeyBindLookup;
import net.anware.minecraft.mods.colourscroller.ui.TileScreen;
import net.anware.minecraft.mods.colourscroller.ui.tiles.KeyConfigTile;
import net.anware.minecraft.mods.colourscroller.ui.tiles.TabTile;
import net.anware.minecraft.mods.colourscroller.ui.tiles.TabTile.Tab;
import net.anware.minecraft.mods.colourscroller.ui.tiles.TextTile;

import java.util.List;

public class ConfigScreen extends TileScreen {
	public ConfigScreen() {
		super("Colour Scroller Config", 20);
		
		this.addTiles(
			new TextTile(this, 20, 0, 20, "Colour Scroller Configs", 0xFFFFFFFF),
			getTabTile(this),
			new KeyConfigTile(this, 30, 0, 5, 20, KeyBindLookup.OPEN_CONFIG),
			new KeyConfigTile(this, 30, 0, 5, 20, KeyBindLookup.SCROLL_HOTBAR),
			new KeyConfigTile(this, 30, 0, 5, 20, KeyBindLookup.SCROLL_SINGLE)
		);
	}
	
	protected static TabTile getTabTile(TileScreen screen) {
		return new TabTile(screen, 20, 0, 10, 20, List.of(
			new Tab("Config", ConfigScreen::new),
			new Tab("Scrolls", ScrollScreen::new)
		));
	}
}