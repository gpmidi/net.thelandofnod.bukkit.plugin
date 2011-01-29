package net.thelandofnod.bukkit.plugin.rdbmsclient;

import java.io.File;

import net.thelandofnod.bukkit.plugin.rdbmscore.RDBMScoreDBConnectEvent;
import net.thelandofnod.bukkit.plugin.rdbmscore.RDBMScoreQueryEvent;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class RDBMSclient extends JavaPlugin {
	private final RDBMSclientEventListener eventListener = new RDBMSclientEventListener(this);
	private final RDBMSclientPlayerListener playerListener = new RDBMSclientPlayerListener(this);
	public static Server server;
	public Player callingPlayer;
	PluginDescriptionFile pdfFile;
	
	public RDBMSclient(PluginLoader pluginLoader, Server instance, PluginDescriptionFile desc, File folder, File plugin, ClassLoader cLoader) {
        super(pluginLoader, instance, desc, folder, plugin, cLoader);
        server = instance;
	}

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEnable() {
		// TODO
		// add rdbmscore plugin dependencies...
		//Plugin p = getServer().getPluginManager().getPlugin(pluginName); ?
				
				
		 // Register our events
        PluginManager pm = getServer().getPluginManager();
        
        pm.registerEvent(Event.Type.CUSTOM_EVENT, this.eventListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_COMMAND, this.playerListener, Event.Priority.Normal, this);
        
        pdfFile = this.getDescription();
        
        // assert out DB Connection Event..
        this.assertDBConnectEvent("minecraftServer",
        						  "minecraftServer", 
        						  "mysql", 
        						  "localhost", 
        						  "3306", 
        						  "minecraftServer", 
        						  "com.mysql.jdbc.Driver");
        

        
        System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
		
	}

	public void assertQueryEvent(String query) {		
		RDBMScoreQueryEvent rcqe = new RDBMScoreQueryEvent(pdfFile.getName(), query);
		RDBMSclient.server.getPluginManager().callEvent(rcqe);
	}
	
	public void assertDBConnectEvent(String userName, String password, String dbms, String serverName, String portNumber, String database, String driver){
		//System.out.println("assertDBConnectEvent(" + pdfFile.getName() + ")");
		RDBMScoreDBConnectEvent rcdce = new RDBMScoreDBConnectEvent(pdfFile.getName(), userName, password, dbms, serverName, portNumber, database, driver);
		RDBMSclient.server.getPluginManager().callEvent(rcdce);
	}
}
