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

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.EnumParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PredicateParameter;

/**
 * @author Marco Grawunder
 * 
 */
@LogicalOperator(minInputPorts = 2, maxInputPorts = 2, name = "JOIN", doc = "Operator to combine two datastreams based on the predicate", category={LogicalOperatorCategory.BASE})
public class JoinAO extends BinaryLogicalOp {

	private static final long serialVersionUID = 3710951139395164614L;
	
	Cardinalities card = null;

	public JoinAO() {
		super();
	}

	public JoinAO(IPredicate<?> joinPredicate) {
		super();
		this.setPredicate(joinPredicate);
	}

	public JoinAO(JoinAO joinPO) {
		super(joinPO);
		this.card = joinPO.card;
	}
	
	@Parameter(type = EnumParameter.class, optional = true, doc = "Type of input streams. For optimization purposes: ONE_ONE, ONE_MANY, MANY_ONE, MANY_MANY")
	public void setCard(Cardinalities card) {
		this.card = card;
	}
	
	public Cardinalities getCard() {
		return card;
	}

	@Override
	@Parameter(type = PredicateParameter.class, optional = true, doc = "Predicate to filter combinations")
	public synchronized void setPredicate(IPredicate<?> joinPredicate) {
		super.setPredicate(joinPredicate);
	}

	public @Override
	JoinAO clone() {
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
		setOutputSchema(outputSchema);
		return outputSchema;
	}

}
