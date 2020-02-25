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

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.Cardinalities;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.HashJoinSweepArea;
import de.uniol.inf.is.odysseus.server.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.server.intervalapproach.JoinTISweepArea;
import de.uniol.inf.is.odysseus.server.intervalapproach.transform.join.JoinTransformationHelper;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;
import de.uniol.inf.is.odysseus.sweeparea.SweepAreaRegistry;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TJoinAOSetSARule extends AbstractTransformationRule<JoinTIPO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(JoinTIPO joinPO, TransformationConfiguration transformConfig) throws RuleException {

		SDFSchema leftSchema = joinPO.getSubscribedToSource(0).getSchema();
		SDFSchema rightSchema = joinPO.getSubscribedToSource(1).getSchema();
		IPredicate predicate = joinPO.getPredicate();
		
		String leftAreaName = joinPO.getSweepAreaName(0);
		String rightAreaName= joinPO.getSweepAreaName(1);
		
		ITimeIntervalSweepArea[] areas = null;

		if (!Strings.isNullOrEmpty(leftAreaName)) {
			// Attention: side effect, areas are filled!
			areas = handleSpecialCased(joinPO, leftAreaName, leftSchema, rightSchema, predicate);
		}
		// if special cases did not find a solution, use the generic one
		if (areas == null) {

			// First try, if a hash join can be used
			if (Strings.isNullOrEmpty(leftAreaName)) {
				leftAreaName = HashJoinSweepArea.NAME;
				areas = createSweepAreas(leftAreaName, leftAreaName, joinPO.getOptions(), leftSchema, rightSchema, predicate, joinPO.getCardinalities());
			}
				
			// When creation of HASH_JOIN_SA does not work, the areas are empty
			// In case of not given areaName try JoinArea
			if (areas[0] == null && Strings.isNullOrEmpty(leftAreaName)) {
				leftAreaName = JoinTISweepArea.NAME;
			}
			
			if (Strings.isNullOrEmpty(rightAreaName)) {
				rightAreaName = leftAreaName;
			}
			
			areas = createSweepAreas(leftAreaName, rightAreaName, joinPO.getOptions(), leftSchema, rightSchema, predicate, joinPO.getCardinalities());
			
		}

		if (areas == null || areas[0] == null || areas[1] == null) {
			throw new TransformationException("Cannot find sweep area of types " + leftAreaName+"/"+rightAreaName);
		}

		joinPO.setAreas(areas);
		joinPO.setSweepAreaName(leftAreaName, rightAreaName);
		
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

	private ITimeIntervalSweepArea[] createSweepAreas(String leftAreaName, String rightAreaName, OptionMap options, SDFSchema leftSchema,
			SDFSchema rightSchema, IPredicate predicate, Cardinalities cardinalities) {
		ITimeIntervalSweepArea[] areas = new ITimeIntervalSweepArea[2];
		try {
			areas[0] = (ITimeIntervalSweepArea) SweepAreaRegistry.getSweepArea(leftAreaName, options, leftSchema, rightSchema, predicate, cardinalities);
			areas[1] = (ITimeIntervalSweepArea) SweepAreaRegistry.getSweepArea(rightAreaName, options, rightSchema, leftSchema, predicate, cardinalities);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace(); // TODO: Ignore later
		}
		return areas;
	}

	private ITimeIntervalSweepArea[] handleSpecialCased(JoinTIPO joinPO, String areaName, SDFSchema leftSchema,
			SDFSchema rightSchema, IPredicate predicate) {
		ITimeIntervalSweepArea[] areas = new ITimeIntervalSweepArea[2];
		// TODO: Can this be moved somewhere else to be used in SweepAreaRegistry?
		if(areaName.equalsIgnoreCase("UnaryOuterJoinSA")) {
			JoinTransformationHelper.useUnaryOuterJoinSA(predicate, leftSchema, rightSchema, areas, false, joinPO.getCardinalities());
		} else if (areaName.equalsIgnoreCase("UnaryOuterJoinSA2")) {
			JoinTransformationHelper.useUnaryOuterJoinSA2(predicate, leftSchema, rightSchema, areas);
		} else if(areaName.equals("UnaryOuterJoinRndSA")) {
			JoinTransformationHelper.useUnaryOuterJoinSA(predicate, leftSchema, rightSchema, areas, true, joinPO.getCardinalities());
		}
		return areas;
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
