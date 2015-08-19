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
package cc.kuka.odysseus.wrapper.ldap.transform;

import cc.kuka.odysseus.wrapper.ldap.logicaloperator.LDAPSinkAO;
import cc.kuka.odysseus.wrapper.ldap.physicaloperator.LDAPSinkPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class TLDAPSinkAORule extends AbstractTransformationRule<LDAPSinkAO> {

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public void execute(LDAPSinkAO accessAO, TransformationConfiguration config) throws RuleException {
        LDAPSinkPO accessPO = new LDAPSinkPO(accessAO.getBase(), accessAO.getOutputSchema(), accessAO.getEnvironment());
        defaultExecute(accessAO, accessPO, config, true, true);
    }

    @Override
    public boolean isExecutable(LDAPSinkAO operator, TransformationConfiguration config) {
        return operator.isAllPhysicalInputSet();
    }

    @Override
    public String getName() {
        return "LDAPSinkAO -> LDAPSinkPO";
    }

    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.TRANSFORMATION;
    }

    @Override
    public Class<? super LDAPSinkAO> getConditionClass() {
        return LDAPSinkAO.class;
    }

}
