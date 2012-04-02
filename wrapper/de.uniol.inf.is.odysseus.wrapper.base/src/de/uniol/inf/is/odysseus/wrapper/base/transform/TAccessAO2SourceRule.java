package de.uniol.inf.is.odysseus.wrapper.base.transform;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
import de.uniol.inf.is.odysseus.wrapper.base.physicaloperator.SourcePO;
import de.uniol.inf.is.odysseus.wrapper.base.pool.SourcePool;

public class TAccessAO2SourceRule extends AbstractTransformationRule<AccessAO> {
    private static Logger LOG = LoggerFactory.getLogger(TAccessAO2SourceRule.class);

    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    public void execute(final AccessAO operator, final TransformationConfiguration config) {
        try {
            @SuppressWarnings({ "rawtypes", "unchecked" })
			SourcePO<?> po = new SourcePO(operator.getOutputSchema(), operator.getAdapter(),
                    operator.getOptionsMap());
//            if (SourcePool.hasSemanticallyEqualSource(po)) {
//                po = SourcePool.getSemanticallyEqualSource(po);
//            }else {
                SourcePool.registerSource(operator.getAdapter(), po, operator.getOptionsMap());
//            }
            final Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(
                    operator, po);
            for (final ILogicalOperator o : toUpdate) {
                this.update(o);
            }
            retract(operator);
            insert(po);
        }
        catch (final Exception e) {
            TAccessAO2SourceRule.LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public boolean isExecutable(final AccessAO operator, final TransformationConfiguration config) {
        return (operator.getAdapter() != null && !"GoogleProtBuf".equalsIgnoreCase(operator.getAdapter()) );
    }

    @Override
    public String getName() {
        return "AccessAO -> SourcePO";
    }

    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.ACCESS;
    }

}
