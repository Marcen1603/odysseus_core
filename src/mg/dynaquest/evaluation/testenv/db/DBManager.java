/*
 * Created on 17.10.2005
 *
 */

package mg.dynaquest.evaluation.testenv.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mg.dynaquest.evaluation.testenv.shared.misc.Value;



/**
 * Die Schnittstelle zur Datenbank. Stellt Methoden zur Verfügung, um Verbindungen auf-
 * und abzubauen, Statements vorzubereiten, diese auszuführen, ...
 * Bevor diese Methoden genutzt werden können, müssen zunächst die Datenbank-Einstellungen
 * durch Aufruf von {@link #init(String, String, String, String, String, String) 
 * init(String, String, String, String, String, String)} übergeben werden. 
 * 
 *@author <a href="mailto:tmueller@polaris-neu.offis.uni-oldenburg.de">Tobias Mueller</a>
 *
 */
public class DBManager {
	
	private static Connection con = null;
	//private static HashMap<String,PreparedStatement> preparedStatements = new HashMap<String, PreparedStatement>();
	//private static HashMap<String,String> preparedStatementsSQL = new HashMap<String, String>();
	// gibt an, ob die letzte DB-Anfrage gescheitert ist
	private static boolean queryFailed = false; 
	
	private static String DB_DRIVER;
	private static String DB_PROTOCOL;
	private static String DB_NAME;
	private static String DB_USER;
	private static String DB_PASS;
	private static String DB_QUERY;
    private static List<String> DB_QUERY_ATTRIBUTE = new ArrayList<String>();
	private static HashMap<String, Integer> attributePositions; 
	
	// Soll nicht instanziiert werden
	private DBManager()  {
	}
	
	/**
	 * Setzt die Datenbank-Einstellungen und den SQL-String für 
	 * das <code>PreparedStatement</code>.
	 * 
	 * @param driv  Klassenname des Datenbank-Treibers
	 * @param prot  das Protokoll, in der Form 
	 * 		            <code>jdbc:<<!-- -->anbieter_protokoll><!-- -->:@</code>
	 * @param name  qualifizierter Name der Datenbank
	 * @param user	Benutzername
	 * @param pass	Passwort
	 * @param query <code>SQL</code>-Anweisung mit entsprechenden '<code>?</code>'
	 *              zum Einsetzen der Werte 
	 * @param db_query_params  
	 * @param attributePositions 
	 */
	public static void init(String driv,String prot,String name,String user,String pass,String query, List<String> db_query_params, HashMap<String, Integer> attributePos)  {
		DB_DRIVER = driv;
		DB_PROTOCOL = prot;
		DB_NAME = name;
		DB_USER = user;
		DB_PASS = pass;
		DB_QUERY = query;
        DB_QUERY_ATTRIBUTE = db_query_params;
		attributePositions = attributePos;
        connect();
        //prepareStms();
	}
	
	/**
	 * Stellt eine Verbindung zur Datenbank her.
	 * 
	 * @return <code>true</code>, wenn eine Verbindung hergestellt werden konnte, ansonsten 
	 * 		   <code>false</code>
	 */
	private static boolean connect()  {
		System.out.println("DBManager.connect: Verbinde mit " + DB_PROTOCOL + DB_NAME);
		if (con == null)  {
			try {
				
				Class.forName(DB_DRIVER);
			} catch(Exception e)  {
				System.out.println("DBManager.connect: Fehler beim Laden des DB-Treibers:");
				e.printStackTrace();
			}
			
			try {
				
				con = DriverManager.getConnection(DB_PROTOCOL + DB_NAME, DB_USER, DB_PASS);
				
			} catch (Exception e) {
				System.out.println("DBManager.connect: Fehler beim Aufbau der Verbindung:");
				e.printStackTrace();
				return false;
			}
			if (con == null)
				return false;
			
			try  {
				DatabaseMetaData meta = con.getMetaData();
				System.out.println("DBManager.connect: JDBC driver version is " + 
						meta.getDriverVersion());
				System.out.println("DBManager.connect: driver name:" + meta.getDriverName());
				
			} catch(SQLException e) {
				e.printStackTrace();
			}
			System.out.println("DBManager.connect: Verbindung erfolgreich aufgebaut");
			return true;
		}
		else {
			System.out.println("DBManager.connect: Verbindung bestand schon");
			return true;
		}	
	}
	
