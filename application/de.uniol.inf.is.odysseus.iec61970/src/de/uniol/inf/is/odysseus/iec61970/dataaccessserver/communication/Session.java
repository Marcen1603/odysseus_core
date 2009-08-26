package de.uniol.inf.is.odysseus.iec61970.dataaccessserver.communication;

import java.nio.channels.Selector;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import de.uniol.inf.is.odysseus.iec61970.library.client.service.ICallBack;
import de.uniol.inf.is.odysseus.iec61970.library.client.service.IShutdownCallBack;
import de.uniol.inf.is.odysseus.iec61970.library.server.service.IGroup;
import de.uniol.inf.is.odysseus.iec61970.library.server.service.ISession;

public class Session extends UnicastRemoteObject implements ISession {
	private String sessionStartTime;
	private ArrayList<IGroup> groups;
	private Selector selector = null;
	public Session() throws RemoteException {
		super();
		this.groups = new ArrayList<IGroup>();
		this.sessionStartTime = (System.currentTimeMillis() / 1000) + "";
	}

	@Override
	/**
	 * Wenn eine Session erzeugt wurde, dann kann mit der Erzeugung einer Group eine Anfrage auf den Datenstrom über diese Gruppe abgesetzt werden
	 * In dem jeweiligen GroupManager zu jeder Gruppe, können Subscriptions gemacht werden.
	 */
	public boolean createGroup(String name, ICallBack cb) throws RemoteException {
		try {
			IGroup g = new Group("Query Group");
			g.getGroupManager().addCallBack(cb);
			getGroups().add(g);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	@Override
	public boolean removeGroup(String name) throws RemoteException {
		for(IGroup group : getGroups()) {
			if(group.getGroupName().equals(name)) {
				getGroups().remove(group);
				return true;
			}
		}
		return false;
	}
	@Override
	public ArrayList<IGroup> getGroups() {
		return groups;
	}

	public void setGroups(ArrayList<IGroup> groups) {
		this.groups = groups;
	}

	public String getSessionStartTime() {
		return sessionStartTime;
	}

	@Override
	public IShutdownCallBack getShutdownCallBack() throws RemoteException {
		return null;
	}

	@Override
	public String sessionStartTime() throws RemoteException {
		return this.sessionStartTime;
	}


}
