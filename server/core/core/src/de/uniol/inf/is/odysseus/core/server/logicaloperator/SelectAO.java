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

import de.uniol.inf.is.odysseus.core.logicaloperator.IOperatorState;
import de.uniol.inf.is.odysseus.core.logicaloperator.IParallelizableOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.InputOrderRequirement;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PredicateParameter;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicate;

/**
 * @author Marco Grawunder
 * 
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "SELECT", doc = "The select operator filters the incoming data stream according to the given predicate.", url = "http://wiki.odysseus.informatik.uni-oldenburg.de/display/ODYSSEUS/Select+operator", category = {
		LogicalOperatorCategory.BASE })
public class SelectAO extends UnaryLogicalOp implements IHasPredicate, IParallelizableOperator {
	private static final long serialVersionUID = 3215936185841514846L;
	private int rate;
	
	private boolean predicateIsUpdateable = false;

	private IPredicate<?> predicate;

	public SelectAO() {
		super();
	}

	public SelectAO(SelectAO po) {
		super(po);
		this.rate = po.rate;
		this.predicate = po.predicate.clone();
		this.predicateIsUpdateable = po.isPredicateIsUpdateable();
	}

	public SelectAO(IPredicate<?> predicate) {
		setPredicate(predicate);
	}

	@Parameter(type = IntegerParameter.class, name = "heartbeatrate", optional = true)
	public void setHeartbeatRate(int rate) {
		this.rate = rate;
	}

	public int getHeartbeatRate() {
		return rate;
	}

	@SuppressWarnings("rawtypes")
	@Parameter(type = PredicateParameter.class)
	public void setPredicate(IPredicate predicate) {
		this.predicate = predicate;

	}

	@Override
	public IPredicate<?> getPredicate() {
		return predicate;
	}
	
	public boolean isPredicateIsUpdateable() {
		return predicateIsUpdateable;
	}
	
	@Parameter(name = "predicateIsUpdateable", optional = true, type = BooleanParameter.class, isList = false, doc = "If set to true, the predicate of the select can be updated with punctuations.")
	public void setPredicateIsUpdateable(boolean predicateIsUpdateable) {
		this.predicateIsUpdateable = predicateIsUpdateable;
	}

	@Override
	public SelectAO clone() {
		return new SelectAO(this);
	}

	@Override
	public String toString() {
		return super.toString() + getPredicate();
	}

	@Override
	public OperatorStateType getStateType() {
		// FIXME check if predicate is stateful
		return IOperatorState.OperatorStateType.STATELESS;

	}

	@Override
	public boolean isParallelizable() {
		return true;
	}

	/**
	 * There should be no restriction for stateless operators
	 */
	@Override
	public InputOrderRequirement getInputOrderRequirement(int inputPort) {
		return InputOrderRequirement.NONE;
	}

	
}
