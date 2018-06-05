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
import java.util.Set;

import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.mep.functions.bool.AndOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.NotOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.OrOperator;

/**
 * Apply the De Morgan rules to an expression.
 * 
 * !(A || B) => !A && !B
 * !(A && B) => !A || !B
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class DeMorganRule extends AbstractExpressionOptimizerRule<NotOperator> {

    /**
     * {@inheritDoc}
     */
    @Override
    public IMepExpression<?> execute(NotOperator expression) {
        IMepExpression<?> leaf = expression.toFunction().getArgument(0);

        if (leaf instanceof AndOperator) {
            Set<IMepExpression<?>> split = getConjunctiveSplit(leaf);
            List<IMepExpression<?>> clauses = new ArrayList<>();
            for (IMepExpression<?> literal : split) {
                NotOperator operator = new NotOperator();
                operator.setArguments(new IMepExpression<?>[] { literal });
                clauses.add(operator);
            }
            return disjunction(clauses);
        }
        if (leaf instanceof OrOperator) {
            Set<IMepExpression<?>> split = getDisjunctiveSplit(leaf);
            List<IMepExpression<?>> clauses = new ArrayList<>();
            for (IMepExpression<?> literal : split) {
                NotOperator operator = new NotOperator();
                operator.setArguments(new IMepExpression<?>[] { literal });
                clauses.add(operator);
            }
            return conjunction(clauses);
        }
        return expression;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExecutable(IMepExpression<?> expression) {
        return expression instanceof NotOperator;
    }

}
