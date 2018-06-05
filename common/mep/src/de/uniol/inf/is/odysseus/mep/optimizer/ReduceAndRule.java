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
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.functions.bool.AndOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.NotOperator;
import de.uniol.inf.is.odysseus.mep.intern.Constant;

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
    public IMepExpression<?> execute(AndOperator expression) {
        IMepExpression<?> left = expression.getArgument(0);
        IMepExpression<?> right = expression.getArgument(1);
        if (left instanceof NotOperator) {
            if (left.toFunction().getArgument(0).equals(right)) {
                return new Constant<>(Boolean.FALSE, SDFDatatype.BOOLEAN);
            }
        }
        if (right instanceof NotOperator) {
            if (right.toFunction().getArgument(0).equals(left)) {
                return new Constant<>(Boolean.FALSE, SDFDatatype.BOOLEAN);
            }
        }
        if (left.toString().equals(right.toString())) {
            return left;
        }
		if ((left.isConstant()) && (left.toConstant().getValue() != null)
				&& (left.toConstant().getValue().equals(Boolean.FALSE))) {
            return new Constant<>(Boolean.FALSE, SDFDatatype.BOOLEAN);
        }
		if ((right.isConstant()) && (right.toConstant().getValue() != null)
				&& (right.toConstant().getValue().equals(Boolean.FALSE))) {
            return new Constant<>(Boolean.FALSE, SDFDatatype.BOOLEAN);
        }
		if ((left.isConstant()) && (left.toConstant().getValue() != null)
				&& (left.toConstant().getValue().equals(Boolean.TRUE))) {
            return right;
        }
		if ((right.isConstant()) && (right.toConstant().getValue() != null)
				&& (right.toConstant().getValue().equals(Boolean.TRUE))) {
            return left;
        }
		if ((right.isConstant()) && (right.toConstant().getValue() == null) && (left.isConstant())
				&& (left.toConstant().getValue() == null)) {
			return new Constant<>(null, SDFDatatype.BOOLEAN);
		}
        return expression;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExecutable(IMepExpression<?> expression) {
        return expression instanceof AndOperator;
    }

}
