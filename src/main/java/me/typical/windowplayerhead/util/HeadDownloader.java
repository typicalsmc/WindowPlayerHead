package me.typical.windowplayerhead.util;

import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Utility class for downloading player head images from mc-heads.net.
 */
public final class HeadDownloader {
    private static final Logger LOGGER = LoggerFactory.getLogger("WindowPlayerHead");
    private static final String HEAD_API_URL = "https://minotar.net/helm/";
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private HeadDownloader() {
        // Utility class
    }

    /**
     * Downloads the player's head image asynchronously.
     *
     * @param playerUuid The player's UUID
     * @return A CompletableFuture containing the path to the downloaded image, or null if failed
     */
    public static CompletableFuture<Path> downloadHeadAsync(String playerUuid) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return downloadHead(playerUuid);
            } catch (Exception e) {
                LOGGER.error("Failed to download head for player: {}", playerUuid, e);
                return null;
            }
        });
    }

    /**
     * Downloads the player's head image synchronously.
     *
     * @param playerUuid The player's UUID
     * @return The path to the downloaded image
     * @throws IOException If the download fails
     * @throws InterruptedException If the download is interrupted
     */
    public static Path downloadHead(String playerUuid) throws IOException, InterruptedException {
        Path cacheDir = getCacheDirectory();
        Files.createDirectories(cacheDir);

        Path headPath = cacheDir.resolve(playerUuid + ".png");

        String url = HEAD_API_URL + playerUuid.replace("-", "") + "/100.png";
        LOGGER.info("Downloading player head from: {}", url);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(30))
                .GET()
                .build();

        HttpResponse<InputStream> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofInputStream());

//        if (response.statusCode() != 200 && response.statusCode() != 301) {
//            throw new IOException("Failed to download head, status code: " + response.statusCode());
//        }

        try (InputStream is = response.body()) {
            Files.copy(is, headPath, StandardCopyOption.REPLACE_EXISTING);
        }

        LOGGER.info("Successfully downloaded player head to: {}", headPath);
        return headPath;
    }

    /**
     * Gets the cache directory for storing downloaded heads.
     *
     * @return The cache directory path
     */
    public static Path getCacheDirectory() {
        return FabricLoader.getInstance().getGameDir().resolve("cache").resolve("windowplayerhead");
    }
}
