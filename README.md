# ColourScroller

A Fabric mod that makes cycling through related items fast and intuitive. Use configurable keys to scroll a single selected item through its color/type variants or to scroll your entire hotbar so all matching item types change together.

## Goal

Enable quick in-game cycling of item colours and related variants. Scroll a single held item through its variant sequence, or synchronize an entire hotbar row so all items of the same family update to the same variant.

## Requirements and info

**This mod requires FabricAPI to function**

You can find FabricAPI [on modrinth](https://modrinth.com/mod/fabric-api?version=1.21.11).

**This mod is for client-side only, and only works when in creative mode**

## Features

* Scroll a single selected item through its colour or variant chain while preserving NBT and stack size.
* Scroll an entire hotbar row so every item that belongs to the same family (wool, stained glass, concrete, walls/slabs/stairs, etc.) updates to match the currently selected variant.
* Works across different block families and combinations of block types.
* Fully configurable key bindings and behavior.

## Planned (please be patient, I'm a University student, and this will be maintained in my free time)
* ~~Let end users edit and create lists of scrollable items/blocks though a JSON or YML configuration file.~~ (DONE 1.0.2)
* ~~Create in game GUI to allow users to easily edit, create and manage scrollable block lists.~~ (DONE 1.1.0)
* Add survival support (Choose the next item in the set that is in the players inventory)
* ~~Add a way to manage duplicate items in sets~~ (DONE 1.1.0)


## How to Use

* Bind keys in your controls for: Base Key, Scroll Row, Scroll Single, Open Set Editor.
* To scroll, hold the Base Key then press the Scroll Row key or the Scroll Single key while using the mouse wheel to move to the next variant.
* If Base Key is unbound, it is optional and you can use Scroll Row or Scroll Single without it.
* Scroll Single: cycles only the selected item (keeps NBT and count). Example: white wool -> light gray wool -> gray wool.
* Scroll Row: advances every scrollable item in your hotbar by one step per scroll tick, regardless of list name. Behavior of the cross-list synchronisation is controlled by the **synchronization toggle** at the top of the in-game set editor:
    * **Sync ON**: the longest list among the scrollable items in your hotbar dictates the cycle. While that longest list has more entries to go, every list advances independently; the moment the longest list would wrap back to its first entry, ALL scrollable hotbar items snap to their own first entry. Smaller lists therefore repeat several times during one full pass of the longest list, with their tail entries skipped at the sync point so everything realigns at the start each cycle.
    * **Sync OFF** (default): every list scrolls fully and independently — each list advances by one modulo its own size, and they won't re-align. 
* Open Set Editor: opens an in-game UI for managing scrollable sets at runtime.

## In-Game Set Editor

Press the Open Set Editor key (default `K`) while in-game to open the editor. From the editor you can:

* See every scrollable set with its type and item count.
* Click `Edit` on a set to open its detail screen.
* In the detail screen: rename the set, add items via `+ Add Item` (opens a searchable item picker), remove the selected item, reorder with `Move Left` / `Move Right`, or delete the whole set.
* Hold `Shift` while clicking an item in the picker to add multiple items without closing it.
* Click `+ New Set` on the main editor to create an empty set.
* Click `Save & Apply` to write changes to `ColourScroller/config/colourscroller.json` and apply them immediately — no restart needed.
* Click `Reload Default Sets` to reload the default sets provided (will wipe all established sets without confirmation at the moment)
* Click `Reload File` to discard local changes and reload from disk, or `Cancel` to discard without saving.

## Default Key Bindings

* Base Key: Left Ctrl (default)
* Scroll Row: C (default)
* Scroll Single: X (default)
* Open Set Editor: K (default)

## Examples

* Hold Base Key + X and scroll while holding white wool to cycle only that stack through wool colours; the stack count and NBT stay the same.
* Hold Base Key + C and scroll while selecting white concrete; your hotbar’s stained glass, wool, and powdered concrete become the same shade.

## Reporting Issues

If you find a bug or want a feature, open an issue on the project GitHub with:
* A clear description of the problem.
* Steps to reproduce.
* Your ColourScroller config file.
* Relevant mod list and Minecraft/Fabric versions.

## Screenshot
<img width="373" height="48" alt="Screenshot 2025-11-15 010750" src="https://github.com/user-attachments/assets/58b81e2e-e0bc-437f-9f66-9f8a77bc349a" />
<img width="373" height="48" alt="Screenshot 2025-11-15 010833" src="https://github.com/user-attachments/assets/4b200ebf-2cee-48b8-81ea-f6fd6e9f9d93" />
<img width="373" height="48" alt="Screenshot 2025-11-15 010853" src="https://github.com/user-attachments/assets/67cf6e5d-f754-4dba-8e13-f266ace91969" />
<img width="373" height="48" alt="Screenshot 2025-11-15 011059" src="https://github.com/user-attachments/assets/d32846de-93f8-4c15-855e-c1b1026b18a3" />

<hr>
<img width="373" height="48" alt="Screenshot 2025-11-15 010917" src="https://github.com/user-attachments/assets/f1b93c5b-f43d-47e8-b383-6e52b93629e6" />
<img width="373" height="48" alt="Screenshot 2025-11-15 010930" src="https://github.com/user-attachments/assets/02ed163a-31c1-4939-b830-950b3d61a5dc" />


## License

Licensed under the MIT License — see LICENSE for details.

## Special thanks

special thanks to Sergent_Patate for concept of this mod, you can find him on YouTube:

* [English channel](https://www.youtube.com/@Potatocrap_TMC)
* [French channel](https://www.youtube.com/@Sergent_Patate_TMC)

and this is his demo video:

(note : this demo is done with the first version of the mod, so the features added later aren't there yet)

[demo video](https://www.youtube.com/watch?v=cycILVtPPE0&t=84s)

### Original project

[colourscroller by Anware-Canella](https://github.com/Anware-Canella/colourscroller)

### Other Previous Maintainers/Forks

[colourscroller by TheMisterFish](https://github.com/TheMisterFish/ColourScroller)
