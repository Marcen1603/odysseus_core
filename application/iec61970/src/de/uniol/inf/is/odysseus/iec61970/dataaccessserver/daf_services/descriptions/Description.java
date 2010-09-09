package de.uniol.inf.is.odysseus.iec61970.dataaccessserver.daf_services.descriptions;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IDescription;


public class Description extends UnicastRemoteObject implements IDescription{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9033580652969169106L;
	private ArrayList<String[]> triple = new ArrayList<String[]>(); 
	protected Description(ArrayList<String[]> triple) throws RemoteException {
		super();
		this.triple = triple;
	}
	@Override
	public void addTriple(String[] triple) throws RemoteException{
		this.triple.add(triple);
	}
	@Override
	public String getSubject(String[] triple)  throws RemoteException{
		if(triple.length==3) {
			return triple[0];
		}
		return "";
	}
	@Override
	public String getPredicate(String[] triple)  throws RemoteException{
		if(triple.length==3) {
			return triple[1];
		}
		return "";
	}
	@Override
	public String getObject(String[] triple)  throws RemoteException{
		if(triple.length==3) {
			return triple[2];
		}
		return "";
	}
	@Override
	public ArrayList<String[]> getDescription()  throws RemoteException{
		return this.triple;
	}
	@Override
	public boolean equals(IDescription desc) throws RemoteException {
		for(int i=0; i<desc.getDescription().size(); i++) {
			if(!triple.get(i)[0].equals(desc.getDescription().get(i)[0]) || !triple.get(i)[1].equals(desc.getDescription().get(i)[1]) || !triple.get(i)[2].equals(desc.getDescription().get(i)[2])) {
				return false;
			}
		}
		return true;
	}
}
