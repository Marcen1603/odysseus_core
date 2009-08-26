package de.uniol.inf.is.odysseus.iec61970.library.server.service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import de.uniol.inf.is.odysseus.iec61970.library.client.service.ICallBack;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IResourceID;

/**
 * Der GroupManager verwaltet die Callbacks und Subscriptions innerhalb einer Gruppe
 * 
 * @author Mart Köhler
 *
 */
public interface IGroupManager  extends Remote{
	public boolean addCallBack(ICallBack cb) throws RemoteException;
	public boolean removeCallBack() throws RemoteException;
	public ICallBack getCallBack() throws RemoteException;
	public void createEntry(IResourceID id) throws RemoteException;
	public ArrayList<IResourceID> getEntries() throws RemoteException;
	public void removeEntry() throws RemoteException;
}
