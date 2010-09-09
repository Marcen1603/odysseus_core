package de.uniol.inf.is.odysseus.iec61970.dataaccessclient;



import java.rmi.RemoteException;

import de.uniol.inf.is.odysseus.iec61970.library.client.service.IShutdownCallBack;


public class Shutdown implements IShutdownCallBack{

	@Override
	public int getStatus() throws RemoteException {
		return 0;
	}

}
