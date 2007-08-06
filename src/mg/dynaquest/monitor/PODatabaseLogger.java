package mg.dynaquest.monitor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Properties;

import mg.dynaquest.queryexecution.event.ExceptionEvent;
import mg.dynaquest.queryexecution.event.POEvent;
import mg.dynaquest.queryexecution.event.POEventType;
import mg.dynaquest.queryexecution.po.base.NAryPlanOperator;
import oracle.jdbc.driver.OracleResultSet;
import oracle.sql.CLOB;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/** PODatabaseLogger: DB Logging für POMonitore
 *  Diese Klasse schreibt die vom POMonitor empfangenen Ereignisse in eine Datenbank
 *
 *	TODO: Funktioniert wahrscheinliche nur mit Oracle --> CLOB des Plans 
 */



public class PODatabaseLogger implements POMonitorEventListener {
	
	static Logger logger = Logger.getLogger(PODatabaseLogger.class);
	
	/* soll auch in die DB geschrieben werden? (false -> kein DB Zugriff) */
	/**
	 * @uml.property  name="writeToDB"
	 */
	private boolean writeToDB = false;
	/**
	 * @uml.property  name="dbConnection"
	 */
	private Connection dbConnection;

	Collection<POMonitor> poMonitors;
	/**
	 * @uml.property  name="jdbcString"
	 */
	String jdbcString;
	/**
	 * @uml.property  name="driverClass"
	 */
	String driverClass;
	/**
	 * @uml.property  name="user"
	 */
	String user;
	/**
	 * @uml.property  name="password"
	 */
	String password;
		
	
	/**
	 * @param poMonitors List von POMonitoren, die geloggt werden sollen
	 * @param user
	 * @param writeToDB2 
	 * @param pass
	 *            DBMS-Zugangs-Parameter
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	public PODatabaseLogger(String user, 
							String password, 
							String jdbcString,
							String driverClass, 
							boolean writeToDB) throws ClassNotFoundException, SQLException {
		this.jdbcString = jdbcString;
		this.driverClass = driverClass;
		this.user = user;
		this.password = password;
		this.writeToDB = writeToDB;
		if (writeToDB){	
			initDB();
		}
	}
	
	public void registerMonitors(Collection<POMonitor> name){
		for (POMonitor pm: name){
			pm.addPOMonitorEventListener(this);
		}		
		this.poMonitors = name;
	}
	
	private void initDB() throws ClassNotFoundException, SQLException{		
		Class.forName(driverClass);
		dbConnection = DriverManager.getConnection(jdbcString, user, password);		
		dbConnection.setAutoCommit(false);		
	}


	public void poMonitorEventOccured(POMonitorEvent ev) {
		
		long time = ev.getTime();
		POEvent poEvent = ev.getEvent();
		String poGUID = poEvent.getSourceGUID();
		POEventType eventType = poEvent.getPOEventType();
		String message = "";
		
		if (eventType.equals(POEventType.Exeception)){
			message = ((ExceptionEvent)poEvent).getMessage();
		}
		
		String sql = "insert into DQ_po_events(time, eventtype, poguid, message) values(" +
				time+","+
				"'"+eventType+"',"+
				"'"+poGUID+"',"+
				"'"+message+"')";
		
		if (writeToDB){
			try {
				Statement stmt = dbConnection.createStatement();
				stmt.executeUpdate(sql);
				stmt.close();
				dbConnection.commit();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			logger.debug(sql);
		}		
	}

	public void removeEventsBefore(long time) throws SQLException{
		String sql = "delete from DQ_po_events where time < "+time;
		Statement stmt = dbConnection.createStatement();
		logger.debug(sql);
		stmt.executeUpdate(sql);
		logger.debug(" durch");
		stmt.close();
		dbConnection.commit();
		logger.debug(" commit");
	}
	
	// Abspeichern des Plans in der Datenbank, damit im nachhinein überhaupt 
	// festgestellt werden kann, welcher Plan ausgeführt worden ist
	public void planToExecute(NAryPlanOperator p) {

		StringBuffer xmlPlan = new StringBuffer();
		p.getXMLPlan(xmlPlan);
		
		if (writeToDB){
			try {
				String sql = "insert into DQ_executedPlans(id,plan,executionTime) values('"+p.PO_ID+"',EMPTY_CLOB(),"+System.currentTimeMillis()+")";
				Statement stmt =  dbConnection.createStatement();
				stmt.executeUpdate(sql);
				
				sql = "Select plan from DQ_executedPlans where id = '"+p.PO_ID+"' FOR UPDATE";
				ResultSet rs = stmt.executeQuery(sql);
				rs.next();
				CLOB clob = ((OracleResultSet)rs).getCLOB("plan");
				int chunkSize = clob.getChunkSize();
				char[] textBuffer = new char[chunkSize];
				
				StringReader reader = new StringReader(xmlPlan.toString());
				
				int pos = 1;
				int charsRead = 0;
				
				try {
					while ((charsRead = reader.read(textBuffer)) != -1){					
						clob.putChars(pos, textBuffer);
						pos += charsRead;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				dbConnection.commit();
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		logger.debug(xmlPlan);
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException, FileNotFoundException, IOException {
		
        Logger root = Logger.getRootLogger();
        root.removeAllAppenders();
        root.addAppender(new ConsoleAppender(
                new PatternLayout("[%t] %l %x - %m%n")));
        root.addAppender(new FileAppender(new PatternLayout("[%t] %l %x - %m%n"), "dq_logging"));
        
        root.setLevel(Level.ALL);
 
		
    	String propFile = System.getProperty("user.home")+"/DynaQuest.properties";
    	Properties properties = new Properties();
    	properties.load(new FileInputStream(propFile));
        String log_user = properties.getProperty("log_user");
        String log_password = properties.getProperty("log_password");
        String log_jdbcString = properties.getProperty("log_jdbcString");
        String log_driverClass = properties.getProperty("log_driverClass");
        
        PODatabaseLogger dbLogger = new PODatabaseLogger(log_user, log_password, log_jdbcString, log_driverClass, true);
        
        for (long i=1160393669663l;i<11674775222839l;i+=1000000){
        	logger.debug("Entferne Events vor "+i);
        	dbLogger.removeEventsBefore(i);
        	logger.debug("durch");
        }
        
        
        
	}

}