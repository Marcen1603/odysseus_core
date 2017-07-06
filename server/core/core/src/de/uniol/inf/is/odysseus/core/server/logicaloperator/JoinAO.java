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

import java.util.Iterator;

import de.uniol.inf.is.odysseus.core.logicaloperator.IOperatorState;
import de.uniol.inf.is.odysseus.core.logicaloperator.IParallelizableOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.IStatefulAO;
import de.uniol.inf.is.odysseus.core.logicaloperator.InputOrderRequirement;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.EnumParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PredicateParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicate;

/**
 * @author Marco Grawunder
 * 
 */
@LogicalOperator(minInputPorts = 2, maxInputPorts = 2, name = "JOIN", doc = "Operator to combine two datastreams based on the predicate", url = "http://odysseus.offis.uni-oldenburg.de:8090/display/ODYSSEUS/Join+operator", category = { LogicalOperatorCategory.BASE })
public class JoinAO extends BinaryLogicalOp implements IHasPredicate, IStatefulAO, IParallelizableOperator, IOutOfOrderHandler {

	private static final long serialVersionUID = 3710951139395164614L;

	Cardinalities card = null;
	private boolean assureOrder = true;

	private IPredicate<?> predicate;
	private String sweepAreaName; 

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
	public boolean isAssureOrder() {
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
		
		boolean outOfOrder = !(left.isInOrder() && right.isInOrder()); 
		
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
