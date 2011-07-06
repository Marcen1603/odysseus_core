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
package de.uniol.inf.is.odysseus.interval.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.intervalapproach.AntiJoinTIPO;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.intervalapproach.predicate.OverlapsPredicate;
import de.uniol.inf.is.odysseus.logicaloperator.ExistenceAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.physicaloperator.ISweepArea;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.predicate.ComplexPredicateHelper;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TExistenceAORule extends AbstractTransformationRule<ExistenceAO> {

	@Override 
	public int getPriority() {		
		return 0;
	}

	@Override
	public void execute(ExistenceAO existenceAO, TransformationConfiguration transformConfig) {
		ISweepArea leftSA = new DefaultTISweepArea();
		ISweepArea rightSA = new DefaultTISweepArea();
		IPredicate predicate = existenceAO.getPredicate();
		if (existenceAO.getType() == ExistenceAO.Type.NOT_EXISTS) {
			predicate = ComplexPredicateHelper.createNotPredicate(predicate);
		}
		leftSA.setQueryPredicate(ComplexPredicateHelper.createAndPredicate(OverlapsPredicate.getInstance(), predicate));
		rightSA.setQueryPredicate(ComplexPredicateHelper.createAndPredicate(OverlapsPredicate.getInstance(), predicate));
		AntiJoinTIPO po = new AntiJoinTIPO(existenceAO, leftSA, rightSA);
		Collection<ILogicalOperator> toUpdate = transformConfig.getTransformationHelper().replace(existenceAO, po);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		retract(existenceAO);
		
	}

	@Override
	public boolean isExecutable(ExistenceAO operator, TransformationConfiguration transformConfig) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "ExistenceAO -> AntiJoinTIPO";
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<?> getConditionClass() {	
		return ExistenceAO.class;
	}

}
