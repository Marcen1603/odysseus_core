package de.uniol.inf.is.odysseus.fusion.transform;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.fusion.logicaloperator.filter.SpatialFilterAO;
import de.uniol.inf.is.odysseus.fusion.physicaloperator.association.SpatialAssociationPO;


import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Kai Pancratz <kai@pancratz.net>
 */
public class TSpatialFilterAORule extends AbstractTransformationRule<SpatialFilterAO> {
    private static Logger LOG = LoggerFactory.getLogger(TSpatialFilterAORule.class);

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#execute(java.lang.Object,
     * java.lang.Object)
     */
    @Override
    public void execute(final SpatialFilterAO operator, final TransformationConfiguration config) {
        try {
            final SpatialAssociationPO po = new SpatialAssociationPO(operator.getOutputSchema());
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
        	TSpatialFilterAORule.LOG.error(e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getName()
     */
    @Override
    public String getName() {
        return "SpatialFilterAO -> SpatialFilterPO";
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
    public boolean isExecutable(final SpatialFilterAO operator, final TransformationConfiguration config) {
        return operator.isAllPhysicalInputSet();
    }

    @Override
    public Class<?> getConditionClass() {
        return SpatialFilterAO.class;
    }
}
