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
package de.uniol.inf.is.odysseus.core.predicate.optimizer;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.predicate.AndPredicate;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.predicate.NotPredicate;
import de.uniol.inf.is.odysseus.core.predicate.OrPredicate;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class PredicateOptimizer {
    private static final int MAX_ITERATION = 100;

    private static List<IPredicateOptimizerRule<?>> RULES = new ArrayList<>();
    static {
        RULES.add(new ReduceAndRule());
        RULES.add(new ReduceOrRule());
        RULES.add(new ReduceNotRule());
        RULES.add(new QuineMcCluskeyRule());
    }

    /**
     * Optimize the given predicate by applying different optimization rules.
     * 
     * @param predicate
     *            The predicate
     * @return The optimized predicate
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static IPredicate<?> optimize(IPredicate<?> predicate) {
        if ((predicate instanceof NotPredicate) || (predicate instanceof AndPredicate) || (predicate instanceof OrPredicate)) {
            if (predicate instanceof NotPredicate) {
                IPredicate<?> child = ((NotPredicate<?>) predicate).getChild();
                if (child != null) {
                    child = optimize(child);
                }
                ((NotPredicate) predicate).setChild(child);
            }
            else if (predicate instanceof AndPredicate) {
                IPredicate<?> left = ((AndPredicate<?>) predicate).getLeft();
                if (left != null) {
                    left = optimize(left);
                }
                IPredicate<?> right = ((AndPredicate<?>) predicate).getRight();
                if (right != null) {
                    right = optimize(right);
                }
                ((AndPredicate) predicate).setLeft(left);
                ((AndPredicate) predicate).setRight(right);

            }
            else if (predicate instanceof OrPredicate) {
                IPredicate<?> left = ((OrPredicate<?>) predicate).getLeft();
                if (left != null) {
                    left = optimize(left);
                }
                IPredicate<?> right = ((OrPredicate<?>) predicate).getRight();
                if (right != null) {
                    right = optimize(right);
                }
                ((OrPredicate) predicate).setLeft(left);
                ((OrPredicate) predicate).setRight(right);
            }
            IPredicate<?> expr = predicate;
            String tmpExpr = "";
            int i = 0;
            while ((!expr.toString().equals(tmpExpr)) && (i < MAX_ITERATION)) {
                tmpExpr = expr.toString();
                for (IPredicateOptimizerRule<?> rule : RULES) {
                    expr = rule.executeRule(expr);
                }
                i++;
            }
            return expr;
        }
        return predicate;
    }

    /**
     * Transforms the given expression into its disjunctive normal form.
     * 
     * @param expression
     *            The expression
     * @return The disjunctive normal form of the expression
     */
    public static IPredicate<?> toDisjunctiveNormalForm(IPredicate<?> expression) {
        DisjunctiveNormalFormRule disjunctiveNormalFormRule = new DisjunctiveNormalFormRule();
        IPredicate<?> dnf = disjunctiveNormalFormRule.executeRule(expression);
        return optimize(dnf);
    }

    /**
     * Transforms the given expression into its conjunctive normal form.
     * 
     * @param expression
     *            The expression
     * @return The conjunctive normal form of the expression
     */
    public static IPredicate<?> toConjunctiveNormalForm(IPredicate<?> expression) {
        ConjunctiveNormalFormRule conjunctiveNormalFormRule = new ConjunctiveNormalFormRule();
        IPredicate<?> cnf = conjunctiveNormalFormRule.executeRule(expression);
        return optimize(cnf);
    }
}
