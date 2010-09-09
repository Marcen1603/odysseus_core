package de.uniol.inf.is.odysseus.iec61970.library.daf_service;

import java.net.URI;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;


/**
 * Der Resource Identifier Service, über diesen Umwandlungen von Identifier zu URI und umgekehrt möglich sind.
 * Außerdem werden die Identifier in Gruppen abgelegt, so dass sie von dort direkt abgerufen werden können.
 * 
 * @author Mart Köhler
 *
 */

public interface IResourceIDService extends Remote {
	public ArrayList<IResourceID> getResourceIDs(ArrayList<URI> uriSequence) throws RemoteException;
	public IResourceID getResourceID(URI uri) throws RemoteException;
	public URI getURI(IResourceID id) throws RemoteException;
	public ArrayList<URI> getURIs(ArrayList<IResourceID> resourceIDSequence) throws RemoteException;
	public ArrayList<IResourceID> getItemGroup() throws RemoteException;
	public ArrayList<IResourceID> getNodeGroup() throws RemoteException;
	public ArrayList<IResourceID> getPropertyGroup() throws RemoteException;
	public ArrayList<IResourceID> getTypeGroup() throws RemoteException;
	public ArrayList<IResourceID> getItemAttributeGroup() throws RemoteException;
	public ArrayList<IResourceID> getAggregateGroup() throws RemoteException;
	public boolean contains(ArrayList<IResourceID> list, String name) throws RemoteException;
}
