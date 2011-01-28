package net.thelandofnod.bukkit.plugin.rdbmscore;

import org.bukkit.event.CustomEventListener;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;


public class RDBMScoreEventListener implements CustomEventListener, Listener {
	private final RDBMScore plugin;
	 
	public RDBMScoreEventListener(RDBMScore instance){
		plugin = instance;
	}
	
	public void onCustomEvent(Event event) {
		if (event instanceof RDBMScoreQueryEvent) {
			onRDBMScoreQueryEvent((RDBMScoreQueryEvent)event);
		}
		else if (event instanceof RDBMScoreDBConnectEvent){
			onRDBMScoreDBConnectEvent((RDBMScoreDBConnectEvent)event);
		}
		
	}

	private void onRDBMScoreDBConnectEvent(RDBMScoreDBConnectEvent rcdce) {
		plugin.assertDBConnectEvent(rcdce);
		rcdce.setCancelled(true);
	}

	private void onRDBMScoreQueryEvent(RDBMScoreQueryEvent rcqe) {
		plugin.assertQueryResultEvent(rcqe.getQuery());
		rcqe.setCancelled(true);
	}

}
