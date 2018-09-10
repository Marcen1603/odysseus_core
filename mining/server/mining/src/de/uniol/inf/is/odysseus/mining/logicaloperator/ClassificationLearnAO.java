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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OptionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.mining.MiningAlgorithmRegistry;
import de.uniol.inf.is.odysseus.mining.MiningDatatypes;

/**
 * @author Dennis Geesen
 * 
 */
@LogicalOperator(name = "CLASSIFICATION_LEARN", minInputPorts = 1, maxInputPorts = 1, doc="This operator is used to create a classifier. Therefore, the result is a stream of classifiers (this is an own datatype!)",category = {LogicalOperatorCategory.MINING, LogicalOperatorCategory.CLASSIFIKATION})
public class ClassificationLearnAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 1231999597473176237L;

	private SDFAttribute classAttribute;

	private List<Option> options = new ArrayList<>();
	private String learner;
	private Map<String, List<String>> nominals = new HashMap<>();

	private String algorithm;

	public ClassificationLearnAO() {

	}

	public ClassificationLearnAO(ClassificationLearnAO old) {
		super(old);
		this.classAttribute = old.classAttribute;
		this.learner = old.learner;
		this.options = new ArrayList<Option>(old.options);
		this.nominals = old.nominals;
		this.algorithm = old.algorithm;
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {

		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		SDFAttribute support = new SDFAttribute(null, "classifier", MiningDatatypes.CLASSIFIER, null, null, null);
		attributes.add(support);
		SDFSchema outSchema = SDFSchemaFactory.createNewWithAttributes(attributes, getInputSchema(0));
		return outSchema;

	}

	@Override
	public AbstractLogicalOperator clone() {
		return new ClassificationLearnAO(this);
	}

	public SDFAttribute getClassAttribute() {
		return classAttribute;
	}

	@Parameter(name = "class", type = ResolvedSDFAttributeParameter.class)
	public void setClassAttribute(SDFAttribute classAttribute) {
		this.classAttribute = classAttribute;
	}
	

	@Parameter(name = "options", type = OptionParameter.class, optional = true, isList = true)
	public void setOptions(List<Option> options) {
		this.options = options;
	}

	@Parameter(name = "nominals", type = StringParameter.class, isList = true, optional = true, isMap = true)
	public void setNominals(Map<String, List<String>> nominals) {
		this.nominals = nominals;
	}

	public Map<SDFAttribute, List<String>> getNominals() {
		DirectAttributeResolver dar = new DirectAttributeResolver(this.getInputSchema(0));
		Map<SDFAttribute, List<String>> values = new HashMap<SDFAttribute, List<String>>();
		for (Entry<String, List<String>> e : this.nominals.entrySet()) {
			SDFAttribute a = dar.getAttribute(e.getKey());
			values.put(a, e.getValue());
		}
		return values;
	}

	public List<Option> getOptions() {
		return this.options;
	}

	public Map<String, String> getOptionsMap() {
		Map<String, String> optionsMap = new HashMap<>();
		for(Option o : this.options){
			optionsMap.put(o.getName(), o.getValue());
		}
		return optionsMap;
	}
	
	public String getLearner() {
		return learner;
	}

	@Parameter(name = "algorithm", type = StringParameter.class, possibleValues = "getAlgorithmValues")
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}
	
	@Parameter(name = "learner", type = StringParameter.class, possibleValues = "getLearnerValues")
	public void setLearner(String learner) {
		this.learner = learner;
	}
	
	public List<String> getLearnerValues(){
		return MiningAlgorithmRegistry.getInstance().getClassificationLearnerNames();		
	}
	
	public List<String> getAlgorithmValues(){
		return MiningAlgorithmRegistry.getInstance().getClassificationLearnerAlgorithms();
	}

	public String getAlgorithm() {
		return algorithm;
	}	

}
