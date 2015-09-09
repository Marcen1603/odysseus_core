package de.uniol.inf.is.odysseus.recommendation.physicaloperator;

import de.uniol.inf.is.odysseus.cache.Cache;
import de.uniol.inf.is.odysseus.cache.CacheEntry;
import de.uniol.inf.is.odysseus.cache.ICacheStore;
import de.uniol.inf.is.odysseus.cache.MainMemoryStore;
import de.uniol.inf.is.odysseus.cache.removalstrategy.IRemovalStrategy;
import de.uniol.inf.is.odysseus.cache.removalstrategy.RemovalStrategyRegistry;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.cache.ICache;
import de.uniol.inf.is.odysseus.core.server.metadata.UseLeftInputMetadata;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalMergeFunction;
import de.uniol.inf.is.odysseus.recommendation.logicaloperator.LODEnrichAO;
import de.uniol.inf.is.odysseus.recommendation.physicaloperator.LODEnrichPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TLODEnrichAORule extends AbstractTransformationRule<LODEnrichAO> {

	@Override
	public void execute(LODEnrichAO logicalOperator, TransformationConfiguration config) throws RuleException {

		IDataMergeFunction<Tuple<ITimeInterval>, ITimeInterval> dataMergeFunction = new RelationalMergeFunction<ITimeInterval>(logicalOperator.getOutputSchema().size());
		IMetadataMergeFunction<ITimeInterval> metaMerge = new UseLeftInputMetadata<>();
		
		ICache cacheManager;
		//Create a cache if its activated by the user
		if(logicalOperator.getCache()) {
			cacheManager = createCache(logicalOperator);
		} else {
			cacheManager = null;
		}

		//	super(cacheManager, dataMergeFunction, metaMergeFunction, uniqueKey);
		
		LODEnrichPO<ITimeInterval> physicalOperator = new LODEnrichPO<ITimeInterval>(cacheManager, dataMergeFunction, metaMerge, logicalOperator.getUniqueKeysAsArray());
		
		
		defaultExecute(logicalOperator, physicalOperator, config, true, true);
		
		
//		physicalOperator.setOutputSchema(logicalOperator.getOutputSchema());
//		replace(logicalOperator, physicalOperator, config);
//		retract(logicalOperator);
	}

	@Override
	public boolean isExecutable(LODEnrichAO logicalOperator, TransformationConfiguration config) {
		return logicalOperator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	/**
	 * Constructor for the ReadOnly Cache with a MainMemoryCacheStore. 
	 * Caching Records are not modified, only read, Caching Records are
	 * hold in the Main Memory. If no cache Entry is found, null will be 
	 * returned. Then, the operator has to get the data from the source
	 * @param logicalOperator
	 * @return the cache
	 */
	private ICache createCache(LODEnrichAO logicalOperator) {
		ICacheStore<Object, CacheEntry> cacheStore = new MainMemoryStore<>(logicalOperator.getCacheSize() + 1);
		IRemovalStrategy removalStrategy = RemovalStrategyRegistry.getInstance(logicalOperator.getRemovalStrategy(), cacheStore);
		return new Cache(cacheStore, removalStrategy, logicalOperator.getExpirationTime(), logicalOperator.getCacheSize());	
	}
}
