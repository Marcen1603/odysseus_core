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

import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.mep.functions.bool.AndOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.NotOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.OrOperator;

/**
 * Rule to transform a given expression into the conjunctive normal form (CNF)
 * Based on
 * http://csliu.com/2009/05/convert-arbitrary-bool-expression-into-conjunctive
 * -normal-form/
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id: ConjunctiveNormalFormRule.java | ConjunctiveNormalFormRule.java
 *          | ConjunctiveNormalFormRule.java $
 *
 */
public class ConjunctiveNormalFormRule extends AbstractExpressionOptimizerRule<IExpression<?>> {
    private final DeMorganRule deMorganRule = new DeMorganRule();

    /**
     * {@inheritDoc}
     */
    @Override
    public IExpression<?> execute(IExpression<?> expression) {
        return toConjunctiveNormalForm(expression);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExecutable(IExpression<?> expression) {
        return true;
    }

    private IExpression<?> toConjunctiveNormalForm(IExpression<?> expression) {
        if (!isBooleanOperator(expression)) {
            return expression;
        }
        if (expression instanceof NotOperator) {
            IExpression<?> leaf = expression.toFunction().getArgument(0);
            if ((leaf.isConstant()) || (leaf.isVariable())) {
                return expression;
            }
            if (leaf instanceof NotOperator) {
                return toConjunctiveNormalForm(leaf.toFunction().getArgument(0));
            }
            IExpression<?> deMorgan = this.deMorganRule.executeRule(expression);
            return toConjunctiveNormalForm(deMorgan);
        }

        IExpression<?> left = expression.toFunction().getArgument(0);
        if (left != null) {
            left = toConjunctiveNormalForm(left);
        }
        IExpression<?> right = expression.toFunction().getArgument(1);
        if (right != null) {
            right = toConjunctiveNormalForm(right);
        }
        if (expression instanceof AndOperator) {
            AndOperator and = new AndOperator();
            and.setArguments(new IExpression<?>[] { left, right });
            return and;
        }
        if (expression instanceof OrOperator) {
            // (A ∧ B) ∨ (C ∧ D) => (A ∨ C) ∧ (B ∨ C) ∧ (A ∨ D) ∧ (B ∨ D)
            if ((left != null) && (left instanceof AndOperator) && (right != null) && (right instanceof AndOperator)) {
                IFunction<?> tmpLeft = left.toFunction();
                IFunction<?> tmpRight = right.toFunction();

                OrOperator leftLeftNode = new OrOperator();
                leftLeftNode.setArguments(new IExpression<?>[] { tmpLeft.toFunction().getArgument(0), tmpRight.toFunction().getArgument(0) });
                OrOperator leftRightNode = new OrOperator();
                leftRightNode.setArguments(new IExpression<?>[] { tmpLeft.toFunction().getArgument(1), tmpRight.toFunction().getArgument(0) });
                left = new AndOperator();
                left.toFunction().setArguments(new IExpression<?>[] { leftLeftNode, leftRightNode });

                OrOperator rightLeftNode = new OrOperator();
                rightLeftNode.setArguments(new IExpression<?>[] { tmpLeft.toFunction().getArgument(0), tmpRight.toFunction().getArgument(1) });
                OrOperator rightRightNode = new OrOperator();
                rightRightNode.setArguments(new IExpression<?>[] { tmpLeft.toFunction().getArgument(1), tmpRight.toFunction().getArgument(1) });
                right = new AndOperator();
                right.toFunction().setArguments(new IExpression<?>[] { rightLeftNode, rightRightNode });

                AndOperator and = new AndOperator();
                and.setArguments(new IExpression<?>[] { toConjunctiveNormalForm(left), toConjunctiveNormalForm(right) });
                return and;

            }
            // ? ∨ (C ∧ D) => (? ∨ D) ∧ (? ∨ C)
            if ((right != null) && (right instanceof AndOperator)) {
                IFunction<?> tmpRight = right.toFunction();
                IExpression<?> tmpLeft = left;

                left = new OrOperator();
                left.toFunction().setArguments(new IExpression<?>[] { tmpLeft, tmpRight.toFunction().getArgument(1) });

                right = new OrOperator();
                right.toFunction().setArguments(new IExpression<?>[] { tmpLeft, tmpRight.toFunction().getArgument(0) });

                AndOperator and = new AndOperator();
                and.setArguments(new IExpression<?>[] { toConjunctiveNormalForm(left), toConjunctiveNormalForm(right) });
                return and;
            }
            // (A ∧ B) ∨ ? => (A ∨ ?) ∧ (B ∨ ?)
            if ((left != null) && (left instanceof AndOperator)) {
                IFunction<?> tmpLeft = left.toFunction();
                IExpression<?> tmpRight = right;

                left = new OrOperator();
                left.toFunction().setArguments(new IExpression<?>[] { tmpLeft.toFunction().getArgument(0), tmpRight });

                right = new OrOperator();
                right.toFunction().setArguments(new IExpression<?>[] { tmpLeft.toFunction().getArgument(1), tmpRight });

                AndOperator and = new AndOperator();
                and.setArguments(new IExpression<?>[] { toConjunctiveNormalForm(left), toConjunctiveNormalForm(right) });
                return and;

            }
            OrOperator or = new OrOperator();
            or.setArguments(new IExpression<?>[] { left, right });
            return or;
        }
        return expression;
    }

}
