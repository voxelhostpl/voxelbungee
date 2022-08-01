package pl.voxelhost.voxelbungee;

import java.util.Map;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.protocol.packet.Handshake;

public class ServerConnectListener implements Listener {

    private final String defaultHost;
    private final Map<String, String> hosts;

    public ServerConnectListener(final String defaultHost, final Map<String, String> hosts) {
        this.defaultHost = defaultHost;
        this.hosts = hosts;
    }

    @EventHandler(priority = 64)
    public void onServerConnect(final ServerConnectEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Connection connection = event.getPlayer();
        if (!(connection instanceof UserConnection)) {
            return;
        }

        final UserConnection userConnection = (UserConnection) connection;
        final Handshake handshake = userConnection.getPendingConnection().getHandshake();
        final String targetServerKey = event.getTarget().getName();
        final String host = this.hosts.getOrDefault(targetServerKey, this.defaultHost);

        handshake.setHost(host);
    }

}
