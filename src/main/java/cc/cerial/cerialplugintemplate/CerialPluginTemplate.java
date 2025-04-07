package cc.cerial.cerialplugintemplate;

import org.bukkit.plugin.java.JavaPlugin;

public final class CerialPluginTemplate extends JavaPlugin {
    private static PluginUtils utils;
    private static CerialPluginTemplate instance;
    private static PluginConfig config;

    public static PluginUtils getUtils() {
        return utils;
    }

    public static PluginConfig getPluginConfig() {
        return config;
    }

    public static CerialPluginTemplate get() {
        if (instance == null)
            throw new IllegalStateException("The instance of the plugin is null!");
        return instance;
    }

    public boolean onReload() {
        utils = new PluginUtils("");
        config = new PluginConfig();
        if (!config.loadConfig()) return false;
        return true;
    }

    @Override
    public void onEnable() {
        instance = this;
        if (!onReload()) return;
    }

    @Override
    public void onDisable() {
        instance = null;
    }
}
