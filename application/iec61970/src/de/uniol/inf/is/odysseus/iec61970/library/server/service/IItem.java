package de.uniol.inf.is.odysseus.iec61970.library.server.service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IDescription;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IResourceDescriptionIterator;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IResourceID;

/**
 * Schnittstelle für das Item Element nach IEC 61970-404 und IEC 61970-407
 * @author Mart Köhler
 *
 */
public interface IItem extends Remote{
	public IDescription find(IResourceID resource) throws RemoteException;
	public IResourceDescriptionIterator<IDescription> findEach(ArrayList<IResourceID> resource) throws RemoteException;
	public IResourceDescriptionIterator<IDescription> findByParent(IResourceID resource) throws RemoteException;
	public IResourceDescriptionIterator<IDescription> findByType(IResourceID resource) throws RemoteException;
	public List<String> getPathnames(List<IResourceID> resource) throws RemoteException;
	public IResourceID getPathnameId(String pathnames) throws RemoteException;
	public List<IResourceID> getPathnameIds(List<String> pathnames) throws RemoteException;
	public List<IResourceID> getAllItemIds() throws RemoteException;
	public IResourceDescriptionIterator<IDescription> getAllDescriptions() throws RemoteException;
	public String getDatatypeOf(IResourceID id) throws RemoteException;
	public List<String> getAllItemPathnames() throws RemoteException;
	public String getPathname(IResourceID id) throws RemoteException;
	public boolean isItem(String pathname) throws RemoteException;
}
