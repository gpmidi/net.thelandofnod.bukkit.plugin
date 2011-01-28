package net.thelandofnod.bukkit.plugin.rdbmscore;

import javax.sql.rowset.CachedRowSet;

import org.bukkit.event.Cancellable;


public class RDBMScoreQueryResultEvent extends org.bukkit.event.Event implements Cancellable {
	private boolean cancel = false;
	private CachedRowSet crs;
	private int effectedRowCount = -1;
	private String exceptionLog;
	private boolean exceptionCaught = false;
	
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
