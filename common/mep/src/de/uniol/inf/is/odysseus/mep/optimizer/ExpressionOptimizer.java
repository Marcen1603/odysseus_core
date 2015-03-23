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

import de.uniol.inf.is.odysseus.core.mep.IExpression;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public final class ExpressionOptimizer {
    private static List<IExpressionOptimizerRule<?>> RULES = new ArrayList<>();
    static {
        RULES.add(new ReduceAndRule());
        RULES.add(new ReduceOrRule());
        RULES.add(new ReduceNotRule());
        RULES.add(new DeMorganRule());
    }

    public static IExpression<?> toDisjunctiveNormalForm(IExpression<?> expression) {
        IExpression<?> expr = expression;
        DisjunctiveNormalFormRule disjunctiveNormalFormRule = new DisjunctiveNormalFormRule();

        List<IExpressionOptimizerRule<?>> rules = new ArrayList<>();
        rules.addAll(RULES);
        rules.add(disjunctiveNormalFormRule);

        String tmpExpr = "";
        while (!expr.toString().equals(tmpExpr)) {
            tmpExpr = expr.toString();
            for (IExpressionOptimizerRule<?> rule : rules) {
                expr = rule.executeRule(expr);
            }
        }
        // expr = disjunctiveNormalFormRule.executeRule(expr);
        return expr;
    }

    public static IExpression<?> toConjunctiveNormalForm(IExpression<?> expression) {
        IExpression<?> expr = expression;
        ConjunctiveNormalFormRule conjunctiveNormalFormRule = new ConjunctiveNormalFormRule();

        List<IExpressionOptimizerRule<?>> rules = new ArrayList<>();
        rules.addAll(RULES);
        rules.add(conjunctiveNormalFormRule);
        String tmpExpr = "";
        while (!expr.toString().equals(tmpExpr)) {
            tmpExpr = expr.toString();
            for (IExpressionOptimizerRule<?> rule : rules) {
                expr = rule.executeRule(expr);
            }
        }
        // expr = conjunctiveNormalFormRule.executeRule(expr);
        return expr;
    }
}
