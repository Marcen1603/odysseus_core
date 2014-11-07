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
package de.uniol.inf.is.odysseus.server.intervalapproach.transform.join;

import de.uniol.inf.is.odysseus.core.physicaloperator.interval.TITransferArea;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.LeftJoinAO;
import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.predicate.TruePredicate;
import de.uniol.inf.is.odysseus.persistentqueries.DirectTransferArea;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.DefaultTIDummyDataCreation;
import de.uniol.inf.is.odysseus.server.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.server.intervalapproach.transform.AbstractIntervalTransformationRule;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TJoinAORule extends AbstractIntervalTransformationRule<JoinAO> {

	@Override
	public void execute(JoinAO joinAO,
			TransformationConfiguration transformConfig) throws RuleException {
		JoinTIPO joinPO = new JoinTIPO();
		boolean isCross = false;
		IPredicate pred = joinAO.getPredicate();
		if (pred == null) {
			joinPO.setJoinPredicate(TruePredicate.getInstance());
			isCross = true;
		} else {
			joinPO.setJoinPredicate(pred.clone());
		}
		joinPO.setCardinalities(joinAO.getCard());
		joinPO.setSweepAreaName(joinAO.getSweepAreaName());
		
		// This is "bullsh*" The is no need for a window. The elements could
		// already have start and end timestamps!

		// if in both input paths there is no window, we
		// use a persistent sweep area
		// check the paths
		// boolean windowFound=false;
		// for(int port = 0; port<2; port++){
		// if(!JoinTransformationHelper.checkLogicalPath(joinAO.getSubscribedToSource(port).getTarget())){
		// windowFound = true;
		// break;
		// }
		// }
		//
		// if(!windowFound){
		// joinPO.setTransferFunction(new PersistentTransferArea());
		// }
		// // otherwise we use a LeftJoinTISweepArea
		// else{

		if (joinAO.isAssureOrder()) {
			joinPO.setTransferFunction(new TITransferArea());
		} else {
			joinPO.setTransferFunction(new DirectTransferArea());
		}
		// }

		joinPO.setMetadataMerge(new CombinedMergeFunction());
		joinPO.setCreationFunction(new DefaultTIDummyDataCreation());

		defaultExecute(joinAO, joinPO, transformConfig, true, true);
		if (isCross) {
			joinPO.setName("Crossproduct");
		}

	}

	@Override
	public boolean isExecutable(JoinAO operator,
			TransformationConfiguration transformConfig) {
		if (super.isExecutable(operator, transformConfig)) {
			return !(operator instanceof LeftJoinAO);
		}
		return false;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super JoinAO> getConditionClass() {
		return JoinAO.class;
	}

}
