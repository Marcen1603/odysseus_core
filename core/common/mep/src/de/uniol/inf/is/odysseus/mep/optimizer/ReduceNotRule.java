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

import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.mep.functions.bool.NotOperator;

/**
 * * Apply the complement operation to an expression.
 * This rule will remove double negation in a Not expression
 * value:
 * 
 * !!A => A
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class ReduceNotRule extends AbstractExpressionOptimizerRule<NotOperator> {

    /**
     * {@inheritDoc}
     */
    @Override
    public IMepExpression<?> execute(NotOperator expression) {
        IMepExpression<?> child = expression.toFunction().getArgument(0);
        if (child instanceof NotOperator) {
            return child.toFunction().getArgument(0);
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
