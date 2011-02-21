package net.thelandofnod.bukkit.plugin.rdbmscore;

import java.sql.Connection;
import java.util.HashMap;

public class RDBMScoreClientRegistry {

	// HashMap of connection objects to plugins names (as string key)
	HashMap<String, Connection> connectionRegistry = new HashMap<String, Connection>();

	HashMap<String, String> userNameRegistry = new HashMap<String, String>();
	HashMap<String, String> passwordRegistry = new HashMap<String, String>();
	HashMap<String, String> dbmsRegistry = new HashMap<String, String>();
	HashMap<String, String> serverNameRegistry = new HashMap<String, String>();
	HashMap<String, String> portNumberRegistry = new HashMap<String, String>();
	HashMap<String, String> databaseRegistry = new HashMap<String, String>();
	HashMap<String, String> driverRegistry = new HashMap<String, String>();

	public boolean isPluginRegistered(String ownerPlugin) {
		// System.out
		// .println("Attempting to send back isPluginRegistered result..");
		return connectionRegistry.containsKey(ownerPlugin);

	}

	public void registerPlugin(Connection conn, String ownerPlugin,
			String userName, String password, String dbms, String serverName,
			String portNumber, String database, String driver) {
		if (!isPluginRegistered(ownerPlugin)) {
			this.connectionRegistry.put(ownerPlugin, conn);

			this.userNameRegistry.put(ownerPlugin, userName);
			this.passwordRegistry.put(ownerPlugin, password);
			this.dbmsRegistry.put(ownerPlugin, dbms);
			this.serverNameRegistry.put(ownerPlugin, serverName);
			this.portNumberRegistry.put(ownerPlugin, portNumber);
			this.databaseRegistry.put(ownerPlugin, database);
			this.driverRegistry.put(ownerPlugin, driver);
		}

	}

	public void unregisterPlugin(String ownerPlugin) {
		if (isPluginRegistered(ownerPlugin)) {
			this.connectionRegistry.remove(ownerPlugin);

			this.userNameRegistry.remove(ownerPlugin);
			this.passwordRegistry.remove(ownerPlugin);
			this.dbmsRegistry.remove(ownerPlugin);
			this.serverNameRegistry.remove(ownerPlugin);
			this.portNumberRegistry.remove(ownerPlugin);
			this.databaseRegistry.remove(ownerPlugin);
			this.driverRegistry.remove(ownerPlugin);
		}

	}

	public Connection getRegisteredConnection(String ownerPlugin) {
		if (isPluginRegistered(ownerPlugin)) {
			return connectionRegistry.get(ownerPlugin);
		}
		return null;
	}

	public String getRegisteredConnectionUserName(String ownerPlugin) {
		if (isPluginRegistered(ownerPlugin)) {
			return userNameRegistry.get(ownerPlugin);
		}
		return null;
	}

	public String getRegisteredConnectionPassword(String ownerPlugin) {
		if (isPluginRegistered(ownerPlugin)) {
			return passwordRegistry.get(ownerPlugin);
		}
		return null;
	}

	public String getRegisteredConnectionDBMS(String ownerPlugin) {
		if (isPluginRegistered(ownerPlugin)) {
			return dbmsRegistry.get(ownerPlugin);
		}
		return null;
	}

	public String getRegisteredConnectionServerName(String ownerPlugin) {
		if (isPluginRegistered(ownerPlugin)) {
			return serverNameRegistry.get(ownerPlugin);
		}
		return null;
	}

	public String getRegisteredConnectionPortNumber(String ownerPlugin) {
		if (isPluginRegistered(ownerPlugin)) {
			return portNumberRegistry.get(ownerPlugin);
		}
		return null;
	}

	public String getRegisteredConnectionDatabase(String ownerPlugin) {
		if (isPluginRegistered(ownerPlugin)) {
			return databaseRegistry.get(ownerPlugin);
		}
		return null;
	}

	public String getRegisteredConnectionDriver(String ownerPlugin) {
		if (isPluginRegistered(ownerPlugin)) {
			return driverRegistry.get(ownerPlugin);
		}
		return null;
	}


}
