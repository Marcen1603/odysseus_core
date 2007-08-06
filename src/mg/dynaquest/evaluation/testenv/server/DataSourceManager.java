/*
 * Created on 18.10.2005
 *
 */
package mg.dynaquest.evaluation.testenv.server;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;

import mg.dynaquest.evaluation.testenv.db.DBManager;
import mg.dynaquest.evaluation.testenv.db.TupleParser;
import mg.dynaquest.evaluation.testenv.settings.DataSourceProperties;
import mg.dynaquest.evaluation.testenv.shared.misc.Tuple;
import mg.dynaquest.evaluation.testenv.shared.misc.Value;
import mg.dynaquest.evaluation.testenv.shared.remote.RemoteDSManager;
import mg.dynaquest.evaluation.testenv.shared.remote.RemoteDataSource;


/**
 * Diese Klasse verwaltet Datenquellen ( {@link server.DataSource  server.DataSource} ).  �ber <i>RMI</i> ruft ein Client {@link #getDataSource()  getDataSource()}   auf und bekommt eine Referenz auf eine neu  erzeugte und registrierte Datenquelle.  Vorher muss der Datenquellen-Manager von einem Client initialisiert werden, was durch die Methode  {@link #init(Value[])  init(Value[])}  geschieht. Dabei werden dem Datenquellen- Manager konkrete Werte �bergeben, mit denen er ein <code>PreparedStatement</code> ausf�hrt. Die Ergebnisse diese Datenbank-Anfrage bereitet er zu Tupeln( {@link Tuple  Tuple} auf und �bergibt sie den von ihm verwalteten Datenquellen. Ein Client kann seine Datenqulle durch  {@link #removeDataSource(String)  removeDataSource(String)}   aus der RMI-Registry entfernen und l�schen lassen.  <p>
 * @author  Tobias Mueller (IP, MG) <tmueller@polaris-neu.offis.uni-oldenburg.de>
 * @see DataSource
 * @see  db.DBManager
 */
public class DataSourceManager extends UnicastRemoteObject implements RemoteDSManager {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 578427703417575739L;
	// Die aus der Konfigurationsdatei eingelesenen Einstellungen, die f�r die 
	// Datenquellen relevant sind (Deswegen wird HOST_NAME extra behandelt)
	/**
	 * @uml.property  name="dataSourceProperties"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private DataSourceProperties dataSourceProperties;
	// Gibt an, ob der Datenquellenmanager bereit ist, Datenquellen zu liefern
	/**
	 * @uml.property  name="initialized"
	 */
	private boolean initialized = false;
	// Die Tupel, die  von den Datenquellen geliefert werden
	/**
	 * @uml.property  name="tuples"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
	private Tuple[] tuples;
	// Die Attribute die von der Quelle geliefert werden
	/**
	 * @uml.property  name="returnAttributes"
	 */
	private String[] returnAttributes;
	
	/**
	 * Erzeugt einen neuen Datenquellen-Manager. 
	 * 
	 * @param hostName    der Host, auf dem dieser Datenquellenmanager und 
	 * 					  die von ihm verwalteten Datenquellen f�r <i>RMI</i> 
	 * 					  registriert werden 
	 * @param dsp         Parameter f�r das Verhalten der Datenquellen
	 * @param db_query_outattributes 
	 * @throws RemoteException
	 */
	public DataSourceManager(String hostName,DataSourceProperties dsp, String[] db_query_outattributes) throws RemoteException {
		this.dataSourceProperties = dsp;
		this.returnAttributes = db_query_outattributes;
	}
	
	/**
	 * Bereitet den Datenquellen-Manager darauf vor, Datenquellen zu liefern.
	 * Die �bergebenen Werte werden in ein vorbereitetes Statement eingesetzt, dieses
	 * wird ausgef�hrt,das Ergebnis (<code>ResultSet</code>) wird zu Tupeln (<code>Tuple</code>) 
	 * aufbereitet.
	 * 
	 * @param values die aktuellen Werte f�r das <code>PreparedStatement</code>
	 * @return <code>true</code>, wenn die Initialisierung erfolgreich war, ansonsten <code>false</code>
     * @throws RemoteException
     * @see DBManager#execute(Value[])
     * @see TupleParser#parseTuples(ResultSet)
	 */
	public boolean init(Value[] values) throws RemoteException  {
		System.out.println("DataSourceManager.init()");
        for (Value v:values){
            if (v == null){
                System.out.print("null ,");
            }else{
                System.out.print(v.toString()+", ");
            }
        }
        System.out.println();
		if (values == null)  {
			System.out.println("DataSourceManager.init: Keine Werte �bergeben");
			return false;
		}
//		if (mg.dynaquest.evaluation.testenv.db.DBManager.connect() == false)
//			return false;
//		if (mg.dynaquest.evaluation.testenv.db.DBManager.prepareStm() == false)
//			return false;
		ResultSet res = mg.dynaquest.evaluation.testenv.db.DBManager.execute(values);
		if (mg.dynaquest.evaluation.testenv.db.DBManager.queryFailed())
			return false;
		tuples = TupleParser.parseTuples(res);
		//mg.dynaquest.evaluation.testenv.db.DBManager.disconnect();
		this.initialized = true;
		return true;
	}
	
