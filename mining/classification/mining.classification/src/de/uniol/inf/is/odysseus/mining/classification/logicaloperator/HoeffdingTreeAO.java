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
package de.uniol.inf.is.odysseus.mining.classification.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.DoubleParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

/**
 * This class represents a logical hoeffding tree operator used to learn a
 * hoeffding tree from a data stream
 * 
 * @author Sven Vorlauf
 * 
 */
@LogicalOperator(name = "HOEFFDINGTREE", minInputPorts = 1, maxInputPorts = 1)
public class HoeffdingTreeAO extends AbstractClassificationLearnerAO {

	/**
	 * the UID to identify this class
	 */
	private static final long serialVersionUID = 2997087529890096578L;

	/**
	 * the probability that a chosen attribut is not the best split attribute
	 */
	private Double probability;

	/**
	 * the name of the evaluation measure to use
	 */
	private String attributeEvaluationMeasure;

	/**
	 * create a new logical hoeffding tree operator as a copy of another
	 * hoeffding tree operator
	 * 
	 * @param hoeffdingTreeAO
	 *            the operator to copy
	 */
	public HoeffdingTreeAO(HoeffdingTreeAO hoeffdingTreeAO) {
		super(hoeffdingTreeAO);
		probability = hoeffdingTreeAO.getProbability();
	}

	/**
	 * create a new hoeffding tree operator
	 */
	public HoeffdingTreeAO() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator#getOutputSchema
	 * ()
	 */
	@Override
	public SDFSchema getOutputSchema() {

		// define the schema as one attribute for the classifier
		SDFSchema outputSchema = new SDFSchema("Classifier", new SDFAttribute(null,"classifier", SDFDatatype.OBJECT));
		return outputSchema;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator#clone()
	 */
	@Override
	public AbstractLogicalOperator clone() {
		return new HoeffdingTreeAO(this);
	}

	/**
	 * set the attribute holding the class
	 * 
	 * @param labelAttribute
	 *            the attribute to set
	 */
	@Parameter(type=ResolvedSDFAttributeParameter.class)
	public void setLabelAttribute(SDFAttribute labelAttribute) {
		this.labelAttribute = labelAttribute;

	}

	/**
	 * get the probability that a chosen attribut is not the best split
	 * attribute
	 * 
	 * @return the probability
	 */
	public Double getProbability() {
		return probability;
	}

	/**
	 * set the probability that a chosen attribut is not the best split
	 * attribute
	 * 
	 * @param probability
	 *            the probability to set
	 */
	@Parameter(type = DoubleParameter.class)
	public void setProbability(Double probability) {
		this.probability = probability;
	}

	/**
	 * te the name of teh evaluation measure to use
	 * 
	 * @param attributeEvaluationMeasure
	 *            the name of the measure
	 */
	@Parameter(name="EVALUATIONMEASURE", type=StringParameter.class, optional = true)
	public void setAttributeEvaluationMeasure(String attributeEvaluationMeasure) {
		this.attributeEvaluationMeasure = attributeEvaluationMeasure;

	}

	/**
	 * get the name of the evaluation measure to use
	 * 
	 * @return the name to set
	 */
	public String getAttributeEvaluationMeasure() {
		return attributeEvaluationMeasure;
	}

	@Override
	public boolean isValid() {
		boolean isValid = super.isValid();
		if (probability < 0 || probability > 1) {
//			addError(new AttributeOutOfRangeException("PROBABILITY",
//					"has to be between 0 and 1"));
			isValid = false;
		}

		return isValid;
	}

}
