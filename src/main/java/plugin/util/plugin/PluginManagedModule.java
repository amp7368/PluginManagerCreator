package plugin.util.plugin;

import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class PluginManagedModule {
    private boolean enabled;
    private Logger logger;
    private PluginManaged parent;

    public void init(PluginManaged parent) {
        this.parent = parent;
    }

    public abstract void enable();

    public void onDisable() {
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public void log(Level level, String msg) {
        // I would love to know how to properly do this
        this.logger.log(level, String.format("[%s] %s", getName(), msg));
    }


    public void doEnable() {
        this.enabled = true;
        enable();
    }

    public File getDataFolder() {
        File file = new File(parent.getDataFolder(), getName());
        if (!file.exists()) file.mkdirs();
        return file;
    }

    public boolean shouldEnable() {
        return true;
    }

    public abstract String getName();

    public PluginManaged getPlugin() {
        return parent;
    }
}
