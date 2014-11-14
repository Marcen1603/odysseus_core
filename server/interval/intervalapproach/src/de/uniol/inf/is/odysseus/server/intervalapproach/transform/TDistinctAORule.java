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
package de.uniol.inf.is.odysseus.server.intervalapproach.transform;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.DistinctAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.DistinctTIPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class TDistinctAORule extends AbstractIntervalTransformationRule<DistinctAO> {
    @Override
    public void execute(DistinctAO operator, TransformationConfiguration configuration) throws RuleException {
        DefaultTISweepArea<IStreamObject<ITimeInterval>> sweepArea = new DefaultTISweepArea<>();
        DistinctTIPO<IStreamObject<ITimeInterval>> po = new DistinctTIPO<>(sweepArea);
        defaultExecute(operator, po, configuration, true, true);
    }

    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.TRANSFORMATION;
    }

    @Override
    public Class<? super DistinctAO> getConditionClass() {
        return DistinctAO.class;
    }
}
