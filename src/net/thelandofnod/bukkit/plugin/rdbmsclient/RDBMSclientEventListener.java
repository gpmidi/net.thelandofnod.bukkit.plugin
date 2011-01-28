package net.thelandofnod.bukkit.plugin.rdbmsclient;

import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;

import net.thelandofnod.bukkit.plugin.rdbmscore.RDBMScoreQueryResultEvent;

import org.bukkit.event.CustomEventListener;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

public class RDBMSclientEventListener implements CustomEventListener, Listener   {
	CachedRowSet crs = null;
	private final RDBMSclient plugin;
	
    public RDBMSclientEventListener(RDBMSclient instance) {
        plugin = instance;
    }
	
	@Override
	public void onCustomEvent(Event customEvent) {
		if (customEvent instanceof RDBMScoreQueryResultEvent) {
			// then there is a query event
			crs = ((RDBMScoreQueryResultEvent) customEvent).getCrs();
			try {
				while (crs.next()){
					// attempt to send the results to the player..
					plugin.callingPlayer.sendMessage(crs.getString("output"));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			((RDBMScoreQueryResultEvent) customEvent).setCancelled(true);
		}
	}


}
