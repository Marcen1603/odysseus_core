package de.uniol.inf.is.odysseus.relational_interval.transform;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalGroupProcessor;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalNoGroupProcessor;
import de.uniol.inf.is.odysseus.relational_interval.logicaloperator.RelationalFastMedianAO;
import de.uniol.inf.is.odysseus.relational_interval.physicaloperator.AbstractFastMedianPO;
import de.uniol.inf.is.odysseus.relational_interval.physicaloperator.RelationalFastMedianHistogramPO;
import de.uniol.inf.is.odysseus.relational_interval.physicaloperator.RelationalFastMedianPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;

public class TRelationalFastMedianAORule extends
		AbstractRelationalIntervalTransformationRule<RelationalFastMedianAO> {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(RelationalFastMedianAO operator,
			TransformationConfiguration config) throws RuleException {
		SDFSchema inputSchema = operator.getInputSchema(0);
		int medianAttrPos = inputSchema.findAttributeIndex(operator
				.getMedianAttribute().getURI());
		AbstractFastMedianPO po;
		
		if (operator.isUseHistogram()) {
			po = new RelationalFastMedianHistogramPO<>(medianAttrPos,
					operator.isNumericalMedian(), operator.getRoundingFactor());
		} else {
			po = new RelationalFastMedianPO(medianAttrPos,
					operator.isNumericalMedian());
		}
		
		po.setPercentiles(operator.getPercentiles());
		
		po.appendGlobalMedian(operator.isAppendGlobalMedian());
		
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
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super RelationalFastMedianAO> getConditionClass() {
		return RelationalFastMedianAO.class;
	}
}
