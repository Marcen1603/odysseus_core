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
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * 
 * @author Dennis Geesen Created at: 14.05.2012
 */
@LogicalOperator(name = "CLUSTERING", minInputPorts = 1, maxInputPorts = 1)
public class ClusteringAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = -4053248940214364499L;

	private Map<String, List<String>> options;

	private String clustererName;

	private List<SDFAttribute> attributes;

	public ClusteringAO() {
	}

	public ClusteringAO(ClusteringAO clusteringAO) {		
		this.options = clusteringAO.options;
		this.clustererName = clusteringAO.clustererName;
		this.attributes = clusteringAO.attributes;
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "ATTRIBUTES", isList = true)
	public void setAttributes(List<SDFAttribute> readingSchema) {
		this.attributes = readingSchema;
	}

	@Parameter(name = "clusterer", type = StringParameter.class)
	public void setAlgorithmus(String clusterer) {
		this.clustererName = clusterer;
	}
	

	@Parameter(name = "options", type = StringParameter.class, isList = true, optional = true, isMap = true)
	public void setOptions(Map<String, List<String>> options) {
		this.options = options;
	}	

	public Map<String, List<String>> getOptions() {
		return this.options;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new ClusteringAO(this);
	}

	@Override
	public SDFSchema getOutputSchemaIntern(int pos) {
		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>(getInputSchema(0).getAttributes());
		SDFAttribute attributeId = new SDFAttribute(null, "clusterid", SDFDatatype.INTEGER);
		attributes.add(attributeId);		
		SDFSchema outSchema = new SDFSchema(getInputSchema(0).getURI(), attributes);
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

	public String getClustererName() {
		return clustererName;
	}

	public void setClustererName(String clustererName) {
		this.clustererName = clustererName;
	}

	
	
}
