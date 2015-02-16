package de.uniol.inf.is.odysseus.trajectory.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.cache.ICache;
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
	public void execute(LevelDBEnrichAO logical,
			TransformationConfiguration config) throws RuleException {
		
		IDataMergeFunction<Tuple<ITimeInterval>, ITimeInterval> dataMergeFunction = new RelationalMergeFunction<ITimeInterval>(
				logical.getOutputSchema().size() + 1);

		IMetadataMergeFunction<ITimeInterval> metaMerge = new UseLeftInputMetadata<>();

		int[] uniqueKeys = logical.getUniqueKeysAsArray();

		LevelDBEnrichPO<ITimeInterval> physical = new LevelDBEnrichPO<ITimeInterval>(
				null, dataMergeFunction, metaMerge, uniqueKeys, 
				logical.getLevelDBPath(), 
				logical.getInputSchema().findAttributeIndex(logical.getIn().getAttributeName()),
				logical.getInputSchema().findAttributeIndex(logical.getOut().getAttributeName()));

		physical.setOutputSchema(logical.getOutputSchema());
		replace(logical, physical, config);
		retract(logical);
	}

	@Override
	public boolean isExecutable(LevelDBEnrichAO operator,
			TransformationConfiguration config) {
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
