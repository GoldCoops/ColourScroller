# Graph Report - .  (2026-07-28)

## Corpus Check
- Corpus is ~9,490 words - fits in a single context window. You may not need a graph.

## Summary
- 214 nodes · 411 edges · 13 communities
- Extraction: 92% EXTRACTED · 8% INFERRED · 0% AMBIGUOUS · INFERRED: 34 edges (avg confidence: 0.82)
- Token cost: 0 input · 0 output

## Community Hubs (Navigation)
- Scrollable Set Registry
- Scroll Input & Item State
- Docs, Release & Project Meta
- Item Scrollable Data Mixin
- Set Edit Screen
- Config Serialization
- Key Binding Mixin
- Item Picker Screen
- Scrollable Editor Screen
- Key Binding Registration
- Visual Branding Identity
- Gradle Wrapper Script

## God Nodes (most connected - your core abstractions)
1. `ScrollableHelper` - 28 edges
2. `ScrollableEditorScreen` - 17 edges
3. `ColouredEntry` - 14 edges
4. `SetEditScreen` - 13 edges
5. `ItemPickerScreen` - 12 edges
6. `MixinKeyBinding` - 12 edges
7. `ConfigurationHandler` - 12 edges
8. `ScrollableItem` - 11 edges
9. `MixinItem` - 11 edges
10. `ColourScroller (Fabric Mod)` - 11 edges

## Surprising Connections (you probably didn't know these)
- `Feature Request Issue Template` --references--> `ColourScroller (Fabric Mod)`  [INFERRED]
  .github/ISSUE_TEMPLATE/feature_request.md → README.md
- `Gradle clean build Step` --references--> `ColourScroller (Fabric Mod)`  [INFERRED]
  .github/workflows/github_release_with_version.yml → README.md
- `Bug Report Issue Template` --references--> `ColourScroller (Fabric Mod)`  [INFERRED]
  .github/ISSUE_TEMPLATE/bug_report.md → README.md
- `Bug Report Issue Template` --conceptually_related_to--> `ColourScroller/config/colourscroller.json`  [INFERRED]
  .github/ISSUE_TEMPLATE/bug_report.md → README.md
- `Required Version Triple (Minecraft / Fabric Loader / Mod)` --references--> `ColourScroller 1.0.0 Release (MC 1.21.5)`  [INFERRED]
  .github/ISSUE_TEMPLATE/bug_report.md → CHANGELOG.md

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **Manual Per-MC-Version Release Publishing Flow** — _github_workflows_github_release_with_version_publish_workflow, _github_workflows_github_release_with_version_build_job, _github_workflows_github_release_with_version_gradle_build, _github_workflows_github_release_with_version_version_scheme, _github_workflows_github_release_with_version_mc_publish, changelog_colourscroller_1_0_0 [EXTRACTED 1.00]
- **Base Key + Action Key Chord Scheme** — readme_base_key, readme_scroll_row, readme_scroll_single, readme_open_set_editor_key, changelog_unbind_base_key [EXTRACTED 1.00]
- **Scrollable Set Authoring and Persistence Flow** — readme_in_game_set_editor, readme_colourscroller_json_config, readme_scrollable_set, readme_synchronization_toggle, readme_scroll_row [EXTRACTED 1.00]
- **ColourScroller Visual Identity (looping gradient ring conveying continuous hue cycling)** — src_main_resources_assets_colourscroller_icon_project_icon, src_main_resources_assets_colourscroller_icon_gradient_torus_motif, src_main_resources_assets_colourscroller_icon_hue_cycle_palette, src_main_resources_assets_colourscroller_icon_colour_scrolling_concept [INFERRED 0.85]

## Communities (13 total, 0 thin omitted)

### Community 0 - "Scrollable Set Registry"
Cohesion: 0.08
Nodes (11): ScrollableHelper, ScrollInfo, BuildingBlockScrollables, ColourScrollables, CopperBlockScrollables, MiscScrollables, MobScrollables, NaturalBlockScroller (+3 more)

