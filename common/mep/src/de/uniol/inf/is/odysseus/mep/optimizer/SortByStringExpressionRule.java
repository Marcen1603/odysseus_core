/*******************************************************************************
 * Copyright (C) 2015  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.uniol.inf.is.odysseus.mep.optimizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.mep.functions.bool.AndOperator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @author Marco Grawunder
 * @version $Id$
 *
 */
public class SortByStringExpressionRule extends AbstractExpressionOptimizerRule<AndOperator> {

    /**
     * {@inheritDoc}
     */
    @Override
    public IMepExpression<?> execute(AndOperator expression) {
        List<IMepExpression<?>> split = new ArrayList<>();
        split.addAll(getConjunctiveSplit(expression));
        Collections.sort(split, new Comparator<IMepExpression<?>>() {
            /**
             * 
             * {@inheritDoc}
             */
            @Override
            public int compare(IMepExpression<?> o1, IMepExpression<?> o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });

        return conjunction(split);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExecutable(IMepExpression<?> expression) {
        return expression instanceof AndOperator;
    }
}
