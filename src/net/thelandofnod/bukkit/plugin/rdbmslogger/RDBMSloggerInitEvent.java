package net.thelandofnod.bukkit.plugin.rdbmslogger;

import org.bukkit.event.Cancellable;

public class RDBMSloggerInitEvent extends org.bukkit.event.Event implements Cancellable {
	private boolean cancel = false;
	
	protected RDBMSloggerInitEvent(String name) {
		super(name);
		
		// now we need to load our 
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

}
