package de.uniol.inf.is.odysseus.iec61970.dataaccessserver.communication;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import de.uniol.inf.is.odysseus.iec61970.dataaccessserver.browser.Item;
import de.uniol.inf.is.odysseus.iec61970.dataaccessserver.daf_services.ResourceIDService;
import de.uniol.inf.is.odysseus.iec61970.dataaccessserver.services.PathnameService;
import de.uniol.inf.is.odysseus.iec61970.library.client.service.ICallBack;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IResourceID;
import de.uniol.inf.is.odysseus.iec61970.library.server.service.IGroupManager;
import de.uniol.inf.is.odysseus.iec61970.library.server.service.IItem;


/**
 * Der Groupmanager tätigt die Subscriptions und nach Spezifikation auch die Übertragung, die wir aber in diesem Fall zentral der Java.nio Schnittstelle übergeben.
 * Über sein verwaltetes Callback Objekt werden nur die Verbindungsinformationen abgeglichen
 * @author Mart Köhler
 *
 */
public class GroupManager extends UnicastRemoteObject implements IGroupManager{
	private ICallBack callBack = null;
	private ArrayList<ItemDescription> itemDescriptions;
	private ArrayList<IResourceID> subscriptions;
	

	public ArrayList<IResourceID> getSubscriptions() {
		return subscriptions;
	}

	public void setSubscriptions(ArrayList<IResourceID> subscriptions) {
		this.subscriptions = subscriptions;
	}

	public GroupManager() throws RemoteException {
		super();
	}

	public ICallBack getCallBack() throws RemoteException{
		return callBack;
	}

	public void setCallBack(ICallBack callBack) {
		this.callBack = callBack;
	}

	public ArrayList<ItemDescription> getItemDescriptions() {
		return itemDescriptions;
	}

	public void setItemDescriptions(ArrayList<ItemDescription> itemDescriptions) {
		this.itemDescriptions = itemDescriptions;
	}

	@Override
	public boolean addCallBack(ICallBack cb) throws RemoteException {
		if(getCallBack()==null) {
			setCallBack(cb);
			return true;
		}
		return false;
	}

	@Override
	public boolean removeCallBack() throws RemoteException {
		if(getCallBack()!=null) {
			setCallBack(null);
			return true;
		}
		return false;
	}

	@Override
	public synchronized void createEntry(IResourceID id) throws RemoteException {
			if(this.subscriptions==null) {
				this.subscriptions = new ArrayList<IResourceID>();
			}
			synchronized (subscriptions) {
				System.err.println("Subscription der Ressource: "+ResourceIDService.getInstance().getURI(id));
				IItem testItem = new Item();
				if(testItem.isItem(testItem.getPathname(id))) {
					System.out.println("ist ein Item"); 
					this.subscriptions.add(id);
				}
				else {
					for(String path : PathnameService.getInstance().getIdPathname().keySet()) {
						if(path.startsWith(testItem.getPathname(id))) {
							if(testItem.isItem(path)) {
								System.out.println("ist ein Item unter Node: "+path); 
								this.subscriptions.add(testItem.getPathnameId(path));
							}
						}
					}
				}
				for(IResourceID elem : this.subscriptions) {
					System.out.println("derzeitige subscripiton "+elem.getContainer()+" "+elem.getFragment()+" von:"+getCallBack().getConnectionAddress()+":"+getCallBack().getConnectionPort());
				}
			}
	}

	@Override
	public synchronized ArrayList<IResourceID> getEntries() throws RemoteException {
			return this.subscriptions;	
	}
	
	@Override
	public synchronized void removeEntry() throws RemoteException {
		if(this.subscriptions!=null) {
			synchronized (this.subscriptions) {
				this.subscriptions.removeAll(this.subscriptions);
				Provider.removeCallBack(callBack.getConnectionAddress(), callBack.getConnectionPort());
			}
		}			
		
	}
	
}
