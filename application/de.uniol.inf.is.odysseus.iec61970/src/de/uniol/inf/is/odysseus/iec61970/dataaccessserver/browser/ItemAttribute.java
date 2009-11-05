package de.uniol.inf.is.odysseus.iec61970.dataaccessserver.browser;

import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.uniol.inf.is.odysseus.iec61970.dataaccessserver.daf_services.ResourceIDService;
import de.uniol.inf.is.odysseus.iec61970.dataaccessserver.daf_services.descriptions.DescriptionStorage;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IDescription;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IResourceID;
import de.uniol.inf.is.odysseus.iec61970.library.server.service.IItemAttribute;

/**
 * Repräsentation von ItemAttribute in TSDA
 * @author Mart Köhler
 *
 */
public class ItemAttribute extends UnicastRemoteObject implements
		IItemAttribute {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5772316887159827651L;

	public ItemAttribute() throws RemoteException {
		super();
	}

	@Override
	public HashMap<String, HashMap<Object,String>> find(IResourceID arg0)
			throws RemoteException {
		return null;
	}

	@Override
	public List<HashMap<String, HashMap<Object,String>>> findAll(IResourceID arg0)
			throws RemoteException {
		return null;
	}
	
	@Override
	/**
	 * Wir können von hier aus das passende Item finden. Das hat den Vorteil, dass im HSDA Modus keine Methode aufgerufen werden kann, die auf ein ItemAttribute zeigt.
	 */
	public IResourceID getReferencingItem(IResourceID attributeID) throws RemoteException{
		for(IResourceID itemID : ResourceIDService.getInstance().getItemGroup()) {
//			if(itemID.equals(attributeID)) {
				IDescription itemDesc = DescriptionStorage.getInstance().getDescription(itemID);
				for(String[] triple : itemDesc.getDescription()) {
					if(itemDesc.getPredicate(triple).endsWith("#hasItemAttribute") && itemDesc.getObject(triple).endsWith(ResourceIDService.getInstance().getURI(attributeID).toString())) {
						try {
							return ResourceIDService.getInstance().getResourceID(new URI(itemDesc.getSubject(triple)));
						} catch (URISyntaxException e) {
							e.printStackTrace();
						}
					}
				}
//			}
		}
		return null;
	}
	
	@Override
	/**
	 * Umkehrung zu getReferencingItem. Wir geben der Methode ein Item und erhalten passende ItemAttributes , da ein Item mehrere Attributes besitzten kann.
	 */
	public List<IResourceID> findByItem(IResourceID itemID) throws RemoteException{
		ArrayList<IResourceID> result = new ArrayList<IResourceID>();
		for(IResourceID attributeID :ResourceIDService.getInstance().getItemAttributeGroup()) {
			System.out.println("ergebnis: "+getReferencingItem(attributeID).getContainer()+" "+getReferencingItem(attributeID).getFragment());
			System.out.println("item??"+itemID.getContainer()+" "+itemID.getFragment());
			if(getReferencingItem(attributeID)!=null && itemID.equals(getReferencingItem(attributeID))) {
				result.add(attributeID);
			}
		}
		return result;
	}
	
	@Override
	public IDescription getItemAttribute(IResourceID id) throws RemoteException {
		return DescriptionStorage.getInstance().getDescription(id);
	}
}
