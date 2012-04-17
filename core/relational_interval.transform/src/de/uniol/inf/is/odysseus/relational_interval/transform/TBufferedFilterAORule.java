package de.uniol.inf.is.odysseus.relational_interval.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BufferedFilterAO;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.metadata.UserLeftInputMetadata;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.BufferedFilterPO;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalLeftMergeFunction;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TBufferedFilterAORule extends
		AbstractTransformationRule<BufferedFilterAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(BufferedFilterAO operator,
			TransformationConfiguration config) {
		IDataMergeFunction<?> dataMerge = new RelationalLeftMergeFunction<ITimeInterval>(operator.getInputSchema(0), operator.getInputSchema(1), operator.getOutputSchema());
		IMetadataMergeFunction<?> metaDataMerge = new UserLeftInputMetadata();
		
		@SuppressWarnings("unchecked")
		BufferedFilterPO po = new BufferedFilterPO(operator.getPredicate(),
				operator.getBufferTime(), operator.getDeliverTime(),dataMerge,metaDataMerge);
		po.setOutputSchema(operator.getOutputSchema());

		Collection<ILogicalOperator> toUpdate = config
				.getTransformationHelper()
				.replace(operator, po);
		for (ILogicalOperator o : toUpdate) {
			update(o);
		}

		insert(po);
		retract(operator);

	}

	@Override
	public boolean isExecutable(BufferedFilterAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "BufferedFilterAO --> BufferedFilterPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<?> getConditionClass() {
		return BufferedFilterAO.class;
	}

}
