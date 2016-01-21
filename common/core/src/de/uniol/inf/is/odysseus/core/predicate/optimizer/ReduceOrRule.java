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

import de.uniol.inf.is.odysseus.core.predicate.FalsePredicate;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.predicate.NotPredicate;
import de.uniol.inf.is.odysseus.core.predicate.NullPredicate;
import de.uniol.inf.is.odysseus.core.predicate.OrPredicate;
import de.uniol.inf.is.odysseus.core.predicate.TruePredicate;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class ReduceOrRule extends AbstractPredicateOptimizerRule<OrPredicate<?>> {

    /**
     * {@inheritDoc}
     */
    @Override
    public IPredicate<?> execute(OrPredicate<?> expression) {
        IPredicate<?> left = expression.getLeft();
        IPredicate<?> right = expression.getRight();
        if (left instanceof NotPredicate) {
            if (((NotPredicate<?>) left).getChild().equals(right)) {
                return TruePredicate.getInstance();
            }
        }
        if (right instanceof NotPredicate) {
            if (((NotPredicate<?>) right).getChild().equals(left)) {
                return TruePredicate.getInstance();
            }
        }
        if (left.toString().equals(right.toString())) {
            return left;
        }
        if (left instanceof FalsePredicate) {
            return right;
        }
        if (right instanceof FalsePredicate) {
            return left;
        }
        if (left instanceof TruePredicate) {
            return TruePredicate.getInstance();
        }
        if (right instanceof TruePredicate) {
            return TruePredicate.getInstance();
        }
        if ((left.isAlwaysTrue()) || (right.isAlwaysTrue())) {
            return TruePredicate.getInstance();
        }
		if ((left instanceof NullPredicate) && (right instanceof NullPredicate)) {
			return NullPredicate.getInstance();
		}
        return expression;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExecutable(IPredicate<?> expression) {
        return expression instanceof OrPredicate;

    }

}
