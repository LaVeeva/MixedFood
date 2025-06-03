# Mixed Food Mod

A Minecraft Forge 1.20.1 mod that lets you combine multiple edible items in a single bowl into one “mixed food” item. The result:

- **Always grants full hunger and saturation** (clamped by Minecraft’s caps).  
- **Applies each ingredient’s effects** (potion buffs, teleportation, etc.) in sequence.  
- **Dynamically blends ingredient textures** into one composite icon.  
- **Returns the bowl** to your inventory (not consumed).

---

## 🚀 Key Features

1. **Up to 8 ingredients + 1 bowl** in any vanilla crafting grid.  
2. **Bowl is not consumed**—remains in place after crafting.  
3. **Stackable mixed food**: shift-click a full inventory of ingredients → full stack of mixed food.  
4. **Dynamic NBT data** stores:
   - Ingredient list (IDs and counts)  
   - Total hunger points (sum of each food’s nutrition)  
   - Total saturation (sum of each food’s saturation×nutrition)  
5. **Dynamic naming**: the mixed item’s display name is “FoodA, FoodB, FoodC…” (in locale) based on ingredients.  
6. **Per-ingredient effect simulation**: eating the mixed food runs each ingredient’s `finishUsingItem()`, so all side effects apply even if player is full.  
7. **Blended icon**: client-side model replaces the vanilla item model, layering each ingredient’s texture at 1⁄N opacity for a composite look.  

---

## 📋 Prerequisites

- **Java 17 JDK** (recommended: Eclipse Adoptium or Oracle JDK 17).  
- **Gradle 8.x** (bundled via the Gradle wrapper scripts).  
- **Visual Studio Code**, IntelliJ IDEA, or Eclipse (with Java support).  
- **Minecraft Forge MDK 1.20.1** (already included in this project).  

---

## 🛠️ Build & Run Instructions

1. **Clone or unzip** this project to a folder (e.g., `~/Projects/mixedfoodmod`).  
2. Open a terminal in the project root (where `build.gradle` lives).  
3. **Generate IDE run configurations**:  
   ```bash
   ./gradlew genVSCodeRuns
