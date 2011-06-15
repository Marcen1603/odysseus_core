package de.uniol.inf.is.odysseus.scheduler.slamodel;

import java.util.ArrayList;
import java.util.List;

/**
 * An instance of this class represents a service level agreement.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class SLA {
	/**
	 * the name of the sla.
	 */
	private String name;
	/**
	 * the price to pay for a query with a QoS defined by the SLA.
	 */
	private int price;
	/**
	 * the scope of the SLA.
	 */
	private Scope scope;
	/**
	 * the evaluation window of the sla.
	 */
	private Window window;
	/**
	 * the metric used in this sla.
	 */
	private Metric<?, ?> metric;
	/**
	 * the list of service levels defined by the sla.
	 */
	private List<ServiceLevel<?>> serviceLevel;

	/**
	 * creates a new sla.
	 */
	public SLA() {
		super();
		this.serviceLevel = new ArrayList<ServiceLevel<?>>();
	}
	
	/**
	 * @return the name of the sla
	 */
	public String getName() {
		return name;
	}

	/**
	 * sets the name of the sla
	 * 
	 * @param name
	 *            the new name of the sla
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the price payable for a query with a QoS defined by this sla
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * sets the price payable for a query with a QoS defined by this sla
	 * 
	 * @param price
	 *            the new price
	 */
	public void setPrice(int price) {
		this.price = price;
	}

	/**
	 * 
	 * @return the scope of the sla
	 */
	public Scope getScope() {
		return scope;
	}

	/**
	 * sets the scope of the sla
	 * 
	 * @param scope
	 *            the new scope
	 */
	public void setScope(Scope scope) {
		this.scope = scope;
	}

	/**
	 * 
	 * @return the evaluation window of the sla
	 */
	public Window getWindow() {
		return window;
	}

	/**
	 * sets the new evaluation window of the sla
	 * 
	 * @param window
	 *            the new window
	 */
	public void setWindow(Window window) {
		this.window = window;
	}

	/**
	 * 
	 * @return the metric used for the sla
	 */
	public Metric<?, ?> getMetric() {
		return metric;
	}

	/**
	 * sets the metric used for this sla
	 * 
	 * @param metric
	 *            the new metric
	 */
	public void setMetric(Metric<?, ?> metric) {
		this.metric = metric;
	}

	/**
	 * 
	 * @return the list of service levels defined by the sla. not null.
	 * the list of service levels is ordered! the first list entry is the most 
	 * valuable service level, the last entry is the less valuable service level
	 */
	public List<ServiceLevel<?>> getServiceLevel() {
		return serviceLevel;
	}

	/**
	 * sets the list of service levels defined by this sla.
	 * 
	 * @param serviceLevel
	 *            the new list of service levels must not be null.
	 * @throws IllegalArgumentException
	 *             iff {@link serviceLevel} is null
	 */
	public void setServiceLevel(List<ServiceLevel<?>> serviceLevel) {
		if (serviceLevel == null)
			throw new IllegalArgumentException("null");
		this.serviceLevel = serviceLevel;
	}
	
}
