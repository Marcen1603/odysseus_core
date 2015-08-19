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

import cc.kuka.odysseus.wrapper.ldap.logicaloperator.LDAPSourceAO;
import cc.kuka.odysseus.wrapper.ldap.physicaloperator.LDAPSourcePO;
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
public class TLDAPSourceAORule extends AbstractTransformationRule<LDAPSourceAO> {

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public void execute(LDAPSourceAO accessAO, TransformationConfiguration config) throws RuleException {
        LDAPSourcePO accessPO = new LDAPSourcePO(accessAO.getBase(), accessAO.getFilter(), accessAO.getOutputSchema(), accessAO.getEnvironment());
        defaultExecute(accessAO, accessPO, config, true, true);
    }

    @Override
    public boolean isExecutable(LDAPSourceAO operator, TransformationConfiguration config) {
        return true;
    }

    @Override
    public String getName() {
        return "LDAPSourceAO -> LDAPSourcePO";
    }

    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.ACCESS;
    }

    @Override
    public Class<? super LDAPSourceAO> getConditionClass() {
        return LDAPSourceAO.class;
    }

}
