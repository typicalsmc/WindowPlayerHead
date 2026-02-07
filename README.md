# WindowPlayerHead

A client-side Fabric mod that changes your Minecraft window icon to your player's head.

## How It Works

When you join a server or singleplayer world, the mod:

1. Fetches your player head from [Minotar](https://minotar.net/)
2. Caches it locally in `.minecraft/cache/windowplayerhead/`
3. Sets it as the Minecraft window icon via GLFW

The download happens asynchronously, so it won't affect your loading times.

## Requirements

- Minecraft 1.21.11
- Fabric Loader 0.18.4+
- Fabric API

## Installation

1. Install [Fabric Loader](https://fabricmc.net/)
2. Download the mod jar and place it in your `.minecraft/mods/` folder
3. Launch the game - the mod works automatically with no configuration needed

## Building

```bash
./gradlew build
```

The built jar will be in `build/libs/`.

## License

All Rights Reserved.
