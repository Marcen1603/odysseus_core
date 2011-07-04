package de.uniol.inf.is.odysseus.sla;

import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

/**
 * Event marking the change of a sla and related data. currently only adding and
 * removing of sla-related partial plans are possible
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class SLAChangedEvent {
	/**
	 * The type of the event
	 */
	private SLAChangedEventType type;
	/**
	 * the sla effected by the event
	 */
	private SLA sla;
	/**
	 * the query effected by the event
	 */
	private IQuery query;

	/**
	 * creates a new event
	 * @param type the vent type
	 * @param sla the sla effected by this event
	 * @param query the query effected by the event
	 */
	public SLAChangedEvent(SLAChangedEventType type, SLA sla, IQuery query) {
		super();
		this.type = type;
		this.sla = sla;
		this.query = query;
	}

	/**
	 * @return the type of the event
	 */
	public SLAChangedEventType getType() {
		return type;
	}

	/**
	 * @return the sla effected by the event
	 */
	public SLA getSla() {
		return sla;
	}

	/**
	 * @return the query effected by the event
	 */
	public IQuery getQuery() {
		return query;
	}

}
