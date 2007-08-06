package mg.dynaquest.evaluation.testenv.server;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import mg.dynaquest.evaluation.testenv.db.DBManager;
import mg.dynaquest.evaluation.testenv.settings.DataSourceProperties;
import mg.dynaquest.evaluation.testenv.settings.ServerConfigVerifier;
import mg.dynaquest.xml.XPathHelper;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Startet die Server-Applikation. <p> Liest die Konstanten aus der Datei, die der Methode  {@link #main(String[])  main(String[])}   als Kommandozeilenparameter übergeben wird und überprüft deren Gültigkeit. <p> Setzt die Systemeigenschaften (Berechtigungen, <i>RMI</i>-Properties, ...) <p> Übergibt die Datenbankeinstellungen ( {@link db.DBManager#init(String,String,String,String,String,String)  db.DBManager.init(String, String, String, String, String, String)} ) <p> Erzeugt einen Datenquellen-Manager ( {@link server.DataSourceManager  server.DataSourceManager} ) und macht ihn <i>remote</i> verfügbar. Von diesem können die Clients anschließend über  <i>RMI</i> Datenquellen ( {@link server.DataSource  server.DataSource} ) beziehen. <p>
 * @see settings.ServerConfigHandler
 * @see settings.ServerConfigVerifier
 * @author  <a href="mailto:tmueller@polaris-neu.offis.uni-oldenburg.de">Tobias Mueller</a>
 */
public class ServerMain {
	// Nur diese Klasse hat das Recht, die VM mit System.exit() zu beenden, alle anderen 
	// müssen ihre Fehler hierher melden, soweit möglich.
	
	private static boolean DEBUG = true;
	
	//private static String[] generalProperties;
	private static DataSourceProperties dataSourceProperties;
	
	// Die allgemeinen Einstellungen ausgepackt, zur besseren Wartbarkeit und Lesbarkeit
	private static String HOST_NAME;
	private static String TEST_SERVER_NAME;
	private static String SERVER_CODEBASE;
	private static String SECURITY_POLICY;
	private static String DB_DRIVER;
	private static String DB_PROTOCOL;
	private static String DB_NAME;
	private static String DB_USER;
	private static String DB_PASS;
	private static String DB_QUERY;
    private static List<String> DB_QUERY_PARAMS;

	private static HashMap<String, Integer> attributePositions;

	private static String[] DB_QUERY_OUTATTRIBUTES;
	
	/**
	 * Startet die Server-Applikation
	 * 
	 * @param args  enthält als erstes Element den Namen der Konfigurationsdatei
	 */
	public static void main(String[] args) {
		// Komandozeilenparameter prüfen
		String configFileName = null;
		try {
			configFileName = args[0];
		} catch(ArrayIndexOutOfBoundsException e)  {
			System.out.println("ServerMain.main: Keine Konfigurationsdatei als " +
					"Kommandozeilenparameter angegeben! \nVerwendung: java -cp . " +
					"mg.dynaquest.evaluation.testenv.server.DataSourceManager <Konfigurationsdatei>");
			System.exit(1);
		}
		
		// Konfiguration einlesen.
		parseSettings(configFileName);
		
		// Systemeigenschaften setzen
		setSystemProperties();
		
		// dem DBManager die Einstellungen übergeben
		DBManager.init(DB_DRIVER,DB_PROTOCOL,DB_NAME,DB_USER,DB_PASS,DB_QUERY, DB_QUERY_PARAMS, attributePositions);
		
		// einen Datenquellen-Manager erzeugen und registrieren
		DataSourceManager dsm = null;
		try  {
			dsm = new DataSourceManager(HOST_NAME, dataSourceProperties, DB_QUERY_OUTATTRIBUTES);
		} catch (RemoteException e)  {
			System.out.println("ServerMain.main: Fehler beim Erzeugen des Datenquellen-Managers:");
			e.printStackTrace();
			System.exit(1);
		}
		try  {
			String name = "//" + HOST_NAME + "/" + TEST_SERVER_NAME;
            System.out.println("Binde: "+name);
			Naming.rebind(name,dsm);
			System.out.println("ServerMain.main: Datenquellen-Manager " + 
													TEST_SERVER_NAME + " gebunden");
		} catch(Exception e)  {
			System.out.println("ServerMain.main: Fehler beim Binden des Datenquellen-Managers:");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/*
	 * liest alle Einstellungen für die Server-Seite (System-Eigenschaften, Konstanten für DB und 
	 * RMI,Verhalten der Datenquellen,...) aus einer xml-Datei 
	 * 
	 */
	private static void parseSettings(String configFileName)  {
		// testen ob es die Datei gibt
//		File f = new File(configFileName);
//		if (!f.canRead())  {
//			System.out.println("ServerMain.parseSettings: Fehler beim Öffnen der Datei  " + 
//					configFileName);
//			System.exit(1);
//		} 
//		
        // Das hier ist so alles Murks ... So kann man XML-Files nicht
        // vernünftig verarbeiten :-(
        
//		try  {
//			// Einstellungen auslesen
//			ServerConfigHandler handler = new ServerConfigHandler();
//			SAXParser sp = SAXParserFactory.newInstance().newSAXParser();
//			sp.parse(new File(configFileName), handler);
//			generalProperties = handler.getGeneralProperties();
//			dataSourceProperties = handler.getDataSourceProperties();
//			
//		} catch (Exception e)  {
//			System.out.println("ServerMain.parseSettings: Fehler beim Einlesen der Konfiguration " +
//					"aus Datei " + configFileName);
//			e.printStackTrace();
//			System.exit(1);
//		}

        InputSource input = new InputSource(configFileName);
        
        HOST_NAME = XPathHelper.dumpSingleElement(input, "/conf/host_name").trim();
        TEST_SERVER_NAME = XPathHelper.dumpSingleElement(input, "/conf/test_server_name").trim();
        SERVER_CODEBASE = XPathHelper.dumpSingleElement(input, "/conf/server_codebase").trim();
        SECURITY_POLICY = XPathHelper.dumpSingleElement(input, "/conf/security_policy").trim();
        DB_DRIVER = XPathHelper.dumpSingleElement(input, "/conf/db_driver").trim();
        DB_PROTOCOL = XPathHelper.dumpSingleElement(input, "/conf/db_protocol").trim();
        DB_NAME = XPathHelper.dumpSingleElement(input, "/conf/db_name").trim();
        DB_USER = XPathHelper.dumpSingleElement(input, "/conf/db_user").trim();
        DB_PASS = XPathHelper.dumpSingleElement(input, "/conf/db_pass").trim();
        DB_QUERY = XPathHelper.dumpSingleElement(input, "/conf/db_query").trim();
        
        NodeList qParams = XPathHelper.getTreeElements(input,"/conf/query_params/query_param");
        DB_QUERY_PARAMS = new ArrayList<String>(qParams.getLength());
        attributePositions = new HashMap<String, Integer>(qParams.getLength());
        for (int i=0;i<qParams.getLength();i++){
            DB_QUERY_PARAMS.add(i, null);
        }
        for (int i=0;i<qParams.getLength();i++){
            Node qpNode = qParams.item(i);
            NamedNodeMap nnm = qpNode.getAttributes();
            
            int pos = Integer.parseInt(nnm.getNamedItem("id").getTextContent().trim());
            String attrib = nnm.getNamedItem("attr").getTextContent().trim();
            String sqlAttrib = nnm.getNamedItem("sqlattrib").getTextContent().trim();          
            
            DB_QUERY_PARAMS.set(pos,sqlAttrib);      
            
            attributePositions.put(attrib, pos);
            System.out.println("Inputparamter: "+attrib+" --> "+pos);
        }
        
        NodeList outAttributes = XPathHelper.getTreeElements(input, "/conf/query_output/output");
        DB_QUERY_OUTATTRIBUTES = new String[outAttributes.getLength()];
        for (int i=0;i<outAttributes.getLength();i++){
            Node qpNode = outAttributes.item(i);
            NamedNodeMap nnm = qpNode.getAttributes();
            
            int pos = Integer.parseInt(nnm.getNamedItem("pos").getTextContent().trim());
            String attrib = nnm.getNamedItem("attr").getTextContent().trim();
            DB_QUERY_OUTATTRIBUTES[pos]=attrib;
            System.out.println("Ausgabeattribute: "+attrib+" --> "+pos);
        }
        
            
        NodeList nl = XPathHelper.getTreeElements(input,"/conf/datasource_properties");
        dataSourceProperties = new DataSourceProperties(nl.getLength());
        dataSourceProperties.setRepeat("TRUE".equalsIgnoreCase(XPathHelper.dumpSingleElement(input, "/conf/repeat_data")));
       
        for (int i=0;i<nl.getLength();i++){
            Node dsNode = nl.item(i);
            NodeList snl = dsNode.getChildNodes();
            for (int j=0;j<snl.getLength();j++){
                Node nloc = snl.item(j);
                if (nloc == null  || nloc.getLocalName() == null) continue;                               
                
                if (nloc.getLocalName().equals("period")){
                    dataSourceProperties.setPeriod(Integer.parseInt(nloc.getTextContent().trim()),i);
                }

                if (nloc.getLocalName().equals("day_of_week")){
                    dataSourceProperties.setDayOfWeek(Integer.parseInt(nloc.getTextContent().trim()),i);
                }
                if (nloc.getLocalName().equals("day_of_month")){
                    dataSourceProperties.setDayOfMonth(Integer.parseInt(nloc.getTextContent().trim()),i);
                }
                if (nloc.getLocalName().equals("start_hour")){
                    dataSourceProperties.setStartHour(Integer.parseInt(nloc.getTextContent().trim()),i);
                }
                if (nloc.getLocalName().equals("start_minute")){
                    dataSourceProperties.setStartMinute(Integer.parseInt(nloc.getTextContent().trim()),i);
                }
                if (nloc.getLocalName().equals("end_hour")){
                    dataSourceProperties.setEndHour(Integer.parseInt(nloc.getTextContent().trim()),i);
                }
                if (nloc.getLocalName().equals("end_minute")){
                    dataSourceProperties.setEndMinute(Integer.parseInt(nloc.getTextContent().trim()),i);
                }
                if (nloc.getLocalName().equals("inv_rate")){
                    dataSourceProperties.setInvRate(Integer.parseInt(nloc.getTextContent().trim()),i);
                }
                if (nloc.getLocalName().equals("delay")){
                    dataSourceProperties.setDelay(Integer.parseInt(nloc.getTextContent().trim()),i);
                }
                if (nloc.getLocalName().equals("burst_size")){
                    dataSourceProperties.setBurstSize(Integer.parseInt(nloc.getTextContent().trim()),i);
                }
                if (nloc.getLocalName().equals("burst_pause")){
                    dataSourceProperties.setBurstPause(Integer.parseInt(nloc.getTextContent().trim()),i);
                }
                if (nloc.getLocalName().equals("burst_lag")){
                    dataSourceProperties.setBurstLag(Integer.parseInt(nloc.getTextContent().trim()),i);
                }
                if (nloc.getLocalName().equals("breakdown")){
                    dataSourceProperties.setBreakdown(Boolean.parseBoolean(nloc.getTextContent().trim()),i);
                }
                if (nloc.getLocalName().equals("delay_compensation")){
                    dataSourceProperties.setDelayCompensation(Integer.parseInt(nloc.getTextContent().trim()),i);
                }
                if (nloc.getLocalName().equals("httperror")){
                    dataSourceProperties.setHttpError(nloc.getTextContent().trim(),i);
                }
                
            }
            
        }
        
        
        
		// Einstellugen für die Datenquellen prüfen
		ServerConfigVerifier verifier = new ServerConfigVerifier();
		if (verifier.checkProperties(dataSourceProperties) == false)  {
			System.out.println("ServerMain.parseSettings: Datenquelleneigenschaften sind ungültig");
			System.out.println(verifier.getErrors());
			System.exit(1);
		}
		
		// allgemeine Einstellungen in Konstanten packen, zur besseren Wartbarkeit und Lesbarkeit

		
		if (DEBUG)  {
			System.out.println("ServerMain.parseSettings: Eingelesene Systemeinstellungen:");
			System.out.println("HOST_NAME = " + HOST_NAME);
			System.out.println("SERVER_CODEBASE = " + SERVER_CODEBASE);
			System.out.println("SECURITY_POLICY = " + SECURITY_POLICY);
			System.out.println("DB_DRIVER = " + DB_DRIVER);
			System.out.println("DB_PROTOCOL = " + DB_PROTOCOL);
			System.out.println("DB_NAME = " + DB_NAME);
			System.out.println("DB_USER = " + DB_USER);
			System.out.println("DB_PASS = " + DB_PASS);
			System.out.println("DB_QUERY = " + DB_QUERY);
            System.out.println("DB_QUERY_PARAMS = "+ DB_QUERY_PARAMS);
			
			System.out.println("\nServerMain.parseSettings: Eigenschaften der Datenquellen:");
			System.out.println(dataSourceProperties.toString());
		}	
		
	}
	
	/*
	 * Setzt die System-Eigenschaften für RMI. 
	 *
	 */
	private static void setSystemProperties()  {
		String currentProperty = "";
		try {
//			currentProperty = "java.rmi.server.hostname";
//			System.out.println("Setze " + currentProperty + " = " + HOST_NAME);
//			System.setProperty(currentProperty,HOST_NAME);
			
			currentProperty = "java.rmi.server.codebase";
			System.out.println("Setze " + currentProperty + " = " + SERVER_CODEBASE);
			System.setProperty(currentProperty,SERVER_CODEBASE);
			
//			currentProperty = "java.security.policy";
//			System.out.println("Setze " + currentProperty + " = " + SECURITY_POLICY);
//			System.setProperty(currentProperty,SECURITY_POLICY);
		} 
		catch (RuntimeException e) {
			System.out.println("ServerMain.setSystemProperties: Fehler beim Setzen von " +
					currentProperty);
			e.printStackTrace();
			System.exit(1);
		}
		
//		System.setSecurityManager(new RMISecurityManager());
//        System.out.println("ServerMain.setSystemProperties: Security Manager gesetzt: " + 
//        		System.getSecurityManager());
	}
}	
