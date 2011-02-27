package net.thelandofnod.bukkit.plugin.postoffice;

import org.bukkit.event.Cancellable;

public class PostOfficeMarkPackagesReadEvent extends org.bukkit.event.Event
        implements Cancellable {

    private boolean cancel = false;
    private String recipient;

    public PostOfficeMarkPackagesReadEvent(String recipient) {
        super("PostOfficeMarkPackagesReadEvent");
        this.recipient = recipient;
    }

    //    @Override
    public boolean isCancelled() {
        return cancel;
    }

    //    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public String getRecipient() {
        return recipient;
    }

}
