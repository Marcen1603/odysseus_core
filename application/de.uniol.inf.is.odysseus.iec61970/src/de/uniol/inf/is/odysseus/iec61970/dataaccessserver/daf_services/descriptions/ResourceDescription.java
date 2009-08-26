package de.uniol.inf.is.odysseus.iec61970.dataaccessserver.daf_services.descriptions;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import de.uniol.inf.is.odysseus.iec61970.dataaccessserver.communication.Server;
import de.uniol.inf.is.odysseus.iec61970.dataaccessserver.daf_services.ResourceIDService;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IDescription;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IResourceDescription;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IResourceID;

/**
 * Dieser Dienst initialisiert den ResourceDescription Dienst, damit Elemente des Jena Frameworks sauber von dem ResourceDescription getrennt sind.
 * Da die Elemente von Jena Probleme mit RMI wegen der Serialisierung haben, muss das so umständlich gelöst werden
 * @author Mart Köhler
 *
 */
public class ResourceDescription extends UnicastRemoteObject implements IResourceDescription{
	private static ResourceDescription service = null;
	public synchronized static ResourceDescription getInstance() throws RemoteException {
		if (service == null) {
			System.out.println("Erzeuge ResourceDescription Dienst...");
			service = new ResourceDescription();
			return service;
		} else {
			return service;
		}
	}
	/**
	 * Konstruktor. Bei jedem Add werden die entsprechenden ResourceIDs und ihre Descriptions an den DescriptionStorage übertragen
	 * 
	 * @throws RemoteException
	 */
	private ResourceDescription() throws RemoteException{
		for(IResourceID id: ResourceIDService.getInstance().getNodeGroup()) {
			add(id);
		}
		for(IResourceID id: ResourceIDService.getInstance().getItemGroup()) {
			add(id);
		}
		for(IResourceID id: ResourceIDService.getInstance().getTypeGroup()) {
			add(id);
		}
		for(IResourceID id: ResourceIDService.getInstance().getInstanceGroup()) {
			add(id);
		}
		for(IResourceID id: ResourceIDService.getInstance().getPropertyGroup()) {
			add(id);
		}
		if(!Server.hMode) {
			for(IResourceID id: ResourceIDService.getInstance().getAggregateGroup()) {
				add(id);
			}
			for(IResourceID id: ResourceIDService.getInstance().getItemAttributeGroup()) {
				add(id);
			}
		}
	}
	
	private void add(IResourceID id) {
		ArrayList<String[]> triple = new ArrayList<String[]>();
		Resource res;
		try {
				res = Server.getModel().getResource(ResourceIDService.getInstance().getURI(id).toString());
			StmtIterator stmtIter = Server.getModel().listStatements(res, null, (RDFNode)null);
			while(stmtIter.hasNext()) {
				Statement stmt = stmtIter.nextStatement();
				triple.add(new String[]{stmt.getSubject().toString(),stmt.getPredicate().toString(),stmt.getObject().toString()});
			}
			IDescription description = new Description(triple);
			DescriptionStorage.getInstance().add(id, description);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
//	private void createResourceDescription() {
//		URI resourceURI = null;
//		try {
//			resourceURI = ResourceIDService.getInstance().getURI(getResource());
//		} catch (RemoteException e) {
//			e.printStackTrace();
//		}
//		Resource res = Server.getModel().getOntResource(resourceURI.toString());
//		StmtIterator stmtIter = Server.getModel().listStatements(res, null, (RDFNode)null);
//		while(stmtIter.hasNext()) {
//			Statement stmt = stmtIter.nextStatement();
////			System.out.println("Statement: "+stmt.getSubject().toString()+" ; "+stmt.getPredicate().toString()+" ; "+stmt.getObject().toString());
//			HashMap<IResourceID,String> map = new HashMap<IResourceID, String>();
//			try {
//				if(ResourceIDService.getInstance().getResourceID(new URI(stmt.getObject().toString()))!=null && !map.containsKey(ResourceIDService.getInstance().getResourceID(new URI(stmt.getObject().toString())))) {
////					System.out.println("main element: "+stmt.getSubject().toString());
////					System.out.println("Adde als Property: "+stmt.getObject().toString() +" und value: "+ (stmt.getObject().toString().contains("#")?stmt.getPredicate().toString().split("#")[1]:stmt.getPredicate().toString()));
//					map.put(ResourceIDService.getInstance().getResourceID(new URI(stmt.getObject().toString())), (stmt.getObject().toString().contains("#")?stmt.getPredicate().toString().split("#")[1]:stmt.getPredicate().toString()));
//					getProperty().add(map);
//					
//				}
////				else if(!stmt.getPredicate().toString().contains(Server.protocolNS)) {
////						System.out.println("Adde Null Property nur mit Value der URI: "+stmt.getPredicate().toString());
//						//map.put(null, stmt.getPredicate().toString());
//						//getProperty().add(map);
//					
////				}
//			} catch (RemoteException e) {
//				e.printStackTrace();
//			} catch (URISyntaxException e) {
//				System.out.println("URI konnte nicht gebildet werden. ist nicht schlimm, denn Element war wahrscheinlich eh nicht dafür vorgesehen");
////				e.printStackTrace();
//			}
//		}
//		
//	}
	
//	@Override
//	public StmtIterator getProperties(IResourceID id) throws RemoteException {
//		URI resURI = ResourceIDService.getInstance().getURI(id);
////		resObj = Server.getModel().getResource(resURI.toString());
//		//Wir setzen eine neue Anfrage ab, weil bei Nutzung der Zeiger sich bewegt und wir generell Zugriff auf alle Elemente haben wollen
//		stmtProperties = Server.getModel().listStatements(Server.getModel().getResource(resURI.toString()), null, (RDFNode) null);
//		return stmtProperties;
//	}
//	@Override
//	public IResourceDescriptionIterator<StmtIterator> getProperties(
//			List<IResourceID> ids) throws RemoteException {
//		ArrayList<StmtIterator> result = new ArrayList<StmtIterator>();
//		for(IResourceID id: ids) {
//			result.add(getProperties(id));
//		}
//			
//			return new ResourceDescriptionIterator<StmtIterator>(result);
//		
//	}
	
}
