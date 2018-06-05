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

/**
 * Instances of this class define a service level of a sla.
 * 
 * @author Thomas Vogelgesang
 *
 * @param <T> the data type of the threshold value
 */
public class ServiceLevel {
	/**
	 * the threshold value of the service level. this value must be reached by 
	 * the sla conformance. if the threshold is violated the given penalty is 
	 * payable
	 */
	private double threshold;
	/**
	 * the penalty that is payable if this service level is violated
	 */
	private Penalty penalty;
	/**
	 * reference to the sla object which contains this service level
	 */
	private SLA sla;
	
	/**
	 * creates a new service level
	 */
	public ServiceLevel() {
		
	}
	
	/**
	 * sets the threshold of the sla
	 * @param threshold the new threshold
	 */
	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	/**
	 * @return the trheshold of this sla
	 */
	public double getThreshold() {
		return threshold;
	}

	/**
	 * sets the containing sla of the service level 
	 * @param sla the containing sla object
	 */
	public void setSla(SLA sla) {
		this.sla = sla;
	}

	/**
	 * returns the sla object that contains the service level
	 * @return
	 */
	public SLA getSla() {
		return sla;
	}

	/**
	 * sets the penalty that is payable if the threshold of the service level
	 * is violated
	 * @param penalty the new penalty
	 */
	public void setPenalty(Penalty penalty) {
		this.penalty = penalty;
	}

	/**
	 * @return the penalty that is payable if the service level is violated
	 */
	public Penalty getPenalty() {
		return penalty;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Service Level (").append(this.threshold).append(", ").
		append(this.penalty).append(")");
		return sb.toString();
	}
	
}
