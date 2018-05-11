package de.uniol.inf.is.odysseus.temporaltypes.transform;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnNestAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalUnNestPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTimes;
import de.uniol.inf.is.odysseus.temporaltypes.physicalopertor.TemporalRelationalUnnestPO;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalDatatype;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TTemporalUnnestAORule extends AbstractTransformationRule<UnNestAO> {

	@Override
	public void execute(UnNestAO operator, TransformationConfiguration config) throws RuleException {
		RelationalUnNestPO<IValidTimes> po = new RelationalUnNestPO<IValidTimes>(operator.getInputSchema(),
				operator.getAttributePosition(), operator.isMultiValue() || operator.isListValue());
		TemporalRelationalUnnestPO<IValidTimes> temporalPo = new TemporalRelationalUnnestPO<>(po);
		defaultExecute(operator, temporalPo, config, true, true);
	}

	@Override
	public int getPriority() {
		return 1;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public boolean isExecutable(final UnNestAO operator, final TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet() && unnestAttributeIsTemporal(operator);
	}

	@Override
	public String getName() {
		return "UnNestAO -> TemporalRelationalUnNestPO";
	}

	private boolean unnestAttributeIsTemporal(UnNestAO operator) {
		SDFAttribute attribute = getAttributeFromSchema(operator.getInputSchema(), operator.getAttribute());
		return TemporalDatatype.isTemporalAttribute(attribute);
	}

	protected SDFAttribute getAttributeFromSchema(SDFSchema inputSchema, SDFAttribute attributeToSearch) {
		for (SDFAttribute attribute : inputSchema.getAttributes()) {
			if (attribute.getAttributeName().equals(attributeToSearch.getAttributeName())) {
				return attribute;
			}
		}
		return attributeToSearch;
	}

}
