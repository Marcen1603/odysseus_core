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
package cc.kuka.odysseus.image.transform.rules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.kuka.odysseus.image.logicaloperator.EyeTrackerAO;
import cc.kuka.odysseus.image.physicaloperator.EyeTrackerPO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * Transformation rule for the {@link EyeTrackerAO}.
 *
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class TEyeTrackerAORule extends AbstractTransformationRule<EyeTrackerAO> {
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(TEyeTrackerAORule.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(final EyeTrackerAO ao, final TransformationConfiguration config) throws RuleException {
        final EyeTrackerPO<Tuple<?>> po = new EyeTrackerPO<>(ao);
        this.defaultExecute(ao, po, config, true, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExecutable(final EyeTrackerAO operator, final TransformationConfiguration config) {
        return operator.isAllPhysicalInputSet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.TRANSFORMATION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? super EyeTrackerAO> getConditionClass() {
        return EyeTrackerAO.class;
    }
}
