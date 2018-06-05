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

import java.util.List;

import de.uniol.inf.is.odysseus.core.expression.IRelationalExpression;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalPlan;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterPredicateOptimizer;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.RewriteConfiguration;
import de.uniol.inf.is.odysseus.core.server.predicate.ComplexPredicateHelper;
import de.uniol.inf.is.odysseus.mep.optimizer.BooleanExpressionOptimizer;
import de.uniol.inf.is.odysseus.rewrite.flow.RewriteRuleFlowGroup;
import de.uniol.inf.is.odysseus.rewrite.rule.AbstractRewriteRule;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;

@SuppressWarnings({ "rawtypes" })
public class RSplitSelectionRule extends AbstractRewriteRule<SelectAO> {

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public void execute(SelectAO sel, RewriteConfiguration config) {
        List<IPredicate> preds = splitPredicate(sel.getPredicate(), config);

        for (int i = 0; i < preds.size() - 1; i++) {
            SelectAO newSel = createNewSelect(sel, preds.get(i));
            RestructParameterInfoUtil.updatePredicateParameterInfo(newSel, newSel.getParameterInfos(), newSel.getPredicate());

            LogicalPlan.insertOperator(newSel, sel, 0, 0, 0);
            insert(newSel);
        }
        sel.setPredicate(preds.get(preds.size() - 1));
        RestructParameterInfoUtil.updatePredicateParameterInfo(sel, sel.getParameterInfos(), sel.getPredicate());

        update(sel);
    }

    private static SelectAO createNewSelect(SelectAO sel, IPredicate pred) {
        SelectAO newSel = new SelectAO(sel);
        for (IOperatorOwner owner : sel.getOwner()) {
            newSel.addOwner(owner);
        }
        newSel.setPredicate(pred);
        return newSel;
    }

    @SuppressWarnings("unchecked")
	private static List<IPredicate> splitPredicate(IPredicate sel, RewriteConfiguration config) {
        List<IPredicate> preds;
        if (ComplexPredicateHelper.isAndPredicate(sel)) {
        	preds = sel.conjunctiveSplit();
        }
        else {
            RelationalExpression relationalPredicate = ((RelationalExpression) sel);
            ParameterPredicateOptimizer optimizeConfig = config.getQueryBuildConfiguration().get(ParameterPredicateOptimizer.class);
            if (optimizeConfig != null && optimizeConfig.getValue().booleanValue()) {
                IMepExpression<?> expression = ((RelationalExpression) sel).getMEPExpression();
                expression = BooleanExpressionOptimizer.optimize(expression);
                IMepExpression<?> cnf = BooleanExpressionOptimizer.toConjunctiveNormalForm(expression);
                SDFExpression sdfExpression = new SDFExpression(cnf.toString(), relationalPredicate.getAttributeResolver(), relationalPredicate.getExpressionParser());

                preds = (new RelationalExpression(sdfExpression)).conjunctiveSplit();
            }
            else {
                preds = ((RelationalExpression) sel).conjunctiveSplit();
            }
        }
        return preds;
    }

    @Override
    public boolean isExecutable(SelectAO operator, RewriteConfiguration config) {
        IPredicate pred = operator.getPredicate();
        if (pred != null) {
            if (ComplexPredicateHelper.isAndPredicate(pred) || (pred instanceof IRelationalExpression && ((RelationalExpression)pred).isAndPredicate())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return RewriteRuleFlowGroup.SPLIT;
    }

    @Override
    public Class<? super SelectAO> getConditionClass() {
        return SelectAO.class;
    }

}
