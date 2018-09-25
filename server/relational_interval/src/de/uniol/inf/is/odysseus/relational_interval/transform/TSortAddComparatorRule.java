/*******************************************************************************
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
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
package de.uniol.inf.is.odysseus.relational_interval.transform;

import java.util.Comparator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.SortTIPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class TSortAddComparatorRule extends AbstractTransformationRule<SortTIPO<Tuple<? extends ITimeInterval>>> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(SortTIPO<Tuple<? extends ITimeInterval>> operator, TransformationConfiguration config) throws RuleException {
        final int[] sortAttributePos = operator.getSortAttributePos();
        final boolean[] ascending = operator.getAscending();
        Comparator<Tuple<? extends ITimeInterval>> comparator = new Comparator<Tuple<? extends ITimeInterval>>() {

            @SuppressWarnings({ "unchecked", "rawtypes" })
            @Override
            public int compare(Tuple<? extends ITimeInterval> o1, Tuple<? extends ITimeInterval> o2) {
                int sum = 0;
                for (int i = 0; i < sortAttributePos.length; i++) {
                    int pos = sortAttributePos[i];
                    sum += ((Comparable) o1.getAttribute(pos)).compareTo(o2.getAttribute(pos)) * (ascending[i] ? 1 : -1);
                }
                return sum;
            }
        };
        operator.setComparator(comparator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExecutable(SortTIPO<Tuple<? extends ITimeInterval>> operator, TransformationConfiguration config) {
        if (operator.getOutputSchema().getType().isAssignableFrom(Tuple.class)) {
            if (operator.getComparator() == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.METAOBJECTS;
    }

}
