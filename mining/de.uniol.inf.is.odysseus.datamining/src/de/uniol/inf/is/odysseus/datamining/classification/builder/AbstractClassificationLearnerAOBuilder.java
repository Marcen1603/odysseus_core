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

import de.uniol.inf.is.odysseus.logicaloperator.builder.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.logicaloperator.builder.DirectParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.ListParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

/**
 * This class is an abstract builder class to define parameters for logical
 * classifier learn operators used to create logical operators from PQL
 * 
 * @author Sven Vorlauf
 * 
 */
public abstract class AbstractClassificationLearnerAOBuilder extends
		AbstractOperatorBuilder {
	/**
	 * the UID to identify this class
	 */
	private static final long serialVersionUID = 816020377240844656L;

	/**
	 * the name of the parameter defining the attributes to use for classification
	 */
	private static final String ATTRIBUTES = "ATTRIBUTES";

	/**
	 * the parameter defining the attributes to use for classification
	 */
	protected ListParameter<SDFAttribute> attributes;

	/**
	 * the name of the parameter defining the attribute for the class
	 */
	private static final String LABEL_ATTRIBUTE = "LABELATTRIBUTE";
	
	/**
	 * the parameter defining the attribute for the class
	 */
	protected ResolvedSDFAttributeParameter labelAttribute;

	/**
	 * the name of the parameter defining the name of a evaluation measure
	 */
	private static final String EVALUATION_MEASURE = "EVALUATIONMEASURE";

	/**
	 * the parameter defining the name of a evaluation measure
	 */
	protected DirectParameter<String> evaluationMeasure;

	/**
	 * create the builder
	 */
	public AbstractClassificationLearnerAOBuilder() {

		super(1, 1);
		attributes = new ListParameter<SDFAttribute>(ATTRIBUTES,
				REQUIREMENT.MANDATORY, new ResolvedSDFAttributeParameter(
						"data mining attribute", REQUIREMENT.MANDATORY));
		labelAttribute = new ResolvedSDFAttributeParameter(LABEL_ATTRIBUTE,
				REQUIREMENT.MANDATORY);
		evaluationMeasure = new DirectParameter<String>(EVALUATION_MEASURE,
				REQUIREMENT.OPTIONAL);
		setParameters(attributes, labelAttribute, evaluationMeasure);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.builder.AbstractOperatorBuilder
	 * #internalValidation()
	 */
	@Override
	protected boolean internalValidation() {
		return true;
	}
}
