/*
 * Created on 31.10.2005
 *
 */
package mg.dynaquest.evaluation.testenv.shared.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import mg.dynaquest.evaluation.testenv.shared.misc.Tuple;




/**
 * Bestimmt das <i>remote</i> verfügbare Verhalten einer <code>DataSource</code>. Wird von Server- und Client-Seite benötigt.
 * @author  Tobias Mueller (IP, MG) <tobias.mueller@polaris-neu.offis.uni-oldenburg.de>
 */
public interface RemoteDataSource extends Remote {
	
	/**
	 * Liefert die nächste <code>Tuple</code>.
	 * @return  das nächste Tupel
	 * @throws RemoteException
	 * @uml.property  name="next"
	 * @uml.associationEnd  
	 */
	public Tuple getNext()  throws RemoteException;
	
	/**
	 * Gibt an, ob diese Datenquelle ihre Daten in ständiger 
	 * Wiederholung sendet.
	 * 
	 * @return <code>true</code>, wenn die Daten immer wieder gesendet
	 * 				werden, ansonsten <code>false</code>
	 * @throws RemoteException
	 */
	public boolean isRepeating()  throws RemoteException;
	
	/**
	 * Gibt an, ob diese Datenquelle noch Tupel zu liefern hat. 
	 * @return <code>true</code>, falls der Index des aktuellen Tupels kleiner als
	 *         die Tupelanzahl ist, ansonsten <code>false</code>.
	 * @throws RemoteException
	 */
	public boolean hasMore() throws RemoteException;
	
	/**
	 * Liefert den Namen, unter dem diese Datenquelle für RMI gebunden wurde. Dieser wird durch den Datenquellenmanager nach erfolgreichem Binden  gesetzt.
	 * @return  den Namen, unter dem die Datenquelle für RMI registriert wurde
	 * @throws RemoteException
	 * @uml.property  name="name"
	 */
	public String getName() throws RemoteException;
	
	
	/**
	 * Ist diese <code>RemoteDataSource</code> ausgefallen oder nicht, wenn ja, wann und warum.
	 * @return  eine kurze Beschreibung des Zustandes der <code>RemoteDataSource</code>
	 * @throws RemoteException
	 * @uml.property  name="status"
	 */
	public String getStatus() throws RemoteException;
	
	
}
