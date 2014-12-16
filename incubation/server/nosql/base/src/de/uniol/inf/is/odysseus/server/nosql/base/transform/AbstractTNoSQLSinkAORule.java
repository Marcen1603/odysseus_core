package de.uniol.inf.is.odysseus.server.nosql.base.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.nosql.base.logicaloperator.AbstractNoSQLSinkAO;
import de.uniol.inf.is.odysseus.server.nosql.base.physicaloperator.AbstractNoSQLSinkPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Erstellt von RoBeaT
 * Date: 15.12.2014
 */
public abstract class AbstractTNoSQLSinkAORule<A extends AbstractNoSQLSinkAO> extends AbstractTransformationRule<A> {

    protected Class logicalOperatorClass;
    protected Class physicalOperatorClass;

    @Override
    public final void execute(AbstractNoSQLSinkAO logicalOperator, TransformationConfiguration config) throws RuleException {

        AbstractNoSQLSinkPO physical;

        try {
            physical = (AbstractNoSQLSinkPO) physicalOperatorClass.getDeclaredConstructor(logicalOperatorClass).newInstance(logicalOperator);
        } catch (Exception e) {
           throw new RuleException(e);
        }

        defaultExecute(logicalOperator, physical, config, true, true);
    }

    @Override
    public boolean isExecutable(A operator, TransformationConfiguration config) {
//        return operator.isAllPhysicalInputSet();
        return true;
    }

    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.TRANSFORMATION;
    }

    @Override
    public String getName() {
        return logicalOperatorClass.getSimpleName() + " -> " + physicalOperatorClass.getSimpleName();
    }

    @Override
    public Class<? super A> getConditionClass() {
        return logicalOperatorClass;
    }

}
