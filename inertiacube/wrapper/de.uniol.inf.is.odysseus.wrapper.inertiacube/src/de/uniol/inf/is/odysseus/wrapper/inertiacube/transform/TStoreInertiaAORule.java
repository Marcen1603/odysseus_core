package de.uniol.inf.is.odysseus.wrapper.inertiacube.transform;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
import de.uniol.inf.is.odysseus.wrapper.inertiacube.logicaloperator.StoreInertiaAO;
import de.uniol.inf.is.odysseus.wrapper.inertiacube.physicaloperator.StoreInertiaPO;

public class TStoreInertiaAORule extends
        AbstractTransformationRule<StoreInertiaAO> {
    /** Logger for this class. */
    private static Logger log = LoggerFactory
            .getLogger(TStoreInertiaAORule.class);

    @Override
    public void execute(StoreInertiaAO visualGridSinkAO,
            TransformationConfiguration transformConfig) throws RuleException {
        try {
            final StoreInertiaPO visualGridSinkPO = new StoreInertiaPO(
                    visualGridSinkAO.getOutputSchema());
            visualGridSinkPO
                    .setOutputSchema(visualGridSinkAO.getOutputSchema());
            visualGridSinkPO.setPath(visualGridSinkAO.getPath());

            final Collection<ILogicalOperator> toUpdate = transformConfig
                    .getTransformationHelper().replace(visualGridSinkAO,
                            visualGridSinkPO);
            for (final ILogicalOperator o : toUpdate) {
                this.update(o);
            }
            this.retract(visualGridSinkAO);
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public boolean isExecutable(StoreInertiaAO operator,
            TransformationConfiguration transformConfig) {
        if (operator.getInputSchema(0).getType() == Tuple.class) {
            if (operator.isAllPhysicalInputSet()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return "StoreInertiaAO -> StoreInertiaPO";
    }
    
    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.TRANSFORMATION;
    }

}
