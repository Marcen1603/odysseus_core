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

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.mining.AttributeOutOfRangeException;

/**
 * This class represents the logical operator for the simple single pass k-means
 * algorithm.
 * 
 * @author Kolja Blohm
 * 
 */
@LogicalOperator(name="SIMPLESINGLEPASSKMEANS", minInputPorts=1, maxInputPorts=1)
public class SimpleSinglePassKMeansAO extends AbstractClusteringAO {

	private static final String CLUSTERCOUNT = "CLUSTERCOUNT";
	private static final long serialVersionUID = 288269790951499746L;
	private int clusterCount;
	private int bufferSize;

	/**
	 * Returns how many clusters the algorithm should find.
	 * 
	 * @return the number of clusters.
	 */
	public int getClusterCount() {
		return clusterCount;
	}

	/**
	 * Copy constructor.
	 * 
	 * @param copy
	 *            the SimpleSinglePassKMeansAO to copy.
	 */
	protected SimpleSinglePassKMeansAO(SimpleSinglePassKMeansAO copy) {
		super(copy);
		this.clusterCount = copy.getClusterCount();
		this.bufferSize = copy.bufferSize;
	}

	/**
	 * Creates a new SimpleSinglePassKMeansAO.
	 */
	public SimpleSinglePassKMeansAO() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator#clone()
	 */
	@Override
	public AbstractLogicalOperator clone() {

		return new SimpleSinglePassKMeansAO(this);
	}

	/**
	 * Sets the number of clusters the algorithm has to find.
	 * 
	 * @param clusterCount
	 *            the number of clusters.
	 */
	@Parameter(type=IntegerParameter.class)
	public void setClusterCount(int clusterCount) {
		this.clusterCount = clusterCount;
	}

	/**
	 * Sets the size of the buffer. The size is measured in the number of data
	 * points the buffer can contain.
	 * 
	 * @param bufferSize
	 *            the buffer size.
	 */
	@Parameter(type=IntegerParameter.class)
	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	/**
	 * Returns the number of data points the buffer can contain.
	 * 
	 * @return the buffer size.
	 */
	public int getBufferSize() {
		return bufferSize;
	}

	@Override
	public boolean isValid() {
		boolean isValid = super.isValid();
		if (clusterCount <= 0) {
			addError(new AttributeOutOfRangeException(CLUSTERCOUNT,
					"has to be greater than zero"));
			isValid = false;
		} else if (bufferSize < clusterCount) {
			addError(new AttributeOutOfRangeException("BUFFERSIZE",
					"has to be equal or greater than the value of "
							+ CLUSTERCOUNT));
			isValid = false;
		}
		return isValid;
	}
}
