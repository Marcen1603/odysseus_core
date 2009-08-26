package de.uniol.inf.is.odysseus.iec61970.dataaccessserver.browser;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import de.uniol.inf.is.odysseus.iec61970.library.server.service.ISimpleIO;


/**
 * Nicht implementiert, aber in TSDA/HSDA vorhanden
 * Da schnelle Zugriffe außerhalb von Subscriptions nicht vorgesehen sind, bleibt diese Klasse leer. Sie kann aber entsprechend erweitert werden
 * @author Mart Köhler
 *
 */
public class SimpleIO extends UnicastRemoteObject implements ISimpleIO {

	protected SimpleIO() throws RemoteException {
		super();
	}

}
