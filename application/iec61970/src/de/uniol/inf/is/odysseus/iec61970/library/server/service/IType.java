package de.uniol.inf.is.odysseus.iec61970.library.server.service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IDescription;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IResourceDescriptionIterator;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IResourceID;


/**
 * Schnittstelle zu dem Type Element nach IEC 61970-404 und IEC 61970-407
 * 
 * @author Mart Köhler
 *
 */
public interface IType extends Remote{
	public IDescription find(IResourceID resource) throws RemoteException;
	public IResourceDescriptionIterator<IDescription> findEach(ArrayList<IResourceID> resource) throws RemoteException;
	public List<IResourceID> getAllTypeIds() throws RemoteException;
	public IResourceDescriptionIterator<IDescription> getAllDescriptions() throws RemoteException;
	public IDescription findByInstance(IResourceID resource) throws RemoteException;
}
