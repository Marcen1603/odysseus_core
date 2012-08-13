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
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.DoubleParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.mining.MiningDatatypes;

/**
 * @author Dennis Geesen
 *
 */
@LogicalOperator(name = "GENERATERULES", minInputPorts = 1, maxInputPorts = 1)
public class RuleGenerationAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 4443355945512399432L;
	private int itemsetposition;
	private double confidence;
	
	public RuleGenerationAO() {
		
	}

	public RuleGenerationAO(RuleGenerationAO old) {
		this.itemsetposition = old.itemsetposition;
		this.confidence = old.confidence;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator#clone()
	 */
	@Override
	public AbstractLogicalOperator clone() {
		return new RuleGenerationAO(this);
	}
	
	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "ITEMSET")
	public void setItemsetAttribute(SDFAttribute itemset) {
		this.itemsetposition = this.getInputSchema(0).indexOf(itemset);
	}
	
	
	protected SDFSchema getOutputSchemaIntern(int pos) {
		if (pos == 0) {
			List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
			SDFAttribute attributeId = new SDFAttribute(null, "id", SDFDatatype.INTEGER);
			attributes.add(attributeId);
			SDFAttribute attributeSet = new SDFAttribute(null, "rule", MiningDatatypes.ASSOCIATION_RULE);
			attributes.add(attributeSet);
			SDFSchema outSchema = new SDFSchema(getInputSchema(0).getURI(), attributes);
			return outSchema;
		} else {
			List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
			SDFAttribute attributeCount = new SDFAttribute(null, "count", SDFDatatype.INTEGER);
			attributes.add(attributeCount);
			SDFAttribute attributeNeeded = new SDFAttribute(null, "needed", SDFDatatype.INTEGER);
			attributes.add(attributeNeeded);
			SDFAttribute attributeTotal = new SDFAttribute(null, "total", SDFDatatype.INTEGER);
			attributes.add(attributeTotal);
			SDFSchema outSchema = new SDFSchema(getInputSchema(0).getURI(), attributes);
			return outSchema;
		}
	}

	/**
	 * @return the itemset position
	 */
	public int getItemsetPosition() {
		return itemsetposition;
	}

	/**
	 * @return the confidence
	 */
	public double getConfidence() {
		return confidence;
	}

	/**
	 * @param confidence the confidence to set
	 */
	@Parameter(type=DoubleParameter.class, name="confidence")
	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}
	


}
