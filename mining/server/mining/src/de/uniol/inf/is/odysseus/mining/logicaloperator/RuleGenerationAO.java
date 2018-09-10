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

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
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
@LogicalOperator(name = "GENERATERULES", minInputPorts = 1, maxInputPorts = 1, doc = "This operator uses a list of tuples and creates rules like \"x => y\". A rule is a special datatype called \"AssociationRule\", which is principally a tuple of two patterns (one for the premise and one for the consequnce of the rule)", category = {
		LogicalOperatorCategory.MINING })
public class RuleGenerationAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 4443355945512399432L;
	private int itemsetposition;
	private int supportposition;
	private double confidence;

	public RuleGenerationAO() {

	}

	public RuleGenerationAO(RuleGenerationAO old) {
		this.itemsetposition = old.itemsetposition;
		this.confidence = old.confidence;
		this.supportposition = old.supportposition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.logicaloperator.
	 * AbstractLogicalOperator#clone()
	 */
	@Override
	public AbstractLogicalOperator clone() {
		return new RuleGenerationAO(this);
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "ITEMSET")
	public void setItemsetAttribute(SDFAttribute itemset) {
		this.itemsetposition = this.getInputSchema(0).indexOf(itemset);
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "SUPPORT")
	public void setSupprtAttribute(SDFAttribute support) {
		this.supportposition = this.getInputSchema(0).indexOf(support);
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		if (pos == 0) {
			List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
			SDFSchema innerSchema = this.getInputSchema(0).get(itemsetposition).getDatatype().getSchema();
			SDFAttribute attributeSet = new SDFAttribute(null, "rule",
					SDFDatatype.createTypeWithSubSchema(MiningDatatypes.ASSOCIATION_RULE, innerSchema));
			attributes.add(attributeSet);
			SDFSchema outSchema = SDFSchemaFactory.createNewWithAttributes(attributes, getInputSchema(0));
			return outSchema;
		} else {
			List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
			SDFAttribute attributeCount = new SDFAttribute(null, "count", SDFDatatype.INTEGER, null, null, null);
			attributes.add(attributeCount);
			SDFAttribute attributeNeeded = new SDFAttribute(null, "needed", SDFDatatype.INTEGER, null, null, null);
			attributes.add(attributeNeeded);
			SDFAttribute attributeTotal = new SDFAttribute(null, "total", SDFDatatype.INTEGER, null, null, null);
			attributes.add(attributeTotal);
			SDFSchema outSchema = SDFSchemaFactory.createNewWithAttributes(attributes, getInputSchema(0));
			return outSchema;
		}
	}

	/**
	 * @return the itemset position
	 */
	public int getItemsetPosition() {
		return itemsetposition;
	}

	public int getSupportPosition() {
		return supportposition;
	}

	/**
	 * @return the confidence
	 */
	public double getConfidence() {
		return confidence;
	}

	/**
	 * @param confidence
	 *            the confidence to set
	 */
	@Parameter(type = DoubleParameter.class, name = "confidence")
	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}

}
