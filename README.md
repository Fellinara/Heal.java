# Trial Spawner Mace Helper

A **client-side** Fabric mod for Minecraft 1.21.1 that automatically picks up
**Heavy Core** items dropped by Ominous Trial Spawners.

## What it does

When an [Ominous Trial Spawner](https://minecraft.wiki/w/Trial_Spawner) is
cleared and its loot vault releases a **Heavy Core** item, the mod detects the
dropped item entity and automatically moves your player toward it so it is
collected without manual effort.

* Works **client-side only** – the server does not need to have the mod
  installed.
* Only you need to have the mod.
* Searches within 16 blocks for any Heavy Core item entity.
* Picks the nearest one and smoothly guides your character to it.
* Press **H** (configurable in *Controls → Trial Spawner Mace Helper*) to
  toggle the auto-pickup feature on/off at any time.

## Requirements

| Requirement        | Version        |
|--------------------|----------------|
| Minecraft          | **1.21.1 or newer** (1.21.x) |
| Fabric Loader      | ≥ 0.16.0       |
| Fabric API         | for your Minecraft version (see below) |
| Java               | 21             |

> ⚠️ **Fabric API is required.** Download it from
> [Modrinth](https://modrinth.com/mod/fabric-api) – choose the version that
> matches your Minecraft version and put it in your `mods/` folder.

## Troubleshooting — Minecraft won't start

If Minecraft shows a "mod compatibility" error or simply won't open after
installing this mod, work through this checklist:

1. **Check your Minecraft version.**
   Open the Minecraft Launcher → Installations → make sure the profile uses
   **Fabric 1.21.1** (or a newer 1.21.x Fabric profile).

2. **Make sure Fabric Loader is installed.**
   Download the Fabric installer from [fabricmc.net](https://fabricmc.net/use/)
   and run it for your Minecraft version.

3. **Make sure Fabric API is in your `mods/` folder.**
   The mod depends on Fabric API. Without it, Fabric will show a
   "missing dependency" error screen.
   Download it from [Modrinth – fabric-api](https://modrinth.com/mod/fabric-api)
   and pick the version matching your Minecraft version.

4. **Only one mod JAR in `mods/`.**
   Make sure you placed only `trial-spawner-mace-1.0.0.jar` and `fabric-api-*.jar`
   in the mods folder, not the `-sources.jar` file.

5. **Java 21 required.**
   Minecraft 1.21.1 requires Java 21. The Fabric Installer usually sets this
   automatically.

## Download the mod JAR (recommended)

Every push to this repository automatically builds the mod and makes the JAR
available for download via GitHub Actions — **no Java or Gradle installation
needed**.

1. Click the **Actions** tab at the top of this GitHub page.
2. Click the latest successful **"Build & Upload Mod JAR"** run (green ✓).
3. Scroll down to the **Artifacts** section at the bottom of the run page.
4. Click **`trial-spawner-mace-mod`** to download a `.zip` file.
5. Extract the `.zip` — inside you will find `trial-spawner-mace-1.0.0.jar`.
6. Place that `.jar` in your Minecraft `mods/` folder.

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/) for Minecraft 1.21.1.
2. Download [Fabric API](https://modrinth.com/mod/fabric-api) and place it in
   your `mods/` folder.
3. Download the mod JAR as described above and place it in your `mods/` folder.

## Building from source (optional)

```bash
# Linux / macOS
chmod +x gradlew
./gradlew build

# Windows
gradlew.bat build
```

The built jar will be in `build/libs/`.

> **Note:** The first build will download all dependencies automatically.
> Make sure you have an internet connection and Java 21 installed.

## Usage

1. Load into a Minecraft world that contains an Ominous Trial Chamber.
2. Clear an Ominous Trial Spawner (requires the Bad Omen effect).
3. Use an **Ominous Trial Key** on an **Ominous Vault**.
4. The mod will automatically detect any **Heavy Core** that drops and move
   your player toward it to collect it.

## Controls

| Keybind | Default | Action                         |
|---------|---------|--------------------------------|
| Toggle  | `H`     | Enable / disable auto-pickup   |

The keybind can be rebound in *Options → Controls → Trial Spawner Mace Helper*.
