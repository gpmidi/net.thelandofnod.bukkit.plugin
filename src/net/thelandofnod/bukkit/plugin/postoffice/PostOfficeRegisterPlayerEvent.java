package net.thelandofnod.bukkit.plugin.postoffice;

import org.bukkit.event.Cancellable;

public class PostOfficeRegisterPlayerEvent extends org.bukkit.event.Event
        implements Cancellable {

    private boolean cancel = false;
    private String player;

    protected PostOfficeRegisterPlayerEvent(String name) {
        super("PostOfficeRegisterPlayerEvent");
        this.player = name;
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

    public String getPlayer() {
        return player;
    }

}
