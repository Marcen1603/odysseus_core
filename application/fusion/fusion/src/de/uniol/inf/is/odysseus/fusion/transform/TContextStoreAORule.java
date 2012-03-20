package de.uniol.inf.is.odysseus.fusion.transform;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.fusion.logicaloperator.context.ContextStoreAO;
import de.uniol.inf.is.odysseus.fusion.physicaloperator.context.ContextStorePO;

import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Kai Pancratz <kai@pancratz.net>
 */
public class TContextStoreAORule extends AbstractTransformationRule<ContextStoreAO> {
    private static Logger LOG = LoggerFactory.getLogger(TContextStoreAORule.class);

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#execute(java.lang.Object,
     * java.lang.Object)
     */
    @Override
    public void execute(final ContextStoreAO operator, final TransformationConfiguration config) {
        try {
            final ContextStorePO po = new ContextStorePO(operator.getOutputSchema());
            po.setOutputSchema(operator.getOutputSchema());
            final Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(
                    operator, po);
            for (final ILogicalOperator o : toUpdate) {
                this.update(o);
            }
            this.replace(operator, po, config);
            this.retract(operator);

        }
        catch (final Exception e) {
        	TContextStoreAORule.LOG.error(e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getName()
     */
    @Override
    public String getName() {
        return "ContextStoreAO -> ContextStorePO";
    }

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getPriority()
     */
    @Override
    public int getPriority() {
        return 0;
    }

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getRuleFlowGroup()
     */
    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.TRANSFORMATION;
    }

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#isExecutable(java.lang.Object,
     * java.lang.Object)
     */
    @Override
    public boolean isExecutable(final ContextStoreAO operator,
            final TransformationConfiguration config) {
        return operator.isAllPhysicalInputSet();
    }

    @Override
    public Class<?> getConditionClass() {
        return ContextStoreAO.class;
    }
}
