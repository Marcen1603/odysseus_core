package de.uniol.inf.is.odysseus.iec61970.dataaccessserver.daf_services.descriptions;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IResourceDescriptionIterator;


public class ResourceDescriptionIterator<E> extends UnicastRemoteObject implements IResourceDescriptionIterator<E> {
	ArrayList<E> list = null;
	int pointer;

	public ResourceDescriptionIterator(ArrayList<E> list) throws RemoteException{
		this.list = list;
		this.pointer = 0;
	}
	@Override
	public boolean hasNext() throws RemoteException {
		if(this.list!=null) {
			if(pointer<=list.size()-1) {
				return true;
			}
			return false;
		}
		return false;
	}

	@Override
	public E next() throws RemoteException{
		if(hasNext()) {
			return list.get(pointer++);
		}
		return null;
	}

	@Override
	public void remove() throws RemoteException{
		if(pointer>0) {
			this.list.remove(--pointer);
		}
	}
	
	@Override
	public int size() throws RemoteException{
		return list.size();
	}

}
