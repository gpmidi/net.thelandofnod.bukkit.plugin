package net.thelandofnod.bukkit.plugin.rdbmscore;

import javax.sql.rowset.CachedRowSet;

import org.bukkit.event.Cancellable;


public class RDBMScoreQueryResultEvent extends org.bukkit.event.Event implements Cancellable {
	private boolean cancel = false;
	private CachedRowSet crs;
	
	public RDBMScoreQueryResultEvent() {
		super("RDBMScoreQueryResultEvent");
	}

	public void setCrs(CachedRowSet crs) {
		this.crs = crs;
	}

	public CachedRowSet getCrs() {
		return crs;
	}

	public boolean isCancelled() {
		return cancel;
	}

	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	} 

	
	
}
