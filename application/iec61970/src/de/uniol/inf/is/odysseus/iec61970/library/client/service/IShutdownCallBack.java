package de.uniol.inf.is.odysseus.iec61970.library.client.service;

import java.rmi.RemoteException;

/**
 * Das Shutdown Objekt soll den angemeldeten Clients den bevorstehenden Shutdown des Servers signalisieren. Dieses Objekt ist optional nach Definition und wird hier nicht weiter verwendet.
 * @author Mart Köhler
 *
 */
public interface IShutdownCallBack {
	public int getStatus() throws RemoteException;
}
