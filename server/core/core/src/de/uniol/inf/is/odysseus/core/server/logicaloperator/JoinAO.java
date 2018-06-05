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
/*
 * Created on 07.12.2004
 *
 */
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.IOperatorState;
import de.uniol.inf.is.odysseus.core.logicaloperator.IParallelizableOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.IStatefulAO;
import de.uniol.inf.is.odysseus.core.logicaloperator.InputOrderRequirement;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.EnumParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PredicateParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicate;

/**
 * @author Marco Grawunder
 * 
 */
@LogicalOperator(minInputPorts = 2, maxInputPorts = 2, name = "JOIN", doc = "Operator to combine two datastreams based on the predicate", url = "http://wiki.odysseus.informatik.uni-oldenburg.de/display/ODYSSEUS/Join+operator", category = { LogicalOperatorCategory.BASE })
public class JoinAO extends BinaryLogicalOp implements IHasPredicate, IStatefulAO, IParallelizableOperator, IOutOfOrderHandler {

	private static final long serialVersionUID = 3710951139395164614L;

	Cardinalities card = null;
	private Boolean assureOrder = null;

	private IPredicate<?> predicate;
	private String sweepAreaName;
	
	// For the internal element window
	private int elementSizePort0 = -1;
	private int elementSizePort1 = -1;
	private List<SDFAttribute> groupingAttributesPort0;
	private List<SDFAttribute> groupingAttributesPort1;
	private boolean keepEndTimestamp = false;

	public JoinAO() {
		super();
	}

	public JoinAO(JoinAO joinAO) {
		super(joinAO);
		this.card = joinAO.card;
		if (joinAO.predicate != null) {
			this.predicate = joinAO.predicate.clone();
		}
		this.assureOrder = joinAO.assureOrder;
		this.sweepAreaName = joinAO.sweepAreaName;
		
		this.elementSizePort0 = joinAO.elementSizePort0;
		this.elementSizePort1 = joinAO.elementSizePort1;
		
		this.groupingAttributesPort0 = joinAO.groupingAttributesPort0;
		this.groupingAttributesPort1 = joinAO.groupingAttributesPort1;
		
		this.keepEndTimestamp = joinAO.keepEndTimestamp;
	}

	@Parameter(type = EnumParameter.class, optional = true, doc = "Type of input streams. For optimization purposes: ONE_ONE, ONE_MANY, MANY_ONE, MANY_MANY")
	public void setCard(Cardinalities card) {
		this.card = card;
	}

	public Cardinalities getCard() {
		return card;
	}

	@Parameter(type = StringParameter.class, optional = true, doc = "Overwrite the sweep area")
	public void setSweepAreaName(String sweepAreaName) {
		this.sweepAreaName = sweepAreaName;
	}
	
	public String getSweepAreaName() {
		return sweepAreaName;
	}
	
	@Override
	public Boolean isAssureOrder() {
		return assureOrder;
	}

	@Override
	@Parameter(type = BooleanParameter.class, optional = true, doc = "If set to false, the operator will not garantee order in output. Default is true")
	public void setAssureOrder(boolean assureOrder) {
		this.assureOrder = assureOrder;
	}

	@Parameter(type = PredicateParameter.class, optional = true, doc = "Predicate to filter combinations")
	public synchronized void setPredicate(IPredicate<?> joinPredicate) {
		this.predicate = joinPredicate;
	}

	@Override
	public IPredicate<?> getPredicate() {
		return predicate;
	}
	
	@Parameter(type = IntegerParameter.class, optional = true, name = "elementSizePort0", doc = "Internal element window size for port 0. Only the n newest elements from that port are used.")
	public void setElementSizePort0(int elementSize) {
		this.elementSizePort0 = elementSize;
	}
	
	public int getElementSizePort1() {
		return this.elementSizePort0;
	}
	
	@Parameter(type = IntegerParameter.class, optional = true, name = "elementSizePort1", doc = "Internal element window size for port 1. Only the n newest elements from that port are used.")
	public void setElementSizePort1(int elementSize) {
		this.elementSizePort1 = elementSize;
	}
	
	public int getElementSizePort2() {
		return this.elementSizePort1;
	}
	
	public boolean keepEndTimestamp() {
		return keepEndTimestamp;
	}

	@Parameter(type = BooleanParameter.class, optional = true, name = "keepEndTimestamp", doc = "When using element size restrictions, the end timestamp is set to infinity due to semantics (later aggeregation). If you want to keep end timestamp, set to true. Default is false.")
	public void setKeepEndTimestamp(boolean keepEndTimestamp) {
		this.keepEndTimestamp = keepEndTimestamp;
	}
	
	@Parameter(name = "group_by_port_0", optional = true, type = ResolvedSDFAttributeParameter.class, isList = true, doc = "Group the element window (if used) be these attributes on port 0.")
	public void setGroupingAttributesPort0(final List<SDFAttribute> attributes) {
		groupingAttributesPort0 = attributes;
	}

	@GetParameter(name = "group_by_port_0")
	public List<SDFAttribute> getGroupingAttributesPort0() {
		if (groupingAttributesPort0 == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(groupingAttributesPort0);
	}
	
	@Parameter(name = "group_by_port_1", optional = true, type = ResolvedSDFAttributeParameter.class, isList = true, doc = "Group the element window (if used) be these attributes on port 1.")
	public void setGroupingAttributesPort1(final List<SDFAttribute> attributes) {
		groupingAttributesPort1 = attributes;
	}

	@GetParameter(name = "group_by_port_1")
	public List<SDFAttribute> getGroupingAttributesPort1() {
		if (groupingAttributesPort1 == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(groupingAttributesPort1);
	}

	public @Override JoinAO clone() {
		return new JoinAO(this);
	}

	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer(super.toString());
		ret.append(" Predicate " + getPredicate());
		return ret.toString();
	}

	@Override
	public synchronized SDFSchema getOutputSchemaIntern(int pos) {
		Iterator<LogicalSubscription> iter = getSubscribedToSource().iterator();
		SDFSchema left = iter.next().getSchema();
		SDFSchema right = iter.next().getSchema();
		SDFSchema outputSchema = SDFSchema.join(left, right);
		
		boolean outOfOrder = !(left.isInOrder() && right.isInOrder()) || (assureOrder != null && !assureOrder); 
		
		SDFSchema outputSchemaWithOrder = SDFSchemaFactory.createNewWithOutOfOrder(outOfOrder, outputSchema);
		
		return outputSchemaWithOrder;
	}
	
	@Override
	public OperatorStateType getStateType() {
		return IOperatorState.OperatorStateType.PARTITIONED_STATE;
	}

	@Override
	public boolean isParallelizable() {
		return true;
	}
	
	@Override
	public InputOrderRequirement getInputOrderRequirement(int inputPort) {
		return InputOrderRequirement.SELFHANDLED;
	}

}
