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
 * Rule to transform a given expression into the disjunctive normal form (DNF)
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class DisjunctiveNormalFormRule extends AbstractExpressionOptimizerRule<AndOperator> {

    /**
     * {@inheritDoc}
     */
    @Override
    public IExpression<?> execute(AndOperator expression) {
        Set<IExpression<?>> split = getConjunctiveSplit(expression);
        for (IExpression<?> conjunctiveClause : split) {
            if (conjunctiveClause instanceof OrOperator) {
                List<IExpression<?>> clauses = new ArrayList<>();
                Set<IExpression<?>> children = new HashSet<>();
                children.addAll(split);
                children.remove(conjunctiveClause);
                for (IExpression<?> disjunctiveClause : getDisjunctiveSplit(conjunctiveClause)) {
                    List<IExpression<?>> literals = new ArrayList<>();
                    literals.addAll(children);
                    literals.add(disjunctiveClause);
                    clauses.add(conjunction(literals));
                }
                return disjunction(clauses);
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
