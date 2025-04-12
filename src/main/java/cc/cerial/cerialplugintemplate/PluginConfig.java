package cc.daniel.danielplugintemplate;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class PluginConfig {
    private CommentedConfigurationNode configNode;
    private YamlConfigurationLoader configLoader;

    protected PluginConfig() {}

    /**
     * Loads the configuration.
     * @return A boolean representing whether the configuration load was successful.
     */
    public boolean loadConfig() {
        CerialPluginTemplate plugin = CerialPluginTemplate.get();
        plugin.getSLF4JLogger().info("Loading configuration...");
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            if (!plugin.getDataFolder().mkdirs()) plugin.getSLF4JLogger().warn("Couldn't make directory for config. Maybe it already exists?");
            try {
                if (!configFile.createNewFile()) {
                    plugin.getSLF4JLogger().error("Couldn't create a new config.yml.");
                    return false;
                }
            } catch (IOException e) {
                plugin.getSLF4JLogger().error("An exception occurred while creating a new config.yml:", e);
                return false;
            }

            try (InputStream is = plugin.getResource("config.yml")) {
                if (is == null) {
                    plugin.getSLF4JLogger().error("The embedded config.yml doesn't exist!");
                    return false;
                }

                Files.copy(is, configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                plugin.getSLF4JLogger().error("Couldn't copy embedded config.yml file:", e);
                return false;
            }
        }

        configLoader = YamlConfigurationLoader.builder()
                .file(configFile)
                .build();

        try {
            configNode = configLoader.load();
        } catch (ConfigurateException e) {
            plugin.getSLF4JLogger().error("Couldn't load config.yml:", e);
            return false;
        }

        return true;
    }

    /**
     * Saves the configuration file.
     * @return A boolean, representing if the save was successful.
     */
    public boolean saveConfig() {
        try {
            configLoader.save(configNode);
        } catch (ConfigurateException ex) {
            CerialPluginTemplate.get().getSLF4JLogger().error("Caught an error while saving config.yml:", ex);
            return false;
        }
        return true;
    }

    /**
     * @return The {@link CommentedConfigurationNode} of this configuration.
     */
    public CommentedConfigurationNode getNode() {
        return this.configNode;
    }

    /**
     * @return The {@link YamlConfigurationLoader} of this configuration.
     */
    public YamlConfigurationLoader getLoader() {
        return this.configLoader;
    }
}
