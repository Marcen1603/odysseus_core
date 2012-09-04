package de.uniol.inf.is.odysseus.dbenrich.transform;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.dbenrich.cache.ComplexParameterKey;
import de.uniol.inf.is.odysseus.dbenrich.cache.DBRetrievalStrategy;
import de.uniol.inf.is.odysseus.dbenrich.cache.DummyReadOnlyCache;
import de.uniol.inf.is.odysseus.dbenrich.cache.IReadOnlyCache;
import de.uniol.inf.is.odysseus.dbenrich.cache.IRemovalStrategy;
import de.uniol.inf.is.odysseus.dbenrich.cache.ReadOnlyCache;
import de.uniol.inf.is.odysseus.dbenrich.cache.removalStrategy.FIFO;
import de.uniol.inf.is.odysseus.dbenrich.cache.removalStrategy.Random;
import de.uniol.inf.is.odysseus.dbenrich.logicaloperator.DBEnrichAO;
import de.uniol.inf.is.odysseus.dbenrich.physicaloperator.DBEnrichPO;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalMergeFunction;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

//@SuppressWarnings({ "rawtypes" })
public class TDBEnrichAORule extends AbstractTransformationRule<DBEnrichAO> {

	private final static String RANDOM = "random";
	private final static String FIFO   = "fifo";
	private final static String LRU    = "lru";
	private final static String LFU    = "lfu";

	@Override
	public int getPriority() {
		return 0; // No prioritization
	}

	@Override
	public void execute(DBEnrichAO logical, TransformationConfiguration transformConfig) {
		// System.out.println("transform; " + logical.getDebugString());

		IDataMergeFunction<Tuple<ITimeInterval>> dataMergeFunction =
				new RelationalMergeFunction<ITimeInterval>(logical.getOutputSchema().size());

		IReadOnlyCache<ComplexParameterKey, Tuple> cacheManager = createCache(logical);

		// Maybe check, if operator is already existent (when is it 100% equal?)
		DBEnrichPO<ITimeInterval> physical = new DBEnrichPO<ITimeInterval>(
				logical.getConnectionName(),
				logical.getQuery(),
				logical.getAttributes(),
				logical.isNoCache(),
				logical.getCacheSize(),
				logical.getExpirationTime(),
				logical.getRemovalStrategy(),
				dataMergeFunction,
				cacheManager);

		physical.setOutputSchema(logical.getOutputSchema());
		replace(logical, physical, transformConfig);
		retract(logical);
	}

	private IReadOnlyCache<ComplexParameterKey, Tuple> createCache(
			DBEnrichAO logical) {

		DBRetrievalStrategy dbRetrievalStrategy = new DBRetrievalStrategy(
				logical.getConnectionName(), logical.getQuery(),
				logical.getAttributes(), logical.getOutputSchema());

		if(logical.isNoCache()) {
			return new DummyReadOnlyCache<ComplexParameterKey, Tuple>(
					dbRetrievalStrategy);
		} else {

			/* It is ensured, that the store size will never exceed the
			 * maximum size, therefore the loadCapacity may be set to 1
			 * to prevent rehashing. */
			Map<ComplexParameterKey, Tuple> cacheStore =
					new HashMap<ComplexParameterKey, Tuple>(logical.getCacheSize()+1, 1.0f);

			/* Instantiate removal strategy. A Switch case (Java 7), or a
			 * factory class could be used here if needed in the future. */
			String removalStrategyLCStr = logical.getRemovalStrategy().toLowerCase();
			IRemovalStrategy<ComplexParameterKey, Tuple> removalStrategy;
			System.out.println("RemovalStrategyLCStr: " + removalStrategyLCStr);
			if (removalStrategyLCStr.equals(RANDOM)) {
				removalStrategy = new Random<ComplexParameterKey, Tuple>(cacheStore);
			} else if (removalStrategyLCStr.equals(FIFO)) {
				removalStrategy = new FIFO<ComplexParameterKey, Tuple>(cacheStore);
			} else if (removalStrategyLCStr.equals(LRU)) {
				throw new RuntimeException("TBD"); //FIXME
			} else if (removalStrategyLCStr.equals(LFU)) {
				throw new RuntimeException("TBD"); //FIXME
			} else {
				throw new RuntimeException("Unknown Removal Strategy" +
						"'"+removalStrategyLCStr+"'");
			}

			return new ReadOnlyCache<ComplexParameterKey, Tuple>(
					cacheStore, dbRetrievalStrategy, removalStrategy, logical.getCacheSize());
		}
	}

	@Override
	public boolean isExecutable(DBEnrichAO logical, TransformationConfiguration config) {
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
