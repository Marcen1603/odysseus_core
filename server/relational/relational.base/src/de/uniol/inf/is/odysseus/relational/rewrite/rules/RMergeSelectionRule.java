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

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterPredicateOptimizer;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.RewriteConfiguration;
import de.uniol.inf.is.odysseus.core.server.predicate.ComplexPredicateHelper;
import de.uniol.inf.is.odysseus.mep.optimizer.ExpressionOptimizer;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.relational.rewrite.RelationalRestructHelper;
import de.uniol.inf.is.odysseus.rewrite.flow.RewriteRuleFlowGroup;
import de.uniol.inf.is.odysseus.rewrite.rule.AbstractRewriteRule;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;

public class RMergeSelectionRule extends AbstractRewriteRule<SelectAO> {

	@Override
	public int getPriority() {
		return 30;
	}

	@Override
	public void execute(SelectAO operator, RewriteConfiguration config) {
		SelectAO sel = (SelectAO) getSubscribingOperatorAndCheckType(operator,
				SelectAO.class);
		if (sel != null) {
			if (sel.getPredicate() != null) {
				if (operator.getPredicate() != null) {
					operator.setPredicate(ComplexPredicateHelper
							.createAndPredicate(operator.getPredicate(),
									sel.getPredicate()));
                    ParameterPredicateOptimizer optimizeConfig = config.getQueryBuildConfiguration().get(ParameterPredicateOptimizer.class);
                    if (optimizeConfig != null && optimizeConfig.getValue().booleanValue()) {
                        if (operator.getPredicate() instanceof RelationalPredicate) {
                            RelationalPredicate relationalPredicate = (RelationalPredicate) operator.getPredicate();
                            IExpression<?> expression = ((RelationalPredicate) operator.getPredicate()).getExpression().getMEPExpression();
                            expression = ExpressionOptimizer.optimize(expression);
                            IExpression<?> cnf = ExpressionOptimizer.toConjunctiveNormalForm(expression);
                            SDFExpression sdfExpression = new SDFExpression(cnf, relationalPredicate.getExpression().getAttributeResolver(), relationalPredicate.getExpression().getExpressionParser());
                            operator.setPredicate(new RelationalPredicate(sdfExpression));
                        }
                    }
				} else {
					operator.setPredicate(sel.getPredicate());
				}
				RestructParameterInfoUtil.updatePredicateParameterInfo(
						operator.getParameterInfos(), operator.getPredicate());

				Collection<ILogicalOperator> toUpdate = RelationalRestructHelper
						.removeOperator(sel);
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
		return RewriteRuleFlowGroup.CLEANUP;
	}

	@Override
	public Class<? super SelectAO> getConditionClass() {
		return SelectAO.class;
	}

}
