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
package de.uniol.inf.is.odysseus.interval.transform.join;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.LeftJoinAO;
import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.predicate.TruePredicate;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTIDummyDataCreation;
import de.uniol.inf.is.odysseus.intervalapproach.LeftJoinTIPO;
import de.uniol.inf.is.odysseus.intervalapproach.LeftJoinTITransferArea;
import de.uniol.inf.is.odysseus.relational.persistentqueries.PersistentTransferArea;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

@SuppressWarnings({"unchecked","rawtypes"})
public class TLeftJoinAORule extends AbstractTransformationRule<LeftJoinAO> {

	@Override
	public int getPriority() {	
		// must be higher than TJoinAORule, since
		// LeftJoinAO extends JoinAO
		return 5; 
	
	}

	@Override
	public void execute(LeftJoinAO joinAO, TransformationConfiguration transformConfig) {
		LeftJoinTIPO joinPO = new LeftJoinTIPO(joinAO.getInputSchema(0), joinAO.getInputSchema(1), joinAO.getOutputSchema());
		IPredicate pred = joinAO.getPredicate();
		joinPO.setJoinPredicate(pred == null ? new TruePredicate() : pred.clone());
		
		// if in both input paths there is no window, we
		// use a persistent sweep area
		// check the paths
		boolean windowFound=false;
		for(int port = 0; port<2; port++){
			if(!JoinTransformationHelper.checkLogicalPath(joinAO.getSubscribedToSource(port).getTarget())){
				windowFound = true;
				break;
			}
		}
		
		if(!windowFound){
			joinPO.setTransferFunction(new PersistentTransferArea());
		}
		// otherwise we use a LeftJoinTISweepArea
		else{
			joinPO.setTransferFunction(new LeftJoinTITransferArea());	
		}
		
		joinPO.setMetadataMerge(new CombinedMergeFunction());
		joinPO.setOutputSchema(joinAO.getOutputSchema() == null?null:joinAO.getOutputSchema().clone());
		joinPO.setCreationFunction(new DefaultTIDummyDataCreation());
		Collection<ILogicalOperator> toUpdate = transformConfig.getTransformationHelper().replace(joinAO, joinPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		insert(joinPO);
		retract(joinAO);		
	}

	@Override
	public boolean isExecutable(LeftJoinAO operator, TransformationConfiguration transformConfig) {
		if(operator.isAllPhysicalInputSet()){
			if(transformConfig.getMetaTypes().contains(ITimeInterval.class.getCanonicalName())){
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "JoinAO -> JoinTIPO";
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<? super LeftJoinAO> getConditionClass() {	
		return LeftJoinAO.class;
	}

}
