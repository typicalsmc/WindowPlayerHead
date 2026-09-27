package me.typical.windowplayerhead.util;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import org.lwjgl.sdl.SDLError;
import org.lwjgl.sdl.SDLSurface;
import org.lwjgl.sdl.SDLVideo;
import org.lwjgl.sdl.SDL_Surface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;

public final class WindowIconUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger("WindowPlayerHead");
    // Same RGBA pixel format used by Minecraft 26.3's Window.createIconSurface.
    private static final int SDL_PIXELFORMAT_RGBA32 = 376840196;

    private WindowIconUtil() {
    }

    public static void setIcon(Path iconPath) {
        try (NativeImage image = NativeImage.read(Files.readAllBytes(iconPath))) {
            SDL_Surface surface = SDLSurface.SDL_CreateSurfaceFrom(
                    image.getWidth(), image.getHeight(), SDL_PIXELFORMAT_RGBA32,
                    image.getPixelBytes(), image.getWidth() * 4);
            if (surface == null) {
                LOGGER.error("Failed to create SDL surface for window icon from {}: {}", iconPath, SDLError.SDL_GetError());
                return;
            }

            try {
                if (SDLVideo.SDL_SetWindowIcon(Minecraft.getInstance().getWindow().handle(), surface)) {
                    LOGGER.info("Successfully set window icon from: {}", iconPath);
                } else {
                    LOGGER.error("Failed to set window icon from {}: {}", iconPath, SDLError.SDL_GetError());
                }
            } finally {
                SDLSurface.SDL_DestroySurface(surface);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to set window icon from path: {}", iconPath, e);
        }
    }
}
