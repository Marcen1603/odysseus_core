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
package de.uniol.inf.is.odysseus.relational.transform;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.predicate.ComplexPredicateHelper;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TInitPredicateRule extends AbstractTransformationRule<ILogicalOperator> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(ILogicalOperator operator,
			TransformationConfiguration config) {
		for(IPredicate<?> pred: operator.getPredicates()){
			pred.init();
			ComplexPredicateHelper.visitPredicates(pred, new InitPredicateFunctor(operator));
		}
	}

	@Override
	public boolean isExecutable(ILogicalOperator operator,
			TransformationConfiguration config) {
		return (operator.providesPredicates());
	}

	@Override
	public String getName() {
		return "Init Predicates";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.INIT;
	}

	@Override
	public Class<?> getConditionClass() {	
		return ILogicalOperator.class;
	}

}
