package net.thelandofnod.bukkit.plugin.rdbmscore;

import org.bukkit.event.Cancellable;

public class RDBMScoreQueryEvent extends org.bukkit.event.Event implements
        Cancellable {
    private boolean cancel = false;
    private String queryText;
    private String ownerPlugin;

    public RDBMScoreQueryEvent(String ownerPlugin, String query) {
        super("RDBMScoreQueryEvent");
        queryText = query;
        this.setOwnerPlugin(ownerPlugin);
    }

    public String getQuery() {
        return queryText;
    }

    //    @Override
    public boolean isCancelled() {
        return cancel;
    }

    //    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    private void setOwnerPlugin(String ownerPlugin) {
        this.ownerPlugin = ownerPlugin;
    }

    public String getOwnerPlugin() {
        return ownerPlugin;
    }

}
