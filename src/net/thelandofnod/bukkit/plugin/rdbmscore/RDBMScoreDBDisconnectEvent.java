package net.thelandofnod.bukkit.plugin.rdbmscore;

import org.bukkit.event.Cancellable;

public class RDBMScoreDBDisconnectEvent extends org.bukkit.event.Event
        implements Cancellable {
    private boolean cancel = false;
    String ownerPlugin;

    public String getOwnerPlugin() {
        return ownerPlugin;
    }

    private void setOwnerPlugin(String ownerPlugin) {
        this.ownerPlugin = ownerPlugin;
    }

    public RDBMScoreDBDisconnectEvent(String ownerPlugin, String userName,
                                      String password, String dbms, String serverName, String portNumber,
                                      String database, String driver) {
        super("RDBMScoreDBDisonnectEvent");
        setOwnerPlugin(ownerPlugin);
    }

    //    @Override
    public boolean isCancelled() {
        return cancel;
    }

    //    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

}
