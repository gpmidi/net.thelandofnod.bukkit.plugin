package net.thelandofnod.bukkit.plugin.postoffice;

import org.bukkit.event.Cancellable;

public class PostOfficeViewInboxEvent extends org.bukkit.event.Event
implements Cancellable  {
	
	private boolean cancel = false;
	private String playerName;
	
	protected PostOfficeViewInboxEvent(String name) {
		super("PostOfficeViewInboxEvent");
		this.playerName = name;
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

	public String getPlayerName() {
		return playerName;
	}

}
