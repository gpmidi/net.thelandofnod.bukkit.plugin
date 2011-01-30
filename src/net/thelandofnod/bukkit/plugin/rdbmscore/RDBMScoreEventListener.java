package net.thelandofnod.bukkit.plugin.rdbmscore;

import org.bukkit.event.CustomEventListener;
import org.bukkit.event.Event;

// implements Listener 
public class RDBMScoreEventListener extends CustomEventListener {
	private final RDBMScore plugin;

	public RDBMScoreEventListener(RDBMScore instance) {
		plugin = instance;
	}

	@Override
	public void onCustomEvent(Event event) {
		if (event instanceof RDBMScoreQueryEvent) {
			onRDBMScoreQueryEvent((RDBMScoreQueryEvent) event);
		} else if (event instanceof RDBMScoreDBConnectEvent) {
			onRDBMScoreDBConnectEvent((RDBMScoreDBConnectEvent) event);
		} else if (event instanceof RDBMScoreDBDisconnectEvent) {
			onRDBMScoreDBDisconnectEvent((RDBMScoreDBDisconnectEvent) event);
		}
	}

	private void onRDBMScoreDBDisconnectEvent(RDBMScoreDBDisconnectEvent rcdde) {
		// TODO Auto-generated method stub
		plugin.assertDBDisconnectEvent(rcdde);
		rcdde.setCancelled(true);
	}

	private void onRDBMScoreDBConnectEvent(RDBMScoreDBConnectEvent rcdce) {
		plugin.assertDBConnectEvent(rcdce);
		rcdce.setCancelled(true);
	}

	private void onRDBMScoreQueryEvent(RDBMScoreQueryEvent rcqe) {
		plugin.assertQueryResultEvent(rcqe);
		rcqe.setCancelled(true);
	}

}
