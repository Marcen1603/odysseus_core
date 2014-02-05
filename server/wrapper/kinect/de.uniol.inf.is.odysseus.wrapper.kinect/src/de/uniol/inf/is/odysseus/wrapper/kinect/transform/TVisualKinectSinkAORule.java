package de.uniol.inf.is.odysseus.wrapper.kinect.transform;

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
import de.uniol.inf.is.odysseus.wrapper.kinect.logicaloperator.VisualKinectSinkAO;
import de.uniol.inf.is.odysseus.wrapper.kinect.physicaloperator.VisualKinectSinkPO;

/**
 * Rule for the visual sink.
 * @author Juergen Boger <juergen.boger@offis.de>
 */
public class TVisualKinectSinkAORule extends
        AbstractTransformationRule<VisualKinectSinkAO> {
    /** Logger for this class. */
    private static Logger log = LoggerFactory
            .getLogger(TVisualKinectSinkAORule.class);

    @Override
    public void execute(final VisualKinectSinkAO visualGridSinkAO,
            final TransformationConfiguration transformConfig) throws RuleException {
        try {
            final VisualKinectSinkPO visualGridSinkPO = new VisualKinectSinkPO(
                    visualGridSinkAO.getOutputSchema());
            visualGridSinkPO
                    .setOutputSchema(visualGridSinkAO.getOutputSchema());

            final Collection<ILogicalOperator> toUpdate = transformConfig
                    .getTransformationHelper().replace(visualGridSinkAO,
                            visualGridSinkPO);
            for (final ILogicalOperator o : toUpdate) {
                this.update(o);
            }
            this.retract(visualGridSinkAO);
        } catch (final Exception e) {
            TVisualKinectSinkAORule.log.error(e.getMessage(), e);
        }
    }

    @Override
    public String getName() {
        return "VisualKinectAO -> VisualKinectPO";
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.TRANSFORMATION;
    }

    @Override
    public boolean isExecutable(final VisualKinectSinkAO operator,
            final TransformationConfiguration transformConfig) {
        if (operator.getInputSchema(0).getType() == Tuple.class) {
            if (operator.isAllPhysicalInputSet()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Class<? super VisualKinectSinkAO> getConditionClass() {
        return VisualKinectSinkAO.class;
    }
}
