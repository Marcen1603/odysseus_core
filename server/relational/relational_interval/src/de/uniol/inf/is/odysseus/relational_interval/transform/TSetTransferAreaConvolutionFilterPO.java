package de.uniol.inf.is.odysseus.relational_interval.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.physicaloperator.interval.TITransferArea;
import de.uniol.inf.is.odysseus.physicaloperator.relational.ConvolutionFilterPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSetTransferAreaConvolutionFilterPO extends AbstractTransformationRule<ConvolutionFilterPO<?>>{

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void execute(ConvolutionFilterPO<?> operator,
			TransformationConfiguration config) throws RuleException {
		operator.setTransferArea(new TITransferArea() );
	}

	@Override
	public boolean isExecutable(ConvolutionFilterPO<?> operator,
			TransformationConfiguration config) {
		return operator.getTransferArea() == null;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.METAOBJECTS;
	}
	
	@Override
	public Class<? super ConvolutionFilterPO<?>> getConditionClass() {
		return ConvolutionFilterPO.class;
	}
	

}
