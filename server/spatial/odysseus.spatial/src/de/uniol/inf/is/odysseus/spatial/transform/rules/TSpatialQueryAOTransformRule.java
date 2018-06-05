package de.uniol.inf.is.odysseus.spatial.transform.rules;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.spatial.datastructures.spatiotemporal.ISpatioTemporalDataStructure;
import de.uniol.inf.is.odysseus.spatial.datastructures.spatiotemporal.SpatioTemporalDataStructureProvider;
import de.uniol.inf.is.odysseus.spatial.logicaloperator.SpatialQueryAO;
import de.uniol.inf.is.odysseus.spatial.physicaloperator.SpatialQueryPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

@SuppressWarnings("deprecation")
public class TSpatialQueryAOTransformRule extends AbstractTransformationRule<SpatialQueryAO> {

	@Override
	public void execute(SpatialQueryAO operator, TransformationConfiguration config) throws RuleException {
		ISpatioTemporalDataStructure dataStructure = SpatioTemporalDataStructureProvider.getInstance()
				.getDataStructure(operator.getDataStructureName());
		defaultExecute(operator,
				new SpatialQueryPO<>(dataStructure, operator.getPolygonPoints(), operator.getGeometryPosition()),
				config, true, true);

	}

	@Override
	public boolean isExecutable(SpatialQueryAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
