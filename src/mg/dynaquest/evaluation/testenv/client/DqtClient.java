package mg.dynaquest.evaluation.testenv.client;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;

import org.apache.log4j.Logger;

import mg.dynaquest.evaluation.testenv.shared.misc.Value;
import mg.dynaquest.evaluation.testenv.shared.remote.RemoteDSManager;
import mg.dynaquest.evaluation.testenv.shared.remote.RemoteDataSource;

/**
 * 
 * Diese Klasse dient dazu, über RMI mit einer Datenquelle zu kommunizieren.
 * Dazu sind die folgenden Methoden vorgesehen: 
 * <p>
 * {@link #connect(String, Value[]) connect(String, Value[])} bereitet die Datenquelle soweit
 * 		vor, dass anschließend Tupel geliefert werden können
 * <p>
 * {@link #getNext() getNext()} fragt das nächste Tupel ab
 * <p>
 * {@link #getStatus() getStatus()} liefert einen kurzen Bericht über den Zustand der 
 * Quelle
 * <p>
 * {@link #disconnect() disconnect()} trennt die Verbindung mit der Datenquelle 
 * 
 * @author tmueller
 *
 */
public class DqtClient {
	
	static private Logger logger = Logger.getLogger(DqtClient.class);
	
	/**
	 * Baut eine Verbindung zu der in der Konfigurationsdatei
	 * angegebenen Datenquelle auf, und stellt eine Anfrage mit 
	 * den übergebenen Werten
	 * 
	 * @param configFileName der Name der Konfigurationsdatei
	 * @param values die Werte für die Datenbank-Anfrage
	 * @param ops Operatoren, die zum Vergleich herangezogen werden sollen
	 * @return <code>true</code> wenn die Quelle anschließend bereit zu Datenlieferung ist,
	 * 			ansonsten <code>false</code>
	 * @throws RemoteException 
	 */
//	public boolean connect(String configFileName, Value[] values)  {
//		return
//	    parseProperties(configFileName) &&
//		setSystemProperties() &&
//		getDataSourceManager() &&
//		sendQuery(values) &&
//		getDataSource();
//	}
    
    static public RemoteDataSource createDateSource(String hostname, String dsname, Value[] values, List<String> retAttributes) throws RemoteException{
        RemoteDSManager manager =  getDataSourceManager(hostname,dsname);
        logger.info("Input Values "+printArray(values));
        manager.init(values);
        for (String s: manager.getReturnAttributes()){
        	retAttributes.add(s);
        }
        return manager.getDataSource();
    }
    
    static private String printArray(Object[] array){
    	StringBuffer ret = new StringBuffer();
    	for (Object o: array){
    		ret.append(""+o+" ");
    	}
    	return ret.toString();
    }
    
	
//	/**
//	 * Liefert das nächste Tupel oder <code>null</code>,
//	 * falls keines mehr gesendet werden kann.
//	 * @return das nächste Tupel oder <code>null</code>, falls das letzte
//	 * 		Tupel gesendet wurde und keine wiederholte Sendung konfiguriert ist
//	 */
//	public Tuple getNext()  {
//		if (source == null) {
//			System.out.println("DqtClient.getNext: ES BESTEHT KEINE VERBINDUNG ZU EINER DATENQUELLE!");
//			return null;
//		}
//		Tuple t = null;
//		try  {
//			if (repeat || source.hasMore()) {  
//				t = source.getNext();
//			}
//		} catch(Exception e)  {
//			System.out.println("DqtClient.getNext: Fehler beim Anfordern eines Tupels: ");
//			e.printStackTrace();
//		}
//		return t;
//	}
	
//	/**
//	 * Liefert eine kurze Meldung über den Zustand der Quelle
//	 * @see DataSource#getStatus
//	 * @return den Status der Quelle
//	 */
//	public String getStatus() {
//		String s = null;
//		if (source == null)  {
//			System.out.println("DqtClient.getStatus: " +
//					"ES BESTEHT KEINE VERBINDUNG ZU EINER DATENQUELLE!");
//			return s;
//		}
//		try {
//			s = source.getStatus();
//		} catch(Exception e)  {
//			System.out.println("DqtClient.getStatus: Fehler beim Abfragen des Quellenzustands:");
//			e.printStackTrace();
//		}
//		return s;
//	}
//	
//	/**
//	 * Beendet die aktuelle Verbindung zu einer Datenquelle.
//	 * @return <code>true</code>, wenn nach Aufrufen dieser Methode keine Verbindung
//	 * 				zu einer Datenquelle besteht, <code>false</code>, wenn beim 
//	 * 				Abbau der Verbindung ein Fehler auftritt
//	 */
//	public boolean disconnect()  {
//		// Verbindung bestand nicht
//		if (source == null)  {
//			manager = null;
//			return true;
//		}
//		else  {
//			try  {
//				manager.removeSource(sourceName);
//			} catch(Exception e)  {
//				System.out.println("DqtClient.disconnect: Fehler beim Trennen der Verbindung " +
//						"mit der Quelle " + sourceName);
//				return false;
//			}
//			System.out.println("DqtClient.disconnect: Verbindung zur Quelle " + sourceName 
//					+ "wurde getrennt");
//			sourceName = null;
//			source = null;
//			manager = null;
//			return true;
//		}
//	}
//	
	// Liest die Einstellungen und speichert sie als Feld und in Konstanten
//	private boolean parseProperties(String configFileName)  {
//		File f = new File(configFileName);
//		if (!f.canRead()) {
//			System.out.println("DqtClient.main: Fehler beim Lesen aus Datei " + configFileName);
//			return false;
//		}
//		try {
//			ClientPropertiesHandler cph = new ClientPropertiesHandler();
//			SAXParser sp = SAXParserFactory.newInstance().newSAXParser();
//			sp.parse(f,cph);
//			properties = cph.getProperties();
//		
//            for (String s: properties){
//                System.out.println("Property: "+s);
//            }
//            
//			// Werte in Konstanten packen, zur besseren Les- und Wartbarkeit
//			int index = 0;
//			this.JAVA_SECURITY_POLICY = properties[0];
//			this.SERVER_HOSTNAME = properties[1];
//			this.DSM_NAME = properties[2];
//			
//		} catch(Exception e)  {
//			System.out.println("DqtClient.parseProperties: Fehler beim Einlesen der Einstellungen:");
//			e.printStackTrace();
//			return false;
//		}
//		System.out.println("DqtClient.parseProperties: Einstellungen erfolgreich gelesen");
//		return true;
//	}
	
