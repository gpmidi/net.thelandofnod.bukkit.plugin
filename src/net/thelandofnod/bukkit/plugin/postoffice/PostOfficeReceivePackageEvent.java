package net.thelandofnod.bukkit.plugin.postoffice;

import org.bukkit.event.Cancellable;

public class PostOfficeReceivePackageEvent extends org.bukkit.event.Event
		implements Cancellable {
	private boolean cancel = false;
	private String recipient;

	public PostOfficeReceivePackageEvent(String recipient) {
		super("PostOfficeReceivePackageEvent");
		this.recipient = recipient;
	}

	@Override
	public boolean isCancelled() {
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
