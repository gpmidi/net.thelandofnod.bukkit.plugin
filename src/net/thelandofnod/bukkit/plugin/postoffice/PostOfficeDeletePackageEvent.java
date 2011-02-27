package net.thelandofnod.bukkit.plugin.postoffice;

import org.bukkit.event.Cancellable;

public class PostOfficeDeletePackageEvent extends org.bukkit.event.Event
        implements Cancellable {

    private boolean cancel = false;
    private String playerName;
    private Integer messageIndex;

    protected PostOfficeDeletePackageEvent(String name, int messageId) {
        super("PostOfficeDeletePackageEvent");
        this.playerName = name;
        this.messageIndex = messageId;
    }

    //    @Override
    public boolean isCancelled() {
        return cancel;
    }

    //    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Integer getMessageIndex() {
        return messageIndex;
    }

}
