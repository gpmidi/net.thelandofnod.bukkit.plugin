package net.thelandofnod.bukkit.plugin.rdbmslogger;

import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;

import net.thelandofnod.bukkit.plugin.rdbmscore.RDBMScoreQueryResultEvent;
import net.thelandofnod.bukkit.plugin.rdbmslogger.RDBMSlogger.applicationState;

import org.bukkit.event.CustomEventListener;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

public class RDBMSloggerEventListener implements CustomEventListener, Listener   {
	CachedRowSet crs = null;
	private final RDBMSlogger plugin;
	
    public RDBMSloggerEventListener(RDBMSlogger instance) {
        plugin = instance;
    }
	
	@Override
	public void onCustomEvent(Event customEvent) {
		if (customEvent instanceof RDBMScoreQueryResultEvent) {
			if (((RDBMScoreQueryResultEvent)customEvent).getOwnerPlugin().equals(plugin.pdfFile.getName())){
				onRDBMScoreQueryResultEvent((RDBMScoreQueryResultEvent)customEvent);
				((RDBMScoreQueryResultEvent) customEvent).setCancelled(true);
			}
			else{
				// this is the correct event, but we are not the owner so ignore
				System.out.println("RDBMSlogger caught RDBMScoreQueryResultEvent, but ignored it, it's not the owner.");
			}				
			//onRDBMScoreQueryResultEvent((RDBMScoreQueryResultEvent)customEvent);
			//((RDBMScoreQueryResultEvent) customEvent).setCancelled(true);
		} else if (customEvent instanceof RDBMSloggerInitEvent) {
			onRDBMSloggerInitEvent((RDBMSloggerInitEvent)customEvent);
			((RDBMSloggerInitEvent) customEvent).setCancelled(true);
			
		}
	}
	
	

	private void onRDBMSloggerInitEvent(RDBMSloggerInitEvent customEvent) {
		// lets get our set of events to monitor for ..
		String eventSelectionQuery = "select eventName from event_type";
		plugin.assertQueryEvent(eventSelectionQuery);
		
	}

	private void onRDBMScoreQueryResultEvent(RDBMScoreQueryResultEvent event) {

		System.out.println("onRDBMScoreQueryResultEvent caught by RDBMSlogger");
		
		if (plugin.getCurrentState() == applicationState.INIT){
			// then we are attempting to read out run-time event listeners
			
			if (event.isExceptionCaught()){
				// then we caught an exception
				System.out.println(event.getExceptionLog());
			}
			else {
				if (event.getEffectedRowCount() > -1){
					// then this was either an update, insert, or delete
					System.out.println(event.getEffectedRowCount() + " rows effected.");
				}
				else{
					// else this was a select statement
					crs = ((RDBMScoreQueryResultEvent) event).getCrs();
					try {
							while(crs.next()){
								// attempt to send the results to the player..
								//plugin.callingPlayer.sendMessage("(" + crs.getTimestamp("timeStamp") + ") " + crs.getString("output"));
								System.out.println(crs.getString("eventName"));
							}
							plugin.setCurrentState(applicationState.NORMAL);
							
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}


}
