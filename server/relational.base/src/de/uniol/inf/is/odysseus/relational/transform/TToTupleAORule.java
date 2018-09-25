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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalPlan;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ToTupleAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.RenameAttribute;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TToTupleAORule extends AbstractTransformationRule<ToTupleAO> {

	@Override
	public int getPriority() {
		return 1;
	}

	@Override
	public void execute(ToTupleAO toTuple, TransformationConfiguration transformConfig) throws RuleException {

		// Two cases: Input schema is same than output schema --> remove
		// operator
		if (SDFSchema.compatible(toTuple.getOutputSchema(), toTuple.getInputSchema())) {
			Collection<ILogicalOperator> toUpdate = LogicalPlan.removeOperator(toTuple, false);
			for (ILogicalOperator o : toUpdate) {
				update(o);
			}
		} else { // replace with map
			MapAO map = new MapAO();
			List<NamedExpression> namedExpressions = new ArrayList<>();
			List<RenameAttribute> attributes = toTuple.getAttributes();
			IAttributeResolver resolver = new DirectAttributeResolver(toTuple.getInputSchema());
			for (RenameAttribute attribute : attributes) {
				namedExpressions.add(new NamedExpression(attribute.getNewName(),
						new SDFExpression(attribute.getAttribute().getAttributeName(), resolver, MEP.getInstance())));
			}
			map.setExpressions(namedExpressions);
			LogicalPlan.replace(toTuple, map);
			insert(map);
		}
		retract(toTuple);
	}

	@Override
	public boolean isExecutable(ToTupleAO operator, TransformationConfiguration transformConfig) {
		// Change to MAP if input is already tuple
		return operator.getInputSchema().getType().isAssignableFrom(Tuple.class);
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.SUBSTITUTION;
	}

	@Override
	public Class<? super ToTupleAO> getConditionClass() {
		return ToTupleAO.class;
	}
}
