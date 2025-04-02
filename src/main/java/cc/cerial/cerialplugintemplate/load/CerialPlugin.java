package cc.cerial.cerialplugintemplate.load;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;

/**
 * An extended class of JavaPlugin, which adds instance handling and reload handling.
 */
@ApiStatus.Internal
public abstract class CerialPlugin extends JavaPlugin {
    /**
     * This method will run when the onEnable is run, and when /debugutil reload is run.
     */
    public abstract void onReload();

    /**
     * In this onEnable, please keep the super.onEnable() call, so onReload() can be called. You may call it later as needed.
     */
    @Override
    public void onEnable() {
        onReload();
    }
}
