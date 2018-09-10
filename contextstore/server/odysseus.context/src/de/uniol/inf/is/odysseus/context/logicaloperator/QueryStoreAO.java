/*******************************************************************************
 * Copyright 2015 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.context.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.context.store.ContextStoreManager;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;


@LogicalOperator(name = "QUERYSTORE", minInputPorts = 1, maxInputPorts = 1, doc="This operator queries a store regarding to the incoming tuple and publishes pairs of matchings tuples", category={LogicalOperatorCategory.ENRICH})
public class QueryStoreAO extends AbstractLogicalOperator {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6201087936567805681L;
	private String storeName;
	private List<Integer> keysIndices;
	private boolean outer = false;
	private List<String> attributes;

	public QueryStoreAO(QueryStoreAO op) {
		super(op);
		this.storeName = op.storeName;
		this.keysIndices = op.keysIndices;
		this.outer = op.outer;
		this.attributes = op.attributes;
	}

	public QueryStoreAO() {
		super();
	}
	
	@Parameter(type = StringParameter.class, name = "ATTRIBUTES", isList = true, optional = true)
	public void setAttributes(List<String> attributes) {
		this.attributes = attributes;
	}

	@Parameter(type = IntegerParameter.class, name = "KEYS", isList = true)
	public void setKeysIndices(List<Integer> keysIndices) {
		this.keysIndices = keysIndices;
	}

	@Parameter(name = "STORE", type = StringParameter.class)
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	@Parameter(name = "OUTER", type = BooleanParameter.class, optional = true)
	public void setOuterEnrich(Boolean outer) {
		this.outer = outer;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new QueryStoreAO(this);
	}
	
	@Override
	public SDFSchema getOutputSchemaIntern(int pos) {
		if (getOutputSchema() == null) {
			calcOutputSchema();
		}
		return getOutputSchema();
	}
	
	@Override
	public void initialize() {
		super.initialize();
		if (this.attributes == null) {
			this.attributes = new ArrayList<String>();
			for (SDFAttribute a : ContextStoreManager.getStore(storeName).getSchema()) {
				this.attributes.add(a.getAttributeName());
			}
		}
		calcOutputSchema();
	}

	public String getStoreName() {
		return this.storeName;
	}

	public boolean isOuter() {
		return this.outer;
	}
	
	public List<Integer> getKeyIndices(){
		return this.keysIndices;
	}
	
	public List<String> getAttributes() {
		return this.attributes;
	}
	
	private void calcOutputSchema() {
		if (getInputSchema(0) != null) {
			// Attributes of the source
			List<SDFAttribute> outattribs = new ArrayList<SDFAttribute>(getInputSchema(0).getAttributes());
			// Attributes of the context store
			for (String attributeName : this.attributes) {
				SDFSchema storeSchema = ContextStoreManager.getStore(storeName).getSchema();
				IAttributeResolver ar = new DirectAttributeResolver(storeSchema);
				SDFAttribute attribute = ar.getAttribute(attributeName);
				if (attribute == null) {
					throw new IllegalArgumentException("Attribute \"" + attributeName + "\" does not exist in store \"" + storeName + "\"");
				}
				outattribs.add(attribute);
			}
			setOutputSchema(SDFSchemaFactory.createNewWithAttributes(outattribs, getInputSchema(0)));
		}
	}

}
