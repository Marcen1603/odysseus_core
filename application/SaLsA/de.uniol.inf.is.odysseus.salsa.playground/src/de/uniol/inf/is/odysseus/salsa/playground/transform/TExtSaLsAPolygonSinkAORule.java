package de.uniol.inf.is.odysseus.salsa.playground.transform;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.salsa.playground.logicaloperator.ExtSaLsAPolygonSinkAO;
import de.uniol.inf.is.odysseus.salsa.playground.physicaloperator.ExtSaLsAPolygonSinkPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Kai Pancratz <kai@pancratz.net>
 */
public class TExtSaLsAPolygonSinkAORule extends AbstractTransformationRule<ExtSaLsAPolygonSinkAO> {
    private static Logger LOG = LoggerFactory.getLogger(TExtSaLsAPolygonSinkAORule.class);

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#execute(java.lang.Object,
     * java.lang.Object)
     */
    @Override
    public void execute(final ExtSaLsAPolygonSinkAO operator, final TransformationConfiguration config) {
        try {
            final ExtSaLsAPolygonSinkPO po = new ExtSaLsAPolygonSinkPO(operator.getOutputSchema());
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
        	TExtSaLsAPolygonSinkAORule.LOG.error(e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getName()
     */
    @Override
    public String getName() {
        return "VExtSaLsAPolygonSinkAO -> ExtSaLsAPolygonSinkPO";
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
    public boolean isExecutable(final ExtSaLsAPolygonSinkAO operator,
            final TransformationConfiguration config) {
        return operator.isAllPhysicalInputSet();
    }

    @Override
    public Class<?> getConditionClass() {
        return ExtSaLsAPolygonSinkAO.class;
    }
}
