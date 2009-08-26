package de.uniol.inf.is.odysseus.iec61970.library.server.service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IDescription;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IResourceID;

/**
 * Schnittstelle zu dem ItemAttribute Element aus dem IEC 61970-407
 * 
 * @author Mart Köhler
 *
 */
public interface IItemAttribute extends Remote{
	public List<HashMap<String, HashMap<Object,String>>> findAll(IResourceID id) throws RemoteException;
	public HashMap<String, HashMap<Object,String>> find(IResourceID id) throws RemoteException;
	public IResourceID getReferencingItem(IResourceID attributeID) throws RemoteException;
	public List<IResourceID> findByItem(IResourceID itemID) throws RemoteException;
	public IDescription getItemAttribute(IResourceID id) throws RemoteException;
}
