/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.mining.clustering.logicaloperator;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.DoubleParameter;

/**
 * This class represents the logical operator for the leader algorithm.
 * 
 * @author Kolja Blohm
 * 
 */
@LogicalOperator(name="LEADER", minInputPorts=1, maxInputPorts=1)
public class LeaderAO extends AbstractClusteringAO {

	private static final long serialVersionUID = -5809997584476169607L;
	private Double threshold;

	/**
	 * Returns the leader algorithms threshold
	 * 
	 * @return the threshold
	 */
	public Double getThreshold() {
		return threshold;
	}

	/**
	 * Creates a new LeaderAO.
	 */
	public LeaderAO() {
		super();
	}

	/**
	 * Copy constructor.
	 * 
	 * @param copy
	 *            the LeaderAO to copy.
	 */
	public LeaderAO(LeaderAO copy) {
		super(copy);
		this.threshold = copy.threshold;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator#clone()
	 */
	@Override
	public LeaderAO clone() {
		return new LeaderAO(this);
	}

	/**
	 * Sets the leader algorithms threshold.
	 * 
	 * @param threshold
	 *            the threshold.
	 */
	@Parameter(type=DoubleParameter.class)
	public void setThreshold(Double threshold) {

		this.threshold = threshold;
	}

}
