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
package de.uniol.inf.is.odysseus.server.intervalapproach.rewrite;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.DifferenceAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IntersectionAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.RewriteConfiguration;
import de.uniol.inf.is.odysseus.rewrite.flow.RewriteRuleFlowGroup;
import de.uniol.inf.is.odysseus.rewrite.rule.AbstractRewriteRule;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class RIntersectionAORule extends AbstractRewriteRule<IntersectionAO> {
    /** The Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(RIntersectionAORule.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPriority() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IntersectionAO operator, RewriteConfiguration config) throws RuleException {

    	throw new UnsupportedOperationException("Sorry. Intersection is currenlty not supported!");

//        ILogicalOperator childLeft = operator.getSubscribedToSource(0).getTarget();
//        ILogicalOperator childRight = operator.getSubscribedToSource(1).getTarget();
//
//        DifferenceAO left = insertDifferenceAO(operator, childLeft);
//        DifferenceAO right = insertDifferenceAO(operator, childRight);
//
//        left.subscribeToSource(childRight, 1, 0, childRight.getOutputSchema());
//        right.subscribeToSource(childLeft, 1, 0, childLeft.getOutputSchema());
//
//        UnionAO unionAO = insertUnionAO(operator);
//
//        left.initialize();
//        this.insert(left);
//        right.initialize();
//        this.insert(right);
//
//        final Collection<ILogicalOperator> toUpdate = LogicalPlan.removeOperator(operator, true);
//        for (final ILogicalOperator o : toUpdate) {
//            this.update(o);
//        }
//        this.update(unionAO);
//        unionAO.initialize();
//        this.retract(operator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExecutable(IntersectionAO operator, RewriteConfiguration config) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return RewriteRuleFlowGroup.DELETE;
    }

	@SuppressWarnings("unused")
    private UnionAO insertUnionAO(final ILogicalOperator parent) {
        final UnionAO unionAO = new UnionAO();
        for (final IOperatorOwner owner : parent.getOwner()) {
            unionAO.addOwner(owner);
        }

        final ILogicalOperator left = parent.getSubscribedToSource(0).getSource();
        final ILogicalOperator right = parent.getSubscribedToSource(1).getSource();

        final Collection<LogicalSubscription> leftSubs = left.getSubscriptions();
        for (final LogicalSubscription sub : leftSubs) {
            left.unsubscribeSink(sub);
            unionAO.subscribeSink(sub.getSink(), sub.getSinkInPort(), sub.getSourceOutPort(), sub.getSchema());
        }

        unionAO.subscribeToSource(left, 0, 0, left.getOutputSchema());

        final Collection<LogicalSubscription> rightSubs = right.getSubscriptions();
        for (final LogicalSubscription sub : rightSubs) {
            right.unsubscribeSink(sub);
            unionAO.subscribeSink(sub.getSink(), sub.getSinkInPort(), sub.getSourceOutPort(), sub.getSchema());
        }

        unionAO.subscribeToSource(right, 1, 0, right.getOutputSchema());
        RIntersectionAORule.LOG.debug("Insert union operator: {}", unionAO.toString());
        return unionAO;
    }

	@SuppressWarnings("unused")
    private DifferenceAO insertDifferenceAO(final ILogicalOperator parent, ILogicalOperator child) {
        final DifferenceAO differenceAO = new DifferenceAO();
        for (final IOperatorOwner owner : parent.getOwner()) {
            differenceAO.addOwner(owner);
        }

        final Collection<LogicalSubscription> subs = child.getSubscriptions();
        for (final LogicalSubscription sub : subs) {
            child.unsubscribeSink(sub);
            differenceAO.subscribeSink(sub.getSink(), sub.getSinkInPort(), sub.getSourceOutPort(), sub.getSchema());
        }

        differenceAO.subscribeToSource(child, 0, 0, child.getOutputSchema());
        RIntersectionAORule.LOG.debug("Insert difference operator: {}", differenceAO.toString());

        return differenceAO;
    }
}
