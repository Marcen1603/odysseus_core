package de.uniol.inf.is.odysseus.spatial.transform.rules;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.spatial.logicaloperator.SpatialPatternAO;
import de.uniol.inf.is.odysseus.spatial.physicaloperator.SpatialAreaPatternPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class SpatialPatternAOTransformRule extends AbstractTransformationRule<SpatialPatternAO> {

	@Override
	public void execute(SpatialPatternAO operator, TransformationConfiguration config) throws RuleException {
		switch (operator.getSpatialPattern()) {
		case REACHED_AREA:
		case LEFT_AREA:
			SpatialAreaPatternPO<Tuple<ITimeInterval>, ITimeInterval> areaDetection = new SpatialAreaPatternPO<>(
					operator);
			defaultExecute(operator, areaDetection, config, true, true);
			break;
		}
	}

	@Override
	public boolean isExecutable(SpatialPatternAO operator, TransformationConfiguration config) {
		if (operator.getInputSchema(0).getType() == Tuple.class) {
			return operator.isAllPhysicalInputSet();
		}
		return false;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public String getName() {
		return "SpatialPatternAO --> SpatialAreaPatterPO";
	}

	@Override
	public Class<? super SpatialPatternAO> getConditionClass() {
		return SpatialPatternAO.class;
	}

}
