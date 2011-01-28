package net.thelandofnod.bukkit.plugin.rdbmscore;

import org.bukkit.event.Cancellable;

public class RDBMScoreQueryEvent extends org.bukkit.event.Event implements Cancellable {
	private boolean cancel = false;
	private String queryText;
		
	public RDBMScoreQueryEvent(String query) {
		super("RDBMScoreQueryEvent");
		queryText = query;
	}

	public String getQuery(){
		return queryText;
	}
	
	public boolean isCancelled() {
		return cancel;
	}

	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}
	
}
