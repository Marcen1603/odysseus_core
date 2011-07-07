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
package de.uniol.inf.is.odysseus.interval.transform.join;

import java.util.Collection;

import de.uniol.inf.is.odysseus.intervalapproach.DefaultTIDummyDataCreation;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.intervalapproach.LeftJoinTITransferArea;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.logicaloperator.LeftJoinAO;
import de.uniol.inf.is.odysseus.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.persistentqueries.PersistentTransferArea;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.predicate.TruePredicate;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

@SuppressWarnings({"unchecked","rawtypes"})
public class TJoinAORule extends AbstractTransformationRule<JoinAO> {

	@Override
	public int getPriority() {	
		return 0;
	}

	@Override
	public void execute(JoinAO joinAO, TransformationConfiguration transformConfig) {
		JoinTIPO joinPO = new JoinTIPO();
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
	public boolean isExecutable(JoinAO operator, TransformationConfiguration transformConfig) {
		if(operator.isAllPhysicalInputSet() && !(operator instanceof LeftJoinAO)){
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
	public Class<?> getConditionClass() {	
		return JoinAO.class;
	}

}
