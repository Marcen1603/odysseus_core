package de.uniol.inf.is.odysseus.iec61970.library.server.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Schnittstelle zu dem Provider, welcher über java.nio die Verbindungen und Datentransfers verwaltet. 
 * Hierüber soll es möglich sein Subscriptions aufzuheben, indem zusätzlich der registrierte Client aus dem Provider entfernt wird.
 * @author Mart Köhler
 *
 */
public interface IProvider extends Remote{
	public void removeCallBack(String host, int port) throws RemoteException;
}
