package de.uniol.inf.is.odysseus.iec61970.dataaccessserver.browser;

import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.iec61970.dataaccessserver.daf_services.ResourceIDService;
import de.uniol.inf.is.odysseus.iec61970.dataaccessserver.daf_services.descriptions.DescriptionStorage;
import de.uniol.inf.is.odysseus.iec61970.dataaccessserver.daf_services.descriptions.ResourceDescriptionIterator;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IDescription;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IResourceDescriptionIterator;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IResourceID;
import de.uniol.inf.is.odysseus.iec61970.library.server.service.IType;


/**
 * Schnittstelle zu Type Elementen in HSDA/TSDA
 * @author Mart Köhler
 *
 */
public class Type extends UnicastRemoteObject implements IType {

	public Type() throws RemoteException {
		super();
	}

	@Override
	public IDescription find(IResourceID resource) throws RemoteException {
		for(IResourceID elem : ResourceIDService.getInstance().getTypeGroup()) {
			if(elem.equals(resource)) {
				return DescriptionStorage.getInstance().getDescription(resource);
			}
		}
		return null;
	}
	@Override
	public IDescription findByInstance(IResourceID resource) throws RemoteException {
		for(IResourceID elem : ResourceIDService.getInstance().getInstanceGroup()) {
			if(elem.equals(resource)) {
				IDescription desc = DescriptionStorage.getInstance().getDescription(resource);
				for(String[] descElem : desc.getDescription()) {
					if(desc.getPredicate(descElem).endsWith("#type")) {
						try {
							return find(ResourceIDService.getInstance().getResourceID(new URI(desc.getObject(descElem))));
						} catch (URISyntaxException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return null;
	}

	@Override
	public IResourceDescriptionIterator<IDescription> findEach(
			ArrayList<IResourceID> ids) throws RemoteException {
		ArrayList<IDescription> resultList = new ArrayList<IDescription>();
		for(IResourceID id : ids) {
			resultList.add(find(id));
		}
		return new ResourceDescriptionIterator(resultList);
	}

	@Override
	public IResourceDescriptionIterator<IDescription> getAllDescriptions()
			throws RemoteException {
		return findEach(ResourceIDService.getInstance().getTypeGroup());
	}

	@Override
	public List<IResourceID> getAllTypeIds() throws RemoteException {
		return ResourceIDService.getInstance().getTypeGroup();
	}


}
