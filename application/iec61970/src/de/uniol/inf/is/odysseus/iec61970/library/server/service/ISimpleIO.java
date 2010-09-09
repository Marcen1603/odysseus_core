package de.uniol.inf.is.odysseus.iec61970.library.server.service;

import java.rmi.Remote;

/**
 * Bereitstellung einer Schnittstelle für einfache IO Operationen, die ohne Subscriptions getätigt werden sollen. 
 * Da nur über Subscriptions der Datentransfer läuft, wird dieses Interface nicht benötigt, aber zur Erweiterung bereitgestellt.
 * @author Mart Köhler
 *
 */
public interface ISimpleIO extends Remote{

}
