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
			onRDBMScoreQueryResultEvent((RDBMScoreQueryResultEvent)customEvent);
			((RDBMScoreQueryResultEvent) customEvent).setCancelled(true);
		}
	}

	private void onRDBMScoreQueryResultEvent(RDBMScoreQueryResultEvent event) {

		if (event.isExceptionCaught()){
			// then we caught an exception
			plugin.callingPlayer.sendMessage(event.getExceptionLog());
		}
		else {
			if (event.getEffectedRowCount() > -1){
				// then this was either an update, insert, or delete
				plugin.callingPlayer.sendMessage(event.getEffectedRowCount() + " rows effected.");
			}
			else{
				// else this was a select statement
				crs = ((RDBMScoreQueryResultEvent) event).getCrs();
				try {
						while(crs.next()){
							// attempt to send the results to the player..
							plugin.callingPlayer.sendMessage(crs.getString("output"));							
						}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		

	}


}
