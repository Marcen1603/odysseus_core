package de.uniol.inf.is.odysseus.iec61970.dataaccessserver.daf_services;

import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import de.uniol.inf.is.odysseus.iec61970.dataaccessserver.communication.Server;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IResourceID;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IResourceIDService;
import de.uniol.inf.is.odysseus.iec61970.library.serializable.ResourceID;



/**
 * Umsetzung des DAF resource identifier interfaces für die Umwandlung
 * ResourceID <-> URI
 * 
 * @author Mart Köhler
 * 
 */
public class ResourceIDService extends UnicastRemoteObject implements
		IResourceIDService {
	private static ResourceIDService service;
	private ArrayList<IResourceID> itemGroup;
	private ArrayList<IResourceID> nodeGroup;
	private ArrayList<IResourceID> propertyGroup;
	private ArrayList<IResourceID> typeGroup;
	private ArrayList<IResourceID> instanceGroup;
	private ArrayList<IResourceID> itemAttributeGroup;
	private ArrayList<IResourceID> aggregateGroup;
	//wird durch die initialisierung der anderen Elemente initialisiert :-)
	private HashMap<Long, String> container;
	private HashMap<Long, String> fragment;

	public synchronized static ResourceIDService getInstance() throws RemoteException {
		if (service == null) {
			System.out.println("Erzeuge ResourceIDService Dienst...");
			service = new ResourceIDService(Server.model, Server.helper);
			return service;
		} else {
			return service;
		}
	}

	private ResourceIDService(OntModel m, OntModel helper) throws RemoteException {
		itemGroup = new ArrayList<IResourceID>();
		aggregateGroup = new ArrayList<IResourceID>();
		itemAttributeGroup = new ArrayList<IResourceID>();
		nodeGroup = new ArrayList<IResourceID>();
		propertyGroup = new ArrayList<IResourceID>();
		instanceGroup = new ArrayList<IResourceID>();
		typeGroup = new ArrayList<IResourceID>();
		container = new HashMap<Long, String>();
		fragment = new HashMap<Long, String>();
		URI uri = null;
		System.out.println("Lese Datenmodell ein...");
		initNodes(m);
		initInstances(m);
		initItems(m);
		initProperties(m);
		initTypes(m);
		if(!Server.hMode) {
			initItemAttributes(m);
			initAggregate(m);
		}
	}


	private void initAggregate(OntModel m) throws RemoteException  {
		OntClass ontClass = m.getOntClass(Server.protocolNS+"#Aggregate");
		ExtendedIterator iter = ontClass.listInstances();
		while(iter.hasNext()) {
			Resource res = (Resource) iter.next();
			try {
				System.out.println("Aggregate: "+res.getURI());
				append(new URI(res.getURI()),getAggregateGroup());
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}

	private void initItemAttributes(OntModel m)  throws RemoteException {
		for(IResourceID itemID : getItemGroup()) {
			Resource itemRes =  m.getResource(getURI(itemID).toString());
			StmtIterator itemResIter = m.listStatements(itemRes, null, (RDFNode)null);
			while(itemResIter.hasNext()) {
				Statement itemStmt = itemResIter.nextStatement();
				if(itemStmt.getPredicate().toString().equals(Server.protocolNS+"#hasItemAttribute")) {
					try {
						System.out.println("ItemAttribute: "+itemStmt.getObject().toString());
						append(new URI(itemStmt.getObject().toString()),getItemAttributeGroup());
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
				}
			}
			
		}
	}

	private void initInstances(OntModel m) throws RemoteException {
		//Alle durch Node gekapselten Werte sind Instanzen, also aus Nodes
		Property property = m.getProperty(Server.protocolNS+"#refersTo");
		StmtIterator stmtIter = m.listStatements(null, property, (RDFNode)null);
		while(stmtIter.hasNext()) {
			Statement stmt = stmtIter.nextStatement();
			try {
				System.out.println("Instanz in Node: "+stmt.getObject().toString());
				append(new URI(stmt.getObject().toString()),getInstanceGroup());
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}

	private void initTypes(OntModel m) throws RemoteException {
		ArrayList<URI> uris = null;
		try {
			// Alle Node IDs in URIs umwandeln
			uris = this.getURIs(getNodeGroup());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		for(URI uriElem : uris) {
			Resource node = m.getOntResource(uriElem.toString());
			
			StmtIterator stmtIterator = m.listStatements(node, null, (RDFNode) null);
			while(stmtIterator.hasNext()) {
				Statement stmt = stmtIterator.nextStatement();				
				//refersTo zeigt zwar auf Instanz einer CIM Klasse, aber noch nicht auf dessen Typ
				if(stmt.getPredicate().getURI().equals(Server.protocolNS+"#refersTo")) {
					Resource resultRes= m.getOntResource(stmt.getObject().toString());
					StmtIterator resultResIter = m.listStatements(resultRes, null, (RDFNode) null);
					while(resultResIter.hasNext()) {
						Statement resultResStmt = resultResIter.nextStatement();
						if(resultResStmt.getPredicate().toString().endsWith("#type")) {
							try {
								System.out.println("Type:" +resultResStmt.getObject().toString());
								append(new URI(resultResStmt.getObject().toString()), getTypeGroup());
								//weitere Attribute registrieren
//								StmtIterator attrIter = m.listStatements(m.getResource(resultResStmt.getObject().toString()), null, (RDFNode)null);
//								while(attrIter.hasNext()) {
//									Statement attrStmt = attrIter.nextStatement();
//									if(attrStmt.getObject().toString().startsWith(Server.cimNS)) {
////										System.out.println("Type:füge hinzu: " +attrStmt.getObject().toString());
//										append(new URI(attrStmt.getObject().toString()),getAttributeGroup());
//									}
//									if(attrStmt.getPredicate().toString().endsWith("#label")) {
////										System.out.println("Type:füge hinzu: " +attrStmt.getPredicate().toString());
//										append(new URI(attrStmt.getPredicate().toString()),getAttributeGroup());
//									}
//								}
							} catch (URISyntaxException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}


	private void append(URI uri, ArrayList<IResourceID> group) throws RemoteException {
		boolean fragSet = false;
		boolean contSet = false;
		boolean newfragSet = false;
		boolean newcontSet = false;
		long frag = 0L;
		long cont = 0L;
		if(!getContainer().containsValue(uri.getScheme()+":"+uri.getSchemeSpecificPart())) {
			contSet = true;
			cont = getContainer().size()+1L;
			getContainer().put(getContainer().size()+1L, uri.getScheme()+":"+uri.getSchemeSpecificPart());
		}
		if(!getFragment().containsValue(uri.getFragment())) {
			fragSet = true;
			frag = getFragment().size()+1L;
			getFragment().put(getFragment().size()+1L, uri.getFragment());
		}
		if(!fragSet) {
			for(long key : getFragment().keySet()) {
				if(getFragment().get(key).equals(uri.getFragment())) {
					frag = key;
					newfragSet = true;
					break;
				}
			}
		}
		if(!contSet) {
			for(long key : getContainer().keySet()) {
				if(getContainer().get(key).equals(uri.getScheme()+":"+uri.getSchemeSpecificPart())) {
					cont = key;
					newcontSet = true;
					break;
				}
			}
		}
		if((fragSet && contSet)||(fragSet && newcontSet) || (contSet && newfragSet)) {
				group.add(new ResourceID(cont,frag));
			fragSet = false;
			contSet = false;
		}
	}

	private void initItems(OntModel m) throws RemoteException {
		
		//Verfahren, wenn Item auf Node zeigt
//		OntClass ontClass = m.getOntClass(Server.protocolNS+"#Item");
//		ExtendedIterator iter = ontClass.listInstances();
//		//Für alle Items
//		while(iter.hasNext()) {
//			//alle Items
//			Resource res = (Resource) iter.next();
//			Property prop = m.getProperty("http://www.student.uni-oldenburg.de/mart.koehler/2008/01/hsda#value");
//			StmtIterator it = m.listStatements(res, prop, (RDFNode)null);
//			while(it.hasNext()) {
//				Statement jo = it.nextStatement();
//				System.out.println("STATEMENT:"+jo.toString());
//				
//			}
//			Statement belongsToNode = res.getProperty(m.getProperty(Server.protocolNS+"#belongsToNode"));
//			//Checke, ob das Item wirklich zu dem Node gehört, indem wir das Property  suchen und den auch gleich mit abstauben
//			//Bis hier hin stehen uns Node und Instance Informationen zur Verfügung
//			Resource node = m.getResource(belongsToNode.getObject().toString());
//			for(IResourceID instance: getInstanceGroup()) {
//				Statement stmt = node.getProperty(m.getProperty(Server.protocolNS+"#refersTo"));
//				if(node.getProperty(m.getProperty(Server.protocolNS+"#refersTo")).getObject().toString().equals(getURI(instance).toString())) {
//					Resource instanceRes = m.getResource(getURI(instance).toString());
//					StmtIterator instanceStmts = m.listStatements(instanceRes, null, (RDFNode)null);
//					while(instanceStmts.hasNext()) {
//						Statement instanceStmt = instanceStmts.nextStatement();
//						System.out.println("STATEMENT INSTANZ:"+instanceStmt.toString());
//						if(instanceStmt.getObject().toString().equals(belongsToNode.getSubject().toString())) {
//							//wir haben unser Property
//							try {
//								System.out.println("Property hinzufügen:"+instanceStmt.getPredicate().toString());
//								//Wenn Property schon gibt, dann ists egal. Es werden auch nur die Properties betrachte, zu denen es Items gibt
//								append(new URI(instanceStmt.getPredicate().toString()), getPropertyGroup());
//								System.out.println("Passendes Item wird auch gleich geadded: "+belongsToNode.getSubject().toString());
//								append(new URI(belongsToNode.getSubject().toString()), getItemGroup());
//							} catch (URISyntaxException e) {
//								e.printStackTrace();
//							}
//							break;
//						}
//					}
//					break;
//				}
//			}
//			
//		}
			
		boolean isDefinedByProperty = false;
		boolean isAssociatedToNode = false;
		OntClass ontClass = m.getOntClass(Server.protocolNS+"#Item");
		ExtendedIterator iter = ontClass.listInstances();
		while(iter.hasNext()) {
			//alle Items
			Resource res = (Resource) iter.next();
				OntClass nodeOntClass = m.getOntClass(Server.protocolNS+"#Node");
				ExtendedIterator nodeIter = nodeOntClass.listInstances();
				while(nodeIter.hasNext()) {
					Resource nodeRes = (Resource) nodeIter.next();
					StmtIterator nodeStmtIter = m.listStatements(nodeRes, m.getProperty(Server.protocolNS+"#hasItem"), (RDFNode)null);
					while(nodeStmtIter.hasNext()) {
						Statement nodeStmt = nodeStmtIter.nextStatement();
						if(nodeStmt.getObject().toString().equals(res.toString())) {
							isAssociatedToNode = true;
							break;
						}
					}
				}
			if(isAssociatedToNode) {
				try {                        
					System.out.println("Item: "+res.getURI());
					append(new URI(res.getURI()), getItemGroup());          
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
				isAssociatedToNode = false;
			}
		}
	}

	private void initProperties(OntModel m) throws RemoteException {
		//ein property ist ein elemen innerhalb der instanz eines nodes, welches vom typ rdf:property ist und eine instanz referenziert
		for(IResourceID instanceElem : getInstanceGroup()) {
			OntResource instance = m.getOntResource(getURI(instanceElem).toString());
			StmtIterator instanceIterator = instance.listProperties();
			while(instanceIterator.hasNext()) {
				Statement instanceStmt = instanceIterator.nextStatement();
				if(instanceStmt.getObject().toString().contains(Server.instanceNS)) {
					try {
						if(getResourceID(new URI(instanceStmt.getObject().toString()))!=null) {
							IResourceID possibleProp = getResourceID(new URI(instanceStmt.getObject().toString()));
							for(IResourceID item : getItemGroup()) {
								if(item.equals(possibleProp)) {
									System.out.println("Property: "+instanceStmt.getPredicate().toString());
									append(new URI(instanceStmt.getPredicate().toString()), getPropertyGroup());
									//weitere Attribute registrieren
//									StmtIterator attrIter = m.listStatements(m.getResource(instanceStmt.getPredicate().toString()), null, (RDFNode)null);
//									while(attrIter.hasNext()) {
//										Statement attrStmt = attrIter.nextStatement();
//										if(attrStmt.getObject().toString().startsWith(Server.cimNS) &&  !attrStmt.getObject().toString().endsWith("domain")){
//											Resource propertyObj = m.getResource(attrStmt.getObject().toString());
//											StmtIterator propIter = m.listStatements(propertyObj, null, (RDFNode) null);
//											while(propIter.hasNext()) {
//												Statement propStmt = propIter.nextStatement();
//												if(contains(getItemGroup(),propStmt.getObject().toString())) {
//													System.out.println("1Prop:füge hinzu: " +attrStmt.getObject().toString());
//													append(new URI(propStmt.getObject().toString()),getAttributeGroup());													
//												}
//											}
//
//										}
//										else if(attrStmt.getPredicate().toString().endsWith("#label")) {
//											System.out.println("2Prop:füge hinzu: " +attrStmt.getPredicate().toString());
//											append(new URI(attrStmt.getPredicate().toString()),getAttributeGroup());
//										}
//										else if(attrStmt.getPredicate().toString().endsWith("#dataType")) {
//											System.out.println("3Prop:füge hinzu: " +attrStmt.getPredicate().toString());
//											append(new URI(attrStmt.getPredicate().toString()),getAttributeGroup());
//										}
//									}
//									break;
								}
							}
						}
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void initNodes(OntModel m) throws RemoteException {
		OntClass ontClass = m.getOntClass(Server.protocolNS+"#Node");
		ExtendedIterator iter = ontClass.listInstances();
		while(iter.hasNext()) {
			Resource res = (Resource) iter.next();
			try {
				System.out.println("Node: "+res.getURI());
				append(new URI(res.getURI()),getNodeGroup());
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public ArrayList<IResourceID> getResourceIDs(ArrayList<URI> uriSequence)
			throws RemoteException {
		ArrayList<IResourceID> returnList = new ArrayList<IResourceID>();
		boolean found = false;
		for (URI uriElem : uriSequence) {
			found = false;
			for (long contKey : getContainer().keySet()) {
				if (getContainer().get(contKey).equals(
						uriElem.getScheme() + ":"
								+ uriElem.getSchemeSpecificPart())) {
					for (long fragKey : getFragment().keySet()) {
						if (getFragment().get(fragKey).equals(
								uriElem.getFragment())) {
							found = true;
							returnList.add(new ResourceID(contKey, fragKey));
							break;
						}
					}
				}
				if (found) {
					break;
				}
			}
		}
		if(returnList.size()>0) {
			return returnList;	
		}
		else {
			return null;
		}
		
	}

	@Override
	public ArrayList<URI> getURIs(ArrayList<IResourceID> resourceIDSequence)
			throws RemoteException {
		ArrayList<URI> returnList = new ArrayList<URI>();
		for (IResourceID idElem : resourceIDSequence) {
			try {
				if (getContainer().get(idElem.getContainer()) != null
						&& getFragment().get(idElem.getFragment()) != null) {
					returnList.add(new URI(getContainer().get(
							idElem.getContainer())
							+ "#" + getFragment().get(idElem.getFragment())));
				}
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		if(returnList.size()>0) {
			return returnList;	
		}
		else {
			return null;
		}
	}

	public HashMap<Long, String> getContainer() throws RemoteException {
		return container;
	}
	public void setContainer(HashMap<Long, String> container) throws RemoteException{
		this.container = container;
	}
	public HashMap<Long, String> getFragment()  throws RemoteException{
		return fragment;
	}
	public void setFragment(HashMap<Long, String> fragment)  throws RemoteException{
		this.fragment = fragment;
	}
	public ArrayList<IResourceID> getItemGroup() throws RemoteException{
		return itemGroup;
	}
	public ArrayList<IResourceID> getNodeGroup() throws RemoteException{
		return nodeGroup;
	}
	public ArrayList<IResourceID> getPropertyGroup() throws RemoteException{
		return propertyGroup;
	}
	public ArrayList<IResourceID> getTypeGroup() throws RemoteException {
		return typeGroup;
	}

	public ArrayList<IResourceID> getInstanceGroup() throws RemoteException {
		return instanceGroup;
	}
	@Override
	public IResourceID getResourceID(URI uri) throws RemoteException {
		ArrayList<URI> list = new ArrayList<URI>();
		list.add(uri);
		ArrayList<IResourceID> resultList = getResourceIDs(list);
		if(resultList!=null) {
			return resultList.get(0);
		}
		else {
			return null;
		}
		
	}

	@Override
	public URI getURI(IResourceID id) throws RemoteException {
		ArrayList<IResourceID> list = new ArrayList<IResourceID>();
		list.add(id);
		ArrayList<URI> resultList = getURIs(list);
		if(resultList!=null) {
			return resultList.get(0);
		}
		else {
			return null;
		}
	}
	@Override
	public boolean contains(ArrayList<IResourceID> list, String name) throws RemoteException {
		IResourceID nameID = null;
		try {
			nameID = getResourceID(new URI(name));
		} catch (URISyntaxException e) {
			return false;
		}
		if(nameID!=null) {
			for(IResourceID elem : list) {
				if(elem.equals(nameID)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public ArrayList<IResourceID> getItemAttributeGroup() {
		return itemAttributeGroup;
	}
	@Override
	public ArrayList<IResourceID> getAggregateGroup() {
		return aggregateGroup;
	}
}
