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
package de.uniol.inf.is.odysseus.datamining.classification.builder;

import de.uniol.inf.is.odysseus.datamining.builder.AttributeOutOfRangeException;
import de.uniol.inf.is.odysseus.datamining.classification.logicaloperator.HoeffdingTreeAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.builder.DirectParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * This class is the builder class to define parameters used to create the
 * logical classification operator
 * 
 * @author Sven Vorlauf
 * 
 */
public class HoeffdingTreeAOBuilder extends
		AbstractClassificationLearnerAOBuilder {

	/**
	 * the UID to identify this class
	 */
	private static final long serialVersionUID = -7070015888103484706L;

	/**
	 * the name of the parameter defining the probibility not to find the best
	 * split attribute
	 */
	private static final String PROBABILITY = "PROBABILITY";

	/**
	 * the parameter defining the probibility not to find the best split
	 * attribute
	 */
	private DirectParameter<Double> probability;

	/**
	 * create a new builder
	 */
	public HoeffdingTreeAOBuilder() {

		probability = new DirectParameter<Double>(PROBABILITY,
				REQUIREMENT.MANDATORY);

		setParameters(probability);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.datamining.classification.builder.
	 * AbstractClassificationLearnerAOBuilder#internalValidation()
	 */
	@Override
	protected boolean internalValidation() {
		if (probability.getValue() < 0 || probability.getValue() > 1) {
			addError(new AttributeOutOfRangeException(probability.getName(),
					"has to be between 0 and 1"));
			return false;
		}
		return super.internalValidation();
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

		// create and configure the logical operator
		HoeffdingTreeAO hoeffdingTreeAO = new HoeffdingTreeAO();
		hoeffdingTreeAO.setAttributes(new SDFAttributeList(attributes
				.getValue()));
		hoeffdingTreeAO.setLabelAttribute(labelAttribute.getValue());
		hoeffdingTreeAO.setProbability(probability.getValue());
		hoeffdingTreeAO.setAttributeEvaluationMeasure(evaluationMeasure
				.getValue());
		return hoeffdingTreeAO;
	}

}
