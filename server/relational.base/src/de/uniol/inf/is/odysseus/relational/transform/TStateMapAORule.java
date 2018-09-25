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
package de.uniol.inf.is.odysseus.relational.transform;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StateMapAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalGroupProcessor;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalMapPO;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalNoGroupProcessor;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalStateMapPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TStateMapAORule extends AbstractTransformationRule<StateMapAO> {

	@Override
	public int getPriority() {
		// Must be higher Prio than Map because MapRule fires before StateMap
		// Rule
		return 10;
	}

	@Override
	public void execute(StateMapAO mapAO, TransformationConfiguration transformConfig) throws RuleException {
		List<SDFAttribute> gAttr = mapAO.getGroupingAttributes();
		@SuppressWarnings("rawtypes")
		IGroupProcessor gp = null;
		if (gAttr != null) {
			gp = new RelationalGroupProcessor<>(mapAO.getInputSchema(), mapAO.getOutputSchema(), gAttr, null, false);
		} else {
			gp = RelationalNoGroupProcessor.getInstance();
		}
		int[] restrictList = SDFSchema.calcRestrictList(mapAO.getInputSchema(), mapAO.getRemoveAttributes());

		@SuppressWarnings("unchecked")
		RelationalMapPO<?> mapPO = new RelationalStateMapPO<IMetaAttribute>(mapAO.getInputSchema(),
				mapAO.getExpressionList().toArray(new SDFExpression[0]), mapAO.isAllowNullInOutput(), gp,
				mapAO.isEvaluateOnPunctuation(), mapAO.isExpressionsUpdateable(), mapAO.isSuppressErrors(),
				mapAO.isKeepInput(), restrictList);
		defaultExecute(mapAO, mapPO, transformConfig, true, true);
	}

	@Override
	public boolean isExecutable(StateMapAO operator, TransformationConfiguration transformConfig) {
		if (operator.getInputSchema().getType() == Tuple.class) {
			if (operator.getPhysSubscriptionTo() != null) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "StateMapAO -> RelationalMapPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super StateMapAO> getConditionClass() {
		return StateMapAO.class;
	}

}
