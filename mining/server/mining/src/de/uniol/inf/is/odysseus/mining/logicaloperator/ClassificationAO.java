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
package de.uniol.inf.is.odysseus.mining.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * @author Dennis Geesen
 *
 */
@LogicalOperator(name = "CLASSIFY", minInputPorts = 2, maxInputPorts = 2, doc="This operator classifies a tuple by using a classifier. The operator needs two inputs: A stream of tuples that should be classified and a stream of classifiers (that normally comes from a CLASSIFICATION_LEARN operator). It a appends a new attribute called \"clazz\" which contains the nominal class value or continuous value from a regression For the classify operator, the type of the classifier (tree, list, bayes net... ) doesn't matter. You may even mixup them to classify the same tuple with different classifiers (see Ensembles). The left port is the input for the tuples that should be classified and the right input is the one with the classifiers.",category={LogicalOperatorCategory.MINING, LogicalOperatorCategory.CLASSIFIKATION})
public class ClassificationAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 1231999597473176237L;
	private SDFAttribute classifierAttribute;
	private boolean oneClassifier = false;
	private String className = "clazz";
	
	public ClassificationAO() {
		
	}

	public ClassificationAO(ClassificationAO classificationAO) {
		this.classifierAttribute = classificationAO.classifierAttribute;
		this.oneClassifier = classificationAO.oneClassifier;
		this.className = classificationAO.className;
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
//
//		List<SDFAttribute> attributes = new ArrayList<>();
//		for(SDFAttribute oldAttribute : this.getInputSchema(0).getAttributes()){
//			attributes.add(new SDFAttribute(null, oldAttribute.getAttributeName(), oldAttribute.getDatatype(), oldAttribute.getUnit(), oldAttribute.getDtConstraints()));
//		}
//		
		SDFAttribute attributeId = new SDFAttribute(null, className, SDFDatatype.DOUBLE, null, null, null);
//		attributes.add(attributeId);
		
//		SDFSchema outSchema = SDFSchemaFactory.createNewWithAttributes(attributes, getInputSchema(0));
		SDFSchema outSchema = SDFSchemaFactory.createNewAddAttribute(attributeId, getInputSchema(0));
		return outSchema;

	}

	@Override
	public ClassificationAO clone() {
		return new ClassificationAO(this);
	}

	public SDFAttribute getClassifier() {
		return classifierAttribute;
	}

	@Parameter(name="classifier", type=ResolvedSDFAttributeParameter.class, doc="The attribute with the classifier", optional = false)
	public void setClassifier(SDFAttribute classifierAttribute) {
		this.classifierAttribute = classifierAttribute;
	}

	public boolean isOneClassifier() {
		return oneClassifier;
	}

	@Parameter(name="oneClassifier", type=BooleanParameter.class, doc="Use only one classifier at once", optional = true)
	public void setOneClassifier(boolean oneClassifier) {
		this.oneClassifier = oneClassifier;
	}

	public String getClassName() {
		return className;
	}

	@Parameter(name="classname", type=StringParameter.class, doc="The name of the classification result", optional = true)
	public void setClassName(String className) {
		this.className = className;
	}
}
