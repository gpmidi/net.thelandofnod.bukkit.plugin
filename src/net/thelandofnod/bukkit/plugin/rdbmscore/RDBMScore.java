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
    Connection conn;
    public static Server server;
    
    public RDBMScore(PluginLoader pluginLoader, Server instance, PluginDescriptionFile desc, File folder, File plugin, ClassLoader cLoader) {
        super(pluginLoader, instance, desc, folder, plugin, cLoader);
        conn=null;
        
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

        // EXAMPLE: Custom code, here we just output some info so we can check all is well
    	PluginDescriptionFile pdfFile = this.getDescription();
    	System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " is disabled!" );
    }
    
    
    public void assertQueryResultEvent(String queryText){
		Statement stmt = null;
		ResultSet rs = null;
    	CachedRowSet rows = null;
    	
		try {
		    stmt = conn.createStatement();
		    if (stmt.execute(queryText)) {
		        rs = stmt.getResultSet();
		    }
		    // Now do something with the ResultSet ....
		    rows = new CachedRowSetImpl();
			rows.populate(rs);
			
			// asserting our query results event ..
			RDBMScoreQueryResultEvent rcqre = new RDBMScoreQueryResultEvent();
			rcqre.setCrs(rows);
			RDBMScore.server.getPluginManager().callEvent(rcqre);
			
		}
		catch (SQLException ex){
		    // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
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
    	
        // initiate database connection
    	if (conn != null) {
    		try {
				conn.close();
				System.out.println("Disconnected from database");
			} catch (SQLException e) {
				e.printStackTrace();
				conn = null;
			}
    		conn = null;
    	}
    	else {
    		try {
				conn = getConnection(event);
				System.out.println("Conneted to database");
			} catch (SQLException e) {
				e.printStackTrace();
				conn = null;
			}
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
