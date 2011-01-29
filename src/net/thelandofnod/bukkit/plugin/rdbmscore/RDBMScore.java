package net.thelandofnod.bukkit.plugin.rdbmscore;

import java.io.File;
import java.util.Properties;

import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.rowset.CachedRowSet;
import com.sun.rowset.CachedRowSetImpl;

import net.thelandofnod.bukkit.plugin.rdbmscore.RDBMScoreEventListener;
import net.thelandofnod.bukkit.plugin.rdbmscore.RDBMScoreQueryResultEvent;


/**
 * Entry point for plugin
 */
@SuppressWarnings("restriction")
public class RDBMScore extends JavaPlugin {
	private final RDBMScoreEventListener queryListener = new RDBMScoreEventListener(this);
	
	// an array of conn are needed, but of conn, and calling pluginname - a connection registry
    //Connection conn;
    public static Server server;
    private RDBMScoreClientRegistry clientRegistry = new RDBMScoreClientRegistry();
    
    public RDBMScore(PluginLoader pluginLoader, Server instance, PluginDescriptionFile desc, File folder, File plugin, ClassLoader cLoader) {
        super(pluginLoader, instance, desc, folder, plugin, cLoader);
        //conn=null;
        
        
        // NOTE: Event registration should be done in onEnable not here as all events are unregistered when a plugin is disabled
		server = instance;         
    }
   

    public void onEnable() {

        // Register our events
        PluginManager pm = getServer().getPluginManager();
        
        //Create listeners
        pm.registerEvent(Event.Type.CUSTOM_EVENT, this.queryListener, Event.Priority.Normal, this);
        
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
    }
    
    
    public void onDisable() {
        // NOTE: All registered events are automatically unregistered when a plugin is disabled
    	
    	PluginDescriptionFile pdfFile = this.getDescription();
    	System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " is disabled!" );
    }
    
    
    public void assertQueryResultEvent(RDBMScoreQueryEvent rcqe){
		Statement stmt = null;
		ResultSet rs = null;
    	CachedRowSet rows = null;
    	Connection conn = null;
    	String ownerPlugin = rcqe.getOwnerPlugin();
    	String queryString = rcqe.getQuery();
    	
    	RDBMScoreQueryResultEvent rcqre = new RDBMScoreQueryResultEvent(ownerPlugin);
    	
		try {
			if (this.clientRegistry.isPluginRegistered(ownerPlugin)){
				conn = this.clientRegistry.getRegisteredConnection(ownerPlugin);
			    stmt = conn.createStatement();
			    if (stmt.execute(queryString)){
			    	// true means it was a select query
			        rs = stmt.getResultSet();
			        
				    // Now do something with the ResultSet ....
				    rows = new CachedRowSetImpl();
					rows.populate(rs);	
					rcqre.setCrs(rows);			    	
			    }
			    else{
			    	// false, means it was an update, insert, or delete
			    	// lets get the number of rows effected
			    	rcqre.setEffectedRowCount(stmt.getUpdateCount());
			    }			    
			}
					
			// asserting our query results event ..
			//RDBMScoreQueryResultEvent rcqre = new RDBMScoreQueryResultEvent();
			//rcqre.setCrs(rows);
			RDBMScore.server.getPluginManager().callEvent(rcqre);
			
		}
		catch (SQLException ex){
		    // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		    rcqre.setExceptionLog("SQLException: " + ex.getMessage() + 
		    		              " SQLState: " + ex.getSQLState() +
		    		              " VendorError: " + ex.getErrorCode()
		    );
		    rcqre.setExceptionCaught(true);
		    
		}
		finally {
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
		}
    }
    
    
    
    public void assertDBConnectEvent(RDBMScoreDBConnectEvent event){
    	Connection conn = null;
    	String ownerPlugin = "";
    	//conn = this.connectionRegistry.getConnection();
    	// first see if we already have a connection
    	// create one if we don't and register it
    	
    	ownerPlugin = event.getOwnerPlugin();
    	System.out.println("Event Owner: " + ownerPlugin);
    	if (!this.clientRegistry.isPluginRegistered(ownerPlugin)){
    		// then create a new connection and register it
    		try {
				conn = getConnection(event);
				this.clientRegistry.registerPlugin(conn, ownerPlugin);
				System.out.println("Conneted to database, and registed.");
				
			} catch (SQLException e) {
				e.printStackTrace();
				conn = null;
			}
    	}        	
    	else {
    		System.out.println("plugin " + ownerPlugin + " already has a registered database connection.");
    	}    		
    }
    

    public void assertDBDisconnectEvent(RDBMScoreDBDisconnectEvent event){
    	Connection conn = null;
    	String ownerPlugin;
    	//conn = this.connectionRegistry.getConnection();
    	// first see if we already have a connection
    	// create one if we don't and register it
    	
    	ownerPlugin = event.getOwnerPlugin();
    	if (this.clientRegistry.isPluginRegistered(ownerPlugin)){
    		conn = this.clientRegistry.getRegisteredConnection(ownerPlugin);
    		this.clientRegistry.unregisterPlugin(ownerPlugin);
    		if (conn != null){
    			try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    		
    	}        	
    	else {
    		System.out.println("plugin " + ownerPlugin + " already has a registered database connection.");
    	}
    }
    
    
    private Connection getConnection(RDBMScoreDBConnectEvent event) throws SQLException {
    	
        // Get the driver ..
		try {
			Class.forName (event.getDriver()).newInstance ();
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
        conn = DriverManager.
        getConnection("jdbc:" + event.getDbms() + "://" + event.getServerName() +
                ":" + event.getPortNumber() + "/" + event.getDatabase(), connectionProps);
        
        return conn;
      }


}
