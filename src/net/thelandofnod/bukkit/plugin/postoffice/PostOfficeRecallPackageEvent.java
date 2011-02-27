package net.thelandofnod.bukkit.plugin.postoffice;

import org.bukkit.event.Cancellable;

public class PostOfficeRecallPackageEvent extends org.bukkit.event.Event
        implements Cancellable {

    private boolean cancel = false;
    private String recipient;

    protected PostOfficeRecallPackageEvent(String name) {
        super("PostOfficeRecallPackageEvent");
        this.recipient = name;
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
