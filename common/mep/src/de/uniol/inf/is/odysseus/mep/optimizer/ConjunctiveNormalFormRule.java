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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.mep.functions.bool.AndOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.OrOperator;

/**
 * Rule to transform a given expression into the conjunctive normal form (CNF)
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class ConjunctiveNormalFormRule extends AbstractExpressionOptimizerRule<OrOperator> {

    /**
     * {@inheritDoc}
     */
    @Override
    public IExpression<?> execute(OrOperator expression) {
        Set<IExpression<?>> split = getDisjunctiveSplit(expression);
        for (IExpression<?> disjunctiveClause : split) {
            if (disjunctiveClause instanceof AndOperator) {
                List<IExpression<?>> clauses = new ArrayList<>();
                Set<IExpression<?>> children = new HashSet<>();
                children.addAll(split);
                children.remove(disjunctiveClause);
                for (IExpression<?> conjunctiveClause : getConjunctiveSplit(disjunctiveClause)) {
                    List<IExpression<?>> literals = new ArrayList<>();
                    literals.addAll(children);
                    literals.add(conjunctiveClause);
                    clauses.add(disjunction(literals));
                }
                return conjunction(clauses);
            }
        }
        return expression;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExecutable(IExpression<?> expression) {
        return expression instanceof OrOperator;
    }

}
