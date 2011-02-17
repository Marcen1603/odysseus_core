/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.datamining.clustering.builder;

import de.uniol.inf.is.odysseus.datamining.builder.AttributeOutOfRangeException;
import de.uniol.inf.is.odysseus.datamining.clustering.logicaloperator.SimpleSinglePassKMeansAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * 
 * This class is the builder class for the {@link SimpleSinglePassKMeansAO}. It
 * specifies two parameters. One for the number of clusters the simple single
 * pass k-means should find and one that specifies the buffer size for the
 * algorithm.
 * 
 * @author Kolja Blohm
 * 
 */
public class SimpleSinglePassKMeansAOBuilder extends
		AbstractClusteringAOBuilder {

	private static final long serialVersionUID = 6320176622859137201L;
	private static final String CLUSTERCOUNT = "CLUSTERCOUNT";
	private IntegerParameter clusterCount;

	private static final String BUFFERSIZE = "BUFFERSIZE";
	private IntegerParameter bufferSize;

	/**
	 * Creates a new SimpleSinglePassKMeansAOBuilder
	 */
	public SimpleSinglePassKMeansAOBuilder() {
		clusterCount = new IntegerParameter(CLUSTERCOUNT, REQUIREMENT.MANDATORY);
		bufferSize = new IntegerParameter(BUFFERSIZE, REQUIREMENT.MANDATORY);
		setParameters(bufferSize);
		setParameters(clusterCount);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.builder.AbstractOperatorBuilder
	 * #createOperatorInternal()
	 */
	@Override
	protected ILogicalOperator createOperatorInternal() {

		// initialize the SimpleSinglePassKMeansAO
		SimpleSinglePassKMeansAO simpleSinglePassKMeansAO = new SimpleSinglePassKMeansAO();
		simpleSinglePassKMeansAO.setAttributes(new SDFAttributeList(attributes
				.getValue()));
		simpleSinglePassKMeansAO.setClusterCount(clusterCount.getValue());
		simpleSinglePassKMeansAO.setBufferSize(bufferSize.getValue());
		return simpleSinglePassKMeansAO;
	}

	/**
	 * Validates that the number of clusters and the buffer size are both
	 * greater than zero. In addition to that, the buffer size has to be greater or
	 * equal to the number of clusters to be able to select enough random data
	 * points from the buffer later on.
	 * 
	 * @see de.uniol.inf.is.odysseus.datamining.clustering.builder.AbstractClusteringAOBuilder#internalValidation()
	 */
	@Override
	protected boolean internalValidation() {
		if (clusterCount.getValue() <= 0) {
			addError(new AttributeOutOfRangeException(clusterCount.getName(),
					"has to be greater than zero"));
			return false;
		} else if (bufferSize.getValue() < clusterCount.getValue()) {
			addError(new AttributeOutOfRangeException(bufferSize.getName(),
					"has to be equal or greater than the value of "
							+ clusterCount.getName()));
			return false;
		}
		return super.internalValidation();
	}

}
