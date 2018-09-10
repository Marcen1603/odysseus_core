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
package de.uniol.inf.is.odysseus.relational.rewrite.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.expression.IRelationalExpression;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.predicate.optimizer.PredicateOptimizer;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterPredicateOptimizer;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.RewriteConfiguration;
import de.uniol.inf.is.odysseus.mep.optimizer.BooleanExpressionOptimizer;
import de.uniol.inf.is.odysseus.rewrite.flow.RewriteRuleFlowGroup;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;

public class RMergeSelectionRule extends AbstractRelationalRewriteRule<SelectAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(SelectAO operator, RewriteConfiguration config) {
		SelectAO sel = (SelectAO) getSubscribingOperatorAndCheckType(operator, SelectAO.class);
		if (sel != null) {
			if (sel.getPredicate() != null) {
				if (operator.getPredicate() != null) {
					operator.setPredicate(operator.getPredicate().and((IPredicate) sel.getPredicate()));
					ParameterPredicateOptimizer optimizeConfig = config.getQueryBuildConfiguration()
							.get(ParameterPredicateOptimizer.class);
					if (optimizeConfig != null && optimizeConfig.getValue().booleanValue()) {
						if (operator.getPredicate() instanceof IRelationalExpression) {
							RelationalExpression<?> relationalPredicate = (RelationalExpression<?>) operator
									.getPredicate();
							IMepExpression<?> expression = ((RelationalExpression<?>) operator.getPredicate())
									.getMEPExpression();
							expression = BooleanExpressionOptimizer.optimize(expression);
							IMepExpression<?> cnf = BooleanExpressionOptimizer.toConjunctiveNormalForm(expression);
							SDFExpression sdfExpression = new SDFExpression(cnf.toString(),relationalPredicate.getAttributeResolver(),
									relationalPredicate.getExpressionParser());
							operator.setPredicate(new RelationalExpression(sdfExpression));
						}
						operator.setPredicate(PredicateOptimizer.optimize(operator.getPredicate()));

					}
				} else {
					operator.setPredicate(sel.getPredicate());
				}
				RestructParameterInfoUtil.updatePredicateParameterInfo(operator, operator.getParameterInfos(),
						operator.getPredicate());

				Collection<ILogicalOperator> toUpdate = removeOperator(sel);
				for (ILogicalOperator o : toUpdate) {
					update(o);
				}
				update(operator);
				retract(sel);
			}
		}
	}

	@Override
	public boolean isExecutable(SelectAO sel, RewriteConfiguration config) {
		return getSubscribingOperatorAndCheckType(sel, SelectAO.class) != null;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return RewriteRuleFlowGroup.GROUP;
	}

	@Override
	public Class<? super SelectAO> getConditionClass() {
		return SelectAO.class;
	}

}
