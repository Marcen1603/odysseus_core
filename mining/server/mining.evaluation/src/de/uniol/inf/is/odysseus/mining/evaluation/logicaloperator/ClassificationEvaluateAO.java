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
package de.uniol.inf.is.odysseus.mining.evaluation.logicaloperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.DoubleParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * @author Stephan Wessels
 *
 */
@LogicalOperator(name = "CLASSIFICATION_EVALUATE", minInputPorts = 2, maxInputPorts = 2, doc="This operator evaluates a classifier incrementally. Needs a stream of classifiers and a stream of tuples from which the classifier is being learned.",category={LogicalOperatorCategory.MINING})
public class ClassificationEvaluateAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 5003642239636323488L;
	private SDFAttribute classAttribute;
	private SDFAttribute classifierAttribute;	
	private List<String> metrics = new ArrayList<>();
	private Map<String, List<String>> nominals = new HashMap<>();
	private double fadingFactor = 1.0;
	
	public ClassificationEvaluateAO() {
	}

	public ClassificationEvaluateAO(ClassificationEvaluateAO classificationEvaluateAO) {
		super(classificationEvaluateAO);
		this.classAttribute = classificationEvaluateAO.classAttribute;
		this.classifierAttribute = classificationEvaluateAO.classifierAttribute;
		this.metrics = new ArrayList<String>(classificationEvaluateAO.metrics);
		this.nominals = classificationEvaluateAO.nominals;
		this.fadingFactor = classificationEvaluateAO.fadingFactor;
	}

	@Override
	public ClassificationEvaluateAO clone() {
		return new ClassificationEvaluateAO(this);
	}
	
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {

		List<SDFAttribute> attributes = new ArrayList<>();
//		for(SDFAttribute oldAttribute : this.getInputSchema(0).getAttributes()){
//			attributes.add(new SDFAttribute(null, oldAttribute.getAttributeName(), oldAttribute.getDatatype(), oldAttribute.getUnit(), oldAttribute.getDtConstraints()));
//		}
		for (String metric : this.getMetrics()) {
			SDFAttribute metricAttribute = new SDFAttribute(null, metric, SDFDatatype.DOUBLE, null, null, null);
			attributes.add(metricAttribute);
		}
		
//		SDFSchema outSchema = new SDFSchema(getInputSchema(0), attributes);
		SDFSchema outSchema = SDFSchemaFactory.createNewAddAttributes(attributes, getInputSchema(0));
		return outSchema;

	}
	
	@Parameter(name = "class", type = ResolvedSDFAttributeParameter.class, doc="The class attribute.", optional = false)
	public void setClassAttribute(SDFAttribute classAttribute) {
		this.classAttribute = classAttribute;
	}
	
	@Parameter(name="classifier", type=ResolvedSDFAttributeParameter.class, doc="The attribute with the classifier.", optional = false)
	public void setClassifier(SDFAttribute classifierAttribute) {
		this.classifierAttribute = classifierAttribute;
	}
	
	@Parameter(name = "metrics", type = StringParameter.class, doc="A list of evaluation metrics to be computed.", isList = true, optional = false)
	public void setMetrics(List<String> metrics) {
		this.metrics = metrics;
	}
	
	@Parameter(name = "nominals", type = StringParameter.class, doc="The class-attribute nominals. Required if class is discrete.", isList = true, optional = true, isMap = true)
	public void setNominals(Map<String, List<String>> nominals) {
		this.nominals = nominals;
	}
	
	@Parameter(name = "fading", type = DoubleParameter.class, doc="The factor by how much old data should fade. Use values 1.0 for no fading (default) to 0.0 for maximal fading.", optional = true)
	public void setFading(Double fadingFactor) {
		this.fadingFactor = fadingFactor;
	}
	
	public SDFAttribute getClassAttribute() {
		return classAttribute;
	}
	
	public SDFAttribute getClassifier() {
		return classifierAttribute;
	}
	
	public List<String> getMetrics() {
		return this.metrics;
	}
	
	public Double getFading() {
		return this.fadingFactor;
	}
	
	public Map<SDFAttribute, List<String>> getNominals() {
		DirectAttributeResolver dar = new DirectAttributeResolver(this.getInputSchema(1));
		Map<SDFAttribute, List<String>> values = new HashMap<SDFAttribute, List<String>>();
		for (Entry<String, List<String>> e : this.nominals.entrySet()) {
			SDFAttribute a = dar.getAttribute(e.getKey());
			values.put(a, e.getValue());
		}
		return values;
	}
}
