package de.uniol.inf.is.odysseus.wrapper.shiproutes.conversation.transformationrule;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.conversation.logicaloperator.ShipRouteIECConverterAO;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.conversation.physicaloperator.ShipRouteIECConverterPO;

public class ShipRouteIECConverterAORule extends
		AbstractTransformationRule<ShipRouteIECConverterAO> {
	
	@Override
	public void execute(ShipRouteIECConverterAO converterAO,
			TransformationConfiguration config) throws RuleException {
		defaultExecute(converterAO, new ShipRouteIECConverterPO<>(converterAO.getConversionType()), config, true, true);
	}

	@Override
	public boolean isExecutable(ShipRouteIECConverterAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet(); 
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public String getName() {
		return "ShipRouteIECConverterAO -> ShipRouteIECConverterPO";
	}
	
	@Override
	public Class<? super ShipRouteIECConverterAO> getConditionClass() {	
		return ShipRouteIECConverterAO.class;
	}

}
