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

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public interface IExpressionOptimizerRule<E extends IMepExpression<?>> {
    /**
     * Execute the given rule. This method applies the the rule only if it is
     * applicable.
     * 
     * @param expression
     *            The expression
     * @return The resulting expression after applying the rule
     */
    IMepExpression<?> executeRule(IMepExpression<?> expression);

    /**
     * Execute the given rule.
     * 
     * @param expression
     *            The expression
     * @return The resulting expression after applying the rule
     */
    IMepExpression<?> execute(E expression);

    /**
     * 
     * @param expression
     *            The expression
     * @return <code>true</code> if the given rule can be applied
     */
    boolean isExecutable(IMepExpression<?> expression);
}
