package de.uniol.inf.is.odysseus.wrapper.base.transform;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
import de.uniol.inf.is.odysseus.wrapper.base.logicaloperator.SinkAO;
import de.uniol.inf.is.odysseus.wrapper.base.physicaloperator.SinkPO;

public class TSinkAORule extends AbstractTransformationRule<SinkAO> {
    private static Logger LOG = LoggerFactory.getLogger(TSinkAORule.class);

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public void execute(final SinkAO operator, final TransformationConfiguration config) {
        try {
            final SinkPO<?> po = new SinkPO(operator.getOutputSchema(), operator.getAdapter(),
                    operator.getOptionsMap());
            final Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(
                    operator, po);
            for (final ILogicalOperator o : toUpdate) {
                this.update(o);
            }
            retract(operator);
            insert(po);
        }
        catch (final Exception e) {
            TSinkAORule.LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public boolean isExecutable(final SinkAO operator, final TransformationConfiguration config) {
        return operator.isAllPhysicalInputSet();
    }

    @Override
    public String getName() {
        return "SinkAO -> SinkPO";
    }

    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.TRANSFORMATION;
    }

}