	/*
	 * Setzt die Systemeigenschaften
	 */
//	private boolean setSystemProperties()  {
//		try {
////			System.out.println("DqtClient.setSystemProperties: Setze java.security.policy = "
////					+ JAVA_SECURITY_POLICY);
////			System.setProperty("java.security.policy",this.JAVA_SECURITY_POLICY);
////		
////			System.out.println("DqtClient.setSystemProperties: Setze java.rmi.server.codebase = " +
////						JAVA_RMI_SERVER_CODEBASE);
////			System.setProperty("java.rmi.server.codebase",this.JAVA_RMI_SERVER_CODEBASE);
//		
//			//System.setSecurityManager(new RMISecurityManager());
//			//System.out.println("DqtClient.setSystemProperties: SecurityManager gesetzt: " + 
//			//	System.getSecurityManager());
//		} catch (Exception e)  {
//			System.out.println("DqtClient.setSystemProperties: Fehler beim Setzen der " +
//					"Systemeigenschaften:");
//			e.printStackTrace();
//			return false;
//		}
//		return true;
//	}
//	
	/*
	 * Baut eine Verbindung zu dem in der Konfigurationsdatei angegebenen 
	 * Datenquellenmanager auf.
	 */
	static private RemoteDSManager getDataSourceManager(String hostname, String dsname)  {
		String dsmName = "//" + hostname + "/" + dsname;
        RemoteDSManager manager = null;
		try  {
            logger.debug("Verbinde mit: "+dsmName);
			manager = (RemoteDSManager)Naming.lookup(dsmName);
		} catch(Exception e)  {
			logger.error("DqtClient.getDataSourceManager: Fehler beim Verbinden mit der " +
					"Quelle: ");
			e.printStackTrace();
		}
		logger.debug("DqtClient.getDataSourceManager: Erfolgreich " +
				"verbunden mit " + dsmName);
		return manager;
	}
	
	/*
	 * Übermittelt die Parameter der Anfrage an den Datenquellenmanager
	 */
//	private boolean sendQuery(Value[] values, RemoteDSManager manager)  {
//		boolean success = false;
//		try  {
//			success = manager.init(values);
//		} catch(Exception e)  {
//			System.out.println("DqtClient.sendQuery: Fehler beim Senden der Anfrageparameter: ");
//			e.printStackTrace();
//			return success;
//		}
//		System.out.println("DqtClient.sendQuery: Anfrageparameter übermittelt");
//		return success;
//	}
	
	/*
	 * Fordert eine DataSource-Objekt an und bestimmt dessen 
	 * Wiederholungsverhalten (Wenn die Daten in ständiger Wiederholung
	 * gesendet werden, können hasMore()-Aufrufe gespart werden).
	 */
//	private boolean getDataSource()  {
//		try  {
//			source = manager.getDataSource();
//			sourceName = source.getName();
//			// Wiederholungsverhalten speichern
//			repeat = source.isRepeating();
//		} catch(Exception e)  {
//			System.out.println("DqtClient.getDataSource: Fehler beim Anfordern des" +
//					" DataSource-Objekts: ");
//			e.printStackTrace();
//			return false;
//		}
//		System.out.println("DqtClient.getDataSource: DataSource-Objekt " + sourceName +
//					" erhalten,");
//		
//		return true;
//	}
}	







