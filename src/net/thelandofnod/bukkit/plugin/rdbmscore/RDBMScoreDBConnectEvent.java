package net.thelandofnod.bukkit.plugin.rdbmscore;

import org.bukkit.event.Cancellable;

public class RDBMScoreDBConnectEvent extends org.bukkit.event.Event implements Cancellable {
	private boolean cancel = false;
	private String userName, password, dbms, serverName, portNumber, database, driver;
	
	public RDBMScoreDBConnectEvent(String userName, String password, String dbms, String serverName, String portNumber, String database, String driver) {
		super("RDBMScoreDBConnectEvent");

        setUserName(userName);
        setPassword(password);
        setDatabase(database);
        setDbms(dbms);
        setServerName(serverName);
        setPortNumber(portNumber);
        setDriver(driver);

	}

	@Override
	public boolean isCancelled() {
		// TODO Auto-generated method stub
		return cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setDbms(String dbms) {
		this.dbms = dbms;
	}

	public String getDbms() {
		return dbms;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getServerName() {
		return serverName;
	}

	public void setPortNumber(String portNumber) {
		this.portNumber = portNumber;
	}

	public String getPortNumber() {
		return portNumber;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getDatabase() {
		return database;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getDriver() {
		return driver;
	}

}
