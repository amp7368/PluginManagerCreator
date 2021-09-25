package plugin.util.plugin;

import java.util.Collection;
import java.util.Collections;

public class PluginInstance extends PluginManaged {
    @Override
    public Collection<PluginManagedModule> getModules() {
        return Collections.emptyList();
    }
}
