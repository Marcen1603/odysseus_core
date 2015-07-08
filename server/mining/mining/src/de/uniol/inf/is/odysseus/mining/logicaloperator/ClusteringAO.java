/** Copyright 2012 The Odysseus Team
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

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
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

/**
 * 
 * @author Dennis Geesen Created at: 14.05.2012
 */
@LogicalOperator(name = "CLUSTERING", minInputPorts = 1, maxInputPorts = 1, doc="This operator clusters a set of tuples.", category={LogicalOperatorCategory.MINING, LogicalOperatorCategory.CLUSTERING})
public class ClusteringAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = -4053248940214364499L;

	private List<Option> options = new ArrayList<>();	

	private List<SDFAttribute> attributes;

	private String learner;
	private String algorithm;

	public ClusteringAO() {
	}

	public ClusteringAO(ClusteringAO clusteringAO) {		
		this.options = new ArrayList<Option>(clusteringAO.options);
		this.learner = clusteringAO.learner;
		this.attributes = clusteringAO.attributes;
		this.algorithm = clusteringAO.algorithm;
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "ATTRIBUTES", isList = true)
	public void setAttributes(List<SDFAttribute> readingSchema) {
		this.attributes = readingSchema;
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
		return MiningAlgorithmRegistry.getInstance().getClustererNames();		
	}
	
	public List<String> getAlgorithmValues(){
		return MiningAlgorithmRegistry.getInstance().getClustererAlgorithms();
	}	

	@Parameter(name = "options", type = OptionParameter.class, optional = true, isList = true)
	public void setOptions(List<Option> options) {		
			this.options = options;	
	}
	
	public List<Option> getOptions(){
		return this.options;
	}
	
	public Map<String, String> getOptionsMap() {
		Map<String, String> optionsMap = new HashMap<>();
		for(Option o : this.options){
			optionsMap.put(o.getName(), o.getValue());
		}
		return optionsMap;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new ClusteringAO(this);
	}

	@Override
	public SDFSchema getOutputSchemaIntern(int pos) {
		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>(getInputSchema(0).getAttributes());		
		SDFAttribute attributeId = new SDFAttribute(null, "clusterid", SDFDatatype.INTEGER, null, null, null);
		attributes.add(attributeId);		
		SDFSchema outSchema = SDFSchemaFactory.createNewWithAttributes(attributes, getInputSchema(0));
		return outSchema;
	}

	public List<SDFAttribute> getAttributes() {
		return this.attributes;
	}

	public int[] getAttributePositions() {
		int[] ret = new int[attributes.size()];
		int i = 0;
		for (SDFAttribute a : attributes) {
			int j = 0;
			int k = i;
			for (SDFAttribute b : getInputSchema(0)) {
				if (b.equals(a)) {
					ret[i++] = j;
				}
				++j;
			}
			if (k == i) {
				throw new IllegalArgumentException("no such attribute: " + a);
			}
		}
		return ret;
	}

	public String getLearner() {
		return learner;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	

	
	
}
