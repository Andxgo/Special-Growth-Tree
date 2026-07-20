# Special Growth Tree - Project Summary

**Last Updated:** 2026-07-19  
**Project Status:** Feature-complete (polish phase finished)  
**Minecraft Version:** 1.20.1  
**Forge Version:** 47.2.0  
**Java Version:** 17  

---

## Project Overview

Special Growth Tree is a Minecraft Forge mod that adds a growth-based farming mechanic. Players can plant special seeds that grow through 5 stages automatically over time, eventually maturing into a harvestable tree-like state.

### Current Build Version
- **Version:** 0.1.0
- **Group ID:** com.example.specialgrowthtree
- **Archive Name:** special-growth-tree

---

## Features Implemented

### 1. **Special Seed Item**
- Placed on dirt, grass block, or farmland
- Automatically consumes the item when planted
- Validates ground conditions before allowing placement
- Schedules the first growth tick after 100 game ticks (~5 seconds)

**File:** `src/main/java/com/example/specialgrowthtree/common/item/SpecialSeedItem.java`

### 2. **Growth Stage Block**
- **6 visual stages** (0-5), each with distinct textures:
  - Stage 0: Dirt (seedling)
  - Stage 1: Oak Sapling
  - Stage 2: Oak Log
  - Stage 3: Stripped Oak Log
  - Stage 4: Green Wool
  - Stage 5: Oak Log (mature)

- **Automatic progression:**
  - Random timing (120-300 ticks between stages)
  - 70% growth chance for stages 0-2
  - 50% growth chance for stages 3+
  - Destroys itself if ground becomes invalid

- **Environmental awareness:**
  - Only grows on dirt, grass, or farmland
  - Resets to air if conditions change

- **Mature stage interaction:**
  - Right-click to harvest
  - Gives 2x Oak Sapling + 3x Stick
  - Destroys the block after harvest

- **Polish effects:**
  - 8 happy villager particles when growing
  - Plant crop grow sound effect at each stage

**File:** `src/main/java/com/example/specialgrowthtree/common/block/GrowthStageBlock.java`

### 3. **Block & Item Registration**
- **ModBlocks:** Registers the growth stage block with 1.0 strength (wood-like hardness)
- **ModItems:** Registers the special seed as a stackable item (max 16)

**Files:**
- `src/main/java/com/example/specialgrowthtree/common/block/ModBlocks.java`
- `src/main/java/com/example/specialgrowthtree/common/item/ModItems.java`

### 4. **Assets & Models**
- **Blockstates:** `assets/special_growth_tree/blockstates/growth_stage_block.json`
  - Maps stages 0-5 to individual models

- **Block Models:** `assets/special_growth_tree/models/block/`
  - `growth_stage_block_stage0.json` → Dirt texture
  - `growth_stage_block_stage1.json` → Oak Sapling texture
  - `growth_stage_block_stage2.json` → Oak Log texture
  - `growth_stage_block_stage3.json` → Stripped Oak Log texture
  - `growth_stage_block_stage4.json` → Green Wool texture
  - `growth_stage_block_stage5.json` → Oak Log texture (mature)

- **Item Models:** `assets/special_growth_tree/models/item/special_seed.json`

- **Localization:** `assets/special_growth_tree/lang/en_us.json`
  - "Special Seed" display name
  - "Growth Stage Block" display name

---

## Build Instructions

### Prerequisites
Ensure you have installed:
- **Java 17** (via `winget install -e --id Microsoft.OpenJDK.17`)
- **Gradle** (via `winget install -e --id Gradle.Gradle`)

### Build Steps

1. **Open PowerShell and navigate to project root:**
   ```powershell
   cd "C:\Users\Nathans Desktop\special-growth-tree"
   ```

2. **Create Gradle wrapper (first time only):**
   ```powershell
   gradle wrapper --gradle-version 8.5
   ```

3. **Build the mod:**
   ```powershell
   .\gradlew.bat build
   ```

4. **Locate the built jar:**
   ```powershell
   dir "C:\Users\Nathans Desktop\special-growth-tree\build\libs"
   ```

The jar file will be named something like `special-growth-tree-0.1.0.jar`

---

## Running in Prism Launcher

### Setup Prism Instance
1. Create a new Prism Launcher instance:
   - **Minecraft Version:** 1.20.1
   - **Mod Loader:** Forge
   - **Forge Version:** 47.2.0 (or latest 1.20.1)
   - **Java:** 17

