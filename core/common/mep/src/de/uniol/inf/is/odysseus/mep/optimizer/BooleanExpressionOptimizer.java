/********************************************************************************** 
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.mep.optimizer;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.mep.functions.bool.AndOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.NotOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.OrOperator;

/**
 * Boolean expression optimizer
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id: ExpressionOptimizer.java | ExpressionOptimizer.java |
 *          ExpressionOptimizer.java $
 *
 */
public final class BooleanExpressionOptimizer {
    private static final int MAX_ITERATION = 100;
    private static List<IExpressionOptimizerRule<?>> RULES = new ArrayList<>();
    static {
        RULES.add(new ReduceAndRule());
        RULES.add(new ReduceOrRule());
        RULES.add(new ReduceNotRule());
        RULES.add(new QuineMcCluskeyRule());
        RULES.add(new SortByComplexityRule());
    }

    /**
     * Optimize the given expression by applying different optimization rules.
     * 
     * @param expression
     *            The expression
     * @return The optimized expression
     */
    public static IMepExpression<?> optimize(IMepExpression<?> expression) {
        if ((expression.isConstant()) || (expression.isVariable())) {
            return expression;
        }
        if ((expression instanceof NotOperator) || (expression instanceof AndOperator) || (expression instanceof OrOperator)) {
            if (expression instanceof NotOperator) {
                IMepExpression<?> child = expression.toFunction().getArgument(0);
                if (child != null) {
                    child = optimize(child);
                }
                expression.toFunction().setArguments(new IMepExpression<?>[] { child });
            }
            else {
                IMepExpression<?> left = expression.toFunction().getArgument(0);
                if (left != null) {
                    left = optimize(left);
                }
                IMepExpression<?> right = expression.toFunction().getArgument(1);
                if (right != null) {
                    right = optimize(right);
                }
                expression.toFunction().setArguments(new IMepExpression<?>[] { left, right });
            }
            IMepExpression<?> expr = expression;
            String tmpExpr = "";
            int i = 0;
            while ((!expr.toString().equals(tmpExpr)) && (i < MAX_ITERATION)) {
                tmpExpr = expr.toString();
                for (IExpressionOptimizerRule<?> rule : RULES) {
                    expr = rule.executeRule(expr);
                }
                i++;
            }
            return expr;
        }
        return expression;
    }

    /**
     * Transforms the given expression into its disjunctive normal form.
     * 
     * @param expression
     *            The expression
     * @return The disjunctive normal form of the expression
     */
    public static IMepExpression<?> toDisjunctiveNormalForm(IMepExpression<?> expression) {
        DisjunctiveNormalFormRule disjunctiveNormalFormRule = new DisjunctiveNormalFormRule();
        IMepExpression<?> dnf = disjunctiveNormalFormRule.executeRule(expression);
        return optimize(dnf);
    }

    /**
     * Transforms the given expression into its conjunctive normal form.
     * 
     * @param expression
     *            The expression
     * @return The conjunctive normal form of the expression
     */
    public static IMepExpression<?> toConjunctiveNormalForm(IMepExpression<?> expression) {
        ConjunctiveNormalFormRule conjunctiveNormalFormRule = new ConjunctiveNormalFormRule();
        IMepExpression<?> cnf = conjunctiveNormalFormRule.executeRule(expression);
        return optimize(cnf);
    }

    public static IMepExpression<?> sortByStringExpressions(IMepExpression<?> expression) {
        SortByStringExpressionRule rules = new SortByStringExpressionRule();
        IMepExpression<?> res = rules.executeRule(expression);
        return res;
    }

}
