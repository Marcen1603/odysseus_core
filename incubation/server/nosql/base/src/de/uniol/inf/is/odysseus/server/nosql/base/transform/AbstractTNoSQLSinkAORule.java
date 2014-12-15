package de.uniol.inf.is.odysseus.server.nosql.base.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.nosql.base.logicaloperator.AbstractNoSQLSinkAO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * Erstellt von RoBeaT
 * Date: 15.12.2014
 */
public abstract class AbstractTNoSQLSinkAORule<A extends AbstractNoSQLSinkAO> extends AbstractTransformationRule<A> {
	
    @Override
    public boolean isExecutable(A operator, TransformationConfiguration config) {
//        return operator.isAllPhysicalInputSet();
        return true;
    }

    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.TRANSFORMATION;
    }

}
