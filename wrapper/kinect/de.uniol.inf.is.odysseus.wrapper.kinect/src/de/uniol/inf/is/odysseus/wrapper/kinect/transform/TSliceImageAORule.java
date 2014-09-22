package de.uniol.inf.is.odysseus.wrapper.kinect.transform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
import de.uniol.inf.is.odysseus.wrapper.kinect.logicaloperator.SliceImageAO;
import de.uniol.inf.is.odysseus.wrapper.kinect.physicaloperator.SliceImagePO;

public class TSliceImageAORule extends
        AbstractTransformationRule<SliceImageAO> {
    /** Logger for this class. */
    private static Logger log = LoggerFactory
            .getLogger(TSliceImageAORule.class);

	@Override
	public void execute(SliceImageAO ao,
			TransformationConfiguration transformConfig) throws RuleException {
        try {
        	SliceImagePO po = new SliceImagePO(ao.getOutputSchema());
            po.setOutputSchema(ao.getOutputSchema());
            po.setRectangle(ao.getRectangle());
            defaultExecute(ao, po, transformConfig, true, true);		
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        }
	}
	
    @Override
    public String getName() {
        return "SliceImageAO -> SliceImagePO";
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
	public boolean isExecutable(SliceImageAO operator,
			TransformationConfiguration config) {
        if (operator.getInputSchema(0).getType() == Tuple.class) {
            if (operator.isAllPhysicalInputSet()) {
                return true;
            }
        }
        return false;
	}
}
