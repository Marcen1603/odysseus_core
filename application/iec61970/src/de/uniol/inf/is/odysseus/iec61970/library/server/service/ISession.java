package de.uniol.inf.is.odysseus.iec61970.library.server.service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import de.uniol.inf.is.odysseus.iec61970.library.client.service.ICallBack;
import de.uniol.inf.is.odysseus.iec61970.library.client.service.IShutdownCallBack;


/**
 * Der Aufbau einer Session, welche eine Verbindung zu einem Client verwaltet
 * 
 * @author Mart Köhler
 *
 */
public interface ISession extends Remote{
	
	
	/**
	 * Startzeit der Session
	 * @return Startzeit als Zeichenkette
	 * @throws RemoteException
	 */
	public String sessionStartTime() throws RemoteException;
	/**
	 * Holt das ShutdownCallBack Objekt und erlaubt es dem Client die Sitzung zu beenden
	 * @return das ShutdownCallBack Objekt
	 * @throws RemoteException
	 */
	public IShutdownCallBack getShutdownCallBack() throws RemoteException;
	/**
	 * Es kann eine beliebige Anzahl Gruppen erzeugt werden, die ein CallBack halten können.
	 * @param name Name der Gruppe
	 * @return Interface der erzeugten Gruppe
	 * @throws RemoteException
	 */
	public boolean createGroup(String name, ICallBack cb) throws RemoteException;
	/**
	 * Holt alle derzeitigen Gruppen der Session
	 * @return Liste der Gruppen
	 * @throws RemoteException
	 */
	public ArrayList<IGroup> getGroups() throws RemoteException;
	
	public boolean removeGroup(String name) throws RemoteException;
	

	
}
