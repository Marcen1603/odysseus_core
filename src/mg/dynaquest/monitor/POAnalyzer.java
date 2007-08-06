package mg.dynaquest.monitor;

/**
 * POAnalyzer: analysiert die PO anhand ihrer History
 * 
 * $Log: POAnalyzer.java,v $
 * Revision 1.4  2004/09/16 08:57:11  grawund
 * Quellcode durch Eclipse formatiert
 * Revision 1.3 2004/09/16 08:53:53 grawund *** empty
 * log message ***
 * 
 * Revision 1.2 2004/06/11 10:21:52 grawund vorrübergehend deaktiviert
 * 
 * Revision 1.1 2003/07/08 13:52:02 hobelmann erstmal nur Export ins ARFF Format
 * für WEKA
 *  
 */

//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.Date;
//import oracle.jdbc.driver.OracleDriver;

public class POAnalyzer {
	//private String connectString = "power2.offis.uni-oldenburg.de:1521:power2";

	//private String driverClass = "jdbc:oracle:thin:";

	//private String user = "hobelmann";

	//private String arffFileName = "test.arff";

	public POAnalyzer() {
	}
	/**
	 * private void convertDBToARFF() { String q = "select * from events"; try {
	 * DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
	 * Connection c =
	 * DriverManager.getConnection(driverClass+"@"+connectString,user,pass);
	 * Statement s = c.createStatement(); ResultSet r = s.executeQuery(q);
	 * PrintWriter w = new PrintWriter(new FileWriter(arffFileName));
	 * w.println("@relation poEvents"); w.println(); w.println("@attribute day
	 * real"); w.println("@attribute weekday { Sunday, Monday, Tuesday,
	 * Wednesday, Thursday, Friday, Saturday }"); w.println("@attribute month
	 * real"); w.println("@attribute hour real"); w.println("@attribute poName {
	 * convMon, htMon, xpMon }"); w.println("@attribute event { Close, Next,
	 * Open, Read, Write }"); w.println("@attribute elapsedTime real");
	 * w.println(); w.println("@data"); while (r.next()) { // Date date = new
	 * Date(r.getLong(1)); // int day = date.getDate(); // String weekday =
	 * weekDay(date.getDay()); // int month = date.getMonth(); // int hour =
	 * date.getHours(); // String poName = r.getString(2); // String evName =
	 * r.getString(3); // long timeElapsed = r.getLong(4); //
	 * w.println(day+","+weekday+","+month+","+hour+","+poName+","+evName+","+timeElapsed); }
	 * w.close(); s.close(); c.close(); } catch(SQLException e) {
	 * System.err.println("SQL Error: " + e); } catch(IOException e) {
	 * System.err.println("IO error: " + e); } }
	 * 
	 * private String weekDay(int day) { switch(day) { case 0: return "Sunday";
	 * case 1: return "Monday"; case 2: return "Tuesday"; case 3: return
	 * "Wednesday"; case 4: return "Thursday"; case 5: return "Friday"; case 6:
	 * return "Saturday"; default: System.err.println("wrong weekday: " + day);
	 * return "???day"; } }
	 * 
	 * public static void main(String[] args) { POAnalyzer an = new
	 * POAnalyzer(); // erst mal nur ARFF erzeugen zum manuellen Test mit WEKA
	 * an.convertDBToARFF(); }
	 */
}