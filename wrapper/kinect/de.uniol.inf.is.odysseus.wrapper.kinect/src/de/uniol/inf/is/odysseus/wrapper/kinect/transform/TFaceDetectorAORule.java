package de.uniol.inf.is.odysseus.wrapper.kinect.transform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
import de.uniol.inf.is.odysseus.wrapper.kinect.logicaloperator.FaceDetectorAO;
import de.uniol.inf.is.odysseus.wrapper.kinect.physicaloperator.FaceDetectorPO;

public class TFaceDetectorAORule extends
        AbstractTransformationRule<FaceDetectorAO> {
    /** Logger for this class. */
    private static Logger log = LoggerFactory
            .getLogger(TFaceDetectorAORule.class);

	@Override
	public void execute(FaceDetectorAO ao,
			TransformationConfiguration transformConfig) throws RuleException {
        try {
        	FaceDetectorPO po = new FaceDetectorPO(ao.getInputSchema());
            po.setOutputSchema(ao.getOutputSchema());
            defaultExecute(ao, po, transformConfig, true, true);		
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        }
	}
	
    @Override
    public String getName() {
        return "FaceDetectorAO -> FaceDetectorPO";
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
	public boolean isExecutable(FaceDetectorAO operator,
			TransformationConfiguration config) {
        if (operator.getInputSchema(0).getType() == Tuple.class) {
            if (operator.isAllPhysicalInputSet()) {
                return true;
            }
        }
        return false;
	}
}
