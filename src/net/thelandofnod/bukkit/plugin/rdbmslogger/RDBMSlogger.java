package net.thelandofnod.bukkit.plugin.rdbmslogger;

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

public class RDBMSlogger extends JavaPlugin {
	private final RDBMSloggerEventListener eventListener = new RDBMSloggerEventListener(
			this);
	public static Server server;
	public Player callingPlayer;
	PluginDescriptionFile pdfFile;

	public enum applicationState {
		INIT, NORMAL;
	}

	private applicationState currentState;

	public RDBMSlogger(PluginLoader pluginLoader, Server instance,
			PluginDescriptionFile desc, File folder, File plugin,
			ClassLoader cLoader) {
		super(pluginLoader, instance, desc, folder, plugin, cLoader);
		server = instance;
		this.setCurrentState(applicationState.INIT);
	}

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEnable() {
		System.out.println("Enabling Logger..");

		// Register our events
		PluginManager pm = getServer().getPluginManager();

		// handles ad-hoc queries
		pm.registerEvent(Event.Type.CUSTOM_EVENT, this.eventListener,
				Event.Priority.Normal, this);

		// for logging of events
		// pm.registerEvent(Event.Type., arg1, arg2, arg3)

		pdfFile = this.getDescription();

		// assert out DB Connection Event..
		this.assertDBConnectEvent("minecraftServer", "minecraftServer",
				"mysql", "localhost", "3306", "minecraftServer",
				"com.mysql.jdbc.Driver");

		// begin run-time configuration
		this.assertRDBMSloggerInitEvent();

		System.out.println(pdfFile.getName() + " version "
				+ pdfFile.getVersion() + " is enabled!");

	}

	private void assertRDBMSloggerInitEvent() {
		RDBMSloggerInitEvent rlie = new RDBMSloggerInitEvent(
				"RDBMSloggerInitEvent");
		RDBMSlogger.server.getPluginManager().callEvent(rlie);
	}

	public void assertQueryEvent(String query) {
		RDBMScoreQueryEvent rcqe = new RDBMScoreQueryEvent(pdfFile.getName(),
				query);
		RDBMSlogger.server.getPluginManager().callEvent(rcqe);
	}

	public void assertDBConnectEvent(String userName, String password,
			String dbms, String serverName, String portNumber, String database,
			String driver) {
		RDBMScoreDBConnectEvent rcdce = new RDBMScoreDBConnectEvent(
				pdfFile.getName(), userName, password, dbms, serverName,
				portNumber, database, driver);
		RDBMSlogger.server.getPluginManager().callEvent(rcdce);
	}

	public void setCurrentState(applicationState currentState) {
		this.currentState = currentState;
	}

	public applicationState getCurrentState() {
		return currentState;
	}
}
