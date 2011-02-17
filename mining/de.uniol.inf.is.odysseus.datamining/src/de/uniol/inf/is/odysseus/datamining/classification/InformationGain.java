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
package de.uniol.inf.is.odysseus.datamining.classification;

import java.util.Map;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;

/**
 * This class represents a evaluation measure using the information gain
 * 
 * @author Sven Vorlauf
 * 
 * @param <T>
 *            the type of the IMetaAttribute
 */
public class InformationGain<T extends IMetaAttribute> extends
		AbstractGainMeasure<T> {

	/**
	 * create a new information gain with a given tie and a given probability
	 * 
	 * @param probability
	 *            the probability that the chosen attribute is not the best one
	 * @param tie
	 *            the bound to split the node when the difference between the
	 *            best two measures is lower than this bound
	 */
	public InformationGain(Double probability, Double  tie) {
		super(probability, tie);
	}

	@Override
	protected Double getPartitionQuality(Map<Object, Integer> vector) {
		Double quality = 0D;
		Integer tupleCount = getTupleCount(vector);
		for (Integer classCount : vector.values()) {
			Double probability = ((double) classCount) / tupleCount;
			quality -= probability * Math.log(probability) / Math.log(2);
		}
		return quality;
	}

}
