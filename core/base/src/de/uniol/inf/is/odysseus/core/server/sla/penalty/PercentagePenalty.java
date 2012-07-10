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
package de.uniol.inf.is.odysseus.core.server.sla.penalty;

/**
 * This kind of penalty is defined by a certain percentage of the price payed
 * for the service.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class PercentagePenalty extends AbstractPenalty {

	/**
	 * the percentage that must be payed if the service level is violated
	 */
	private double percentage;

	/**
	 * creates a new percentage penalty
	 * 
	 * @param percentage
	 *            the percentage
	 */
	public PercentagePenalty(double percentage) {
		this.setPercentage(percentage);
	}

	/**
	 * returns the cost of the penalty, defined as percentage of the price for
	 * the service
	 */
	@Override
	public double getCost() {
		return this.getServiceLevel().getSla().getPrice() * percentage;
	}

	/**
	 * @return the percentage
	 */
	public double getPercentage() {
		return percentage;
	}

	/**
	 * sets a new percentage
	 * @param percentage the new percentage
	 */
	public void setPercentage(double percentage) {
		if (percentage < 0.0f)
			throw new IllegalArgumentException("negative percentage");
		this.percentage = percentage;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Percentage Penalty (").append(this.percentage).append(")");
		
		return sb.toString();
	}
	
}
