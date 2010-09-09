package de.uniol.inf.is.odysseus.iec61970.dataaccessclient.handler;

import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IDescription;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IResourceID;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IResourceIDService;
import de.uniol.inf.is.odysseus.iec61970.library.server.service.IFacade;


/**
 * Stellt der GUI die Daten zur Verfügung
 * @author Mart Köhler
 *
 */
public class ContentHandler {
	private IFacade facade;
	private IResourceIDService service;
	public IResourceIDService getService() {
		return service;
	}
	public IFacade getFacade() {
		return facade;
	}
	public ContentHandler(IFacade facade, IResourceIDService service) {
		this.facade = facade;
		this.service = service;
	}
	public String[][] getTableData(String[] header) {
		String[][] result = null;
		try {
			result = new String[getService().getNodeGroup().size()+getService().getItemGroup().size()][header.length];
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		ArrayList<String> pathname = new ArrayList<String>();
		try {
			pathname.addAll(getFacade().getNode().getAllNodePathnames());
			pathname.addAll(getFacade().getItem().getAllItemPathnames());
			for(int i=0; i<pathname.size(); i++) {
				String cimType = "";
				//Node
				if(getFacade().getNode().find(getFacade().getNode().getPathnameId(pathname.get(i)))!=null) {
					IDescription description = getFacade().getNode().find(getFacade().getNode().getPathnameId(pathname.get(i)));
					for(String[] elem :description.getDescription()) {
						if(description.getPredicate(elem).endsWith("#refersTo")) {
							IDescription type = getFacade().getType().findByInstance(service.getResourceID(new URI(description.getObject(elem))));
							for(String[] typeDesc : type.getDescription()) {
								cimType = type.getSubject(typeDesc);
							}
						}
					}
				}
				//Item
				else if(getFacade().getItem().find(getFacade().getItem().getPathnameId(pathname.get(i)))!=null) {
					IDescription propDesc = getFacade().getProperty().findByItem(getFacade().getItem().getPathnameId(pathname.get(i)));
					for(String[] elem : propDesc.getDescription()) {
						if(propDesc.getObject(elem).equals(service.getURI(getFacade().getItem().getPathnameId(pathname.get(i))).toString())) {
							cimType = propDesc.getPredicate(elem);
							System.out.println(propDesc.getSubject(elem)+ " "+propDesc.getPredicate(elem)+ " "+propDesc.getObject(elem));
						}
					}
				}
				String[] row = {pathname.get(i), cimType, (getFacade().getNode().find(getFacade().getNode().getPathnameId(pathname.get(i)))!=null ? "Node" : "Item") };
				result[i] = row;			
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	return result;	
	}
	
	public String[] fillInfoBox(String pathname) {
//		if(Client.hMode) {
//			
//		}
//		else {
			System.out.println(pathname);
			String[] result = new String[2];
			try {
				result[0] =  service.getURI(facade.getNode().getPathnameId(pathname)).toString();
				result[1] = facade.getNode().getPathnameId(pathname).getContainer() + " "+ facade.getNode().getPathnameId(pathname).getFragment();
				return result;
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			
//		}
		return null;
		
	}
//	Triple: http://www.student.uni-oldenburg.de/mart.koehler/2008/01/tsda_instance#_006 http://www.student.uni-oldenburg.de/mart.koehler/2008/01/tsda#hasItemAttributeDefinition Integer
//		Triple: http://www.student.uni-oldenburg.de/mart.koehler/2008/01/tsda_instance#_006 http://www.student.uni-oldenburg.de/mart.koehler/2008/01/tsda#hasItemAttributeValue 3
//		Triple: http://www.student.uni-oldenburg.de/mart.koehler/2008/01/tsda_instance#_006 http://www.w3.org/2000/01/rdf-schema#label Version
//		Triple: http://www.student.uni-oldenburg.de/mart.koehler/2008/01/tsda_instance#_006 http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.student.uni-oldenburg.de/mart.koehler/2008/01/tsda#ItemAttribute
	public String[] fillItemAttributeList(String pathname) {
		List<IResourceID> itemAttributeList = null;
		ArrayList<String> result = new ArrayList<String>();
		try {
			IResourceID itemID = facade.getItem().getPathnameId(pathname);
			if(itemID!=null) {
				itemAttributeList = facade.getItemAttribute().findByItem(itemID);
				if(itemAttributeList!=null) {
					for(IResourceID id : itemAttributeList) {
						IDescription attrDesc= facade.getItemAttribute().getItemAttribute(id);
						//haben wir tatsächlich genug Informationen?
						if(attrDesc.getDescription().size()>3) {
							for(String[] triple : attrDesc.getDescription()) {
								System.out.println("Triple: "+attrDesc.getSubject(triple)+" "+attrDesc.getPredicate(triple)+" "+attrDesc.getObject(triple));
								if(attrDesc.getPredicate(triple).endsWith("hasItemAttributeDefinition")) {
									for(String[] triple2 : attrDesc.getDescription()) {
										if(attrDesc.getPredicate(triple2).endsWith("label")) {
											for(String[] triple3 : attrDesc.getDescription()) {
												if(attrDesc.getPredicate(triple3).endsWith("hasItemAttributeValue")) {
													result.add(attrDesc.getObject(triple2).toString() +" = "+attrDesc.getObject(triple3).toString()+" ("+attrDesc.getObject(triple).toString()+")");
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
//			else {
//				System.out.println("hier bin ich");
//				String[] empty = new String[1];
//				empty[0] = "";
//				return empty;
//			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		if(!result.isEmpty()) {
			String[] resultString = new String[result.size()];
			for(int i=0; i<result.size(); i++) {
				resultString[i] = result.get(i);
			}
			return resultString;
		}
		return null;
	}
	
	/**
	 * Füllt eine Liste mit den verfügbaren AggregateDefinitions
	 * @return
	 */
	public String[] getAggregateList() {
		String[] data = null;
		try {
				data = new String[facade.getAggregate().findAll().size()];
			for(int i=0; i<facade.getAggregate().findAll().size(); i++) {
				data[i] = facade.getAggregate().findAll().get(i);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return data;
	}
}
