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
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.predicate.optimizer.PredicateOptimizer;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterPredicateOptimizer;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.RewriteConfiguration;
import de.uniol.inf.is.odysseus.mep.optimizer.BooleanExpressionOptimizer;
import de.uniol.inf.is.odysseus.relational.rewrite.RelationalRestructHelper;
import de.uniol.inf.is.odysseus.rewrite.flow.RewriteRuleFlowGroup;
import de.uniol.inf.is.odysseus.rewrite.rule.AbstractRewriteRule;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;

public class RMergeSelectionJoinRule extends AbstractRewriteRule<JoinAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(JoinAO join, RewriteConfiguration config) {
		SelectAO sel = (SelectAO) getSubscribingOperatorAndCheckType(join,
				SelectAO.class);
		if (sel != null) {
			if (sel.getPredicate() != null) {
				RelationalExpression<?> newPredicate;
				if (join.getPredicate() != null) {
					newPredicate = (RelationalExpression<?>) join.getPredicate().and((IPredicate)sel.getPredicate());
				} else {
					newPredicate = (RelationalExpression<?>) sel.getPredicate().clone();
				}
				join.setPredicate(newPredicate);
				newPredicate.initVars(join.getInputSchema(0), join.getInputSchema(1));
				
				ParameterPredicateOptimizer optimizeConfig = config
						.getQueryBuildConfiguration().get(
								ParameterPredicateOptimizer.class);
				if (optimizeConfig != null
						&& optimizeConfig.getValue().booleanValue()) {
					if (join.getPredicate() instanceof IRelationalExpression) {
						RelationalExpression<?> relationalPredicate = (RelationalExpression<?>) join
								.getPredicate();
						IExpression<?> expression = ((RelationalExpression<?>) join
								.getPredicate()).getMEPExpression();
						expression = BooleanExpressionOptimizer.optimize(expression);
						IExpression<?> cnf = BooleanExpressionOptimizer
								.toConjunctiveNormalForm(expression);
						SDFExpression sdfExpression = new SDFExpression(cnf.toString(),
								relationalPredicate.getExpressionParser());
						join.setPredicate(new RelationalExpression(sdfExpression));
					}
                    join.setPredicate(PredicateOptimizer.optimize(join.getPredicate()));
				}
				RestructParameterInfoUtil.updatePredicateParameterInfo(
						join.getParameterInfos(), join.getPredicate());

				Collection<ILogicalOperator> toUpdate = RelationalRestructHelper
						.removeOperator(sel);
				for (ILogicalOperator o : toUpdate) {
					update(o);
				}
				update(join);
				retract(sel);
			}
		}
	}

	@Override
	public boolean isExecutable(JoinAO join, RewriteConfiguration config) {
		SelectAO sel = (SelectAO) getSubscribingOperatorAndCheckType(join,
				SelectAO.class);
		// Join and select can always be merged
		return sel != null;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return RewriteRuleFlowGroup.GROUP;
	}

	@Override
	public Class<? super JoinAO> getConditionClass() {
		return JoinAO.class;
	}

}
