package de.uniol.inf.is.odysseus.iec61970.library.daf_service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Dieses Interface stellt die Repräsentation einer Ressourcen Beschreibung dar. Durch den DescriptionStorage kann ein Objekt mit Daten gefüllt und abgefragt werden.
 * @author Mart Köhler
 *
 */
public interface IDescription extends Remote{
	public void addTriple(String[] triple) throws RemoteException;
	public String getSubject(String[] triple) throws RemoteException;
	public String getPredicate(String[] triple) throws RemoteException;
	public String getObject(String[] triple) throws RemoteException;
	public ArrayList<String[]> getDescription() throws RemoteException;
	public boolean equals(IDescription desc) throws RemoteException;
}
