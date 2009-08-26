package de.uniol.inf.is.odysseus.iec61970.dataaccessserver.browser;

import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.iec61970.dataaccessserver.communication.Server;
import de.uniol.inf.is.odysseus.iec61970.dataaccessserver.daf_services.ResourceIDService;
import de.uniol.inf.is.odysseus.iec61970.dataaccessserver.daf_services.descriptions.DescriptionStorage;
import de.uniol.inf.is.odysseus.iec61970.dataaccessserver.daf_services.descriptions.ResourceDescriptionIterator;
import de.uniol.inf.is.odysseus.iec61970.dataaccessserver.services.PathnameService;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IDescription;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IResourceDescriptionIterator;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IResourceID;
import de.uniol.inf.is.odysseus.iec61970.library.server.service.INode;
import de.uniol.inf.is.odysseus.iec61970.library.server.service.IType;

/**
 * Schnittstelle von Node in HSDA/TSDA
 * @author Mart Köhler
 *
 */
public class Node extends UnicastRemoteObject implements INode {

	public Node() throws RemoteException {
		super();
	}

	@Override
	public IDescription find(IResourceID resource) throws RemoteException {
		for(IResourceID elem : ResourceIDService.getInstance().getNodeGroup()) {
			if(elem.equals(resource)) {
				return DescriptionStorage.getInstance().getDescription(resource);
			}
		}
		return null;
	}

	@Override
	public IResourceDescriptionIterator<IDescription> findByParent(IResourceID resourceID)
			throws RemoteException {
		ArrayList<IDescription> resultList = new ArrayList<IDescription>();
		boolean exist = false;
		for(IResourceID elem : ResourceIDService.getInstance().getNodeGroup()) {
			if(elem.equals(resourceID)) {
				exist = true;	
			}
		}
		for(IResourceID elem : ResourceIDService.getInstance().getNodeGroup()) {
			IDescription desc = DescriptionStorage.getInstance().getDescription(elem);
			for(String[] dataset : desc.getDescription()) {
				if(desc.getPredicate(dataset).equals(Server.protocolNS+"#parentNode")) {
					if(desc.getObject(dataset).endsWith(ResourceIDService.getInstance().getURI(resourceID).toString())) {
						try {
							resultList.add(DescriptionStorage.getInstance().getDescription(ResourceIDService.getInstance().getResourceID(new URI(desc.getSubject(dataset)))));
						} catch (URISyntaxException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return new ResourceDescriptionIterator<IDescription>(resultList);
//		if(exist) {
//			ArrayList<URI> resourceURIs = ResourceIDService.getInstance().getURIs(ResourceIDService.getInstance().getNodeGroup());
//			for(URI uri : resourceURIs) {
//				OntResource resourceInOntology = Server.model.getOntResource(uri.toString());
//				StmtIterator iterator = Server.model.listStatements(resourceInOntology, null, (RDFNode)null);
//				//StmtIterator iterator = Server.model.listStatements(Server.helper.createResource(uri.toString()),Server.helper.createProperty(Server.protocolNS+"#parentNode"),(RDFNode)null);
//				while(iterator.hasNext()) {
//					Statement stmt = iterator.nextStatement();
//					if(stmt.getPredicate().toString().equals(Server.protocolNS+"#parentNode") && stmt.getObject().toString().equals(ResourceIDService.getInstance().getURI(resourceID).toString())) {
//					try {
//						IDescription resDesc = DescriptionStorage.getInstance().getDescription(ResourceIDService.getInstance().getResourceID(new URI(stmt.getSubject().toString())));
//						resultList.add(resDesc);
//					} catch (URISyntaxException e) {
//						e.printStackTrace();
//					}
//					}
//				}
//			}
//		}
//		return new ResourceDescriptionIterator<IDescription>(resultList);
	}


	@Override
	//Es wird die Type ID übergeben und zu einem gebenen Node alle Kinder gesucht, die von diesem Type id sind. TODO: noch richtig zu implementieren. Fraglich, ob das gebraucht wird
	public IResourceDescriptionIterator<IDescription> findByType(IResourceID typeID)
				throws RemoteException {
		ArrayList<IDescription> resultList = new ArrayList<IDescription>();
		IDescription typeDesc = DescriptionStorage.getInstance().getDescription(typeID);
		for(String[] desc: typeDesc.getDescription()) {
			System.out.println(typeDesc.getSubject(desc) +" "+typeDesc.getPredicate(desc)+ " "+typeDesc.getObject(desc));
		}
		IType type = new Type();
		for(IResourceID instance : ResourceIDService.getInstance().getInstanceGroup()) {
			IDescription instanceDesc = type.findByInstance(instance);
//			for(String[] desc :instanceDesc.getDescription()) {
//				if(instanceDesc.getSubject(desc).equals())
//			}
		}
		
//		ArrayList<IDescription> resultList = new ArrayList<IDescription>();
//		URI typeURI = null;
//		boolean exist = false;
//		for(IResourceID elem : ResourceIDService.getInstance().getTypeGroup()) {
//			if(elem.equals(typeID)) {
//				exist = true;
//				typeURI = ResourceIDService.getInstance().getURI(typeID);
//				break;
//			}
//		}
//		if(exist) {
//			OntClass ontClass = Server.model.getOntClass(Server.protocolNS+"#Node");
//			ExtendedIterator nodeIter = ontClass.listInstances();
//			while(nodeIter.hasNext()) {
//				Resource nodeRes = (Resource) nodeIter.next();
//				StmtIterator stmtIter = Server.model.listStatements(nodeRes, Server.model.createProperty(Server.protocolNS+"#refersTo"), (RDFNode)null);
//				while(stmtIter.hasNext()) {
//					Statement resultStmt = stmtIter.nextStatement();
//					try {
//						IDescription resDesc = DescriptionStorage.getInstance().getDescription(ResourceIDService.getInstance().getResourceID(new URI(resultStmt.getObject().toString())));
//						resultList.add(resDesc);
//					} catch (URISyntaxException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}
//		return new ResourceDescriptionIterator<IDescription>(resultList);
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
	public IResourceDescriptionIterator<IDescription> getAllDescriptions()
			throws RemoteException {
		return findEach(ResourceIDService.getInstance().getNodeGroup());
	}

	@Override
	public List<IResourceID> getAllNodeIds() throws RemoteException {
		return ResourceIDService.getInstance().getNodeGroup();
	}

	@Override
	public List<String> getAllNodePathnames() throws RemoteException {
		
		List<String> result = getPathnames(ResourceIDService.getInstance().getNodeGroup());
		return result;
	}
	
	@Override
	public IResourceID getInstanceFromNode(IResourceID nodeID) throws RemoteException {
		try {
			IDescription nodeDescription = DescriptionStorage.getInstance().getDescription(nodeID);
			for(String[] triple : nodeDescription.getDescription()) {
				if(nodeDescription.getPredicate(triple).endsWith("refersTo")) {
					return ResourceIDService.getInstance().getResourceID(new URI(nodeDescription.getObject(triple)));
				}
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
		
	}

}
