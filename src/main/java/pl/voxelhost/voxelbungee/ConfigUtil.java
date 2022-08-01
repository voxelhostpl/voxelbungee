package pl.voxelhost.voxelbungee;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Optional;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public final class ConfigUtil {

    public static Optional<Configuration> loadConfigFile(final Plugin plugin, final String configFileName) {
        if (!plugin.getDataFolder().exists()) {
            if (!plugin.getDataFolder().mkdirs()) {
                plugin.getLogger().warning("Failed to create plugin folder");
                return Optional.empty();
            }
        }

        final File configFile = new File(plugin.getDataFolder(), configFileName);
        if (!configFile.exists()) {
            try (final InputStream in = plugin.getResourceAsStream(configFileName)) {
                Files.copy(in, configFile.toPath());
            } catch (final IOException exception) {
                plugin.getLogger().warning("Failed to save default file \"" + configFileName + "\"");
            }

            plugin.getLogger().info("Default config saved, configure the plugin and restart the proxy");
            return Optional.empty();
        }

        try {
            return Optional.of(ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile));
        } catch (final IOException exception) {
            plugin.getLogger().warning("Failed to open configuration file \"" + configFileName + "\"");
            return Optional.empty();
        }
    }

    private ConfigUtil() {}

}
