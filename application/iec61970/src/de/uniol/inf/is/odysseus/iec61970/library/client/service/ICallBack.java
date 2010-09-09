package de.uniol.inf.is.odysseus.iec61970.library.client.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Das CallBack Interface vom Client. Es wird dem Server eine Instanz übergeben, wenn eine neue Gruppe angelegt wird 
 * @author Mart Köhler
 *
 */
public interface ICallBack extends Remote{
    public String getConnectionAddress() throws RemoteException;
	public int getConnectionPort() throws RemoteException;
}
