package de.uniol.inf.is.odysseus.fastflowerdelivery.driver.event;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.fastflowerdelivery.driver.dao.BidRequestDAO;
import de.uniol.inf.is.odysseus.fastflowerdelivery.driver.dao.LiveEventRegistry;
import de.uniol.inf.is.odysseus.fastflowerdelivery.event.AbstractEvent;
import de.uniol.inf.is.odysseus.fastflowerdelivery.io.ISinkEventHandler;

/**
 * Represents the Bid Request event
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class BidRequest extends AbstractEvent implements ISinkEventHandler {

	private String storeReference = "";
	private String adresseeLocation = "";
	private long requiredPickupTime = 0;
	private long requiredDeliveryTime = 0;
	private String neighborhood = "";
	private int minimumRanking = 0;
	private String driverReference = "";
	private boolean assigned = false;
	private String assignedTo = "";
	private boolean bidPlaced = false;
	private boolean pickedUp = false;
	private boolean delivered = false;
	
	public BidRequest() {}
	
	@SuppressWarnings("rawtypes")
	public BidRequest(Tuple tuple) {
		Object[] attributes = tuple.getAttributes();
		this.occurrenceTime = (long) attributes[0];
		this.requestId = (int) attributes[1];
		this.storeReference = (String) attributes[2];
		this.adresseeLocation = (String) attributes[3];
		this.requiredPickupTime = (long) attributes[4];
		this.requiredDeliveryTime = (long) attributes[5];
		this.neighborhood = (String) attributes[6];
		this.minimumRanking = (int) attributes[7];
		this.driverReference = (String) attributes[8];
	}
	
	@Override
	public List<String> getSchema() {
		ArrayList<String> result = new ArrayList<String>();
		result.add("StartTimestamp");
		result.add("Integer");
		result.add("String");
		result.add("String");
		result.add("Long");
		result.add("Long");
		result.add("String");
		result.add("Integer");
		result.add("String");
		result.add("StartTimestamp");
		result.add("EndTimestamp");
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void processTuple(Tuple tuple) {
		BidRequest request = new BidRequest(tuple);
		LiveEventRegistry.getInstance().register(request);
		BidRequestDAO.getInstance().add(request);
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
	public String getNeighborhood() {
		return neighborhood;
	}
	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}
	public int getMinimumRanking() {
		return minimumRanking;
	}
	public void setMinimumRanking(int minimumRanking) {
		this.minimumRanking = minimumRanking;
	}
	public String getDriverReference() {
		return driverReference;
	}
	public void setDriverReference(String driverReference) {
		this.driverReference = driverReference;
	}

	public boolean isAssigned() {
		return assigned;
	}

	public void setAssigned(boolean assigned) {
		this.assigned = assigned;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public boolean isBidPlaced() {
		return bidPlaced;
	}

	public void setBidPlaced(boolean bidPlaced) {
		this.bidPlaced = bidPlaced;
	}

	public boolean isPickedUp() {
		return pickedUp;
	}

	public void setPickedUp(boolean pickedUp) {
		this.pickedUp = pickedUp;
	}

	public boolean isDelivered() {
		return delivered;
	}

	public void setDelivered(boolean delivered) {
		this.delivered = delivered;
	}
	
}
