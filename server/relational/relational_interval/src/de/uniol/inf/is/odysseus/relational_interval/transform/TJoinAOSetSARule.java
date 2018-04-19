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

		// check, which sweep area to use
		// if the join is an equi join
		// and there is not window in one
		// path to the source, use
		// a hash sweep area
		// otherwise use a JoinTISweepArea
		// IPredicate pred = joinPO.getJoinPredicate();

		String areaName = "";

		if (joinPO.getSweepAreaName() != null) {
			areaName = joinPO.getSweepAreaName();
		}

		SDFSchema ownSchema = joinPO.getSubscribedToSource(0).getSchema();
		SDFSchema otherSchema = joinPO.getSubscribedToSource(1).getSchema();
		IPredicate predicate = joinPO.getPredicate();

		// Attention: side effect, areas are filled!
		boolean check = JoinTransformationHelper.canBeUsedWithHashJoin(predicate, ownSchema, otherSchema, areas);

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

		if (areas[0] == null || areas[1] == null) {
			if (areaName == null || areaName.isEmpty()) {
				areaName = "TIJoinSA";
			}
			try {
				areas[0] = (ITimeIntervalSweepArea) SweepAreaRegistry.getSweepArea(areaName);
				areas[1] = (ITimeIntervalSweepArea) SweepAreaRegistry.getSweepArea(areaName);
			} catch (InstantiationException | IllegalAccessException e) {
				throw new TransformationException("Cannot find sweep area of type " + areaName);
			}
			;
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
