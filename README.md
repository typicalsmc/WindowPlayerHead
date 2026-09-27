# WindowPlayerHead

A client-side Fabric mod that changes your Minecraft window icon to your player's head.

## How It Works

When you join a server or singleplayer world, the mod:

1. Fetches your player head from [Minotar](https://minotar.net/)
2. Caches it locally in `.minecraft/cache/windowplayerhead/`
3. Sets it as the Minecraft window icon via GLFW (1.21.11) or SDL3 (26.3)

The download happens asynchronously, so it won't affect your loading times.

## Requirements

- Minecraft 1.21.11 with Fabric Loader 0.18.4+, or Minecraft 26.3 with Fabric Loader 0.19.5+
- Fabric API

## Installation

1. Install [Fabric Loader](https://fabricmc.net/)
2. Put only the JAR for your Minecraft version in `.minecraft/mods/`:
   - `WindowPlayerHead-GLFW-1.21.11-1.2.jar` for Minecraft 1.21.11
   - `WindowPlayerHead-SDL3-26.3-1.2.jar` for Minecraft 26.3
3. Launch the game - the mod works automatically with no configuration needed

## Building

```bash
./gradlew build
./gradlew -p sdl3 build
```

The installable JARs are in `build/libs/` and `sdl3/build/libs/` respectively. Do not install the `-sources.jar` file.

## License

All Rights Reserved.
