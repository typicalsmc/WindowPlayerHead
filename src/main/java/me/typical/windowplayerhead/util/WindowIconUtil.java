package me.typical.windowplayerhead.util;

import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Utility class for setting the Minecraft window icon.
 */
public final class WindowIconUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger("WindowPlayerHead");

    private WindowIconUtil() {
        // Utility class
    }

    /**
     * Sets the window icon from the given path.
     *
     * @param iconPath Path to the icon image file
     */
    public static void setIcon(Path iconPath) {
        try {
            long handle = Minecraft.getInstance().getWindow().handle();
            setWindowIcon(handle, iconPath);
        } catch (Exception e) {
            LOGGER.error("Failed to get window handle", e);
        }
    }
    // Credit to https://github.com/chylex/Minecraft-Window-Title/blob/b17a1cb5ccfaec3fcc763cd812f9bc773b34cc46/src/main/java/chylex/customwindowtitle/IconChanger.java#L26 for this code
    private static void setWindowIcon(long windowHandle, Path iconPath) {
        ByteBuffer icon = null;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            icon = loadIcon(iconPath, w, h, channels);
            if (icon == null) {
                return;
            }

            try (GLFWImage.Buffer icons = GLFWImage.malloc(1)) {
                GLFWImage iconImage = icons.get(0);
                iconImage.set(w.get(0), h.get(0), icon);

                GLFW.glfwSetWindowIcon(windowHandle, icons);
                LOGGER.info("Successfully set window icon from: {}", iconPath);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to set window icon from path: {}", iconPath, e);
        } finally {
            if (icon != null) {
                STBImage.stbi_image_free(icon);
            }
        }
    }

    private static ByteBuffer loadIcon(Path path, IntBuffer w, IntBuffer h, IntBuffer channels) throws IOException {
        byte[] iconBytes = Files.readAllBytes(path);

        ByteBuffer buffer = ByteBuffer.allocateDirect(iconBytes.length).put(iconBytes).flip();
        ByteBuffer icon = STBImage.stbi_load_from_memory(buffer, w, h, channels, 4);

        if (icon == null) {
            LOGGER.error("Failed to load image from path: {} - {}", path, STBImage.stbi_failure_reason());
        }

        return icon;
    }
}
