package de.uniol.inf.is.odysseus.wsenrich.transform;

import java.util.List;

import de.uniol.inf.is.odysseus.cache.Cache;
import de.uniol.inf.is.odysseus.cache.CacheEntry;
import de.uniol.inf.is.odysseus.cache.ICacheStore;
import de.uniol.inf.is.odysseus.cache.MainMemoryStore;
import de.uniol.inf.is.odysseus.cache.removalstrategy.IRemovalStrategy;
import de.uniol.inf.is.odysseus.cache.removalstrategy.RemovalStrategyRegistry;
import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.cache.ICache;
import de.uniol.inf.is.odysseus.core.server.metadata.UseLeftInputMetadata;
import de.uniol.inf.is.odysseus.core.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.ILeftMergeFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalLeftMergeFunction;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalMergeFunction;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
import de.uniol.inf.is.odysseus.wsenrich.logicaloperator.WSEnrichAO;
import de.uniol.inf.is.odysseus.wsenrich.physicaloperator.WSEnrichPO;
import de.uniol.inf.is.odysseus.wsenrich.util.HttpEntityToStringConverter;
import de.uniol.inf.is.odysseus.wsenrich.util.implementation.HttpBodyMessage;
import de.uniol.inf.is.odysseus.wsenrich.util.implementation.HttpBodyMessageManipulator;
import de.uniol.inf.is.odysseus.wsenrich.util.interfaces.IConnectionForWebservices;
import de.uniol.inf.is.odysseus.wsenrich.util.interfaces.IKeyFinder;
import de.uniol.inf.is.odysseus.wsenrich.util.interfaces.IMessageManipulator;
import de.uniol.inf.is.odysseus.wsenrich.util.interfaces.IRequestBuilder;
import de.uniol.inf.is.odysseus.wsenrich.util.interfaces.IMessageCreator;
import de.uniol.inf.is.odysseus.wsenrich.util.serviceregistry.ConnectionForWebservicesRegistry;
import de.uniol.inf.is.odysseus.wsenrich.util.serviceregistry.KeyFinderRegistry;
import de.uniol.inf.is.odysseus.wsenrich.util.serviceregistry.MessageManipulatorRegistry;
import de.uniol.inf.is.odysseus.wsenrich.util.serviceregistry.RequestBuilderRegistry;
import de.uniol.inf.is.odysseus.wsenrich.util.serviceregistry.MessageCreatorRegistry;

public class TWSEnrichAORule extends AbstractTransformationRule<WSEnrichAO> {

	@Override
	public int getPriority() {
		return 0; // No priorization
	}

	@Override
	public void execute(WSEnrichAO logical, TransformationConfiguration transformConfig) throws RuleException {

		IDataMergeFunction<Tuple<ITimeInterval>, ITimeInterval> dataMergeFunction = new RelationalMergeFunction<ITimeInterval>(
				logical.getOutputSchema().size());
		IMetadataMergeFunction<ITimeInterval> metaMerge = new UseLeftInputMetadata<>();

		ILeftMergeFunction<Tuple<ITimeInterval>, ITimeInterval> dataLeftMergeFunction = null;
		if (logical.getOuterJoin()) {
			dataLeftMergeFunction = new RelationalLeftMergeFunction<>(logical.getInputSchema().size(),
					logical.getOutputSchema().size() - logical.getInputSchema().size(),
					logical.getOutputSchema().size());
		}

		IMessageCreator messageCreator;
		IMessageManipulator messageManipulator;
		List<Option> arguments = logical.getArguments();

		if (logical.getServiceMethod().equals(WSEnrichAO.SERVICE_METHOD_SOAP)) {
			messageCreator = MessageCreatorRegistry.getInstance(logical.getServiceMethod(), logical.getWsdlLocation(),
					logical.getOperation());
			messageManipulator = MessageManipulatorRegistry.getInstance(logical.getServiceMethod());
		} else if (logical.getTemplate() != null) {
			messageCreator = new HttpBodyMessage(logical.getTemplate());
			messageManipulator = new HttpBodyMessageManipulator();
		} else {
			messageCreator = null;
			messageManipulator = null;
		}

		IConnectionForWebservices connection = ConnectionForWebservicesRegistry.getInstance(logical.getGetOrPost());
		IRequestBuilder requestBuilder = RequestBuilderRegistry.getInstance(logical.getMethod());
		HttpEntityToStringConverter converter = new HttpEntityToStringConverter(logical.getCharset());
		IKeyFinder keyFinder = KeyFinderRegistry.getInstance(logical.getParsingMethod());

		ICache cacheManager;
		// Create a cache if its activated by the user
		if (logical.getCache()) {
			cacheManager = createCache(logical);
		} else {
			cacheManager = null;
		}

		WSEnrichPO<ITimeInterval> physical = new WSEnrichPO<ITimeInterval>(logical.getServiceMethod(),
				logical.getMethod(), logical.getContentType(), logical.getUrl(), logical.isUrlIsTemplate() ,logical.getUrlSuffix(), arguments,
				logical.getHeader(), logical.getOperation(), logical.getReceivedData(), logical.getCharset(),
				logical.getParsingMethod(), logical.getOuterJoin(), logical.isAllowNullValues(), logical.getKeyValueOutput(),
				logical.getMultiTupleOutput(), logical.getUniqueKeysAsArray(), dataMergeFunction, dataLeftMergeFunction,
				metaMerge, connection, requestBuilder, converter, keyFinder, messageCreator, messageManipulator,
				cacheManager);

		// defaultExecute(logical, physical, transformConfig, true, true);
		physical.setOutputSchema(logical.getOutputSchema());
		replace(logical, physical, transformConfig);
		retract(logical);
	}

	@Override
	public boolean isExecutable(WSEnrichAO logical, TransformationConfiguration config) {
		return logical.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "WSEnrichAO --> WSEnrichPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	/**
	 * Constructor for the ReadOnly Cache with a MainMemoryCacheStore. Caching
	 * Records are not modified, only read, Caching Records are hold in the Main
	 * Memory. If no cache Entry is found, null will be returned. Then, the
	 * operator has to get the data from the source
	 *
	 * @param logical
	 * @return the cache
	 */
	private ICache createCache(WSEnrichAO logical) {
		ICacheStore<Object, CacheEntry> cacheStore = new MainMemoryStore<>(logical.getCacheSize() + 1);
		IRemovalStrategy removalStrategy = RemovalStrategyRegistry.getInstance(logical.getRemovalStrategy(),
				cacheStore);
		return new Cache(cacheStore, removalStrategy, logical.getExpirationTime(), logical.getCacheSize());
	}

}
