package de.uniol.inf.is.odysseus.iec61970.library.server.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Facade für die HSDA und TSDA Schnittstellen
 * 
 * @author Mart Köhler
 *
 */
public interface IFacade extends Remote{
	public String getMode() throws RemoteException;
	public INode getNode() throws RemoteException;
	public IItem getItem() throws RemoteException;
	public IType getType() throws RemoteException;
	public IProperty getProperty() throws RemoteException;
	public IItemAttribute getItemAttribute() throws RemoteException;
	public IAggregate getAggregate() throws RemoteException;
}
