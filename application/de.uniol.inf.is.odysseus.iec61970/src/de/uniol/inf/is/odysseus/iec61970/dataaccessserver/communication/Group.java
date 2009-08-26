package de.uniol.inf.is.odysseus.iec61970.dataaccessserver.communication;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import de.uniol.inf.is.odysseus.iec61970.library.server.service.IGroup;
import de.uniol.inf.is.odysseus.iec61970.library.server.service.IGroupManager;
/**
 * Eine neue Gruppe nach TSDA/HSDA wird erzeugt, wenn der Client eine neue Anfrage stellen möchte und dafür eine Anzahl von Ressourcen registrieren möchte
 * Nicht alles ausimplementiert, da Gruppen automatisch zerstört werden, wenn der Client eine neue Anfrage stellt
 * 
 * @author Mart Köhler
 *
 */
public class Group extends UnicastRemoteObject implements IGroup {
	String groupName;
	IGroupManager gm = null;
	
	public Group(String group) throws RemoteException {
		super();
		setGroupName(group);
		setGroupManager(new GroupManager());
	}

	public IGroup createGroup(String group) throws RemoteException {
		return null;
	}


	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Override
	public IGroupManager createGroupFromPublic(String arg0) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IGroupManager find(String arg0) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IGroupManager> findPublicGroups() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removePublicGroup() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IGroupManager createGroup() throws RemoteException {
		return null;
	}

	@Override
	public IGroupManager getGroupManager() throws RemoteException {
		return this.gm;
	}

	@Override
	public void setGroupManager(IGroupManager gm) throws RemoteException {
		this.gm = gm;
	}

}
