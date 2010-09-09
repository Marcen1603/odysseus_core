package de.uniol.inf.is.odysseus.iec61970.library.serializable;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IResourceID;

/**
 * Konkrete Implementierung des IResourceID Interfaces
 * 
 * @author Mart Köhler
 *
 */
public class ResourceID extends UnicastRemoteObject implements IResourceID {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2646568776331336286L;
	private  long container;
	private  long fragment;
	
	public ResourceID(long container, long fragment) throws RemoteException{
		this.container = container;
		this.fragment = fragment;
	}

	@Override
	public long getContainer() {
		return this.container;
	}

	@Override
	public long getFragment() {
		return this.fragment;
	}

	@Override
	public boolean equals(IResourceID id) throws RemoteException{
		if((id.getContainer()==getContainer()) && (id.getFragment()==getFragment())) {
			return true;
		}
		return false;
	}
}
