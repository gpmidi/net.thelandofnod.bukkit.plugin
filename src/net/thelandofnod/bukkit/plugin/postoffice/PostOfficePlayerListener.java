package net.thelandofnod.bukkit.plugin.postoffice;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerListener;

public class PostOfficePlayerListener extends PlayerListener {
    private final PostOffice plugin;

    public PostOfficePlayerListener(PostOffice instance) {
        plugin = instance;
    }

    @Override
    public void onPlayerJoin(PlayerEvent event) {
        if (event.getType().equals(Event.Type.PLAYER_JOIN)) {
            // then lets register the current player
            plugin.assertRegisterPlayerEvent(event.getPlayer().getName());
        }
    }

    // This method is called whenever a player uses a command.
    @Override
    public void onPlayerCommand(PlayerChatEvent event) {
        /*Usage:
        *   /postoffice
        *       inbox
        *       read message {messageId}
        *       read package
        *       send message {player} {message}
        *       send package {player} {amount} {materialId}
        *       delete message {messageId}
        *       delete package {packageId}
        *       package send {player} {amount} {materialId}
        *       package get
        *       package delete {packageId}
        *       package recall
        */

        try {
            String[] split = event.getMessage().split(" ");
            String i = split[0].toLowerCase();
            if (i.equals("/postoffice") || i.equals("/po")) {
                event.setCancelled(true);
                plugin.callingPlayer = event.getPlayer();
                String i1 = split[1].toLowerCase();
                if (i1.equals("read")) {
                    String i2 = split[2].toLowerCase();
                    if (i2.equals("package")) {
                        plugin.assertReceivePackageEvent(event.getPlayer().getName(), Integer.valueOf(split[3]));
                    } else if (i2.equals("message")) {
                        plugin.assertReadMessageEvent(event.getPlayer().getName(), Integer.valueOf(split[3]));
                    }
                } else if (i1.equals("send")) {
                    String i2 = split[2].toLowerCase();
                    if (i2.equals("package")) {
                        String recipient = split[3];
                        int amount = Integer.parseInt(split[4].trim());
                        int materialId = Integer.parseInt(split[5].trim());
                        plugin.assertSendPackageEvent(event.getPlayer().getName(), recipient, materialId, amount);
                    } else if (i2.equals("message")) {
                        String message = event.getMessage().substring(
                                split[0].length() + split[1].length() + split[2].length()
                                        + split[3].length() + 4);
                        plugin.assertSendMessageEvent(event.getPlayer()
                                .getName(), split[3].toLowerCase().toString(), message);
                    }
                } else if (i1.equals("delete")) {
                    String i2 = split[2].toLowerCase().toString();
                    if (i2.equals("package")) {
                        plugin.assertDeletePackageEvent(event.getPlayer().getName(), Integer.valueOf(split[3]));

                    } else if (i2.equals("message")) {
                        plugin.assertDeleteMessageEvent(event.getPlayer().getName(), Integer.valueOf(split[3]));
                    }
                } else if (i1.equals("package")) {
                    String i2 = split[2].toLowerCase().toString();
                    if (i2.equals("send")) {
                        String recipient = split[3];
                        int amount = Integer.parseInt(split[4].trim());
                        int materialId = Integer.parseInt(split[5].trim());
                        plugin.assertSendPackageEvent(event.getPlayer().getName(), recipient, materialId, amount);
                    } else if (i2.equals("get")) {
                        plugin.assertReceivePackageEvent(event.getPlayer().getName(), Integer.valueOf(split[3]));
                    } else if (i2.equals("delete")) {
                        plugin.assertDeletePackageEvent(event.getPlayer().getName(), Integer.valueOf(split[3]));
                    } else if (i2.equals("recall")) {
                        plugin.assertRecallPackageEvent(event.getPlayer().getName());
                    }
                } else {
//                    plugin.assertPostOfficeViewInboxEvent(event.getPlayer().getName());
                    // else present the stand usage message
                    event.getPlayer().sendMessage("Usage: /postoffice or /po");
                    event.getPlayer().sendMessage("read message {messageId}");
                    event.getPlayer().sendMessage("read package {packageId}");
                    event.getPlayer().sendMessage("send message {player} {message}");
                    event.getPlayer().sendMessage("send package {player} {amount} {materialId}");
                    event.getPlayer().sendMessage("delete message {messageId}");
                    event.getPlayer().sendMessage("delete package {packageId}");
                    event.getPlayer().sendMessage("package send {player} {amount} {materialId}");
                    event.getPlayer().sendMessage("package get {packageId}");
                    event.getPlayer().sendMessage("package delete {packageId}");
                    event.getPlayer().sendMessage("package recall");
                }
            }
        } catch (Exception ArrayIndexOutOfBoundsException) {
            plugin.assertPostOfficeViewInboxEvent(event.getPlayer().getName());
        }
    }
}
