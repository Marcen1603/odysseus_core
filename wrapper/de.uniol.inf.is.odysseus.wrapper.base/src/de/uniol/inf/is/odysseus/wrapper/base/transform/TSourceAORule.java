package de.uniol.inf.is.odysseus.wrapper.base.transform;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
import de.uniol.inf.is.odysseus.wrapper.base.logicaloperator.SourceAO;
import de.uniol.inf.is.odysseus.wrapper.base.physicaloperator.SourcePO;

public class TSourceAORule extends AbstractTransformationRule<SourceAO> {
    private static Logger LOG = LoggerFactory.getLogger(TSourceAORule.class);

    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    public void execute(final SourceAO operator, final TransformationConfiguration config) {
        try {
            final SourcePO<?> po = new SourcePO(operator.getOutputSchema(), operator.getAdapter(),
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
            TSourceAORule.LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public boolean isExecutable(final SourceAO operator, final TransformationConfiguration config) {
        return true;
    }

    @Override
    public String getName() {
        return "SourceAO -> SourcePO";
    }

    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.ACCESS;
    }

}
