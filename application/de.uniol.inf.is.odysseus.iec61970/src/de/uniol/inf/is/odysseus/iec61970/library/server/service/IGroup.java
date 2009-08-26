package de.uniol.inf.is.odysseus.iec61970.library.server.service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Repräsentation der Gruppenschnittstelle nach IEC 61970-404
 * Davon werden jedoch nicht alle Methoden verwendet, da die Umsetzung auf einen Client (ODYSSEUS) beschränkt ist
 * 
 * @author Mart Köhler
 *
 */
public interface IGroup extends Remote{
	public List<IGroupManager> findPublicGroups() throws RemoteException;
	public IGroupManager find(String id) throws RemoteException;
	public IGroupManager createGroup() throws RemoteException;
	public IGroupManager createGroupFromPublic(String description) throws RemoteException;
	public boolean removePublicGroup() throws RemoteException;
	public void setGroupManager(IGroupManager gm) throws RemoteException;
	public IGroupManager getGroupManager() throws RemoteException;
	public String getGroupName() throws RemoteException; 
	public void setGroupName(String groupName) throws RemoteException;
}