2. **Copy the built jar to mods folder:**
   ```powershell
   $prismModsPath = "C:\path\to\PrismLauncher\instances\<instance-name>\mods"
   Copy-Item "C:\Users\Nathans Desktop\special-growth-tree\build\libs\special-growth-tree-0.1.0.jar" -Destination $prismModsPath
   ```

3. **Launch the instance** from Prism Launcher

---

## Project Structure

```
special-growth-tree/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/specialgrowthtree/
│       │       ├── SpecialGrowthTreeMod.java (main mod class)
│       │       └── common/
│       │           ├── block/
│       │           │   ├── GrowthStageBlock.java
│       │           │   └── ModBlocks.java
│       │           └── item/
│       │               ├── SpecialSeedItem.java
│       │               └── ModItems.java
│       └── resources/
│           ├── assets/special_growth_tree/
│           │   ├── blockstates/
│           │   ├── models/
│           │   │   ├── block/
│           │   │   └── item/
│           │   └── lang/
│           ├── data/special_growth_tree/
│           └── META-INF/
├── build.gradle
├── settings.gradle
└── .gradle/
```

---

## Key Implementation Details

### Growth Progression Flow
1. **Player plants seed** on valid ground
2. **SpecialSeedItem.useOn()** places block and schedules first tick (100 ticks)
3. **GrowthStageBlock.tick()** is called → **advanceGrowth()** executes:
   - Checks if ground is still valid
   - Random chance test based on current stage
   - If pass: advance stage, play sound, spawn particles
   - Reschedule next tick (120-300 ticks)
4. **At stage 5 (mature):**
   - Right-click interaction harvests the block
   - Player receives 2x Sapling + 3x Stick
   - Block is destroyed

### Environmental Logic
- Block monitors the block below it every tick
- If ground becomes invalid → block disappears
- Prevents "floating" growth blocks

### Randomness & Timing
- **Stage delay:** 120-300 ticks (6-15 seconds at 20 TPS)
- **Growth chance (0-2):** 70% success rate
- **Growth chance (3-5):** 50% success rate
- Makes progression feel natural, not mechanical

### Audio-Visual Feedback
- **8 happy villager particles** spawned at growth location
- **Plant Crop Grow sound** played at 0.5F volume
- Provides clear feedback when plant progresses

---

## Testing Checklist

- [ ] Seed placement validates ground correctly
- [ ] Seed accepts only dirt/grass/farmland
- [ ] First growth tick delays ~5 seconds
- [ ] Visual stages render correctly in-game
- [ ] Particles spawn at each stage
- [ ] Sound plays at each growth event
- [ ] Block destruction removes all traces
- [ ] Harvest interaction gives correct items
- [ ] Environmental reset works (removes block if ground changes)

---

## Known Limitations & Future Ideas

### Current Limitations
- Only uses vanilla textures (could add custom models)
- Single plant type (could add variations)
- No drop animation at harvest

### Potential Future Enhancements
- Custom 3D plant models for each stage
- Bonemeal acceleration support
- Multiple plant types (different seeds)
- Configurable growth rates
- Particle customization
- Custom harvest drops
- Growth sound variations

---

## Troubleshooting

### "gradlew.bat not found"
→ Run `gradle wrapper --gradle-version 8.5` first

### "gradle: command not found"
→ Install Gradle via winget and restart PowerShell

### Build fails with "Java not found"
→ Install Java 17 via winget: `winget install -e --id Microsoft.OpenJDK.17`

### Prism doesn't load the mod
→ Verify jar is in the correct mods folder
→ Verify Forge version matches (47.2.0 for 1.20.1)
→ Check Prism launcher log for error details

---

## Contact & Attribution

**Project:** Special Growth Tree Minecraft Mod  
**Version:** 0.1.0  
**Minecraft:** 1.20.1  
**Forge:** 47.2.0  
**Java:** 17  

---

## Session Notes

**Work completed on 2026-07-14 to 2026-07-19:**
1. ✅ Implemented seed placement logic
2. ✅ Added growth stage block with 6 stages
3. ✅ Automatic progression with random timing
4. ✅ Environmental awareness (ground validation)
5. ✅ Harvest interaction with rewards
6. ✅ Particle & sound effects
7. ✅ Asset models for all stages
8. ✅ Initial growth delay for natural feel

**Next session action items:**
- Build the jar and test in Prism Launcher
- Verify all visuals render correctly
- Test gameplay loop end-to-end
- Adjust timing/rewards if needed
