package net.thelandofnod.bukkit.plugin.rdbmscore;

//import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.rowset.CachedRowSet;

//import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
//import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.sun.rowset.CachedRowSetImpl;

/**
 * Entry point for the RDBMS core plugin
 */
/**
 * @author josh
 * 
 */
@SuppressWarnings("restriction")
public class RDBMScore extends JavaPlugin {
	private final RDBMScoreEventListener queryListener = new RDBMScoreEventListener(
			this);

	private RDBMScoreClientRegistry clientRegistry = new RDBMScoreClientRegistry();


	@Override
	public void onEnable() {
		
		PluginManager pm = getServer().getPluginManager();

		// Create listeners
		pm.registerEvent(Event.Type.CUSTOM_EVENT, this.queryListener,
				Event.Priority.Normal, this);

		PluginDescriptionFile pdfFile = this.getDescription();
		System.out.println(pdfFile.getName() + " version "
				+ pdfFile.getVersion() + " is enabled!");
	}

	@Override
	public void onDisable() {
		// NOTE: All registered events are automatically unregistered when a
		// plugin is disabled

		PluginDescriptionFile pdfFile = this.getDescription();
		System.out.println(pdfFile.getName() + " version "
				+ pdfFile.getVersion() + " is disabled!");
	}

	/**
	 * This is where we handle the query requests from plugins
	 * 
	 * @param rcqe
	 */
	public void assertQueryResultEvent(RDBMScoreQueryEvent rcqe) {
		Statement stmt = null;
		ResultSet rs = null;
		CachedRowSet rows = null;
		Connection conn = null;
		String ownerPlugin = rcqe.getOwnerPlugin();
		String queryString = rcqe.getQuery();
		boolean stale = false;


		// create our response object and give the sender ownership
		RDBMScoreQueryResultEvent rcqre = new RDBMScoreQueryResultEvent(
				ownerPlugin);

		try {
			if (this.clientRegistry.isPluginRegistered(ownerPlugin)) {
				conn = this.clientRegistry.getRegisteredConnection(ownerPlugin);

				try {
					if (conn.isValid(2000)) {
						// then we are cleared to proceed
						// Throws:
						// SQLException - if a database access error occurs or
						// this method is called on a closed connection
						stmt = conn.createStatement();
					} else {
						// otherwise we have an issue with the connection to the
						// db..
						System.out
								.println("Plugin "
										+ ownerPlugin
										+ " no longer has a valid database connection..");
						// System.out.println("SQL to execute: " + queryString);
						stale = true;

						// attempt to reconnect
						System.out.println("Attempting reconnect..");
						this.reConnect(ownerPlugin);
						conn = this.clientRegistry
								.getRegisteredConnection(ownerPlugin);
						if (conn.isValid(2000)) {
							// then we are cleared to proceed
							// Throws:
							// SQLException - if a database access error occurs
							// or this method is called on a closed connection

							stmt = conn.createStatement();
							stale = false;
						}

					}
				} catch (SQLException e1) {
					System.out
							.println("Connection was most likely stale, exception was thrown on createStatement, or isValid..");
					System.out.println("Plugin: " + ownerPlugin);
					System.out.println("SQL to execute: " + queryString);
					stale = true;
					e1.printStackTrace();
				}

				if (!stale) {
					if (stmt.execute(queryString)) {
						// true means it was a select query
						rs = stmt.getResultSet();

						// Now do something with the ResultSet ....
						rows = new CachedRowSetImpl();
						rows.populate(rs);
						rcqre.setCrs(rows);
					} else {
						// false, means it was an update, insert, or delete
						// lets get the number of rows effected
						rcqre.setEffectedRowCount(stmt.getUpdateCount());
					}
				}
			}

		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			rcqre.setExceptionLog("SQLException: " + ex.getMessage()
					+ " SQLState: " + ex.getSQLState() + " VendorError: "
					+ ex.getErrorCode());
			rcqre.setExceptionCaught(true);

		} finally {
			// it is a good idea to release
			// resources in a finally{} block
			// in reverse-order of their creation
			// if they are no-longer needed
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				rs = null;
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				stmt = null;
			}

			// asserting our query results event ..
			// even if we caught exceptions along the way..
			//RDBMScore.server.getPluginManager().callEvent(rcqre);
			PluginManager pm = getServer().getPluginManager();
			pm.callEvent(rcqre);
		}
	}

	private void reConnect(String ownerPlugin) {
		// attempt to re-establish our database connection

		Connection conn = null;

		String userName = this.clientRegistry
				.getRegisteredConnectionUserName(ownerPlugin);
		String password = this.clientRegistry
				.getRegisteredConnectionPassword(ownerPlugin);
		String dbms = this.clientRegistry
				.getRegisteredConnectionDBMS(ownerPlugin);
		String serverName = this.clientRegistry
				.getRegisteredConnectionServerName(ownerPlugin);
		String portNumber = this.clientRegistry
				.getRegisteredConnectionPortNumber(ownerPlugin);
		String database = this.clientRegistry
				.getRegisteredConnectionDatabase(ownerPlugin);
		String driver = this.clientRegistry
				.getRegisteredConnectionDriver(ownerPlugin);

		if (this.clientRegistry.isPluginRegistered(ownerPlugin)) {
			conn = this.clientRegistry.getRegisteredConnection(ownerPlugin);
			this.clientRegistry.unregisterPlugin(ownerPlugin);
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			conn = null;

		}

		if (!this.clientRegistry.isPluginRegistered(ownerPlugin)) {
			// then create a new connection and register it
			try {
				// conn = getConnection(event);
				conn = GetNewConnect(userName, password, dbms, serverName,
						portNumber, database, driver);
				this.clientRegistry.registerPlugin(conn, ownerPlugin, userName,
						password, dbms, serverName, portNumber, database,
						driver);

				System.out
						.println("Connected to database, and connection registered.");

			} catch (SQLException e) {
				e.printStackTrace();
				conn = null;
			}
		} else {
			System.out.println("plugin " + ownerPlugin
					+ " already has a registered database connection.");
		}

		// RDBMScoreDBDisconnectEvent rcdde = new RDBMScoreDBDisconnectEvent(
		// ownerPlugin,
		// userName,
		// password,
		// dbms,
		// serverName,
		// portNumber,
		// database,
		// driver);
		// this.assertDBDisconnectEvent(rcdde);
		// rcdde = null;
		//
		// RDBMScoreDBConnectEvent rdce = new RDBMScoreDBConnectEvent(
		// ownerPlugin,
		// userName,
		// password,
		// dbms,
		// serverName,
		// portNumber,
		// database,
		// driver);
		// this.assertDBConnectEvent(rdce);
		// rdce = null;

	}

	/**
	 * @param event
	 */
	public void assertDBConnectEvent(RDBMScoreDBConnectEvent event) {
		Connection conn = null;
		String ownerPlugin = "";

		// first see if we already have a connection
		// create one if we don't and register it

		ownerPlugin = event.getOwnerPlugin();
		// System.out.println("Event Owner: " + ownerPlugin);
		if (!this.clientRegistry.isPluginRegistered(ownerPlugin)) {
			// then create a new connection and register it
			try {

				conn = getConnection(event);
				this.clientRegistry.registerPlugin(conn, ownerPlugin,
						event.getUserName(), event.getPassword(),
						event.getDbms(), event.getServerName(),
						event.getPortNumber(), event.getDatabase(),
						event.getDriver());

				System.out
						.println("Connected to database, and connection registered.");

			} catch (SQLException e) {
				e.printStackTrace();
				conn = null;
			}
		} else {
			System.out.println("plugin " + ownerPlugin
					+ " already has a registered database connection.");
		}
	}

	public void assertDBDisconnectEvent(RDBMScoreDBDisconnectEvent event) {
		Connection conn = null;
		String ownerPlugin;

		// first see if we already have a connection
		// create one if we don't and register it

		ownerPlugin = event.getOwnerPlugin();
		if (this.clientRegistry.isPluginRegistered(ownerPlugin)) {
			conn = this.clientRegistry.getRegisteredConnection(ownerPlugin);
			this.clientRegistry.unregisterPlugin(ownerPlugin);
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			conn = null;

		}
	}

	private Connection getConnection(RDBMScoreDBConnectEvent event)
			throws SQLException {

		// Get the driver ..
		try {
			Class.forName(event.getDriver()).newInstance();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}

		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", event.getUserName());
		connectionProps.put("password", event.getPassword());

		conn = DriverManager.getConnection("jdbc:" + event.getDbms() + "://"
				+ event.getServerName() + ":" + event.getPortNumber() + "/"
				+ event.getDatabase(), connectionProps);
		return conn;
	}

	private Connection GetNewConnect(String userName, String password,
			String dbms, String serverName, String portNumber, String database,
			String driver) throws SQLException {

		// Get the driver ..
		try {
			Class.forName(driver).newInstance();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}

		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", userName);
		connectionProps.put("password", password);

		conn = DriverManager.getConnection("jdbc:" + dbms + "://" + serverName
				+ ":" + portNumber + "/" + database, connectionProps);
		return conn;
	}

}
