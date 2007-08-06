/*
 * Created on 31.10.2005
 *
 */
package mg.dynaquest.evaluation.testenv.shared.remote;


import java.rmi.Remote;
import java.rmi.RemoteException;
import mg.dynaquest.evaluation.testenv.shared.misc.Value;


/**
 * Bestimmt das <i>remote</i> verfügbare Verhalten eines <code>DataSourceManager</code>. Wird von Server- und Client-Seite benötigt.
 * @author  Tobias Mueller (IP, MG) <tobias.mueller@polaris-neu.offis.uni-oldenburg.de>
 */
public interface RemoteDSManager extends Remote {
	
	/**
	 * Initalisiert den <code>DataSourceManager</code> mit konkreten Werten für 
	 * die Datenbankanfrage. 
	 * 
	 * @param values  die Werte für das <code>PreparedStatement</code>
	 * @return <code>true</code>, wenn die Initialisierung erfolgreich war, ansonsten <code>false</code>
	 * @throws RemoteException
	 */
	public boolean init(Value[] values) throws RemoteException;
	
	/**
	 * Liefert eine neue <code>DataSource</code> als Referenz. Diese muss er  zuvor für <i>RMI</i> registrieren.
	 * @return  	eine Referenz auf eine neue und gebundene <code>DataSource</code>
	 * @throws RemoteException
	 * @uml.property  name="dataSource"
	 * @uml.associationEnd  
	 */
	public RemoteDataSource getDataSource() throws RemoteException;
	
	/**
	 * Entfernt eine Datenquelle aus der RMI-Registry.
	 * @param name der Name, mit dem die Quelle gebunden wurde
	 * @return <code>true</code>, falls das Unbinden erfolgreich war,
	 * 			ansonsten <code>false</code> 
	 * @throws RemoteException
	 */
	public boolean removeSource(String name) throws RemoteException;
	
	/**
	 * Liefert die URIs der Attribute in der Reihenfolge wie sie die Quelle liefert 
	 * @return
	 * @throws RemoteException
	 */
	public String[] getReturnAttributes() throws RemoteException;
}
