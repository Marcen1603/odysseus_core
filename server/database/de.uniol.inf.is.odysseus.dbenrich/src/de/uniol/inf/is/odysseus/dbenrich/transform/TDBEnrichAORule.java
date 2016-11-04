package de.uniol.inf.is.odysseus.dbenrich.transform;

import java.util.List;

import de.uniol.inf.is.odysseus.cache.Cache;
import de.uniol.inf.is.odysseus.cache.CacheEntry;
import de.uniol.inf.is.odysseus.cache.ICacheStore;
import de.uniol.inf.is.odysseus.cache.MainMemoryStore;
import de.uniol.inf.is.odysseus.cache.removalstrategy.IRemovalStrategy;
import de.uniol.inf.is.odysseus.cache.removalstrategy.RemovalStrategyRegistry;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.cache.ICache;
import de.uniol.inf.is.odysseus.core.server.metadata.UseLeftInputMetadata;
import de.uniol.inf.is.odysseus.core.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.ILeftMergeFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.dbenrich.IRetrievalStrategy;
import de.uniol.inf.is.odysseus.dbenrich.cache.ComplexParameterKey;
import de.uniol.inf.is.odysseus.dbenrich.cache.DBRetrievalStrategy;
import de.uniol.inf.is.odysseus.dbenrich.logicaloperator.DBEnrichAO;
import de.uniol.inf.is.odysseus.dbenrich.physicaloperator.DBEnrichPO;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalLeftMergeFunction;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalMergeFunction;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TDBEnrichAORule extends AbstractTransformationRule<DBEnrichAO> {

	@Override
	public int getPriority() {
		return 0; // No prioritization
	}

	@Override
	public void execute(DBEnrichAO logical,
			TransformationConfiguration transformConfig) throws RuleException {
		// System.out.println("transform; " + logical.getDebugString());

		IDataMergeFunction<Tuple<ITimeInterval>, ITimeInterval> dataMergeFunction = new RelationalMergeFunction<ITimeInterval>(
				logical.getOutputSchema().size());
		
		ILeftMergeFunction<Tuple<ITimeInterval>, ITimeInterval> dataLeftMergeFunction = null;
		if(logical.getOuterJoin()) {
			dataLeftMergeFunction = new RelationalLeftMergeFunction<>(logical.getInputSchema().size(), logical.getOutputSchema().size() - logical.getInputSchema().size(), logical.getOutputSchema().size());
		}

		IMetadataMergeFunction<ITimeInterval> metaMerge = new UseLeftInputMetadata<>();
		IRetrievalStrategy<ComplexParameterKey, List<IStreamObject<?>>> retrievalStrategy = new DBRetrievalStrategy(
				logical.getConnectionName(), logical.getQuery());
		ICache cache = null;
		if (logical.getCache() && logical.getCacheSize() > 0) {
			ICacheStore<Object, CacheEntry> cacheStore = new MainMemoryStore<Object, CacheEntry>(
					logical.getCacheSize() + 1);
			IRemovalStrategy removalStrategy = RemovalStrategyRegistry
					.getInstance(logical.getRemovalStrategy(), cacheStore);
			cache = new Cache(cacheStore, removalStrategy,
					logical.getExpirationTime(), logical.getCacheSize());
		}
		int[] uniqueKeys = logical.getUniqueKeysAsArray();

		// Maybe check, if operator is already existent (when is it 100% equal?)
		DBEnrichPO<ITimeInterval> physical = new DBEnrichPO<ITimeInterval>(
				logical.getConnectionName(), logical.getQuery(),
				logical.getAttributes(), dataMergeFunction, dataLeftMergeFunction, metaMerge,
				retrievalStrategy, cache, uniqueKeys);

		physical.setOutputSchema(logical.getOutputSchema());
		replace(logical, physical, transformConfig);
		retract(logical);
	}

	@Override
	public boolean isExecutable(DBEnrichAO logical,
			TransformationConfiguration config) {
		return logical.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "DBEnrichAO -> DBEnrichPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
}
