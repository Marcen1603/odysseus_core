package de.uniol.inf.is.odysseus.wrapper.shiproutes.conversion.transformationrule;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.conversion.logicaloperator.ShipRouteConverterAO;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.conversion.physicaloperator.ShipRouteConverterPO;

public class ShipRouteConverterAORule extends
		AbstractTransformationRule<ShipRouteConverterAO> {
	
	@Override
	public void execute(ShipRouteConverterAO converterAO,
			TransformationConfiguration config) throws RuleException {
		defaultExecute(converterAO, new ShipRouteConverterPO<>(converterAO.getConversionType()), config, true, true);
	}

	@Override
	public boolean isExecutable(ShipRouteConverterAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet(); 
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public String getName() {
		return "ShipRouteConverterAO -> ShipRouteConverterPO";
	}
	
	@Override
	public Class<? super ShipRouteConverterAO> getConditionClass() {	
		return ShipRouteConverterAO.class;
	}

}
