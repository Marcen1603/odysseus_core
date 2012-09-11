package de.uniol.inf.is.odysseus.dbenrich.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.dbenrich.cache.CacheEntry;
import de.uniol.inf.is.odysseus.dbenrich.cache.ComplexParameterKey;
import de.uniol.inf.is.odysseus.dbenrich.cache.DBRetrievalStrategy;
import de.uniol.inf.is.odysseus.dbenrich.cache.DummyReadOnlyCache;
import de.uniol.inf.is.odysseus.dbenrich.cache.ICacheStore;
import de.uniol.inf.is.odysseus.dbenrich.cache.IReadOnlyCache;
import de.uniol.inf.is.odysseus.dbenrich.cache.IRetrievalStrategy;
import de.uniol.inf.is.odysseus.dbenrich.cache.WorkingMemoryCacheStore;
import de.uniol.inf.is.odysseus.dbenrich.cache.ReadOnlyCache;
import de.uniol.inf.is.odysseus.dbenrich.cache.removalStrategy.IRemovalStrategy;
import de.uniol.inf.is.odysseus.dbenrich.cache.removalStrategy.RemovalStrategyRegistry;
import de.uniol.inf.is.odysseus.dbenrich.logicaloperator.DBEnrichAO;
import de.uniol.inf.is.odysseus.dbenrich.physicaloperator.DBEnrichPO;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalMergeFunction;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

//@SuppressWarnings({ "rawtypes" })
public class TDBEnrichAORule extends AbstractTransformationRule<DBEnrichAO> {

	@Override
	public int getPriority() {
		return 0; // No prioritization
	}

	@Override
	public void execute(DBEnrichAO logical,
			TransformationConfiguration transformConfig) {
		// System.out.println("transform; " + logical.getDebugString());

		IDataMergeFunction<Tuple<ITimeInterval>> dataMergeFunction = new RelationalMergeFunction<ITimeInterval>(
				logical.getOutputSchema().size());

		IReadOnlyCache<ComplexParameterKey, Tuple<?>> cacheManager = createCache(logical);

		// Maybe check, if operator is already existent (when is it 100% equal?)
		DBEnrichPO<ITimeInterval> physical = new DBEnrichPO<ITimeInterval>(
				logical.getConnectionName(),
				logical.getQuery(),
				logical.getAttributes(),
				logical.isNoCache(),
				logical.getCacheSize(),
				logical.getExpirationTime(),
				logical.getRemovalStrategy(),
				dataMergeFunction, cacheManager);

		physical.setOutputSchema(logical.getOutputSchema());
		replace(logical, physical, transformConfig);
		retract(logical);
	}

	private IReadOnlyCache<ComplexParameterKey, Tuple<?>> createCache(
			DBEnrichAO logical) {

		IRetrievalStrategy<ComplexParameterKey, Tuple<?>> retrievalStrategy = new DBRetrievalStrategy(
				logical.getConnectionName(), logical.getQuery());

		if (logical.isNoCache()) {
			return new DummyReadOnlyCache<>(retrievalStrategy);
		} else {

			/*
			 * It is ensured, that the store size will never exceed the maximum
			 * size, therefore the loadCapacity may be set to 1 to prevent
			 * rehashing.
			 */
			ICacheStore<ComplexParameterKey, CacheEntry<Tuple<?>>> cacheStore = 
					new WorkingMemoryCacheStore<>(logical.getCacheSize() + 1, 1.0f);

			/* Instantiate removal strategy using the declarative service */
			IRemovalStrategy removalStrategy = RemovalStrategyRegistry
					.getInstance(logical.getRemovalStrategy(), cacheStore);
			if (removalStrategy == null) {
				throw new TransformationException("No removal strategy '"
						+ logical.getRemovalStrategy() + "' found.");
			}

			return new ReadOnlyCache<>(cacheStore, retrievalStrategy,
					removalStrategy, logical.getCacheSize(),
					logical.getExpirationTime());
		}
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
