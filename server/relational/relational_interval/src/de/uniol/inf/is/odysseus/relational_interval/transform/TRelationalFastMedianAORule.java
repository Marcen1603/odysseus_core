package de.uniol.inf.is.odysseus.relational_interval.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalGroupProcessor;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalNoGroupProcessor;
import de.uniol.inf.is.odysseus.relational_interval.logicaloperator.RelationalFastMedianAO;
import de.uniol.inf.is.odysseus.relational_interval.physicaloperator.RelationalFastMedianPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TRelationalFastMedianAORule extends
		AbstractTransformationRule<RelationalFastMedianAO> {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(RelationalFastMedianAO operator,
			TransformationConfiguration config) {
		SDFSchema inputSchema = operator.getInputSchema(0);
		int medianAttrPos = inputSchema.findAttributeIndex(operator
				.getMedianAttribute().getURI());
		RelationalFastMedianPO po = new RelationalFastMedianPO(medianAttrPos,
				operator.isNumericalMedian());
		if (operator.getGroupingAttributes() != null
				&& operator.getGroupingAttributes().size() > 0) {
			po.setGroupProcessor(new RelationalGroupProcessor(inputSchema,
					operator.getOutputSchema(), operator
							.getGroupingAttributes(), null, false));
		} else {
			po.setGroupProcessor(new RelationalNoGroupProcessor<>());
		}
		defaultExecute(operator, po, config, true, true);
	}

	@Override
	public boolean isExecutable(RelationalFastMedianAO operator,
			TransformationConfiguration config) {
		return operator.getInputSchema(0).getType() == Tuple.class
				&& operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "RelationalFastMedianAO --> RelationalFastMedianPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super RelationalFastMedianAO> getConditionClass() {
		return RelationalFastMedianAO.class;
	}
}
