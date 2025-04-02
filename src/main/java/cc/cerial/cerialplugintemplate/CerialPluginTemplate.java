package cc.cerial.cerialplugintemplate;

import cc.cerial.cerialplugintemplate.load.CerialPlugin;

public final class CerialPluginTemplate extends CerialPlugin {
    private static CerialPluginTemplate instance;
    private static PluginUtils utils;

    public static PluginUtils getUtils() {
        return utils;
    }

    public static CerialPluginTemplate get() {
        if (instance == null)
            throw new IllegalStateException("The instance of the plugin is null!");
        return instance;
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onEnable() {
        instance = this;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        instance = null;
    }
}
