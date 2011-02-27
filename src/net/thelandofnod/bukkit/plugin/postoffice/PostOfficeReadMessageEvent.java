package net.thelandofnod.bukkit.plugin.postoffice;

import org.bukkit.event.Cancellable;

public class PostOfficeReadMessageEvent extends org.bukkit.event.Event
        implements Cancellable {

    private boolean cancel = false;
    private String recipient;
    private Integer messageIndex;

    public PostOfficeReadMessageEvent(String recipient, Integer messageId) {
        super("PostOfficeReadMessageEvent");
        this.recipient = recipient;
        this.messageIndex = messageId;
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
