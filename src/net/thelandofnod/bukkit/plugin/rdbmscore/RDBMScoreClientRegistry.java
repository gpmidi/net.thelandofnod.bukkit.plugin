package net.thelandofnod.bukkit.plugin.rdbmscore;

import java.sql.Connection;
import java.util.HashMap;

public class RDBMScoreClientRegistry {

	// HashMap of connection objects to plugins names (as string key)
	HashMap<String, Connection> connectionRegistry = new HashMap<String, Connection>();

	public boolean isPluginRegistered(String ownerPlugin) {
		System.out
				.println("Attempting to send back isPluginRegistered result..");
		return connectionRegistry.containsKey(ownerPlugin);

	}

	public void registerPlugin(Connection conn, String ownerPlugin) {
		if (!isPluginRegistered(ownerPlugin)) {
			this.connectionRegistry.put(ownerPlugin, conn);
		}

	}

	public void unregisterPlugin(String ownerPlugin) {
		if (isPluginRegistered(ownerPlugin)) {
			this.connectionRegistry.remove(ownerPlugin);
		}

	}

	public Connection getRegisteredConnection(String ownerPlugin) {
		if (isPluginRegistered(ownerPlugin)) {
			return connectionRegistry.get(ownerPlugin);
		}
		return null;
	}

}
