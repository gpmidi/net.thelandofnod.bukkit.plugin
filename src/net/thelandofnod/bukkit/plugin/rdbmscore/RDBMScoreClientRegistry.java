package net.thelandofnod.bukkit.plugin.rdbmscore;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class RDBMScoreClientRegistry {

	//HashMap of connection objects to plugins names (as string key)
	HashMap<String, Connection> connectionRegistry = new HashMap<String, Connection>();


	public boolean isPluginRegistered(String ownerPlugin) {

		
/*		Set regSet = connectionRegistry.entrySet();
		Iterator i = regSet.iterator();
		 
		   while(i.hasNext()){
			      Map.Entry me = (Map.Entry)i.next();
			      //System.out.println(me.getKey() + " : " + me.getValue() );
			      String indexValue;
			      indexValue = (String) me.getKey();
			      if (ownerPlugin.equals(indexValue)){
			    	  // then we found a match
			    	  return true;
			      }
			    }
*/
		System.out.println("Attempting to send back isPluginRegistered result..");
		return connectionRegistry.containsKey(ownerPlugin);
		
	}


	public void registerPlugin(Connection conn, String ownerPlugin) {
		// TODO Auto-generated method stub
		if (!isPluginRegistered(ownerPlugin)){
			this.connectionRegistry.put(ownerPlugin, conn);
		}
		
	}

	
	public void unregisterPlugin(String ownerPlugin) {
		// TODO Auto-generated method stub
		if (isPluginRegistered(ownerPlugin)){
			this.connectionRegistry.remove(ownerPlugin);
		}
		
	}


	public Connection getRegisteredConnection(String ownerPlugin) {
		if (isPluginRegistered(ownerPlugin)){
			return connectionRegistry.get(ownerPlugin);
		}
		return null;
	}	
		

	
}
