package de.uniol.inf.is.odysseus.fastflowerdelivery.event;

import java.util.Date;

/**
 * Base class for all event models.
 *
 * @author Weert Stamm
 * @version 1.0
 */
abstract public class AbstractEvent {

	/**
	 * A timestamp of the moment this event occurred
	 */
	protected long occurrenceTime = new Date().getTime();
	
	/**
	 * Retrieve the occurrence time
	 * @return
	 * 		a timestamp
	 */
	public long getOccurrenceTime() {
		return occurrenceTime;
	}

	/**
	 * Set the occurrence time
	 * @param occurrenceTime
	 * 			a timestamp
	 */
	public void setOccurrenceTime(long occurrenceTime) {
		this.occurrenceTime = occurrenceTime;
	}
	
	/**
	 * the id of the corresponding DeliveryRequest
	 */
	protected int requestId = 0;
	
	/**
	 * Retrieve the identification of the corresponding
	 * DeliveryRequest event
	 * @return
	 * 		the id
	 */
	public int getRequestId() {
		return requestId;
	}

	/**
	 * Set the identification of the corresponding
	 * DeliveryRequest event
	 * @param requestId
	 * 			the id to set
	 */
	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}
	
}