	/**
	 * Trennt die Verbindung zur Datenbank.
	 *
	 */
	public static void disconnect()  {
		System.out.println("DBManager.disconnect: Trenne Verbindung");
		try {
			con.close();
			con = null;
		} catch (SQLException e)  {
			System.out.println("DBManager.disconnect: Fehler beim Trennen der Verbindung:");
			e.printStackTrace();
		}
	}
	
	/**
	 * Gibt an, ob eine Verbindung zur Datenbank besteht. 
	 * 
	 * @return  <code>true</code>, falls eine Verbindung besteht, ansonsten <code>false</code>
	 */
	public static boolean isConnected()  {
		return con != null;
	}
	

	
//	/**
//	 * Erzeugt ein <code>PreparedStatement</code> aus dem angegebenen SQL-String.
//	 * Zuvor muss durch Aufrufen von {@link #connect() connect()}  eine Verbindung 
//	 * zur Datenbank hergestellt werden.
//	 * 
//	 * @return <code>true</code>, wenn das Statement erfolgreich vorbereitet wurde, ansonsten
//	 * 	       <code>false</code>
//	 *
//	 */
//	private static boolean prepareStms()  {
//		if (con == null)  {
//			System.out.println("DBManager.prepareStatement: Nicht verbunden!");
//			return false;
//		}
//	
//        // Hier jetzt neu: Nicht ein Statement, sondern eine Menge von Statements definieren
//        // jeweils abhängig von den Parametern für die Query
//		// und jetzt neu: Auch mit Hilfe der Operatoren
//        
//        // 1. Schritt Potenzmenge über allen Parametern bilden, damit alle Kombinationen
//        //    berücksichtig werden
//        List<List> paramPowerSet = PowerSet.buildPowerSet(DB_QUERY_PARAMS);
//        List<String> defaultOpsList = new ArrayList<String>();
//       
//        // Das geht leider nicht mit den Operatoren, da hier die Operatoren natürlich
//        // auch mehrfach auftreten können
//        defaultOpsList.add("=");
//        defaultOpsList.add("<=");
//        defaultOpsList.add("<");
//        defaultOpsList.add(">=");
//        defaultOpsList.add(">");
//                
//        // 2. Für jedes Element der Potenzmenge ein PreparedStatement machen
//        for(int i=0;i<paramPowerSet.size();i++){
//        	List attribList =  paramPowerSet.get(i);
//            // ... und zwar für alle möglichen Operatoren ... Anmerkungen: Ich kann leider nicht testen, ob die Operatoren passend sind, d.h. z.B. < für String
//            // ist aber egal
//        	List<List> opsList = PowerSet.hurz(defaultOpsList, attribList.size());
//        	for (List opList: opsList){
//        		        		        		
//	            StringBuffer sql = null;
//	            
//	            sql = createSQLStatement(attribList, opList);
//
//	            // Da es jetzt ziemlich viele PreparedStatement gäbe, wird
//	            // das Statement erst zur Ausführungszeit generiert
////	            PreparedStatement p = null;
////				try {
////					p = con.prepareStatement(sql.toString());
////				} catch (SQLException e) {
////					System.out.println("DBManager.prepareStatement: Fehler beim Erstellen des " +
////					"Prepared Statements: "+sql.toString()+" --> IGNORED");
////					e.printStackTrace();
////				}
//				
//				// Jetzt noch den Key zusammenbauen (Binärzahl plus Operatoren)
//				String binKey = Integer.toBinaryString(i);
//				StringBuffer key = new StringBuffer();
//				int t=0;
//				for (char c: binKey.toCharArray()){
//					key.append(c);
//					if (opList.size() > 0 && c=='1'){ //?
//						key.append(opList.get(t++));
//					}
//				}
//				
//				
//	            System.out.println("Erstelle SQL Statement -->"+sql.toString()+" "+key);
//	            
//	            //preparedStatements.put(key.toString(),p);
//	            preparedStatementsSQL.put(key.toString(), sql.toString());
//        	}
//       }
//		 
//		return true;
//	}

