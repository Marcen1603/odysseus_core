package de.uniol.inf.is.odysseus.fastflowerdelivery.store.event;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.fastflowerdelivery.event.AbstractEvent;
import de.uniol.inf.is.odysseus.fastflowerdelivery.service.ServiceRequest;
import de.uniol.inf.is.odysseus.fastflowerdelivery.store.dao.DeliveryRequestDAO;
import de.uniol.inf.is.odysseus.fastflowerdelivery.io.ISourceEventHandler;
import de.uniol.inf.is.odysseus.generator.DataTuple;

/**
 * Represents a Delivery Request event
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class DeliveryRequest extends AbstractEvent implements ISourceEventHandler {

	private String storeReference = "";
	private String adresseeLocation = "";
	private long requiredPickupTime = 0;
	private long requiredDeliveryTime = 0;
	private String neighborhood = "";
	private List<Driver> drivers = new ArrayList<Driver>();
	private boolean pickupDone = false;
	private boolean deliveryDone = false;
	
	public DeliveryRequest() {}
	
	public DeliveryRequest(ServiceRequest request) {
		long pickupOffset = request.getLongParameter("PickupOffset");
		long deliveryOffset = request.getLongParameter("DeliveryOffset");
		String customer = request.getStringParameter("Customer");
		String neighborhood = request.getStringParameter("Neighborhood");
		String storeReference = request.getStringParameter("store");
		
		this.storeReference = storeReference;
		this.adresseeLocation = customer;
		this.requiredPickupTime = (pickupOffset * 60 * 1000) + occurrenceTime;
		this.requiredDeliveryTime = (deliveryOffset * 60 * 1000) + occurrenceTime;
		this.neighborhood = neighborhood;
		
		DeliveryRequestDAO.getInstance().add(this);
	}
	
	@Override
	public DataTuple getTuple() {
		DataTuple result = new DataTuple();
		result.addAttribute(new Long(occurrenceTime)); // Occurrence time (header)
		result.addAttribute(new Integer(requestId)); // Request id
		result.addAttribute(new String(storeReference)); // Store reference
		result.addAttribute(new String(adresseeLocation)); // Addressee location
		result.addAttribute(new Long(requiredPickupTime)); // Required pickup time +10min
		result.addAttribute(new Long(requiredDeliveryTime)); // Required delivery time +20min
		result.addAttribute(new String(neighborhood)); // Neighborhood
		return result;
	}
	
	public void addDriver(Driver driver) {
		driver.setRequestId(requestId);
		drivers.add(driver);
	}
	
	public String getStoreReference() {
		return storeReference;
	}
	public void setStoreReference(String storeReference) {
		this.storeReference = storeReference;
	}
	public String getAdresseeLocation() {
		return adresseeLocation;
	}
	public void setAdresseeLocation(String adresseeLocation) {
		this.adresseeLocation = adresseeLocation;
	}
	public long getRequiredPickupTime() {
		return requiredPickupTime;
	}
	public void setRequiredPickupTime(long requiredPickupTime) {
		this.requiredPickupTime = requiredPickupTime;
	}
	public long getRequiredDeliveryTime() {
		return requiredDeliveryTime;
	}
	public void setRequiredDeliveryTime(long requiredDeliveryTime) {
		this.requiredDeliveryTime = requiredDeliveryTime;
	}
	public List<Driver> getDrivers() {
		return drivers;
	}
	public void setDrivers(List<Driver> drivers) {
		this.drivers = drivers;
	}
	public boolean isPickupDone() {
		return pickupDone;
	}
	public void setPickupDone(boolean pickupDone) {
		this.pickupDone = pickupDone;
	}
	public boolean isDeliveryDone() {
		return deliveryDone;
	}
	public void setDeliveryDone(boolean deliveryDone) {
		this.deliveryDone = deliveryDone;
	}
	public String getNeighborhood() {
		return neighborhood;
	}
	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}
}
