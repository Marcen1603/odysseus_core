package de.uniol.inf.is.odysseus.iec61970.dataaccessserver.daf_services.descriptions;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IDescription;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IDescriptionStorage;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IResourceID;

/**
 * Hier liegen alle Descriptions zu allen Ressourcen, die von ResourceDescription stammen.
 * @author Mart Köhler
 *
 */
public class DescriptionStorage extends UnicastRemoteObject implements IDescriptionStorage{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1615974196757752592L;
	private static DescriptionStorage service = null;
	private HashMap<IResourceID,IDescription> descriptionStore;
	
	public synchronized static DescriptionStorage getInstance() throws RemoteException {
		if (service == null) {
			System.out.println("Erzeuge ResourceDescription Dienst...");
			service = new DescriptionStorage();
			return service;
		} else {
			return service;
		}
	}
	
	private DescriptionStorage() throws RemoteException{
		this.descriptionStore = new HashMap<IResourceID, IDescription>();
	}
	
	public DescriptionStorage(HashMap<IResourceID, IDescription> store) throws RemoteException {
		this.descriptionStore = store;
	}
	
	@Override
	public  HashMap<IResourceID,IDescription> getAllDescriptions()  throws RemoteException{
		return this.descriptionStore;
	}
	
	@Override
	public void add(IResourceID id, IDescription description)  throws RemoteException{
		this.descriptionStore.put(id, description);
	}
	
	@Override
	public IDescription getDescription(IResourceID id)  throws RemoteException{
		for(IResourceID elem : this.descriptionStore.keySet()) {
			try {
				if(id.equals(elem)) {
					return this.descriptionStore.get(elem);
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
