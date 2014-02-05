/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.securitypunctuation.rules.join;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator.SAJoinPO;
import de.uniol.inf.is.odysseus.server.intervalapproach.JoinTISweepArea;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSAJoinAOSetSARule<K extends ITimeInterval, T extends IStreamObject<K>> extends AbstractTransformationRule<SAJoinPO<K,T>>  {

	@Override
	public int getPriority() {		
		return 100;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(SAJoinPO<K,T> joinPO, TransformationConfiguration transformConfig) throws RuleException {
		ITimeIntervalSweepArea[] areas = new ITimeIntervalSweepArea[2];
		
		// check, which sweep area to use
		// if the join is an equi join
		// and there is not window in one
		// path to the source, use
		// a hash sweep area
		// otherwise use a JoinTISweepArea
		//IPredicate pred = joinPO.getJoinPredicate();
		
		areas[0] = new JoinTISweepArea();
		areas[1] = new JoinTISweepArea();
		
		// See TJoinAISetSARule
		
//		// check the paths
//		for(int port = 0; port<2; port++){
//			int otherPort = port^1;
//			if(JoinTransformationHelper.checkPhysicalPath(joinPO.getSubscribedToSource(port).getTarget())){
//				// check the predicate and calculate
//				// the restrictList
//				Set<Pair<SDFAttribute, SDFAttribute>> neededAttrs = new TreeSet<Pair<SDFAttribute, SDFAttribute>>();				
//				
//				if(JoinTransformationHelper.checkPredicate(
//						joinPO.getPredicate(),
//						neededAttrs,
//						joinPO.getSubscribedToSource(port).getSchema(),
//						joinPO.getSubscribedToSource(otherPort).getSchema())){
//				
//					// transform the set into a list to guarantee the
//					// same order of attributes for both restrict lists
//					List<Pair<SDFAttribute, SDFAttribute>> neededAttrsList = new ArrayList<Pair<SDFAttribute, SDFAttribute>>();
//					for(Pair<SDFAttribute, SDFAttribute> pair: neededAttrs){
//						neededAttrsList.add(pair);
//					}
//					
//					Pair<int[], int[]> restrictLists = JoinTransformationHelper.createRestrictLists(joinPO, neededAttrsList, port);
//					areas[port] = new HashJoinSweepArea(restrictLists.getE1(), restrictLists.getE2());
//				}
//			}
//		}
		
		
		joinPO.setAreas(areas);
		/*
		# no update, because otherwise
		# other rules may overwrite this rule
		# example: rule with priority 5 setting the areas has been
		# processed, update causes rule engine to search for other
		# rules applicable for the updated object. The rule with
		# priority 5 cannot be processed because of no-loop term, however
		# other rules with lower priority could be used of the updated
		# objects fulfills the when clause. However, these lower priority
		# rules should not be used because of the high priority rule
		# 
		# do not use retract also, because
		# other fields of the object should still be modified
		*/
		
	}
	
	

	@Override
	public boolean isExecutable(SAJoinPO<K,T> operator, TransformationConfiguration transformConfig) {
		if(transformConfig.getMetaTypes().contains(ITimeInterval.class.getCanonicalName())){
			if(operator.getAreas()==null){
				if (transformConfig.getOption("isSecurityAware") != null) {
					if (Boolean.parseBoolean((String)transformConfig.getOption("isSecurityAware")) )  {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "JoinTIPO set SweepArea";
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.METAOBJECTS;
	}

}
