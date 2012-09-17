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

package de.uniol.inf.is.odysseus.securitypunctuation.rules.join;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.LeftJoinAO;
import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.predicate.TruePredicate;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTIDummyDataCreation;
import de.uniol.inf.is.odysseus.intervalapproach.TITransferArea;
import de.uniol.inf.is.odysseus.persistentqueries.PersistentTransferArea;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator.SAJoinPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
import de.uniol.inf.is.odysseus.interval.transform.join.JoinTransformationHelper;

@SuppressWarnings({"unchecked","rawtypes"})
public class TSAJoinAORule extends AbstractTransformationRule<JoinAO> {

	@Override
	public int getPriority() {	
		return 100;
	}

	@Override
	public void execute(JoinAO joinAO, TransformationConfiguration transformConfig) {
		SAJoinPO joinPO = new SAJoinPO();
		IPredicate pred = joinAO.getPredicate();
		joinPO.setJoinPredicate(pred == null ? new TruePredicate() : pred.clone());
		
		// Der Name des Schemas soll nicht beide vorherigen Schemanamen nur aneinanderhängen, sondern sie mit einem Komma trennen!!!
		// Der neue Name wird dabei in joinAO geschrieben, da er bei defaultExecute() nochmal daraus gelesen wird.
		// So können die Quellen später wieder getrennt werden für die SP
		String newOutputSchemaName = joinAO.getSubscribedToSource(0).getTarget().getOutputSchema().getURI() + ",";
		newOutputSchemaName += joinAO.getSubscribedToSource(1).getTarget().getOutputSchema().getURI();
		SDFSchema tmpSchema = new SDFSchema(newOutputSchemaName, joinAO.getOutputSchema());
		joinAO.setOutputSchema(tmpSchema);	
		
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
			joinPO.setTransferFunction(new TITransferArea());
		}
		
		joinPO.setMetadataMerge(new CombinedMergeFunction());
		joinPO.setCreationFunction(new DefaultTIDummyDataCreation());
		
		defaultExecute(joinAO, joinPO, transformConfig, true, true);

	}

	@Override
	public boolean isExecutable(JoinAO operator, TransformationConfiguration transformConfig) {
		if(operator.isAllPhysicalInputSet() && !(operator instanceof LeftJoinAO)){
			if(transformConfig.getMetaTypes().contains(ITimeInterval.class.getCanonicalName())){
				if (transformConfig.getOption("isSecurityAware") != null) {
					if (transformConfig.getOption("isSecurityAware")) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "JoinAO -> SAJoinPO";
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
