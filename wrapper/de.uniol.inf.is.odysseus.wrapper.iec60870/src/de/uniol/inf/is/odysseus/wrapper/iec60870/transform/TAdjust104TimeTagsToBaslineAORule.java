package de.uniol.inf.is.odysseus.wrapper.iec60870.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
import de.uniol.inf.is.odysseus.wrapper.iec60870.logicaloperator.Adjust104TimeTagsToBaselineAO;
import de.uniol.inf.is.odysseus.wrapper.iec60870.physicaloperator.Adjust104TimeTagsToBaselinePO;

// TODO javaDoc
public class TAdjust104TimeTagsToBaslineAORule extends AbstractTransformationRule<Adjust104TimeTagsToBaselineAO> {

	@Override
	public void execute(Adjust104TimeTagsToBaselineAO operator, TransformationConfiguration config)
			throws RuleException {
		Adjust104TimeTagsToBaselinePO po = new Adjust104TimeTagsToBaselinePO(operator.getIosAttributePos(),
				operator.getAcceleration(), operator.getBaseline());
		defaultExecute(operator, po, config, true, true);
	}

	@Override
	public boolean isExecutable(Adjust104TimeTagsToBaselineAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet() && operator.getInputSchema().getType() == Tuple.class
				&& operator.getInputSchema().size() > operator.getIosAttributePos() && operator.getInputSchema()
						.getAttribute(operator.getIosAttributePos()).getDatatype().equals(SDFDatatype.LIST)
				&& operator.getBaseline() >= 0 && operator.getAcceleration() > 0;
	}

	@Override
	public String getName() {
		return "Adjust104TimeTagsToBaslineAO -> Adjust104TimeTagsToBaslinePO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super Adjust104TimeTagsToBaselineAO> getConditionClass() {
		return Adjust104TimeTagsToBaselineAO.class;
	}

}