package de.uniol.inf.is.odysseus.iec61970.library.server.service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IDescription;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IResourceDescriptionIterator;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IResourceID;

/**
 * Schnittstelle zu dem Node Elemente aus dem IEC 61970-404 und IEC 61970-407
 * @author Mart Köhler
 *
 */
public interface INode extends Remote{
	public IDescription find(IResourceID resource) throws RemoteException;
	public IResourceDescriptionIterator<IDescription> findEach(ArrayList<IResourceID> resource) throws RemoteException;
	public IResourceDescriptionIterator<IDescription>  findByParent(IResourceID resource) throws RemoteException;
	public IResourceDescriptionIterator<IDescription>  findByType(IResourceID resource) throws RemoteException;
	public List<String> getPathnames(List<IResourceID> resource) throws RemoteException;
	public List<IResourceID> getPathnameIds(List<String> pathnames) throws RemoteException;
	public List<String> getAllNodePathnames() throws RemoteException;
	public IResourceID getPathnameId(String pathname) throws RemoteException;
	public List<IResourceID> getAllNodeIds() throws RemoteException;
	public IResourceDescriptionIterator<IDescription> getAllDescriptions() throws RemoteException;
	public IResourceID getInstanceFromNode(IResourceID nodeID) throws RemoteException;
}
