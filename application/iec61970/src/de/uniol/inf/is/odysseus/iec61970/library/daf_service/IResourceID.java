package de.uniol.inf.is.odysseus.iec61970.library.daf_service;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Serialisierbare Repräsentation des Resource Identifiers nach IEC 61970-402.
 * Dieser erzeugt Identifier für den Container und dem Fragment Teil einer URI.
 * 
 * @author Mart Köhler
 *
 */
public interface IResourceID extends Remote{
	public long getContainer() throws RemoteException;
	public long getFragment() throws RemoteException;
	public boolean equals(IResourceID id) throws RemoteException;
}