	/**
	 * Erzeugt eine neue Datenquelle und registriert sie f�r <i>RMI</i>.
	 * Diese liefert die in {@link #init(Value[]) init(Value[])} erzeugten Tupel
	 * so, wie es in der Konfiguration festgelegt ist, die diesem Datenquellen-Manager
	 * bei der Konstruktion �bergeben wurde.
	 * 
	 * @see #removeSource(String)
	 * @return eine Referenz auf eine neue, schon f�r <i>RMI</i> registrierte Datenquelle
	 * @throws RemoteException
	 * 
	 */
	public RemoteDataSource getDataSource() throws RemoteException  {
		if (this.initialized == false)  {
			System.out.println("DataSourceManager.getDataSource: Datenquellen-Manager noch nicht " +
					"initialisiert, zuerst init() aufrufen");
			return null;
		}
		DataSource ds = new DataSource(tuples, dataSourceProperties);
        
        // Warum sollte man die Datenquelle binden??
        
//		// f�r den sehr unwahrscheinlichen Fall dass versucht wird, zwei Quellen mit dem
//		// selben Namen zu binden
//		boolean alreadyBound = false;
//		String name = null;
//		do  {
//			try  {
//				// Der Name spielt f�r den Client vorerst keine Rolle, da
//				// dieser die Referenz direkt bekommt und nicht in der registry sucht.
//				// Er muss nur eindeutig sein.
//				name = createDataSourceName(HOST_NAME);
//				//Naming.bind(name, ds);
//				System.out.println("DataSourceManager.getDataSource: Datenquelle " + name + 
//						" gebunden");
//				alreadyBound = false;
////			} catch (Exception abe)  {
////				System.out.println("DataSourceManager.getDataSource: AlreadyBoundException aufgetreten," +
////						" versuche erneut zu binden");
////				alreadyBound = true;
//			} catch (Exception ex)  {
//				System.out.println("DataSourceManager.getDataSource: Unerwartete Exception beim " +
//						"Binden einer Datenquelle:");
//				ex.printStackTrace();
//			}
//		} while (alreadyBound == true);
//		
//		// RMI-Namen setzen!!! Zur sp�teren Deregistrierung
//		ds.setName(name);
//		
		return ds;
	}
	
	/**
	 * Veranlasst den Datenquellenmanager, ein <code>DataSource</code>-Objekt
	 * aus der RMI-Registry zu entfernen.
	 * Dazu ben�tigt er den Namen, mit dem das entsprechende Objekt 
	 * gebunden worden ist. Dieser kann �ber {@link DataSource#getName() DataSource.getName()}
	 * ermittelt werden.
	 * 
	 */
	public boolean removeSource(String name) throws RemoteException {
		try  {
			Naming.unbind(name);
		} catch(Exception e)  {
			System.out.println("DataSourceManager.removeSource: Fehler beim Deregistrieren der " +
					"Datenquelle " + name);
			e.printStackTrace();
			return false;
		}
		System.out.println("DataSourceManager.removeSource: Datenquelle " + name + " gel�scht");
		return true;
	}

	/**
	 * @return  the returnAttributes
	 * @uml.property  name="returnAttributes"
	 */
	public String[] getReturnAttributes() throws RemoteException {
		return returnAttributes;
	}

   /*
	* Erzeugt Namen zum Binden von remote Objekten, die h�chstwahrscheinlich eindeutig sind 
	* (so lange die Methode nicht zweimal in der selben Millisekunde aufgerufen wird).
	* Falls dies doch passiert, sollte die AlreadyBoundException> abgefangen werden,
	* und ein neuer Versuch unternommen werden.
	* 
    */
//	private static String createDataSourceName(String hostName)  {
//		return "//" + hostName + "/" + System.currentTimeMillis();
//	}
}

















