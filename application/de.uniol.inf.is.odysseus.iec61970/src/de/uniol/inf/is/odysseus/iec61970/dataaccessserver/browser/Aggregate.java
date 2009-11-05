package de.uniol.inf.is.odysseus.iec61970.dataaccessserver.browser;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.iec61970.dataaccessserver.daf_services.ResourceIDService;
import de.uniol.inf.is.odysseus.iec61970.dataaccessserver.daf_services.descriptions.DescriptionStorage;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IDescription;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IResourceID;
import de.uniol.inf.is.odysseus.iec61970.library.server.service.IAggregate;

/**
 * Schnittstelle von Aggregate im TSDA Datenmodell
 * @author Mart Köhler
 *
 */
public class Aggregate extends UnicastRemoteObject implements IAggregate {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3120505509211887076L;

	public Aggregate() throws RemoteException {
		super();
	}


	@Override
	/**
	 * Gibt Label des Aggregate zurück
	 */
	public String find(IResourceID id) throws RemoteException {
		for(IResourceID groupId : ResourceIDService.getInstance().getAggregateGroup()) {
			if(id.equals(groupId)) {
				IDescription aggregateDesc = DescriptionStorage.getInstance().getDescription(groupId);
				for(String[] triple : aggregateDesc.getDescription()) {
					if(aggregateDesc.getPredicate(triple).endsWith("#label")) {
						return aggregateDesc.getObject(triple);
					}
				}
			}
		}
		return null;
	}

	@Override
	public List<String> findAll() throws RemoteException {
		ArrayList<String> label = new ArrayList<String>();
		for(IResourceID id : ResourceIDService.getInstance().getAggregateGroup()) {
			label.add(find(id));
		}
		return label;
	}

}
