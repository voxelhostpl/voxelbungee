package pl.voxelhost.voxelbungee;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.md_5.bungee.BungeeServerInfo;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;

public final class VoxelBungee extends Plugin {

    private String defaultHost;
    private final Map<String, String> hosts = new HashMap<>();

    @Override
    public void onEnable() {
        final Optional<Configuration> configOptional = ConfigUtil.loadConfigFile(this, "config.yml");
        if (!configOptional.isPresent()) {
            return;
        }

        this.loadConfig(configOptional.get());
        this.hackBungeecord();

        final PluginManager pluginManager = this.getProxy().getPluginManager();
        pluginManager.registerListener(this, new ServerConnectListener(this.defaultHost, this.hosts));
    }

    private void loadConfig(final Configuration cfg) {
        this.defaultHost = cfg.getString("defaultHost");
        final Configuration hostsSection = cfg.getSection("hosts");
        hostsSection.getKeys().forEach(key -> this.hosts.put(key, hostsSection.getString(key)));
    }

    // Make equals() in BungeeServerInfo look at names, not addresses
    private void hackBungeecord() {
        ByteBuddyAgent.install();
        final ByteBuddy byteBuddy = new ByteBuddy();

        byteBuddy
                .rebase(BungeeServerInfoInterceptor.class)
                .make()
                .load(BungeeServerInfo.class.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());

        byteBuddy
                .redefine(BungeeServerInfo.class)
                .method(ElementMatchers.isEquals())
                .intercept(MethodDelegation.to(BungeeServerInfoInterceptor.class))
                .make()
                .load(BungeeServerInfo.class.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
    }

}
