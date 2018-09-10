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
import java.util.Set;

import de.uniol.inf.is.odysseus.core.predicate.AndPredicate;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.predicate.NotPredicate;
import de.uniol.inf.is.odysseus.core.predicate.OrPredicate;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class DeMorganRule extends AbstractPredicateOptimizerRule<NotPredicate<?>> {

    /**
     * {@inheritDoc}
     */
    @Override
    public IPredicate<?> execute(NotPredicate<?> expression) {
        IPredicate<?> leaf = expression.getChild();

        if (leaf instanceof AndPredicate) {
            Set<IPredicate<?>> split = getConjunctiveSplit(leaf);
            List<IPredicate<?>> clauses = new ArrayList<>();
            for (IPredicate<?> literal : split) {
                @SuppressWarnings({ "rawtypes", "unchecked" })
                NotPredicate<?> operator = new NotPredicate(literal);
                clauses.add(operator);
            }
            return disjunction(clauses);
        }
        if (leaf instanceof OrPredicate) {
            Set<IPredicate<?>> split = getDisjunctiveSplit(leaf);
            List<IPredicate<?>> clauses = new ArrayList<>();
            for (IPredicate<?> literal : split) {
                @SuppressWarnings({ "rawtypes", "unchecked" })
                NotPredicate<?> operator = new NotPredicate(literal);
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
    public boolean isExecutable(IPredicate<?> expression) {
        return expression instanceof NotPredicate;
    }

}
