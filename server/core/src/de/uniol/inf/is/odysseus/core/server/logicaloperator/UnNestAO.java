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
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "UNNEST", doc = "The UnNest operator unpacks incoming tuple with a multi value attribute to create multiple tuples", category = { LogicalOperatorCategory.TRANSFORM })
public class UnNestAO extends UnaryLogicalOp {

	/**
     * 
     */
	private static final long serialVersionUID = -5918972476973244744L;
	private static Logger LOG = LoggerFactory.getLogger(UnNestAO.class);
	private SDFAttribute attribute;

	/**
     * 
     */
	public UnNestAO() {
		super();
	}

	/**
	 * @param ao
	 */
	public UnNestAO(final UnNestAO ao) {
		super(ao);
		this.attribute = ao.getAttribute();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator
	 * #clone()
	 */
	@Override
	public AbstractLogicalOperator clone() {
		return new UnNestAO(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.logicaloperator.ILogicalOperator
	 * #getOutputSchema()
	 */
	@Override
	public SDFSchema getOutputSchemaIntern(int pos) {
		List<SDFAttribute> attrs = new ArrayList<SDFAttribute>();
		for (int i = 0; i < getInputSchema().size(); i++) {
			SDFAttribute attribute = getInputSchema().getAttribute(i);

			if (attribute.equals(this.attribute)
					&& ((this.attribute.getDatatype().isComplex() || this.attribute
							.getDatatype().isListValue()))) {
				SDFDatatype attrDataType = attribute.getDatatype();
				if (attrDataType.isMultiValue()
						|| attrDataType.isListValue()) {
					if(attrDataType.hasSchema()) {
						attrs.add(new SDFAttribute(attribute.getSourceName(),
								attribute.getAttributeName(), SDFDatatype.createTypeWithSubSchema(attrDataType.getSubType(), attrDataType.getSchema()), null, null,
								null));
					} else {
						attrs.add(new SDFAttribute(attribute.getSourceName(),
								attribute.getAttributeName(), attrDataType.getSubType(), null, null,
								null));
					}
				} else {
					SDFSchema subschema = attribute.getDatatype().getSchema();
					for (int j = 0; j < subschema.size(); j++) {
						attrs.add(subschema.get(j));
					}
				}

			} else {
				attrs.add(getInputSchema().get(i));
			}
		}
		recalcOutputSchemata = false;
		setOutputSchema(SDFSchemaFactory.createNewWithAttributes("UNNEST",
				attrs, getInputSchema()));
		LOG.debug("Set output schema to: {}", getOutputSchema());
		return getOutputSchema();
	}

	/**
	 * @param attribute
	 *            The attribute for unnest
	 */
	@Parameter(name = "ATTRIBUTE", type = ResolvedSDFAttributeParameter.class)
	public void setAttribute(final SDFAttribute attribute) {
		UnNestAO.LOG.debug("Set UnNest attribute to {}",
				attribute.getAttributeName());
		this.attribute = attribute;
	}

	@Parameter(name = "RECALCULATE", type = BooleanParameter.class, optional = true, deprecated = true)
	public void setRecalculate(final boolean recalculate) {
	}

	public boolean isRecalculate() {
		return true;
	}

	/**
	 * @return The attribute for unnest
	 */
	public SDFAttribute getAttribute() {
		return this.attribute;
	}

	public int getAttributePosition() {
		return this.getInputSchema().indexOf(getAttribute());
	}

	public boolean isMultiValue() {
		return this.attribute.getDatatype().isMultiValue();
	}

	public boolean isListValue() {
		return this.attribute.getDatatype().isListValue();
	}
}
