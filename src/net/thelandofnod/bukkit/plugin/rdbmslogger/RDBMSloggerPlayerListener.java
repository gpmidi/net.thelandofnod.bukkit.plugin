package net.thelandofnod.bukkit.plugin.rdbmslogger;

import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;

public class RDBMSloggerPlayerListener extends PlayerListener {
	private final RDBMSlogger plugin;
	
    public RDBMSloggerPlayerListener(RDBMSlogger instance) {
        plugin = instance;
    }
	
    //This method is called whenever a player uses a command.
    public void onPlayerCommand(PlayerChatEvent event) {    	
         //Make the message a string.
               String[] split = event.getMessage().split(" ");

               if (split[0].equalsIgnoreCase("/query")){
            		   String[] queryText = event.getMessage().split("/query ");
            		   
            		   // store calling player for later reference ..
            		   plugin.callingPlayer = event.getPlayer();
            		   
            		   plugin.assertQueryEvent(queryText[1]);
               }
               event.setCancelled(true);
       }

}
