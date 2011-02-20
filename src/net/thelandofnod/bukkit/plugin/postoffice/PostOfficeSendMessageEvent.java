package net.thelandofnod.bukkit.plugin.postoffice;

import org.bukkit.event.Cancellable;

public class PostOfficeSendMessageEvent extends org.bukkit.event.Event
		implements Cancellable {

	private boolean cancel = false;
	private String sender;
	private String recipient;
	private String message;

	public PostOfficeSendMessageEvent(String sender, String recipient,
			String message) {
		super("PostOfficeSendMessageEvent");
		this.sender = sender;
		this.recipient = recipient;
		this.message = message;
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

	public String getSender() {
		return sender;
	}

	public String getRecipient() {
		return recipient;
	}

	public String getMessage() {
		return message;
	}

}
