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

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.DoubleParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * This operator can reduce traffic. It lets an event pass if its different than
 * the last event, if specified, numeric values can have a tolerance band
 * (relative or absolute defined), e.i. only if the new values lies outside this
 * band, it is send (aka known as deadband or histerese band)
 * 
 * @author Marco Grawunder
 * 
 */
@LogicalOperator(name = "CHANGEDETECT", minInputPorts = 1, maxInputPorts = 1, doc = "This operator can reduce traffic. It lets an event pass if its different than the last event, if specified, numeric values can have a tolerance band (relative or absolute defined) e.i. only if the new values lies outside this band, it is send (aka known as deadband or histerese band)", category = { LogicalOperatorCategory.PATTERN })
public class ChangeDetectAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -9042464546094886480L;
	private List<SDFAttribute> attributes;
	private double tolerance = 0;
	private boolean isRelativeTolerance = false;
	private int rate;
	private boolean deliverFirstElement = false;
	private boolean sendLastOfSameObjects = false;
	private List<SDFAttribute> groupingAttributes = new ArrayList<>();
	private SDFAttribute suppressCountAttribute;
	private double baseValue;
	private boolean useBaseValue = false;
	private boolean useWindow = false;

	public ChangeDetectAO(ChangeDetectAO ao) {
		super(ao);
		attributes = ao.attributes;
		this.rate = ao.rate;
		this.deliverFirstElement = ao.deliverFirstElement;
		this.groupingAttributes = new ArrayList<>(ao.getGroupingAttributes());
		this.tolerance = ao.tolerance;
		this.isRelativeTolerance = ao.isRelativeTolerance;
		this.suppressCountAttribute = ao.suppressCountAttribute;
		this.sendLastOfSameObjects = ao.sendLastOfSameObjects;
		this.baseValue = ao.getBaseValue();
		this.useBaseValue = ao.isUseBaseValue();
		this.useWindow = ao.isUseWindow();
	}

	public ChangeDetectAO() {
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "ATTR", isList = true)
	public void setAttr(List<SDFAttribute> attributes) {
		this.attributes = attributes;
	}

	public List<SDFAttribute> getAttr() {
		return this.attributes;
	}

	@Parameter(type = IntegerParameter.class, name = "heartbeatrate", optional = true)
	public void setHeartbeatRate(int rate) {
		this.rate = rate;
	}

	public int getHeartbeatRate() {
		return rate;
	}

	public boolean isDeliverFirstElement() {
		return deliverFirstElement;
	}

	public boolean isSendLastOfSameObjects() {
		return sendLastOfSameObjects;
	}

	@Parameter(type = BooleanParameter.class, name = "SendLastOfSameObjects", optional = true, doc = "If set to false (default), in a group of same objects, the first is send. If set to true, the last one is send.")
	public void setSendLastOfSameObjects(boolean sendLastOfSameObjects) {
		this.sendLastOfSameObjects = sendLastOfSameObjects;
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
	public void setSuppressCountAttribute(String name) {
		this.suppressCountAttribute = new SDFAttribute(null, name,
				SDFDatatype.INTEGER, null, null, null);
	}

	public String getSuppressCountAttribute() {
		return suppressCountAttribute != null ? suppressCountAttribute
				.getAttributeName() : null;
	}

	public SDFAttribute getSuppressCountAttributeValue() {
		return suppressCountAttribute;
	}

	public boolean isRelativeTolerance() {
		return isRelativeTolerance;
	}

	public double getBaseValue() {
		return baseValue;
	}

	@Parameter(type = DoubleParameter.class, name = "baseValue", optional = true, doc = "If 'useBaseValue' is true, the actual value is compared to the base value instead to the last value.")
	public void setBaseValue(double baseValue) {
		this.baseValue = baseValue;
	}

	public boolean isUseBaseValue() {
		return useBaseValue;
	}

	@Parameter(type = BooleanParameter.class, name = "useBaseValue", optional = true, doc = "If this is set to true, the actual value is compared to the base value instead to the last value. Default is false. Does not work with 'useWindow'.")
	public void setUseBaseValue(boolean useBaseValue) {
		this.useBaseValue = useBaseValue;
	}

	public boolean isUseWindow() {
		return useWindow;
	}

	@Parameter(type = BooleanParameter.class, name = "useWindow", optional = true, doc = "If this is set to true, the operator compares not to the last value (or base value), but instead to the elements in the window. Therefore the difference to the minimum and maximum value to the new value is calculated. Default is false.")
	public void setUseWindow(boolean useWindow) {
		this.useWindow = useWindow;
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
				int pos = inputSchema.indexOf(a);
				// Could be, if a metadata attribute is contained
				if (pos == -1) {
					return null;
				}
				ret[i++] = inputSchema.indexOf(a);
			}
			return ret;
		}
		return null;
	}

	public List<Pair<Integer, Integer>> getComparePositions2() {
		if (attributes.size() > 0) {

			List<Pair<Integer, Integer>> ret = new ArrayList<Pair<Integer, Integer>>();
			SDFSchema inputSchema = getInputSchema();
			for (SDFAttribute a : attributes) {
				int pos = inputSchema.indexOf(a);
				if (pos > 0) {
					ret.add(new Pair<Integer, Integer>(-1, pos));
				} else {
					ret.add(inputSchema.indexOfMetaAttribute(a));
				}
			}
			return ret;
		}
		return null;
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		if (suppressCountAttribute == null) {
			return super.getOutputSchemaIntern(pos);
		}
		SDFSchema inputSchema = getInputSchema();
		List<SDFAttribute> attributes = new ArrayList<>(
				inputSchema.getAttributes());
		attributes.add(suppressCountAttribute);
		SDFSchema outputSchema = SDFSchemaFactory.createNewWithAttributes(
				attributes, inputSchema);

		return outputSchema;
	}

	@Override
	public boolean isValid() {
		boolean isValid = true;
		if (hasAttributes()) {
			int[] comPos = getComparePositions();
			if (comPos != null) {
				for (int c : comPos) {
					if (c == -1) {
						addError("Not all attributes in input found!");
						isValid = false;
					}
				}
			}else{
				List<Pair<Integer, Integer>> pos = getComparePositions2();
				for (Pair<Integer,Integer> p:pos){
					if (p.getE2() == -1){
						addError("Not all attributes in input found!");
						isValid = false;
					}
				}
			}
			
		}
		return isValid && super.isValid();
	}

	public boolean hasAttributes() {
		return attributes != null && attributes.size() > 0;
	}

}
