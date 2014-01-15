/*******************************************************************************
 * Copyright 2012 The Odysseus Team
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
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.DoubleParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IllegalParameterException;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * This operator can reduce traffic. It lets an event pass if its different than
 * the last event, if specified, numeric values can have a tolerance band (relative or absolute defined), 
 * e.i. only if the new values lies outside this band, it is send (aka known as deadband or histerese band)
 * 
 * @author Marco Grawunder
 * 
 */
@LogicalOperator(name = "CHANGEDETECT", minInputPorts = 1, maxInputPorts = 1, doc="This operator can reduce traffic. It lets an event pass if its different than the last event, if specified, numeric values can have a tolerance band (relative or absolute defined) e.i. only if the new values lies outside this band, it is send (aka known as deadband or histerese band)", category={LogicalOperatorCategory.PATTERN})
public class ChangeDetectAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -9042464546094886480L;
	private List<SDFAttribute> attributes;
	private double tolerance = 0;
	private boolean isRelativeTolerance = false;
	private int rate;
	private boolean deliverFirstElement = false;
	private List<SDFAttribute> groupingAttributes = new ArrayList<>();
	private SDFAttribute suppressCountAttribute;

	public ChangeDetectAO(ChangeDetectAO po) {
		super(po);
		attributes = po.attributes;
		this.rate = po.rate;
		this.deliverFirstElement = po.deliverFirstElement;
		this.groupingAttributes = new ArrayList<>(po.getGroupingAttributes());
		this.tolerance = po.tolerance;
		this.isRelativeTolerance = po.isRelativeTolerance;
		this.suppressCountAttribute = po.suppressCountAttribute;
	}

	public ChangeDetectAO() {
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "ATTR", isList = true)
	public void setAttr(List<SDFAttribute> outputSchema) {
		this.attributes = outputSchema;
	}
	
	public List<SDFAttribute> getAttr() {
		return this.attributes;
	}
		
	@Parameter(type = IntegerParameter.class, name = "heartbeatrate", optional = true)
	public void setHeartbeatRate(int rate){
		this.rate = rate;
	}

	public int getHeartbeatRate() {
		return rate;
	}

	public boolean isDeliverFirstElement() {
		return deliverFirstElement;
	}

	@Parameter(type = BooleanParameter.class, name = "deliverFirstElement", optional = true)
	public void setDeliverFirstElement(boolean deliverFirstElement) {
		this.deliverFirstElement = deliverFirstElement;
	}
	
	@Parameter(name = "GROUP_BY", optional = true, type = ResolvedSDFAttributeParameter.class, isList = true)
	public void setGroupingAttributes(List<SDFAttribute> attributes) {
		this.groupingAttributes = attributes;
	}
	
	public List<SDFAttribute> getGroupingAttributes() {
		return groupingAttributes;
	}

	
	@Parameter(type = DoubleParameter.class, name = "tolerance", optional = true)
	public void setTolerance(double tolerance) {
		this.tolerance = tolerance;
	}
	
	public double getTolerance() {
		return tolerance;
	}
	
	@Parameter(type = BooleanParameter.class, name = "relativeTolerance", optional = true)
	public void setRelativeTolerance(boolean isRelativeTolerance) {
		this.isRelativeTolerance = isRelativeTolerance;
	}
	
	@Parameter(type = StringParameter.class, name = "suppressCountAttribute", optional = true)
	public void setSuppressCountAttribute_(String name){
		this.suppressCountAttribute = new SDFAttribute(null,name,SDFDatatype.INTEGER);
	}
	
	public SDFAttribute getSuppressCountAttribute() {
		return suppressCountAttribute;
	}
	
	public boolean isRelativeTolerance() {
		return isRelativeTolerance;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new ChangeDetectAO(this);
	}

	public int[] getComparePositions() {
		SDFSchema inputSchema = getInputSchema();
		if (attributes.size() > 0) {
			int[] ret = new int[attributes.size()];
			int i = 0;
			for (SDFAttribute a : attributes) {
				ret[i++] = inputSchema.indexOf(a);
			}
			return ret;
		} else {
			return null;
		}
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		if (suppressCountAttribute == null){
			return super.getOutputSchemaIntern(pos);
		}
		SDFSchema inputSchema = getInputSchema();
		List<SDFAttribute> attributes = new ArrayList<>(inputSchema.getAttributes());
		attributes.add(suppressCountAttribute);
		SDFSchema outputSchema = new SDFSchema(inputSchema.getURI(), inputSchema.getType(), attributes);
		
		return outputSchema;
	}
	
	@Override
	public boolean isValid() {
		boolean isValid = true;
		if (hasAttributes()) {
			int[] comPos = getComparePositions();
			for (int c : comPos) {
				if (c == -1) {
					addError(new IllegalParameterException(
							"Not all attributes in input found!"));
					isValid = false;
				}
			}
		}
		return isValid && super.isValid();
	}

	public boolean hasAttributes() {
		return attributes != null && attributes.size() > 0;
	}

}
