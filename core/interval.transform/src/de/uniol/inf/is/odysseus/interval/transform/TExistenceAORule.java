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
package de.uniol.inf.is.odysseus.interval.transform;

import de.uniol.inf.is.odysseus.core.metadata.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ExistenceAO;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sa.ISweepArea;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.predicate.ComplexPredicateHelper;
import de.uniol.inf.is.odysseus.intervalapproach.AntiJoinTIPO;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.intervalapproach.predicate.OverlapsPredicate;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

@SuppressWarnings({"unchecked"})
public class TExistenceAORule extends AbstractTransformationRule<ExistenceAO> {

	@Override 
	public int getPriority() {		
		return 0;
	}

	@Override
	public void execute(ExistenceAO existenceAO, TransformationConfiguration transformConfig) {
		ISweepArea<MetaAttributeContainer<ITimeInterval>> leftSA = new DefaultTISweepArea<MetaAttributeContainer<ITimeInterval>>();
		ISweepArea<MetaAttributeContainer<ITimeInterval>> rightSA = new DefaultTISweepArea<MetaAttributeContainer<ITimeInterval>>();
		IPredicate<?> predicate = existenceAO.getPredicate();
		if (existenceAO.getType() == ExistenceAO.Type.NOT_EXISTS) {
			predicate = ComplexPredicateHelper.createNotPredicate(predicate);
		}
		leftSA.setQueryPredicate(ComplexPredicateHelper.createAndPredicate(OverlapsPredicate.getInstance(), predicate));
		rightSA.setQueryPredicate(ComplexPredicateHelper.createAndPredicate(OverlapsPredicate.getInstance(), predicate));
		AntiJoinTIPO<ITimeInterval, MetaAttributeContainer<ITimeInterval>> po = new AntiJoinTIPO<ITimeInterval, MetaAttributeContainer<ITimeInterval>>(existenceAO, leftSA, rightSA);
		defaultExecute(existenceAO, po, transformConfig, true, true);
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
	public Class<? super ExistenceAO> getConditionClass() {	
		return ExistenceAO.class;
	}

}
