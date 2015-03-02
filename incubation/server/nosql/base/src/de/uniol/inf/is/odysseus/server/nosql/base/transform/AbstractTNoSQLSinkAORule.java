package de.uniol.inf.is.odysseus.server.nosql.base.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.nosql.base.logicaloperator.AbstractNoSQLSinkAO;
import de.uniol.inf.is.odysseus.server.nosql.base.physicaloperator.AbstractNoSQLSinkPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * Erstellt von RoBeaT
 * Date: 15.12.2014
 */
public abstract class AbstractTNoSQLSinkAORule<A extends AbstractNoSQLSinkAO> extends AbstractTransformationRule<A> {

    @SuppressWarnings("rawtypes")
	private Class logicalOperatorClass;
    @SuppressWarnings("rawtypes")
	private Class physicalOperatorClass;

    public AbstractTNoSQLSinkAORule() {
        logicalOperatorClass = getLogicalOperatorClass();
        physicalOperatorClass = getPhysicalOperatorClass();
    }

    @SuppressWarnings("unchecked")
	@Override
    public final void execute(AbstractNoSQLSinkAO logicalOperator, TransformationConfiguration config) throws RuleException {

        AbstractNoSQLSinkPO physical;

        try {
            // creates a new instance of the specified physicalOperatorClass with the logicalOperatorClass as parameter
            // noinspection unchecked
            physical = (AbstractNoSQLSinkPO) physicalOperatorClass.getDeclaredConstructor(logicalOperatorClass).newInstance(logicalOperator);
        } catch (Exception e) {
           throw new RuleException(e);
        }

        defaultExecute(logicalOperator, physical, config, true, true);
    }

    protected abstract Class<? extends AbstractNoSQLSinkAO> getLogicalOperatorClass();

    protected abstract Class<? extends AbstractNoSQLSinkPO> getPhysicalOperatorClass();

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

    @SuppressWarnings("unchecked")
    @Override
    public Class<? super A> getConditionClass() {
        return logicalOperatorClass;
    }
}
