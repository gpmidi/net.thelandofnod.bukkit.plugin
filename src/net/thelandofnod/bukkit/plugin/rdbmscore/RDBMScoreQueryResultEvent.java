package net.thelandofnod.bukkit.plugin.rdbmscore;

import org.bukkit.event.Cancellable;

import javax.sql.rowset.CachedRowSet;

public class RDBMScoreQueryResultEvent extends org.bukkit.event.Event implements
        Cancellable {
    private boolean cancel = false;
    private CachedRowSet crs;
    private int effectedRowCount = -1;
    private String exceptionLog;
    private boolean exceptionCaught = false;
    private String ownerPlugin;

    public RDBMScoreQueryResultEvent(String ownerPlugin) {
        super("RDBMScoreQueryResultEvent");
        this.setOwnerPlugin(ownerPlugin);
    }

    private void setOwnerPlugin(String ownerPlugin) {
        this.ownerPlugin = ownerPlugin;

    }

    public String getOwnerPlugin() {
        return this.ownerPlugin;
    }

    public void setCrs(CachedRowSet crs) {
        this.crs = crs;
    }

    public CachedRowSet getCrs() {
        return crs;
    }

    //    @Override
    public boolean isCancelled() {
        return cancel;
    }

    //    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public void setEffectedRowCount(int effectedRowCount) {
        this.effectedRowCount = effectedRowCount;
    }

    public Integer getEffectedRowCount() {
        return effectedRowCount;
    }

    public void setExceptionLog(String exceptionLog) {
        this.exceptionLog = exceptionLog;
    }

    public String getExceptionLog() {
        return exceptionLog;
    }

    public void setExceptionCaught(boolean exceptionCaught) {
        this.exceptionCaught = exceptionCaught;
    }

    public boolean isExceptionCaught() {
        return exceptionCaught;
    }

}
