package de.uniol.inf.is.odysseus.iec61970.dataaccessserver.communication;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.iec61970.library.client.service.ICallBack;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IResourceID;
import de.uniol.inf.is.odysseus.iec61970.library.serializable.HSDAObject;
import de.uniol.inf.is.odysseus.iec61970.library.serializable.TSDAObject;
import de.uniol.inf.is.odysseus.iec61970.library.server.service.IGroup;
import de.uniol.inf.is.odysseus.iec61970.library.server.service.ISession;


/**
 * Hier werden Datenpakete von einer Quelle entsprechend an die Subscriber weitergeleitet bzw. der Schreibwunsch an den Provider geleitet.
 * @author Mart Köhler
 *
 */
public class Processing implements Runnable{
	private List queue = new LinkedList();
	private Server server = null;
	private HashMap<IResourceID, ICallBack> subScriberMap;

	public Processing(Server server) {
		this.server = server;
		this.queue = new ArrayList();
		this.subScriberMap = new HashMap<IResourceID, ICallBack>();
	}
	public void processSession(Provider provider, byte[] data) {
		synchronized(queue) {
			queue.add(new Data(provider,data));
			queue.notify();
		}
	}


	@Override
	public void run() {
		Data data = null;
		while(true) {
			//Wenn queue leer ist, dann warten wir
			synchronized(queue) {
				while(queue.isEmpty()) {
					try {
						queue.wait();
					} catch (InterruptedException e) {
					}
				}
				data = (Data) queue.remove(0);
			}
			synchronized (subScriberMap) {
				setSubScriberMap(getSubscriber());
//				if(this.removeCallbacks!=null) {
//					data.getProvider().removeCallBack(this.removeCallbacks);
//					this.removeCallbacks = null;
//					
//					
//				}
			}
			if(!this.subScriberMap.isEmpty()) {
				// wir schicken, die Daten an alle, die diese registriert haben
				//zwar unschön aber funktioniert
				try {
					if(getServer().getTransferMode()) {
						data.getProvider();
						HSDAObject hsdaData = Provider.getHSDAData(data.getData());
						//sende erstmal an alle
	//					if(getSubscriber()!=null) {
						for(IResourceID id : getSubScriberMap().keySet()) {
							if(id.equals(hsdaData.getId())) {
								System.err.println("HSDA Objekt "+hsdaData.getId().getContainer()+" "+hsdaData.getId().getFragment()+" für "+getSubscriber().get(id).getConnectionAddress()+":"+getSubscriber().get(id).getConnectionPort()+"in die Übertragungsqueue hinzugefügt");
								data.getProvider().sendPending(getSubscriber().get(id), data.getData());
							}
						}
	
					}
					else {
						data.getProvider();
						TSDAObject tsdaData = Provider.getTSDAData(data.getData());
						for(IResourceID id : getSubScriberMap().keySet()) {
							if(id.equals(tsdaData.getId())) {
								System.err.println("TSDA Objekt "+tsdaData.getId().getContainer()+" "+tsdaData.getId().getFragment()+" für "+getSubscriber().get(id).getConnectionAddress()+":"+getSubscriber().get(id).getConnectionPort()+"in die Übertragungsqueue hinzugefügt");
								data.getProvider().sendPending(getSubscriber().get(id), data.getData());
							}
						}
	
					}
					
				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else {
				System.err.println("keine Subscriber zu diesem Objekt");			
			}
	
		}
	}
	private synchronized HashMap<IResourceID, ICallBack> getSubscriber() {
		HashMap<IResourceID, ICallBack> resultMap = new HashMap<IResourceID, ICallBack>();
		for(ISession session : getServer().getSessions()) {
			try {
				for(IGroup group : session.getGroups()) {
					if(group.getGroupManager().getEntries()!=null) {
						for(IResourceID id : group.getGroupManager().getEntries()) {
							resultMap.put(id, group.getGroupManager().getCallBack());
						}
					}
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return resultMap;
	}
	public Server getServer() {
		return server;
	}
	public HashMap<IResourceID, ICallBack> getSubScriberMap() {
		return subScriberMap;
	}
	public void setSubScriberMap(HashMap<IResourceID, ICallBack> subScriberMap) {
		this.subScriberMap = subScriberMap;
	}
}
