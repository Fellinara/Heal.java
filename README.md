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
| Minecraft          | 1.21.1         |
| Fabric Loader      | ≥ 0.16.0       |
| Fabric API         | 0.102.0+1.21.1 |
| Java               | 21             |

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/) for Minecraft 1.21.1.
2. Download [Fabric API](https://modrinth.com/mod/fabric-api) and place it in
   your `mods/` folder.
3. Build the mod (see below) or download a pre-built jar, then place it in your
   `mods/` folder.

## Building from source

```bash
# Linux / macOS
chmod +x gradlew
./gradlew build

# Windows
gradlew.bat build
```

The built jar will be in `build/libs/`.

> **Note:** The first build will download the Gradle wrapper jar automatically.
> Make sure you have an internet connection.

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
