package de.uniol.inf.is.odysseus.iec61970.library.server.service;

import java.rmi.Remote;
import java.rmi.RemoteException;



/**
 * 
 * Interface für den Aufbau einer Serverklasse nach IEC 61970-404 (HSDA) und IEC 61970-407 (TSDA)
 * Der Server ist in der Lage Client Sessions zu erzeugen 
 * 
 * @author Mart Köhler
 *
 */
public interface IServer extends Remote{
	/**
	 * Angabe über den Start Zeitpunkt des Servers
	 * @return Startzeit des Servers als Zeichenkette
	 * @throws RemoteException
	 */
	public String getServerStartTime() throws RemoteException;
	/**
	 * Angabe über die Online Zeit des Servers
	 * @return Zeitangabe als Zeichenkette
	 * @throws RemoteException
	 */
	public String getServerUpTime() throws RemoteException;
	/**
	 * Hier wird eine neue Session für einen Client erzeugt
	 / Session Objekt für Hierarchien erzeugen* @return die Session wird zurückgeliefert
	 * @throws RemoteException
	 */
	public void createDataAccessSession() throws RemoteException;
	/**
	 * TODO: entscheiden, ob dieses Interface nötig ist
	 * @return
	 * @throws RemoteException
	 */
	public ISession createDataAccessSessionForView() throws RemoteException;
	/**
	 * TODO:  entscheiden, ob dieses Interface nötig ist
	 * @return
	 * @throws RemoteException
	 */
	public String findViews() throws RemoteException;
	
	
	/**
	 * 
	 * @return true: HSDA , false: TSDA
	 * @throws RemoteException
	 */
	public boolean getTransferMode() throws RemoteException;
	
}
