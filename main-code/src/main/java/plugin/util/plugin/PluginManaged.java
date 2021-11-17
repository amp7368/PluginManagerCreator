package plugin.util.plugin;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

public abstract class PluginManaged extends JavaPlugin {
    private PaperCommandManager commandManager;
    private final List<PluginManagedModule> modules = new ArrayList<>();
    private LuckPerms luckPerms;

    @Override
    public void onLoad() {
        onLoadPre();
        loadModules();
        onLoadPost();
    }

    public void onLoadPre() {
    }

    public void onLoadPost() {
    }


    @Override
    public void onEnable() {
        onEnablePreInit();
        setUp();
        onEnablePre();
        initialize();
        enableModules();
        onEnablePost();
    }


    public void onEnablePreInit() {
    }

    public void onEnablePre() {
    }

    public void initialize() {
    }

    public void onEnablePost() {
    }


    @Override
    public void onDisable() {
        onDisablePre();
        for (PluginManagedModule module : modules) {
            if (module.shouldEnable()) {
                module.onDisable();
                module.setEnabled(false);
            }
        }
        onDisablePost();
    }

    public void onDisablePre() {
    }

    public void onDisablePost() {
    }


    //
    // Dealing with modules
    //
    private void loadModules() {
        for (PluginManagedModule module : getModules()) {
            registerModule(module);
            if (module.shouldEnable()) {
                loadModule(module);
            }
        }
    }

    public abstract Collection<PluginManagedModule> getModules();

    private void enableModules() {
        for (PluginManagedModule module : getRegisteredModules()) {
            if (module.shouldEnable()) {
                enableModule(module);
            }
        }
    }

    public List<PluginManagedModule> getRegisteredModules() {
        return modules;
    }

    private void registerModule(PluginManagedModule module) {
        modules.add(module);
        module._init(this);
        module.setLogger(getLogger());
    }

    private void loadModule(PluginManagedModule module) {
        module.init();
    }

    public void enableModule(PluginManagedModule module) {
        module.doEnable();
        getLogger().log(Level.INFO, "Enabled Module: " + module.getName());
    }

    // 
    // Setting up integration
    //
    private void setUp() {
        setupACF();
        setupLuckPerms();
    }

    private void setupACF() {
        commandManager = new PaperCommandManager(this);
    }

    private void setupLuckPerms() {
        if (Bukkit.getPluginManager().isPluginEnabled("LuckPerms")) {
            luckPerms = LuckPermsProvider.get();
        }
    }

    public @NotNull LuckPerms getLuckPerms() {
        return luckPerms;
    }

    public PaperCommandManager getCommandManager() {
        return commandManager;
    }


    // 
    // Quality of Life utilities
    //
    public void registerEvents(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    public void registerCommand(BaseCommand command) {
        getCommandManager().registerCommand(command);
    }

    public void scheduleSyncDelayedTask(Runnable task, long delay) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, task, delay);
    }

    public void scheduleSyncDelayedTask(Runnable task) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, task);
    }
}
