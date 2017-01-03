package de.uniol.inf.is.odysseus.spatial.transform.rules;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.spatial.datastructures.IMovingObjectDataStructure;
import de.uniol.inf.is.odysseus.spatial.datastructures.SpatialDataStructureProvider;
import de.uniol.inf.is.odysseus.spatial.logicaloperator.SpatialRangeAO;
import de.uniol.inf.is.odysseus.spatial.physicaloperator.SpatialRangePO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSpatialRangeAOTransformRule extends AbstractTransformationRule<SpatialRangeAO> {

	@Override
	public void execute(SpatialRangeAO operator, TransformationConfiguration config) throws RuleException {
		if (operator.getDataStructureName() != null && !operator.getDataStructureName().isEmpty()) {
			IMovingObjectDataStructure dataStructure = SpatialDataStructureProvider.getInstance()
					.getDataStructure(operator.getDataStructureName());
			defaultExecute(operator,
					new SpatialRangePO<>(dataStructure, operator.getGeometryPosition(), operator.getRange()), config,
					true, true);
		} else {
			defaultExecute(operator, new SpatialRangePO<>(operator.getGeometryPosition(), operator.getRange()), config,
					true, true);
		}
	}

	@Override
	public boolean isExecutable(SpatialRangeAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
