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
package de.uniol.inf.is.odysseus.relational_interval.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.server.intervalapproach.transform.join.JoinTransformationHelper;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;
import de.uniol.inf.is.odysseus.sweeparea.SweepAreaRegistry;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TJoinAOSetSARule extends AbstractTransformationRule<JoinTIPO> {

	private static final String HASH_JOIN_SA = "HashJoinSA";

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(JoinTIPO joinPO, TransformationConfiguration transformConfig) throws RuleException {
		ITimeIntervalSweepArea[] areas = new ITimeIntervalSweepArea[2];

		String areaName = "";

		if (joinPO.getSweepAreaName() != null) {
			areaName = joinPO.getSweepAreaName();
		}

		SDFSchema leftSchema = joinPO.getSubscribedToSource(0).getSchema();
		SDFSchema rightSchema = joinPO.getSubscribedToSource(1).getSchema();
		IPredicate predicate = joinPO.getPredicate();
		
		// Attention: side effect, areas are filled!
		areaName = handleSpecialCased(joinPO, areas, areaName, leftSchema, rightSchema, predicate);

		// if special cases did not find a solution, use the generic one
		if (areas[0] == null || areas[1] == null) {
			// When no area name is set, use TIJoinSA as default
			if (areaName == null || areaName.isEmpty()) {
				areaName = "TIJoinSA";
			}
			
			try {
				areas[0] = (ITimeIntervalSweepArea) SweepAreaRegistry.getSweepArea(areaName, joinPO.getOptions(), leftSchema, rightSchema, predicate);
				areas[1] = (ITimeIntervalSweepArea) SweepAreaRegistry.getSweepArea(areaName, joinPO.getOptions(), leftSchema, rightSchema, predicate);
			} catch (InstantiationException | IllegalAccessException e) {
				// ignore, is handled below
			}
		}

		if (areas[0] == null || areas[1] == null) {
			throw new TransformationException("Cannot find sweep area of type " + areaName);
		}

		joinPO.setAreas(areas);
		if (!areaName.equals(HASH_JOIN_SA)) {
			/*
			 * The hash join sa cannot be created by the SweepAreaRegistry so don't use the
			 * name to create new instances.
			 */
			joinPO.setSweepAreaName(areaName);
		}
		/*
		 * # no update, because otherwise # other rules may overwrite this rule #
		 * example: rule with priority 5 setting the areas has been # processed, update
		 * causes rule engine to search for other # rules applicable for the updated
		 * object. The rule with # priority 5 cannot be processed because of no-loop
		 * term, however # other rules with lower priority could be used of the updated
		 * # objects fulfills the when clause. However, these lower priority # rules
		 * should not be used because of the high priority rule # # do not use retract
		 * also, because # other fields of the object should still be modified
		 */

	}

	private String handleSpecialCased(JoinTIPO joinPO, ITimeIntervalSweepArea[] areas, String areaName, SDFSchema leftSchema,
			SDFSchema rightSchema, IPredicate predicate) {
		boolean check = JoinTransformationHelper.canBeUsedWithHashJoin(predicate, leftSchema, rightSchema, areas);

		// Automatically set HashJoinSA if predicate is pure equals predicate
		if (areaName.isEmpty() && check) {
			areaName = HASH_JOIN_SA;
		}

		// TMP-Hack
		if (areaName.equalsIgnoreCase(HASH_JOIN_SA)) {
			if (check == false) {
				throw new TransformationException("Cannot use " + areaName + " with this predicate "
						+ joinPO.getPredicate() + ". Only equals predicates are possible!");
			}
		} else {
			// The user does not want to use a HashSweepArea but something else. As the
			// areas are already created, we will remove them here
			areas[0] = null;
			areas[1] = null;
		}
		
		if(areaName.equalsIgnoreCase("UnaryOuterJoinSA")) {
			JoinTransformationHelper.useUnaryOuterJoinSA(predicate, leftSchema, rightSchema, areas, false, joinPO.getCardinalities());
		} else if (areaName.equalsIgnoreCase("UnaryOuterJoinSA2")) {
			JoinTransformationHelper.useUnaryOuterJoinSA2(predicate, leftSchema, rightSchema, areas);
		} else if(areaName.equals("UnaryOuterJoinRndSA")) {
			JoinTransformationHelper.useUnaryOuterJoinSA(predicate, leftSchema, rightSchema, areas, true, joinPO.getCardinalities());
		}
		return areaName;
	}

	@Override
	public boolean isExecutable(JoinTIPO operator, TransformationConfiguration transformConfig) {
		if (operator.getOutputSchema().getType() == Tuple.class) {
			return !operator.isAreasSet();
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

	@Override
	public Class<? super JoinTIPO> getConditionClass() {
		return JoinTIPO.class;
	}

}
