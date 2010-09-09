package de.uniol.inf.is.odysseus.iec61970.library.daf_service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * Hier werden alle Descriptions gespeichert, die zuvor vom den Datenmodellen mit Hilfe des Jena Frameworks extrahiert wurden.
 * @author Mart Köhler
 *
 */
public interface IDescriptionStorage extends Remote{
	public void add(IResourceID id, IDescription description) throws RemoteException;
	public IDescription getDescription(IResourceID id) throws RemoteException;
	public HashMap<IResourceID,IDescription> getAllDescriptions() throws RemoteException;
}
