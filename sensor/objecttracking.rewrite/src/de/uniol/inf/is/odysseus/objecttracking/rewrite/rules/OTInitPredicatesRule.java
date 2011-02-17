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
package de.uniol.inf.is.odysseus.objecttracking.rewrite.rules;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.objecttracking.logicaloperator.IHasRangePredicates;
import de.uniol.inf.is.odysseus.objecttracking.util.ObjectTrackingPredicateInitializer;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.RewriteConfiguration;
import de.uniol.inf.is.odysseus.rewrite.flow.RewriteRuleFlowGroup;
import de.uniol.inf.is.odysseus.rewrite.rule.AbstractRewriteRule;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;


public class OTInitPredicatesRule extends AbstractRewriteRule<IHasRangePredicates>{

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 15;
	}

	@Override
	public void execute(IHasRangePredicates operator,
			RewriteConfiguration config) {
		System.out.println("Init predicates of operator : " + operator);
		ObjectTrackingPredicateInitializer.visitPredicates(operator.getRangePredicates(), (ILogicalOperator)operator);
		operator.setInitialized(true);
		//retract(operator);
		System.out.println("Init predicates of operator finished: " + operator);
	}

	@Override
	public boolean isExecutable(IHasRangePredicates operator,
			RewriteConfiguration config) {
		if(operator.getRangePredicates() != null && !operator.isInitialized()){
			return true;
		}
		return false;
		
		// DRL-Code
//		$op : IHasRangePredicates(rangePredicates != null && initialized == false)
	}

	@Override
	public String getName() {
		return "Init RangePredicates";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return RewriteRuleFlowGroup.CLEANUP;
	}

}
