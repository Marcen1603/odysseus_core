package de.uniol.inf.is.odysseus.iec61970.library.server.service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IResourceID;

/**
 * Schnittstelle von AggregateDefinition aus dem TSDA Datenmodell
 * @author Mart Köhler
 *
 */
public interface IAggregate extends Remote {
	public List<String> findAll() throws RemoteException;
	public String find(IResourceID id) throws RemoteException;
}
