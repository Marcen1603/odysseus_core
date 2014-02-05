package de.uniol.inf.is.odysseus.wrapper.kinect.transform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
import de.uniol.inf.is.odysseus.wrapper.kinect.logicaloperator.FaceRecognizerAO;
import de.uniol.inf.is.odysseus.wrapper.kinect.physicaloperator.FaceRecognizerPO;

public class TFaceRecognizerAORule extends
        AbstractTransformationRule<FaceRecognizerAO> {
    /** Logger for this class. */
    private static Logger log = LoggerFactory
            .getLogger(TFaceRecognizerAORule.class);

	@Override
	public void execute(FaceRecognizerAO ao,
			TransformationConfiguration transformConfig) throws RuleException {
        try {
        	FaceRecognizerPO po = new FaceRecognizerPO(ao.getInputSchema());
            po.setOutputSchema(ao.getOutputSchema());
            ao.getParameterInfos().put("recordDataRate".toUpperCase(),
            		ao.getRecordDataRate());
            po.setParameterInfos(ao.getParameterInfos());            
            defaultExecute(ao, po, transformConfig, true, true);		
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        }
	}
	
    @Override
    public String getName() {
        return "FaceRecognizerAO -> FaceRecognizerPO";
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
	public boolean isExecutable(FaceRecognizerAO operator,
			TransformationConfiguration config) {
        if (operator.getInputSchema(0).getType() == Tuple.class) {
            if (operator.isAllPhysicalInputSet()) {
                return true;
            }
        }
        return false;
	}
}
