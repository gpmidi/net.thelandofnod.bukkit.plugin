package net.thelandofnod.bukkit.plugin.postoffice;

import org.bukkit.event.Cancellable;

public class PostOfficeMarkMessagesReadEvent extends org.bukkit.event.Event
		implements Cancellable {

	private boolean cancel = false;
	private String recipient;

	public PostOfficeMarkMessagesReadEvent(String recipient) {
		super("PostOfficeMarkMessagesReadEvent");
		this.recipient = recipient;
	}

	@Override
	public boolean isCancelled() {
		// TODO Auto-generated method stub
		return cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

	public String getRecipient() {
		return recipient;
	}
}
