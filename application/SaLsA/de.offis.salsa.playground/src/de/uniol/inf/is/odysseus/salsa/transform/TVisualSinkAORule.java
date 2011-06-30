package de.uniol.inf.is.odysseus.salsa.transform;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.salsa.logicaloperator.VisualSinkAO;
import de.uniol.inf.is.odysseus.salsa.physicaloperator.VisualSinkPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 *
 */
public class TVisualSinkAORule extends AbstractTransformationRule<VisualSinkAO> {
    private static Logger LOG = LoggerFactory.getLogger(TVisualSinkAORule.class);

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#execute(java.lang.Object,
     * java.lang.Object)
     */
    @Override
    public void execute(final VisualSinkAO operator, final TransformationConfiguration config) {
        final VisualSinkPO<?> po = new VisualSinkPO(operator.getOutputSchema());
        final Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(
                operator, po);
        for (final ILogicalOperator o : toUpdate) {
            this.update(o);
        }
        this.retract(operator);

    }

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getName()
     */
    @Override
    public String getName() {
        return "VisualSinkAO -> VisualSinkPO";
    }

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getPriority()
     */
    @Override
    public int getPriority() {
        return 10;
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
    public boolean isExecutable(final VisualSinkAO operator,
            final TransformationConfiguration config) {
        return operator.isAllPhysicalInputSet();
    }
}
