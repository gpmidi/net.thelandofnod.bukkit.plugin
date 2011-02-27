package net.thelandofnod.bukkit.plugin.postoffice;

import org.bukkit.event.Cancellable;

public class PostOfficeMarkMessageReadEvent extends org.bukkit.event.Event
        implements Cancellable {
    private boolean cancel = false;
    private String recipient;
    private Integer messageIndex;

    public PostOfficeMarkMessageReadEvent(String recipient, Integer messageIndex) {
        super("PostOfficeMarkMessageReadEvent");
        this.recipient = recipient;
        this.messageIndex = messageIndex;
    }

    //    @Override
    public boolean isCancelled() {
        // TODO Auto-generated method stub
        return cancel;
    }

    //    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public String getRecipient() {
        return recipient;
    }

    public Integer getMessageIndex() {
        return messageIndex;
    }
}
