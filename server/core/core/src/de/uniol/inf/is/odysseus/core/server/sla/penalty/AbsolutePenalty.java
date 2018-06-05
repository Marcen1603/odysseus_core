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
 * This kind of penalty is defined by an absolute amount payable on violation of
 * a service level.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class AbsolutePenalty extends AbstractPenalty {

	/**
	 * the absolute cost for service level violation
	 */
	private double cost;

	/**
	 * creates a new absolute penalty object
	 * 
	 * @param cost
	 *            the absolute costs of teh penalty
	 */
	public AbsolutePenalty(double cost) {
		super();
		this.setCost(cost);
	}

	/**
	 * returns the absolute costs of the penalty
	 */
	@Override
	public double getCost() {
		return this.cost;
	}

	/**
	 * sets the new absolute costs of the penalty
	 * 
	 * @param cost
	 *            the new cost
	 */
	public void setCost(double cost) {
		if (cost < 0)
			throw new IllegalArgumentException("negative costs");
		this.cost = cost;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Absolute Penalty (").append(this.cost).append(")");
		
		return sb.toString();
	}

}
