package de.uniol.inf.is.odysseus.wrapper.shiproutes.conversion.transformationrule;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.conversion.logicaloperator.ShipRouteConverterAO;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.conversion.physicaloperator.ShipRoute015ConverterPO;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.conversion.physicaloperator.ShipRoute025ConverterPO;

public class ShipRouteConverterAORule extends
		AbstractTransformationRule<ShipRouteConverterAO> {

	@Override
	public void execute(ShipRouteConverterAO converterAO,
			TransformationConfiguration config) throws RuleException {
		if (converterAO.getConversionType().toString().toUpperCase()
				.contains(new String("IVEF").toUpperCase())) {
			switch (converterAO.getIvefVersion()) {
			case V015:
				defaultExecute(converterAO,
						new ShipRoute015ConverterPO<>(converterAO.getConversionType()),
						config, true, true);
				break;
			case V025:
				defaultExecute(converterAO,
						new ShipRoute025ConverterPO<>(converterAO.getConversionType()),
						config, true, true);
				break;
			default:
				break;
			}
		} else {
			defaultExecute(converterAO,
					new ShipRoute015ConverterPO<>(converterAO.getConversionType()),
					config, true, true);
		}
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
