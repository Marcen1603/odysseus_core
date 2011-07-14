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
    public void execute(SourceAO operator, TransformationConfiguration config) {
        try {
            final SourcePO<?> po = new SourcePO(operator.getOutputSchema(), operator.getAdapter(),
                    operator.getOptionsMap());
            final Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(
                    operator, po);
            for (final ILogicalOperator o : toUpdate) {
                this.update(o);
            }
            this.retract(operator);
        }
        catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public boolean isExecutable(SourceAO operator, TransformationConfiguration config) {
        return operator.isAllPhysicalInputSet();
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
