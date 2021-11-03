package plugin.util.plugin;

import apple.utilities.util.FileFormatting;

import java.io.File;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class PluginManagedModule {
    private boolean enabled;
    private PluginManaged parent;
    private final PluginManagedLogger loggerWrapper = new PluginManagedLogger();

    public void _init(PluginManaged parent) {
        this.parent = parent;
    }

    public void init() {
    }

    public abstract void enable();

    public void onDisable() {
    }

    public void setLogger(Logger logger) {
        this.loggerWrapper.setLogger(logger);
    }

    public void log(Level level, String formatted, Object... args) {
        // I would love to know how to properly do this
        this.loggerWrapper.log(level, formatted, args);
    }

    public PluginManagedLogger logger() {
        return this.loggerWrapper;
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

    public File getFile(String... children) {
        return FileFormatting.fileWithChildren(getDataFolder(), children);
    }

    public boolean shouldEnable() {
        return true;
    }

    public abstract String getName();

    public PluginManaged getPlugin() {
        return parent;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    private class PluginManagedLogger {
        private Logger logger;

        public void run(BiConsumer<Logger, String> loggerFunction, String formatted, Object... args) {
            // I would love to know how to properly do this
            loggerFunction.accept(logger, getLoggingFormatted(formatted, args));
        }

        public void info(String msg) {
            log(Level.INFO, msg);
        }

        public void warning(String msg) {
            log(Level.WARNING, msg);
        }

        public void severe(String msg) {
            log(Level.SEVERE, msg);
        }

        public void log(Level level, String formatted, Object... args) {
            this.logger.log(level, getLoggingFormatted(formatted, args));
        }

        private void setLogger(Logger logger) {
            this.logger = logger;
        }

        private String getLoggingFormatted(String formatted, Object[] args) {
            return String.format("[%s] %s", getName(), String.format(formatted, args));
        }
    }
}
