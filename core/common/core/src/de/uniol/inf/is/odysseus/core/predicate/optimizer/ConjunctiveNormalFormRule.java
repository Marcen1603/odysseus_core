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
public class ConjunctiveNormalFormRule extends AbstractPredicateOptimizerRule<IPredicate<?>> {
    private final DeMorganRule deMorganRule = new DeMorganRule();

    /**
     * {@inheritDoc}
     */
    @Override
    public IPredicate<?> execute(IPredicate<?> expression) {
        return toConjunctiveNormalForm(expression);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExecutable(IPredicate<?> expression) {
        return true;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private IPredicate<?> toConjunctiveNormalForm(IPredicate<?> expression) {
        if (!isBooleanOperator(expression)) {
            return expression;
        }
        if (expression instanceof NotPredicate) {
            IPredicate<?> leaf = ((NotPredicate<?>) expression).getChild();
            if (!isBooleanOperator(leaf)) {
                return expression;
            }
            if (leaf instanceof NotPredicate) {
                return toConjunctiveNormalForm(((NotPredicate<?>) leaf).getChild());
            }
            IPredicate<?> deMorgan = this.deMorganRule.executeRule(expression);
            return toConjunctiveNormalForm(deMorgan);
        }
        IPredicate<?> left = null;
        IPredicate<?> right = null;
        if (expression instanceof AndPredicate) {
            left = ((AndPredicate<?>) expression).getLeft();
            if (left != null) {
                left = toConjunctiveNormalForm(left);
            }
            right = ((AndPredicate<?>) expression).getRight();
            if (right != null) {
                right = toConjunctiveNormalForm(right);
            }
        }
        if (expression instanceof OrPredicate) {
            left = ((OrPredicate<?>) expression).getLeft();
            if (left != null) {
                left = toConjunctiveNormalForm(left);
            }
            right = ((OrPredicate<?>) expression).getRight();
            if (right != null) {
                right = toConjunctiveNormalForm(right);
            }
        }

        if (expression instanceof AndPredicate) {
            AndPredicate and = new AndPredicate();
            and.setLeft(left);
            and.setRight(right);
            return and;
        }
        if (expression instanceof OrPredicate) {
            // (A ∧ B) ∨ (C ∧ D) => (A ∨ C) ∧ (B ∨ C) ∧ (A ∨ D) ∧ (B ∨ D)
            if ((left != null) && (left instanceof AndPredicate) && (right != null) && (right instanceof AndPredicate)) {
                AndPredicate<?> tmpLeft = (AndPredicate<?>) left;
                AndPredicate<?> tmpRight = (AndPredicate<?>) right;

                OrPredicate leftLeftNode = new OrPredicate();
                leftLeftNode.setLeft(tmpLeft.getLeft());
                leftLeftNode.setRight(tmpRight.getLeft());
                OrPredicate leftRightNode = new OrPredicate();
                leftRightNode.setLeft(tmpLeft.getRight());
                leftRightNode.setRight(tmpRight.getLeft());
                left = new AndPredicate();
                ((AndPredicate<?>) left).setLeft(leftLeftNode);
                ((AndPredicate<?>) left).setRight(leftRightNode);

                OrPredicate rightLeftNode = new OrPredicate();
                rightLeftNode.setLeft(tmpLeft.getLeft());
                rightLeftNode.setRight(tmpRight.getRight());

                OrPredicate rightRightNode = new OrPredicate();
                rightRightNode.setLeft(tmpLeft.getRight());
                rightRightNode.setRight(tmpRight.getRight());

                right = new AndPredicate();
                ((AndPredicate<?>) right).setLeft(rightLeftNode);
                ((AndPredicate<?>) right).setRight(rightRightNode);

                AndPredicate and = new AndPredicate();
                and.setLeft(toConjunctiveNormalForm(left));
                and.setRight(toConjunctiveNormalForm(right));
                return and;

            }
            // ? ∨ (C ∧ D) => (? ∨ D) ∧ (? ∨ C)
            if ((right != null) && (right instanceof AndPredicate)) {
                AndPredicate<?> tmpRight = (AndPredicate<?>) right;
                IPredicate<?> tmpLeft = left;

                left = new OrPredicate();
                ((OrPredicate) left).setLeft(tmpLeft);
                ((OrPredicate) left).setRight(tmpRight.getRight());

                right = new OrPredicate();
                ((OrPredicate) right).setLeft(tmpLeft);
                ((OrPredicate) right).setRight(tmpRight.getLeft());

                AndPredicate and = new AndPredicate();
                and.setLeft(toConjunctiveNormalForm(left));
                and.setRight(toConjunctiveNormalForm(right));
                return and;
            }
            // (A ∧ B) ∨ ? => (A ∨ ?) ∧ (B ∨ ?)
            if ((left != null) && (left instanceof AndPredicate)) {
                AndPredicate<?> tmpLeft = (AndPredicate<?>) left;
                IPredicate<?> tmpRight = right;

                left = new OrPredicate();
                ((OrPredicate) left).setLeft(tmpLeft.getLeft());
                ((OrPredicate) left).setRight(tmpRight);

                right = new OrPredicate();
                ((OrPredicate) right).setLeft(tmpLeft.getRight());
                ((OrPredicate) right).setRight(tmpRight);

                AndPredicate and = new AndPredicate();
                and.setLeft(toConjunctiveNormalForm(left));
                and.setRight(toConjunctiveNormalForm(right));
                return and;

            }
            OrPredicate or = new OrPredicate();
            or.setLeft(left);
            or.setRight(right);
            return or;
        }
        return expression;
    }
}
