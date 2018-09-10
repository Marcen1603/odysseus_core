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
package de.uniol.inf.is.odysseus.relational_interval.transform;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ChangeDetectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalGroupProcessor;
import de.uniol.inf.is.odysseus.relational_interval.RelationalAbsoluteNumericBasevalueChangeDetectPO;
import de.uniol.inf.is.odysseus.relational_interval.RelationalAbsoluteNumericChangeDetectPO;
import de.uniol.inf.is.odysseus.relational_interval.RelationalAbsoluteNumericWindowChangeDetectPO;
import de.uniol.inf.is.odysseus.relational_interval.RelationalChangeDetectPO;
import de.uniol.inf.is.odysseus.relational_interval.RelationalRelativeNumericBasevalueChangeDetectPO;
import de.uniol.inf.is.odysseus.relational_interval.RelationalRelativeNumericChangeDetectPO;
import de.uniol.inf.is.odysseus.relational_interval.RelationalRelativeNumericWindowChangeDetectPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.NElementHeartbeatGeneration;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;

public class TRelationalChangeDetectAORule extends AbstractRelationalIntervalTransformationRule<ChangeDetectAO> {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void execute(ChangeDetectAO operator, TransformationConfiguration config) throws RuleException {
		RelationalChangeDetectPO po = null;
		if (operator.getTolerance() == 0) {
			if (operator.getComparePositions() != null){
				po = new RelationalChangeDetectPO(operator.getComparePositions());
			}else if (operator.getComparePositions2() != null){
				po = new RelationalChangeDetectPO(operator.getComparePositions2());
			}
		} else {
			if (operator.isRelativeTolerance()) {
				if (!operator.isUseBaseValue() && !operator.isUseWindow()) {
					// Use the normal relative change detect (change to previous
					// tuple)
					po = new RelationalRelativeNumericChangeDetectPO(operator.getComparePositions(),
							operator.getTolerance());
				} else if (operator.isUseWindow()) {
					// Use the window checking operator
					po = new RelationalRelativeNumericWindowChangeDetectPO(operator.getComparePositions(),
							operator.getTolerance());
				} else {
					// Use the operator with base value
					po = new RelationalRelativeNumericBasevalueChangeDetectPO(operator.getComparePositions(),
							operator.getTolerance(), operator.getBaseValue());
				}

			} else {
				if (!operator.isUseBaseValue() && !operator.isUseWindow()) {
					// Use the normal absolute change detect (change to previous
					// tuple)
					po = new RelationalAbsoluteNumericChangeDetectPO(operator.getComparePositions(),
							operator.getTolerance());
				} else if (operator.isUseWindow()) {
					// Use the window checking operator
					po = new RelationalAbsoluteNumericWindowChangeDetectPO(operator.getComparePositions(),
							operator.getTolerance());
				} else {
					// Use the operator with base value
					po = new RelationalAbsoluteNumericBasevalueChangeDetectPO(operator.getComparePositions(),
							operator.getTolerance(), operator.getBaseValue());
				}

			}
		}
		if (po != null) {
			if (operator.getHeartbeatRate() > 0) {
				po.setHeartbeatGenerationStrategy(new NElementHeartbeatGeneration(operator.getHeartbeatRate()));
			}
			po.setDeliverFirstElement(operator.isDeliverFirstElement());
			po.setSendLastOfSameObjects(operator.isSendLastOfSameObjects());
			if (operator.getGroupingAttributes().size() > 0) {
				RelationalGroupProcessor r = new RelationalGroupProcessor(operator.getInputSchema(),
						operator.getOutputSchema(), operator.getGroupingAttributes(), null, false);
				po.setGroupProcessor(r);
			}
			SDFAttribute suppressAttribute = operator.getSuppressCountAttributeValue();
			if (suppressAttribute != null) {
				po.setSuppressAttribute(operator.getOutputSchema().indexOf(suppressAttribute));
			}
			defaultExecute(operator, po, config, true, true);
		}
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super ChangeDetectAO> getConditionClass() {
		return ChangeDetectAO.class;
	}

}
