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
import de.uniol.inf.is.odysseus.iec61970.library.server.service.IProperty;


/**
 * Schnittstelle zu Property Elemente in HSDA/TSDA
 * @author Mart Köhler
 *
 */
@SuppressWarnings("unchecked")
public class Property extends UnicastRemoteObject implements IProperty {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7509951825891898018L;

	public Property() throws RemoteException {
		super();
	}

	@Override
	public IDescription find(IResourceID resource) throws RemoteException {
		for(IResourceID elem : ResourceIDService.getInstance().getPropertyGroup()) {
			if(elem.equals(resource)) {
				return DescriptionStorage.getInstance().getDescription(resource);
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
	public IResourceDescriptionIterator<IDescription> findByNode(
			IResourceID arg0) throws RemoteException {
		return null;
	}

	@Override
	public IResourceDescriptionIterator<IDescription> findByType(
			IResourceID arg0) throws RemoteException {
		return null;
	}

	@Override
	public IResourceDescriptionIterator<IDescription> getAllDescriptions()
			throws RemoteException {
		return findEach(ResourceIDService.getInstance().getPropertyGroup());
	}

	@Override
	public List<IResourceID> getAllPropertyIds() throws RemoteException {
		return ResourceIDService.getInstance().getPropertyGroup();
	}

	@Override
	public IDescription findByItem(IResourceID id)throws RemoteException {
		URI itemUri = ResourceIDService.getInstance().getURI(id);
		for(IResourceID instanceID :ResourceIDService.getInstance().getInstanceGroup()) {
			IDescription instanceDesc = DescriptionStorage.getInstance().getDescription(instanceID);
			for(String[] instanceDescElem :instanceDesc.getDescription()) {
				if(instanceDesc.getObject(instanceDescElem).equals(itemUri.toString())) {
					try {
						return DescriptionStorage.getInstance().getDescription(ResourceIDService.getInstance().getResourceID(new URI(instanceDesc.getSubject(instanceDescElem))));
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}

}
