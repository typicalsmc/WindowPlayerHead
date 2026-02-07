package me.typical.windowplayerhead.client;

import me.typical.windowplayerhead.util.HeadDownloader;
import me.typical.windowplayerhead.util.WindowIconUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WPHModClient implements ClientModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger("WindowPlayerHead");

    @Override
    public void onInitializeClient() {
        LOGGER.info("WindowPlayerHead client initializing...");

        // Set window icon as soon as the client is fully initialized
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            String playerUuid = client.getUser().getProfileId().toString();
            LOGGER.info("Client started, updating window icon for UUID: {}", playerUuid);

            // Download head asynchronously to avoid blocking the main thread
            HeadDownloader.downloadHeadAsync(playerUuid).thenAccept(headPath -> {
                if (headPath != null) {
                    // Set the icon on the main render thread
                    client.execute(() -> WindowIconUtil.setIcon(headPath));
                }
            });
        });

        LOGGER.info("WindowPlayerHead client initialized!");
    }
}
