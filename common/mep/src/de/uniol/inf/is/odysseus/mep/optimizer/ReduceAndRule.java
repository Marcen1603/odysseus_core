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

import java.util.Set;

import de.uniol.inf.is.odysseus.core.mep.Constant;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.functions.bool.AndOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.NotOperator;

/**
 * * Apply the complement operation to an expression.
 * This rule will replace complements in an Or expression by a constant False
 * value:
 * 
 * A && !A => False
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class ReduceAndRule extends AbstractExpressionOptimizerRule<AndOperator> {

    /**
     * {@inheritDoc}
     */
    @Override
    public IExpression<?> execute(AndOperator expression) {
        Set<IExpression<?>> split = getConjunctiveSplit(expression);
        for (IExpression<?> expr : split) {
            if (expr instanceof NotOperator) {
                IExpression<?> notChild = expr.toFunction().getArgument(0);
                for (IExpression<?> child : split) {
                    if (child.equals(notChild)) {
                        return new Constant<>(Boolean.FALSE, SDFDatatype.BOOLEAN);
                    }
                }
            }
        }
        return expression;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExecutable(IExpression<?> expression) {
        return expression instanceof AndOperator;
    }

}
