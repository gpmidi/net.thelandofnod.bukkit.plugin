package net.thelandofnod.bukkit.plugin.rdbmsclient;

import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;

import net.thelandofnod.bukkit.plugin.rdbmscore.RDBMScoreQueryResultEvent;

import org.bukkit.event.CustomEventListener;
import org.bukkit.event.Event;

// implements Listener   
public class RDBMSclientEventListener extends CustomEventListener {
	CachedRowSet crs = null;
	private final RDBMSclient plugin;

	public RDBMSclientEventListener(RDBMSclient instance) {
		plugin = instance;
	}

	@Override
	public void onCustomEvent(Event customEvent) {
		if (customEvent instanceof RDBMScoreQueryResultEvent) {
			// see if the event is owned by us ..
			if (((RDBMScoreQueryResultEvent) customEvent).getOwnerPlugin()
					.equals(plugin.pdfFile.getName())) {
				onRDBMScoreQueryResultEvent((RDBMScoreQueryResultEvent) customEvent);
				((RDBMScoreQueryResultEvent) customEvent).setCancelled(true);
			} else {
				// else the event doesn't belong to us..
				System.out
						.println("RDBMSclient ignoring caught RDBMScoreQueryResultsEvent, doesn't belong to it.");
			}
		}
	}

	private void onRDBMScoreQueryResultEvent(RDBMScoreQueryResultEvent event) {
		if (event.isExceptionCaught()) {
			// then we caught an exception
			plugin.callingPlayer.sendMessage(event.getExceptionLog());
		} else {
			if (event.getEffectedRowCount() > -1) {
				// then this was either an update, insert, or delete
				plugin.callingPlayer.sendMessage(event.getEffectedRowCount()
						+ " rows effected.");
			} else {
				// else this was a select statement
				crs = (event).getCrs();
				try {
					while (crs.next()) {
						// attempt to send the results to the player..
						plugin.callingPlayer.sendMessage(crs
								.getString("eventName"));
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

}
