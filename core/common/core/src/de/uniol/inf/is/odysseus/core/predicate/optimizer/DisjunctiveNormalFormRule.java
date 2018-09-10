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

import de.uniol.inf.is.odysseus.core.predicate.AndPredicate;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.predicate.NotPredicate;
import de.uniol.inf.is.odysseus.core.predicate.OrPredicate;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class DisjunctiveNormalFormRule extends AbstractPredicateOptimizerRule<IPredicate<?>> {
    private final DeMorganRule deMorganRule = new DeMorganRule();

    /**
     * {@inheritDoc}
     */
    @Override
    public IPredicate<?> execute(IPredicate<?> expression) {
        return toDisjunctiveNormalForm(expression);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExecutable(IPredicate<?> expression) {
        return true;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private IPredicate<?> toDisjunctiveNormalForm(IPredicate<?> expression) {
        if (!isBooleanOperator(expression)) {
            return expression;
        }
        if (expression instanceof NotPredicate) {
            IPredicate<?> leaf = ((NotPredicate<?>) expression).getChild();
            if (!isBooleanOperator(leaf)) {
                return expression;
            }
            if (leaf instanceof NotPredicate) {
                return toDisjunctiveNormalForm(((NotPredicate<?>) leaf).getChild());
            }
            IPredicate<?> deMorgan = this.deMorganRule.executeRule(expression);
            return toDisjunctiveNormalForm(deMorgan);
        }
        IPredicate<?> left = null;
        IPredicate<?> right = null;
        if (expression instanceof AndPredicate) {
            left = ((AndPredicate<?>) expression).getLeft();
            if (left != null) {
                left = toDisjunctiveNormalForm(left);
            }
            right = ((AndPredicate<?>) expression).getRight();
            if (right != null) {
                right = toDisjunctiveNormalForm(right);
            }
        }
        if (expression instanceof OrPredicate) {
            left = ((OrPredicate<?>) expression).getLeft();
            if (left != null) {
                left = toDisjunctiveNormalForm(left);
            }
            right = ((OrPredicate<?>) expression).getRight();
            if (right != null) {
                right = toDisjunctiveNormalForm(right);
            }
        }

        if (expression instanceof OrPredicate) {
            OrPredicate or = new OrPredicate();
            or.setLeft(left);
            or.setRight(right);
            return or;
        }
        if (expression instanceof AndPredicate) {
            // (A v B) ∧ (C v D) => (A ∧ C) v (B ∧ C) v (A ∧ D) v (B ∧ D)
            if ((left != null) && (left instanceof OrPredicate) && (right != null) && (right instanceof OrPredicate)) {
                OrPredicate<?> tmpLeft = (OrPredicate<?>) left;
                OrPredicate<?> tmpRight = (OrPredicate<?>) right;

                AndPredicate leftLeftNode = new AndPredicate();
                leftLeftNode.setLeft(tmpLeft.getLeft());
                leftLeftNode.setRight(tmpRight.getLeft());
                AndPredicate leftRightNode = new AndPredicate();
                leftRightNode.setLeft(tmpLeft.getRight());
                leftRightNode.setRight(tmpRight.getLeft());
                left = new OrPredicate();
                ((OrPredicate) left).setLeft(leftLeftNode);
                ((OrPredicate) left).setRight(leftRightNode);

                AndPredicate rightLeftNode = new AndPredicate();
                rightLeftNode.setLeft(tmpLeft.getLeft());
                rightLeftNode.setRight(tmpRight.getRight());
                AndPredicate rightRightNode = new AndPredicate();
                rightRightNode.setLeft(tmpLeft.getRight());
                rightRightNode.setRight(tmpRight.getRight());
                right = new OrPredicate();
                ((OrPredicate) right).setLeft(rightLeftNode);
                ((OrPredicate) right).setRight(rightRightNode);

                OrPredicate or = new OrPredicate();
                or.setLeft(toDisjunctiveNormalForm(left));
                or.setRight(toDisjunctiveNormalForm(right));
                return or;

            }
            // ? ∧ (C v D) => (? ∧ D) v (? ∧ C)
            if ((right != null) && (right instanceof OrPredicate)) {
                OrPredicate<?> tmpRight = (OrPredicate<?>) right;
                IPredicate<?> tmpLeft = left;

                left = new AndPredicate();
                ((AndPredicate) left).setLeft(tmpLeft);
                ((AndPredicate) left).setRight(tmpRight.getRight());

                right = new AndPredicate();
                ((AndPredicate) right).setLeft(tmpLeft);
                ((AndPredicate) right).setRight(tmpRight.getLeft());

                OrPredicate or = new OrPredicate();
                or.setLeft(toDisjunctiveNormalForm(left));
                or.setRight(toDisjunctiveNormalForm(right));
                return or;
            }
            // (A v B) ∧ ? => (A ∧ ?) v (B ∧ ?)
            if ((left != null) && (left instanceof OrPredicate)) {
                OrPredicate tmpLeft = (OrPredicate) left;
                IPredicate<?> tmpRight = right;

                left = new AndPredicate();
                ((AndPredicate) left).setLeft(tmpLeft.getLeft());
                ((AndPredicate) left).setRight(tmpRight);

                right = new AndPredicate();
                ((AndPredicate) right).setLeft(tmpLeft.getRight());
                ((AndPredicate) right).setRight(tmpRight);

                OrPredicate or = new OrPredicate();
                or.setLeft(toDisjunctiveNormalForm(left));
                or.setRight(toDisjunctiveNormalForm(right));
                return or;

            }
            AndPredicate and = new AndPredicate();
            and.setLeft(left);
            and.setRight(right);
            return and;
        }
        return expression;
    }

}
