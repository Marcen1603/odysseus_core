package de.uniol.inf.is.odysseus.iec61970.dataaccessserver.services;

import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.util.HashMap;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import de.uniol.inf.is.odysseus.iec61970.dataaccessserver.browser.Node;
import de.uniol.inf.is.odysseus.iec61970.dataaccessserver.communication.Server;
import de.uniol.inf.is.odysseus.iec61970.dataaccessserver.daf_services.ResourceIDService;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IDescription;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IResourceDescriptionIterator;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IResourceID;
import de.uniol.inf.is.odysseus.iec61970.library.server.service.INode;

/**
 * Generiert die Pfadnamen und stellt diese zur Verfügung
 *
 * @author Mart Köhler
 *
 */
public class PathnameService {
	// Ordnet Pathnames zu IDs zu
	private HashMap<String,IResourceID> idPathname;
	private HashMap<String,IResourceID> idPropertyMap;
	private static PathnameService instance;
	public static PathnameService getInstance() {
		if(instance==null) {
			instance = new PathnameService();
			return instance;
		}
		else {
			return instance;
		}
	}
	private PathnameService() {
		this.idPathname = new HashMap<String, IResourceID>();
		this.idPropertyMap = new HashMap<String, IResourceID>();
	}
	
	
	public IResourceID getID(String pathname) {
		return this.idPathname.get(pathname);
	}
	public String getPathname(IResourceID id) {
		for(String path : getIdPathname().keySet()) {
			try {
				if(getID(path).equals(id)) {
					return path;
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	
	public Resource findRoot(OntModel model) {
		INode node = null;
		try {
			node = new Node();
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
		try {
			boolean hasParent = false;
			for(IResourceID resourceID : ResourceIDService.getInstance().getNodeGroup()) {
				IDescription stmtIter = node.find(resourceID);
//				if(description!=null) {
//					StmtIterator stmtIter = description.getProperties(); 
					for(String[] elem : stmtIter.getDescription()) {
						if(stmtIter.getPredicate(elem).toString().endsWith("parentNode")) {
							hasParent = true;
						}
					}
//				}
				if(!hasParent) {
					return Server.getModel().getOntResource(ResourceIDService.getInstance().getURI(resourceID).toString());
				}
				else {
					hasParent = false;
				}
				
				
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Das Ziel ist es Alle Nodes und Items mit einem Pfadnamen zu versehen
	 * @param model
	 */
	public void appendPathnames(OntModel model) {
		System.out.println("Automtatisches Hinzufügen von Pfaden zum Modell...");
		Resource rootNode = findRoot(model);
		StringBuffer pathnameConcat = new StringBuffer("IECTC57PhysicalView");
		System.out.println("Pfadname: "+pathnameConcat+" ("+rootNode.getURI()+")");
		try {
			getIdPathname().put(pathnameConcat.toString(),ResourceIDService.getInstance().getResourceID(new URI(rootNode.getURI())));
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		searchTree(rootNode, pathnameConcat, model);
	}
	
	private void searchTree(Resource actualNode, StringBuffer pathnameConcat, OntModel model) {
		INode node = null;
		try {
		node = new Node();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		// Root Node braucht kein Pathname, da er keine Funktion hat, sondern nur den Anfang markiert für die Konformität gegenüber IECTC 57 View
		IResourceDescriptionIterator<IDescription> descriptionIterator = null;
		try {
			descriptionIterator = node.findByParent(ResourceIDService.getInstance().getResourceID(new URI(actualNode.getURI())));
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		try {
			while(descriptionIterator.hasNext()) {
				IDescription iter = descriptionIterator.next();
				Resource res = null;
				//					res = model.getOntResource(ResourceIDService.getInstance().getURI(descriptionIterator.next().getResource()).toString());
				res = model.getOntResource(iter.getSubject(iter.getDescription().get(0)).toString());
				Statement property = res.getProperty(model.getProperty(Server.protocolNS+"#refersTo"));
				OntResource ontRes = model.getOntResource(property.getObject().toString());
				Statement label = ontRes.getProperty(model.getProperty("http://www.w3.org/2000/01/rdf-schema"+"#label"));
				pathnameConcat.append("."+label.getObject().toString());
				if(getID(pathnameConcat.toString())==null && getIdPathname().get(pathnameConcat.toString())==null) {
					getIdPathname().put(pathnameConcat.toString(),ResourceIDService.getInstance().getResourceID(new URI(res.getURI())));
					System.out.println("Pfadname: "+pathnameConcat+" ("+res.getURI()+")");
//					res.addLiteral(model.getProperty(Server.protocolNS+"#pathname"), pathnameConcat);
				}
				//Dann reinspringen in eine Methode für Item Check
				itemPathnameAppend(model, ontRes, pathnameConcat);
				//rekursion - schmeiß die derzeitige Ressource rein ,wenn alles eingefügt wurde.
				searchTree(res, pathnameConcat, model);
				pathnameConcat.delete(pathnameConcat.lastIndexOf("."), pathnameConcat.length());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Dient dazu Item Elemente anhand ihrer Properties in Pathname aufgenommen zu werden
	 * @param model
	 * @param res
	 * @param pathnameConcat
	 */
	private void itemPathnameAppend(OntModel model, Resource res, StringBuffer pathnameConcat) {
		boolean hasItem = false;
		URI property = null;
		IResourceID objRes =null;
				Resource instance = res;
				StmtIterator instanceStmts = model.listStatements(instance, null, (RDFNode)null);
				while(instanceStmts.hasNext()) {
					Statement instanceStmt = instanceStmts.nextStatement();
					//Prüfe jedes Object im Statement, da es eventuell ein Item sein kann
					try {
						if((objRes = ResourceIDService.getInstance().getResourceID(new URI(instanceStmt.getObject().toString())))!=null) {
							for(IResourceID id : ResourceIDService.getInstance().getItemGroup()) {
								if(id.equals(objRes)) {
									property = new URI(instanceStmt.getPredicate().toString());
									hasItem = true;
									break;
								}
							}
						}
					} catch (RemoteException e) {
						e.printStackTrace();
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
					if(hasItem) {
						break;
					}
				}
			if(hasItem) {
				try {
					hasItem = false;
					//Prüfe, ob das gefundene Property wirklich eins ist, man weiß ja nie
					for(IResourceID id : ResourceIDService.getInstance().getPropertyGroup()) {
						if(ResourceIDService.getInstance().getResourceID(property)!=null && id.equals(ResourceIDService.getInstance().getResourceID(property))) {
							pathnameConcat.append(property.getFragment().substring(property.getFragment().lastIndexOf("."), property.getFragment().length()));
							//wir führen noch eine Property Map, um später einfacher rauszufinden, welches Item zu welchem Property gehört
							idPropertyMap.put(pathnameConcat.toString(), ResourceIDService.getInstance().getResourceID(property));
							OntResource item = model.getOntResource(ResourceIDService.getInstance().getURI(objRes).toString());
							if(getID(pathnameConcat.toString())==null && getIdPathname().get(pathnameConcat.toString())==null) {
//								item.addLiteral(model.getProperty(Server.protocolNS+"#pathname"), pathnameConcat);
								System.out.println("Pfadname: "+pathnameConcat.toString()+" ("+property.toString()+")");
								getIdPathname().put(pathnameConcat.toString(),ResourceIDService.getInstance().getResourceID(new URI(item.getURI())));
							}
							pathnameConcat.delete(pathnameConcat.lastIndexOf("."), pathnameConcat.length());
							objRes = null;
							break;
						}
					}
				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
			}
	}
	public HashMap<String, IResourceID> getIdPathname() {
		return idPathname;
	}
	public HashMap<String, IResourceID> getIdPropertyMap() {
		return idPropertyMap;
	}
	
}
