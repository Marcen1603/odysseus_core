/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.core.server.sla;

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
	private double price;
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
	private Metric<?> metric;
	/**
	 * the list of service levels defined by the sla.
	 */
	private List<ServiceLevel> serviceLevel;
	/**
	 * maximum admission costs allowed for a user owning this sla
	 */
	private double maxAdmissionCost;
	/**
	 * a penalty to fine if the query was killed by the system/provider
	 */
	private Penalty queryKillPenalty;

	/**
	 * creates a new sla.
	 */
	public SLA() {
		super();
		this.serviceLevel = new ArrayList<ServiceLevel>();
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
	public double getPrice() {
		return price;
	}

	/**
	 * sets the price payable for a query with a QoS defined by this sla
	 * 
	 * @param price
	 *            the new price
	 */
	public void setPrice(double price) {
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
	public Metric<?> getMetric() {
		return metric;
	}

	/**
	 * sets the metric used for this sla
	 * 
	 * @param metric
	 *            the new metric
	 */
	public void setMetric(Metric<?> metric) {
		this.metric = metric;
	}

	/**
	 * 
	 * @return the list of service levels defined by the sla. not null. the list
	 *         of service levels is ordered! the first list entry is the most
	 *         valuable service level, the last entry is the less valuable
	 *         service level
	 */
	public List<ServiceLevel> getServiceLevel() {
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
	public void setServiceLevel(List<ServiceLevel> serviceLevel) {
		if (serviceLevel == null)
			throw new IllegalArgumentException("null");
		this.serviceLevel = serviceLevel;
	}

	/**
	 * returns the maximum penalty costs
	 * 
	 * @return
	 */
	public double getMaxPenalty() {
		return this.serviceLevel.get(this.serviceLevel.size() - 1).getPenalty()
				.getCost();
	}
	

	public double getMaxAdmissionCost() {
		return maxAdmissionCost;
	}

	public void setMaxAdmissionCost(double maxAdmissionCost) {
		this.maxAdmissionCost = maxAdmissionCost;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("SLA (").append(this.name).append(", ");
		sb.append(this.metric).append(", ").append(this.scope);
		sb.append(", ").append(this.window);
		
		for (ServiceLevel sl : this.serviceLevel) {
			sb.append(", ").append(sl);
		}
		sb.append(")");
		return sb.toString();
	}
	
}
