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
package de.uniol.inf.is.odysseus.mining.classification.model;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;

/**
 * This class is an abstract class used to calculate a quality of the
 * partitioning by subtracting the quality of an partitioning from the quality
 * of the unpartitioned node
 * 
 * @author Sven Vorlauf
 * 
 * @param <T>
 *            the type of the IMetaAttribute
 */
public abstract class AbstractGainMeasure<T extends IMetaAttribute> extends
		AbstractAttributeEvaluationMeasure<T> {

	/**
	 * create a new gain measure with a given tie and a given probability
	 * 
	 * @param probability
	 *            the probability that the chosen attribute is not the best one
	 * @param tie
	 *            the bound to split the node when the difference between the
	 *            best two measures is lower than this bound
	 */
	public AbstractGainMeasure(Double probability, Double tie) {
		super(probability, tie);
	}

	/**get the quality of the partitioning created by splitting on a specific attribute
	 * @param dataLayer the distribution of the classes refering to an specific attribute
	 * @param tupleCount the number of tuples used to create the distribution
	 * @return the quality of the partitioning
	 */
	protected Double getAttributeQuality(
			HashMap<Object, HashMap<Object, Integer>> dataLayer,
			Integer tupleCount) {
		Double quality = 0D;
		
		// calculate the weighted average of the partition qualities
		for (Map<Object, Integer> dataVector : dataLayer.values()) {
			quality += ((double) getTupleCount(dataVector)) / tupleCount
					* getPartitionQuality(dataVector);
		}
		return quality;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.datamining.classification.
	 * AbstractAttributeEvaluationMeasure
	 * #getEvaluationMeasure(de.uniol.inf.is.odysseus
	 * .datamining.classification.DataCube, int)
	 */
	@Override
	public Double getEvaluationMeasure(DataCube<T> statistics, int attributeId) {
		// return the difference between the partitioning quality and the
		// quality of the unpartitioned node
		return getPartitionQuality(statistics.getClassCountVector())
				- getAttributeQuality(
						statistics.getClassCountLayer(attributeId),
						getTupleCount(statistics.getClassCountVector()));
	}

	/**
	 * get the quality of a partition
	 * @param vector the class distribution in the partition
	 * @return the partition quality
	 */
	protected abstract Double getPartitionQuality(Map<Object, Integer> vector);

}