### Community 1 - "Scroll Input & Item State"
Cohesion: 0.15
Nodes (16): CallbackInfo, ClientModInitializer, ClientPlayerEntity, Environment, Inject, Mixin, PlayerEntity, ScrollableItem (+8 more)

### Community 2 - "Docs, Release & Project Meta"
Cohesion: 0.13
Nodes (27): Bug Report Issue Template, Required Version Triple (Minecraft / Fabric Loader / Mod), Feature Request Issue Template, build Job (ubuntu-latest), Gradle clean build Step, Java 21 Temurin Toolchain, mc-publish Release Action, Publish on GitHub with version (Workflow) (+19 more)

### Community 3 - "Item Scrollable Data Mixin"
Cohesion: 0.13
Nodes (4): Mixin, Override, MixinItem, ScrollableItem

### Community 4 - "Set Edit Screen"
Cohesion: 0.20
Nodes (7): Click, DrawContext, Override, TextFieldWidget, SetEditScreen, ColouredEntry, Item

### Community 5 - "Config Serialization"
Cohesion: 0.19
Nodes (7): Click, MinecraftClient, ObjectMapper, ConfigSet, ConfigurationHandler, Item, ScrollerSettings

### Community 6 - "Key Binding Mixin"
Cohesion: 0.31
Nodes (7): Key, CallbackInfo, Inject, KeyBinding, Mixin, Unique, MixinKeyBinding

### Community 7 - "Item Picker Screen"
Cohesion: 0.25
Nodes (7): Screen, ItemPickerScreen, Click, DrawContext, Item, Override, TextFieldWidget

### Community 8 - "Scrollable Editor Screen"
Cohesion: 0.32
Nodes (3): DrawContext, Override, ScrollableEditorScreen

### Community 9 - "Key Binding Registration"
Cohesion: 0.47
Nodes (3): Category, KeyBinding, Settings

### Community 10 - "Visual Branding Identity"
Cohesion: 0.60
Nodes (5): Colour Scrolling (Cycling Hue Animation), Gradient Torus Motif, Continuous Hue Cycle Palette, Mod Metadata Branding Asset, ColourScroller Project Icon

### Community 11 - "Gradle Wrapper Script"
Cohesion: 0.83
Nodes (3): gradlew script, die(), warn()

## Knowledge Gaps
- **2 isolated node(s):** `Publish on GitHub with version (Workflow)`, `Open Set Editor Key Binding (K)`
  These have ≤1 connection - possible missing edges or undocumented components.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `ScrollableHelper` connect `Scrollable Set Registry` to `Scroll Input & Item State`, `Set Edit Screen`, `Config Serialization`?**
  _High betweenness centrality (0.151) - this node is a cross-community bridge._
- **Why does `ScrollableEditorScreen` connect `Scrollable Editor Screen` to `Scroll Input & Item State`, `Set Edit Screen`, `Config Serialization`, `Item Picker Screen`?**
  _High betweenness centrality (0.068) - this node is a cross-community bridge._
- **Why does `ColouredEntry` connect `Set Edit Screen` to `Scrollable Set Registry`, `Scrollable Editor Screen`, `Config Serialization`, `Scroll Input & Item State`?**
  _High betweenness centrality (0.046) - this node is a cross-community bridge._
- **What connects `Publish on GitHub with version (Workflow)`, `Open Set Editor Key Binding (K)` to the rest of the system?**
  _2 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Scrollable Set Registry` be split into smaller, more focused modules?**
  _Cohesion score 0.07804878048780488 - nodes in this community are weakly interconnected._
- **Should `Scroll Input & Item State` be split into smaller, more focused modules?**
  _Cohesion score 0.1455026455026455 - nodes in this community are weakly interconnected._
- **Should `Docs, Release & Project Meta` be split into smaller, more focused modules?**
  _Cohesion score 0.1282051282051282 - nodes in this community are weakly interconnected._