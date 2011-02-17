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
package de.uniol.inf.is.odysseus.parser.pql.priority;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.builder.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.PredicateParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.priority.PriorityAO;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class PriorityAOBuilder extends AbstractOperatorBuilder {

	// DirectParameter<Long> defaultPriority = new
	// DirectParameter<Long>("DEFAULT", REQUIREMENT.OPTIONAL);
	private PredicateParameter prioritize = new PredicateParameter(
			"PRIORITIZE", REQUIREMENT.MANDATORY);

	public PriorityAOBuilder() {
		super(1, 1);
		setParameters(prioritize);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected ILogicalOperator createOperatorInternal() {
		PriorityAO<RelationalTuple<?>> priorityAO = new PriorityAO<RelationalTuple<?>>();
		priorityAO.setPriority((byte) 1,
				(IPredicate<? super RelationalTuple<?>>) prioritize.getValue());
		priorityAO.setDefaultPriority((byte) 0);

		return priorityAO;
	}

	@Override
	protected boolean internalValidation() {
		return true;
	}
}
