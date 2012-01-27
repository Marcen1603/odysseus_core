/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.PredicateParameter;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Marco Grawunder
 * 
 */
@LogicalOperator(minInputPorts = 2, maxInputPorts = 2, name = "JOIN")
public class JoinAO extends BinaryLogicalOp {

	private static final long serialVersionUID = 3710951139395164614L;
	private SDFAttributeList outputSchema = null;

	public JoinAO() {
		super();
	}

	public JoinAO(IPredicate<?> joinPredicate) {
		super();
		this.setPredicate(joinPredicate);
	}

	public JoinAO(JoinAO joinPO) {
		super(joinPO);
		SDFAttributeList schema = joinPO.getOutputSchema();
		if (schema != null) {
			this.outputSchema = schema.clone();
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Parameter(type = PredicateParameter.class, optional=true)
	public synchronized void setPredicate(IPredicate joinPredicate) {
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
	public synchronized SDFAttributeList getOutputSchema() {
		// The Sum of all InputSchema
		if (outputSchema == null || recalcOutputSchemata) {
			outputSchema = new SDFAttributeList("");
			for (LogicalSubscription l : getSubscribedToSource()) {
				outputSchema = SDFAttributeList.union(outputSchema, l.getSchema());
			}
			recalcOutputSchemata = false;
		}
		return outputSchema;
	}

}
