package net.anware.minecraft.mods.colourscroller.ui.tiles;

import net.anware.minecraft.mods.colourscroller.keybind.KeyBind;
import net.anware.minecraft.mods.colourscroller.keybind.KeyBindLookup;
import net.anware.minecraft.mods.colourscroller.keybind.KeySequence;
import net.anware.minecraft.mods.colourscroller.ui.Tile;
import net.anware.minecraft.mods.colourscroller.ui.TileScreen;
import net.anware.minecraft.mods.colourscroller.ui.components.Button;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;

public class KeyConfigTile extends Tile {
	public KeyConfigTile(TileScreen parent, int x, int padTop, int padBottom, int height, KeyBind keyBind) {
		super(parent, x, padTop, padBottom);
		this.keyBind = keyBind;
		this.height = height;
	}
	
	protected final ArrayList<Integer> bufKey = new ArrayList<>(4);
	protected final KeyBind keyBind;
	protected final int height;
	protected Button<KeyConfigTile> keyButton, setButton, resetButton;
	
	@Override
	public void init() {
		this.keyButton = new Button<>(this, 100, 0, 100, this.height, this.keyBind.toString()) {
			@Override
			protected boolean clicked() {
				this.parent.bufKey.clear();
				return true;
			}
		};
		this.setButton = new Button<>(this, 205, 0, 40, this.height, "Set") {
			@Override
			protected boolean clicked() {
				if (!this.parent.bufKey.isEmpty()) {
					this.parent.keyBind.setKeySeq(new KeySequence(this.parent.bufKey));
					KeyBindLookup.save();
					this.parent.updateBufKey();
					this.parent.parent.setActiveTile(null);
				}
				return false;
			}
		};
		this.resetButton = new Button<>(this, 250, 0, 40, this.height, "Reset") {
			@Override
			protected boolean clicked() {
				this.parent.keyBind.resetKeySeq();
				KeyBindLookup.save();
				this.parent.updateBufKey();
				this.parent.parent.setActiveTile(null);
				return false;
			}
		};
		this.setButton.setEnabled(false);
		super.init();
	}
	
	@Override
	protected void draw(MatrixStack matrices, int mouse_x, int mouse_y, float delta) {
		this.drawText(matrices, this.keyBind.id, this.x, this.y + (float) this.height / 2, 0xFFFFFFFF, ALIGN_MID_V);
	}
	
	@Override
	public int getContentHeight() {
		return this.height;
	}
	
	@Override
	public void setActive(boolean active) {
		super.setActive(active);
		if (!active) {
			this.bufKey.clear();
			this.updateBufKey();
		}
	}
	
	@Override
	protected boolean onKey(int key) {
		if (key < 0) {
			return false;
		}
		if (this.bufKey.contains(key)) {
			return false;
		}
		if (this.bufKey.size() >= 4) {
			return false;
		}
		this.bufKey.add(key);
		this.updateBufKey();
		return true;
	}
	
	public void updateBufKey() {
		if (this.bufKey.isEmpty()) {
			this.setButton.setEnabled(false);
			this.keyButton.setText(this.keyBind.toString());
		} else {
			this.setButton.setEnabled(true);
			this.keyButton.setText(KeySequence.formatKeySequence(this.bufKey));
		}
	}
}
