package de.uniol.inf.is.odysseus.fastflowerdelivery.store.event;

import de.uniol.inf.is.odysseus.fastflowerdelivery.event.AbstractEvent;

/**
 * Represents a Driver, which is part of the internal structure
 * of Delivery Request events, to keep track of the current state.
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class Driver extends AbstractEvent {

	private String reference = "";
	private boolean assigned = false;
	
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public boolean isAssigned() {
		return assigned;
	}
	public void setAssigned(boolean assigned) {
		this.assigned = assigned;
	}
	
}
