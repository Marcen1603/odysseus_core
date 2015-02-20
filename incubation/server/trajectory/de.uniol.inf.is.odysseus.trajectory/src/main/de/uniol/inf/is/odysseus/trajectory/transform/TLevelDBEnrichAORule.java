package de.uniol.inf.is.odysseus.trajectory.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.metadata.UseLeftInputMetadata;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalMergeFunction;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.trajectory.logicaloperator.LevelDBEnrichAO;
import de.uniol.inf.is.odysseus.trajectory.physicaloperator.LevelDBEnrichPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TLevelDBEnrichAORule extends AbstractTransformationRule<LevelDBEnrichAO> {

	@Override
	public void execute(final LevelDBEnrichAO logical,
			final TransformationConfiguration config) throws RuleException {
		
		final IDataMergeFunction<Tuple<ITimeInterval>, ITimeInterval> dataMergeFunction = new RelationalMergeFunction<ITimeInterval>(
				logical.getOutputSchema().size() + 1);

		final IMetadataMergeFunction<ITimeInterval> metaMerge = new UseLeftInputMetadata<>();

		final int[] uniqueKeys = logical.getUniqueKeysAsArray();

		final LevelDBEnrichPO<ITimeInterval> physical = new LevelDBEnrichPO<ITimeInterval>(
				null, dataMergeFunction, metaMerge, uniqueKeys, 
				logical.getLevelDBPath(), 
				logical.getInputSchema().findAttributeIndex(logical.getIn().getAttributeName()),
				logical.getInputSchema().findAttributeIndex(logical.getOut().getAttributeName()));

		physical.setOutputSchema(logical.getOutputSchema());
		this.replace(logical, physical, config);
		this.retract(logical);
	}

	@Override
	public boolean isExecutable(final LevelDBEnrichAO operator,
			final TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}
	
	@Override
	public String getName() {
		return "LevelDBEnrichAO -> LevelDBEnrichPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
