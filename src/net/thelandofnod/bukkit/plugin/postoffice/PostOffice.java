package net.thelandofnod.bukkit.plugin.postoffice;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import net.thelandofnod.bukkit.plugin.rdbmscore.RDBMScoreDBConnectEvent;
import net.thelandofnod.bukkit.plugin.rdbmscore.RDBMScoreDBDisconnectEvent;
import net.thelandofnod.bukkit.plugin.rdbmscore.RDBMScoreQueryEvent;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PostOffice extends JavaPlugin {
	private final PostOfficeEventListener eventListener = new PostOfficeEventListener(
			this);
	private final PostOfficePlayerListener playerListener = new PostOfficePlayerListener(
			this);

	PluginDescriptionFile pdfFile;
	Properties dbProperties = new Properties();

	public Player callingPlayer;
	public int materialId;
	public int amount;

	public enum applicationState {
		RESPONSE_READ, RESPONSE_PACKAGE_RECEIVE, NORMAL, WAIT_FOR_SEND_RECEIPT, SHOW_INBOX
	}

	private applicationState currentState;
	private boolean isEnabled = true;

	public int getMaterialId() {
		return materialId;
	}

	public void setMaterialId(int materialId) {
		this.materialId = materialId;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	@Override
	public void onDisable() {
		if (this.isEnabled) {
			this.assertDBDisonnectEvent(
					dbProperties.getProperty("db.username"),
					dbProperties.getProperty("db.password"),
					dbProperties.getProperty("db.rdbms"),
					dbProperties.getProperty("db.servername"),
					dbProperties.getProperty("db.portnumber"),
					dbProperties.getProperty("db.database"),
					dbProperties.getProperty("db.driver"));
		}
		this.isEnabled = false;

		System.out.println(pdfFile.getName() + " version "
				+ pdfFile.getVersion() + " is disabled!");
	}

	@Override
	public void onEnable() {

		this.setCurrentState(applicationState.NORMAL);

		// load our postoffice.properties file
		try {
			dbProperties.load(new FileInputStream("postoffice.properties"));
			// Register our events
			PluginManager pm = getServer().getPluginManager();

			// Dependency Check
			// Ensure that rdbmscore has been loaded otherwise we disable
			// ourselves...
			Plugin p = null;
			p = getServer().getPluginManager().getPlugin("rdbmscore");
			if (p != null) {

				// see if plugin is loaded
				if (p.isEnabled()) {
					pm.registerEvent(Event.Type.CUSTOM_EVENT, this.eventListener,
							Event.Priority.Normal, this);
					pm.registerEvent(Event.Type.PLAYER_COMMAND, this.playerListener, 
							Event.Priority.Normal, this);
					pm.registerEvent(Event.Type.PLAYER_JOIN, this.playerListener,
							Event.Priority.Normal, this);

					pdfFile = this.getDescription();

					// assert out DB Connection Event..
					this.assertDBConnectEvent(
							dbProperties.getProperty("db.username"),
							dbProperties.getProperty("db.password"),
							dbProperties.getProperty("db.rdbms"),
							dbProperties.getProperty("db.servername"),
							dbProperties.getProperty("db.portnumber"),
							dbProperties.getProperty("db.database"),
							dbProperties.getProperty("db.driver"));

					System.out.println("[postoffice] " + pdfFile.getName() + " version "
							+ pdfFile.getVersion() + " is enabled!");
				} else {
					// then we don't have our dependencies!
					System.out
							.println("[postoffice] rdbmscore must load prior to this plugin, going dormant!");
					this.isEnabled = false;
				}
			} else {
				// then we don't have our dependencies!
				System.out
						.println("[postoffice] Could not find dependency rdbmscore, going dormant!");
				this.isEnabled = false;
			}
		} catch (FileNotFoundException e) {
			System.out
			.println("[postoffice] Could not find postoffice.properties, going dormant!");
			e.printStackTrace();
			this.isEnabled = false;
		} catch (IOException e) {
			System.out
			.println("[postoffice] Could not open postoffice.properties, going dormant!");
			e.printStackTrace();
			this.isEnabled = false;
		}
	}

	public void setCurrentState(applicationState currentState) {
		this.currentState = currentState;
	}

	public applicationState getCurrentState() {
		return currentState;
	}

	public void assertQueryEvent(String query) {
		RDBMScoreQueryEvent rcqe = new RDBMScoreQueryEvent(pdfFile.getName(),
				query);
		getServer().getPluginManager().callEvent(rcqe);
	}

	public void assertDBConnectEvent(String userName, String password,
			String dbms, String serverName, String portNumber, String database,
			String driver) {
		RDBMScoreDBConnectEvent rcdce = new RDBMScoreDBConnectEvent(
				pdfFile.getName(), userName, password, dbms, serverName,
				portNumber, database, driver);
		getServer().getPluginManager().callEvent(rcdce);
	}

	public void assertDBDisonnectEvent(String userName, String password,
			String dbms, String serverName, String portNumber, String database,
			String driver) {
		RDBMScoreDBDisconnectEvent rcdde = new RDBMScoreDBDisconnectEvent(
				pdfFile.getName(), userName, password, dbms, serverName,
				portNumber, database, driver);
		getServer().getPluginManager().callEvent(rcdde);
	}

	public void assertSendMessageEvent(String sender, String recipient,
			String message) {
		PostOfficeSendMessageEvent posme = new PostOfficeSendMessageEvent(
				sender, recipient, message);
		getServer().getPluginManager().callEvent(posme);
	}

	public void assertReadMessageEvent(String recipient, Integer messageId) {
		PostOfficeReadMessageEvent porme = new PostOfficeReadMessageEvent(
				recipient, messageId);
		getServer().getPluginManager().callEvent(porme);
	}

	public void assertMarkMessagesReadEvent(String recipient) {
		PostOfficeMarkMessagesReadEvent pommrme = new PostOfficeMarkMessagesReadEvent(
				recipient);
		getServer().getPluginManager().callEvent(pommrme);
	}

	public void assertSendPackageEvent(String sender, String recipient,
			int materialId, int amount) {
		PostOfficeSendPackageEvent pospe = new PostOfficeSendPackageEvent(
				sender, recipient, materialId, amount);
		getServer().getPluginManager().callEvent(pospe);
	}

	public void assertReceivePackageEvent(String recipient) {
		PostOfficeReceivePackageEvent porpe = new PostOfficeReceivePackageEvent(
				recipient);
		getServer().getPluginManager().callEvent(porpe);
	}

	public void assertMarkPackagesReadEvent(String recipient) {
		PostOfficeMarkPackagesReadEvent pompre = new PostOfficeMarkPackagesReadEvent(
				recipient);
		getServer().getPluginManager().callEvent(pompre);
	}

	public void assertRegisterPlayerEvent(String name) {
		PostOfficeRegisterPlayerEvent porpe = new PostOfficeRegisterPlayerEvent(
				name);
		getServer().getPluginManager().callEvent(porpe);
	}

	public void assertRecallPackageEvent(String name) {
		PostOfficeRecallPackageEvent porpe = new PostOfficeRecallPackageEvent(
				name);
		getServer().getPluginManager().callEvent(porpe);
	}

	public void assertRDBMScoreQueryEvent(String queryString) {
		RDBMScoreQueryEvent rcqe = new RDBMScoreQueryEvent(
				this.pdfFile.getName(), queryString);
		getServer().getPluginManager().callEvent(rcqe);
	}

	public void assertPostOfficeViewInboxEvent(String name) {
		PostOfficeViewInboxEvent povie = new PostOfficeViewInboxEvent(name);
		getServer().getPluginManager().callEvent(povie);
	}

	public void assertMarkMessageReadEvent(String name, Integer messageIndex) {
		PostOfficeMarkMessageReadEvent pommre = new PostOfficeMarkMessageReadEvent(
				name, messageIndex);
		getServer().getPluginManager().callEvent(pommre);

	}

}
