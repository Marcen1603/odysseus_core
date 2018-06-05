package de.uniol.inf.is.odysseus.relational.transform;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregatePO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TAggregateAORule extends AbstractTransformationRule<AggregateAO> {

	@Override
	public void execute(AggregateAO operator, TransformationConfiguration config) throws RuleException {
		
		List<String> metadataSet = operator.getInputSchema()
				.getMetaAttributeNames();
		@SuppressWarnings("rawtypes")
		IMetadataMergeFunction mf = MetadataRegistry
				.getMergeFunction(metadataSet);
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		AggregatePO po = new AggregatePO(
				operator.getInputSchema(),
				operator.getOutputSchemaIntern(0),
				operator.getGroupingAttributes(),
				operator.getAggregations(),
				operator.isFastGrouping(), mf);
		defaultExecute(operator, po, config, true, true);
	}

	@Override
	public boolean isExecutable(AggregateAO operator, TransformationConfiguration config) {
		// TODO: If we allow other processing approaches (e.g. pos-neg) this has to be adapted ...
		return !(operator.getInputSchema().hasMetatype(ITimeInterval.class)) && operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super AggregateAO> getConditionClass() {
		return AggregateAO.class;
	}

}
