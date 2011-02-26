package net.thelandofnod.bukkit.plugin.postoffice;

import org.bukkit.event.Cancellable;

public class PostOfficeSendPackageEvent extends org.bukkit.event.Event
		implements Cancellable {

	private boolean cancel = false;
	private String sender;
	private String recipient;
	private int materialId;
	private int amount;

	public PostOfficeSendPackageEvent(String sender, String recipient,
			int materialId, int amount) {
		super("PostOfficeSendPackageEvent");
		this.sender = sender;
		this.recipient = recipient;
		this.materialId = materialId;
		this.amount = amount;
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

	public int getMaterialId() {
		return materialId;
	}

	public int getAmount() {
		return amount;
	}

}
