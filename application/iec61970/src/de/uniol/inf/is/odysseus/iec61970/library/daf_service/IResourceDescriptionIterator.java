package de.uniol.inf.is.odysseus.iec61970.library.daf_service;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * 
 * Iterator für die Ressourcen Beschreibungen. Er kommt zum Einsatz, falls eine Anfrage mehr als eine Ressourcenbeschreibung als Antwort liefert.
 * 
 * @author Mart Köhler
 *
 * @param <E>
 */
public interface IResourceDescriptionIterator<E> extends Remote{
	public boolean hasNext() throws RemoteException;
	public E next() throws RemoteException;
	public void remove() throws RemoteException;
	public int size() throws RemoteException;
}