	private static String createSQLStatement(Value[] values) {
		StringBuffer sql;
		sql = new StringBuffer(DB_QUERY);
		// Achtung. Die Query kann bereits einen Where-Teil haben dann muss jeder Parameter
		// einfach nur mit AND angehängt werden. Ansonsten wird statt des ersten AND ein
		// where genommen (ACHTUNG! Dies geht bei SubQueries u.U. schief ...)
		String conCat = " AND ";
		if (sql.indexOf("WHERE") < 0) conCat = " WHERE ";
		for (Value v:values){
			if (v != null){
				sql.append(conCat);
			    // Hier jetzt ein Element bestehend aus Attribut, Operator und Fragezeichen als Stellvertreter zusammenbauen
			    sql.append(v.getAttr()+" "+v.getOperator()+" "+" ?");
			    // beim eventuellen zweiten durchlauf muss es auf jeden Fall AND sein. 
			    conCat = " AND ";
			}
		}
		return sql.toString();
	}
    
	/**
	 * Führt das vorbereitete Statement (<code>PreparedStatement</code>)
	 * mit den angegebenen Werten aus. Dieses muss zuvor durch Aufruf von 
	 * {@link #prepareStms() prepareStm()}vorbereitet worden sein.
	 * 
	 * @param values die Parameter für das PreparedStatement 
	 * @return das Ergebnis der Anfrage als <code>ResultSet</code>
	 * @see ResultSet
	 * @see Value
	 */
	public static synchronized ResultSet execute(Value[] vals)  {
        // Anhand der Values das korrekte Prepared Statement ermitteln
		
	    //String keyStr = "";
        // Eingaben/Attribute in die richtige Reihenfolge (die der Quelle) bringen)
        Value[] values = new Value[vals.length];
        for (Value v :vals){
        	System.out.println(v);
        	if (v != null){
        		if (attributePositions == null){
        			System.err.println("ACHTUNG!! attributePositions == null");
        		}
        		if (v.getAttr() == null){
        			System.err.println("ACHTUNG!! v.getAttr == null in"+v);
        		}
        		if (attributePositions.get(v.getAttr()) != null){
        			int pos = attributePositions.get(v.getAttr());
        			values[pos] = v;
        			values[pos].setAttribute(DB_QUERY_ATTRIBUTE.get(pos));
        		}else{
        			System.err.println("ACHTUNG!! Ungültiges Attribut "+v.getAttr()+" übergeben");
        		}
        	}
        }
        
//        // Und dann das Statement suchen
//        for (int i=0;i<values.length;i++){
//            if (values[i] != null){
//            	// Die Operatoren gelten natürlich nur für die belegten Attribute
//                keyStr = keyStr + "1"+cops[i];
//            }else{
//                // Null sonst, aber führende Nullen vermeiden
//                if (keyStr.length() > 0 && keyStr.startsWith("1") ){
//                    keyStr = keyStr + "0";
//                }
//            }        
//        }
//        // Fall ohne Parameter
//        if (keyStr.length() == 0) keyStr = "0";
//         
//        
//        String sql = preparedStatementsSQL.get(keyStr);
        

     
     
        
		String sql = createSQLStatement(values);
        
        PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(sql);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        // System.out.println(keyStr+" "+sql);
        System.out.println(sql);
       
        
		ResultSet rs = null;
		int type;
		try  {
            int pos = 0;
			for (Value v:values)  {				
                if (v != null){    
                	pos++;
                    System.out.println("Setze an Position "+pos+" "+v.toString());
    				type = v.getType();
    				switch (type)  {
    					case Value.STRING:
    						// Nummerierung der PreparedStatement-Variablen beginnt mit 1
    						ps.setString(pos, v.getSVal()); 
    						break;
    					case Value.LONG:
    						ps.setLong(pos,v.getLVal());
    						break;
    					case Value.INT:
    						ps.setInt(pos, v.getIVal());
    						break;
    					case Value.FLOAT:
    						ps.setFloat(pos, v.getFVal());
    						break;
    					case Value.DOUBLE:
    						ps.setDouble(pos, v.getDVal());
    						break;
    					case Value.BIGDEC:
    						ps.setBigDecimal(pos, v.getBVal());
    						break;
    					default: 
    						System.out.println("DBManager.executeQuery: Unbekannter Typ: " 
    								+ v.getType());
    				}
                }// v!=null
			} 
			
			rs = ps.executeQuery();
			queryFailed = false;
		} catch(SQLException e) {
			System.out.println("DBManager.executeQuery: Fehler beim Ausführen der Anfrage:");
			e.printStackTrace();
			queryFailed = true;
		}
		
		return rs;
	}
	
	/**
	 * Ist während der letzten DB-Anfrage ein Fehler aufgetreten?
	 * 
	 * @return <code>true</code>, wenn ja, ansonsten <code>false</code>
	 */
	public static boolean queryFailed() {
		return queryFailed;
	}
}

