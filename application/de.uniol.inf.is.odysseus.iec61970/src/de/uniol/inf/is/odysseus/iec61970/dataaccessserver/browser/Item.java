package de.uniol.inf.is.odysseus.iec61970.dataaccessserver.browser;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.iec61970.dataaccessserver.daf_services.ResourceIDService;
import de.uniol.inf.is.odysseus.iec61970.dataaccessserver.daf_services.descriptions.DescriptionStorage;
import de.uniol.inf.is.odysseus.iec61970.dataaccessserver.daf_services.descriptions.ResourceDescriptionIterator;
import de.uniol.inf.is.odysseus.iec61970.dataaccessserver.services.PathnameService;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IDescription;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IResourceDescriptionIterator;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IResourceID;
import de.uniol.inf.is.odysseus.iec61970.library.server.service.IItem;

/**
 * Schnittstelle von Item im Datenmodell zu TSDA/HSDA
 * Hier werden Methoden für den Zugriff zu Pathnames bereitgestellt.
 * @author Mart Köhler
 *
 */
public class Item extends UnicastRemoteObject implements IItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1581504842664402423L;

	public Item() throws RemoteException {
		super();
	}

	@Override
	public IDescription find(IResourceID resource) throws RemoteException {
		for(IResourceID elem : ResourceIDService.getInstance().getItemGroup()) {
			if(elem.equals(resource)) {
				return DescriptionStorage.getInstance().getDescription(resource);
			}
		}
		return null;
	}

	@Override
	public IResourceDescriptionIterator<IDescription> findByParent(IResourceID arg0)
			throws RemoteException {
		return null;
	}

	@Override
	public IResourceDescriptionIterator<IDescription> findByType(IResourceID arg0)
			throws RemoteException {
		return null;
	}

	@Override
	public IResourceDescriptionIterator<IDescription> findEach(
			ArrayList<IResourceID> ids) throws RemoteException {
		ArrayList<IDescription> resultList = new ArrayList<IDescription>();
		for(IResourceID id : ids) {
			resultList.add(find(id));
		}
		return new ResourceDescriptionIterator<IDescription>(resultList);
	}
	
	@Override
	public IResourceID getPathnameId(String pathname) throws RemoteException {
		return PathnameService.getInstance().getID(pathname);
	}
	
	@Override
	public List<IResourceID> getPathnameIds(List<String> pathnames) throws RemoteException {
		ArrayList<IResourceID> resultList = new ArrayList<IResourceID>();
		for(String pathname : pathnames) {
			resultList.add(PathnameService.getInstance().getID(pathname));
		}

//		for(String pathname : pathnames) {
//			for(IResourceID id : ResourceIDService.getInstance().getNodeGroup()) {
//				OntResource node = Server.getModel().getOntResource(ResourceIDService.getInstance().getURI(id).toString());
//				if(node!=null) {
//					StmtIterator iter = node.listProperties();
//					while(iter.hasNext()) {
//						Statement st  = iter.nextStatement();
//					}
//					Statement stmt = node.getProperty(Server.getModel().getProperty(Server.protocolNS+"#pathname"));
//					if(stmt!=null && stmt.getObject().toString().equals(pathname)) {
//						resultList.add(id);
//						break;
//					}
//				}
//			}
//		}
		return resultList;
	}

	@Override
	public List<String> getPathnames(List<IResourceID> ids)
			throws RemoteException {
		ArrayList<String> resultList = new ArrayList<String>();
		for(IResourceID id : ids) {
			resultList.add(PathnameService.getInstance().getPathname(id));	
		}
		
//		for(IResourceID id : ids) {
//			OntResource nodeOrItem = Server.getModel().getOntResource(ResourceIDService.getInstance().getURI(id).toString());
//			if(nodeOrItem!=null) {
//				Statement stmt = nodeOrItem.getProperty(Server.getModel().getProperty(Server.protocolNS+"#pathname"));
//				if(stmt!=null) {
//					resultList.add(stmt.getObject().toString());
//				}
//			}
//		}
		return resultList;
	}

	@Override
	public String getPathname(IResourceID id) throws RemoteException {
		return PathnameService.getInstance().getPathname(id);
	}
	
	@Override
	public List<IResourceID> getAllItemIds() throws RemoteException {
			return ResourceIDService.getInstance().getItemGroup();

	}

	@Override
	public IResourceDescriptionIterator<IDescription> getAllDescriptions()
			throws RemoteException {
		return findEach(ResourceIDService.getInstance().getItemGroup());
	}
	
	@Override
	public String getDatatypeOf(IResourceID id) throws RemoteException{
		String itemPath = getPathname(id);
		IResourceID property = PathnameService.getInstance().getIdPropertyMap().get(itemPath);
		IDescription description = DescriptionStorage.getInstance().getDescription(property);
		for(String[] triple : description.getDescription()) {
			if(description.getPredicate(triple).endsWith("dataType")) {
				return description.getObject(triple).split("#")[1];
			}
		}
		return "not defined";		
	}

	@Override
	public List<String> getAllItemPathnames() throws RemoteException {
		List<String> result = getPathnames(ResourceIDService.getInstance().getItemGroup());
		return result;
	}

	@Override
	public boolean isItem(String pathname) throws RemoteException{
		if(getPathnameId(pathname)==null) {
			return false;
		}
		IResourceID id = getPathnameId(pathname);
		IDescription desc = find(id);
		if(desc!=null) {
			return true;
		}
		else {
			return false;
		}
	}

}
