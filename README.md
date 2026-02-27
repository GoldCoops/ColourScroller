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

## How to Use

* Bind keys in your controls for: Base Key, Scroll Row, Scroll Single.
* To scroll, hold the Base Key then press the Scroll Row key or the Scroll Single key while using the mouse wheel to move to the next variant.
* If Base Key is unbound, it is optional and you can use Scroll Row or Scroll Single without it.
* Scroll Single: cycles only the selected item (keeps NBT and count). Example: white wool -> light gray wool -> gray wool.
* Scroll Row: finds all items in your hotbar that share the same family as the selected item and sets them to the same variant you scrolled to. Example: selected white concrete; stained glass, wool, and powdered concrete in the hotbar will also change to the corresponding shade.

## Default Key Bindings

* Base Key: Left Ctrl + Left (default)
* Scroll Row: C (default)
* Scroll Single: X (default)

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
