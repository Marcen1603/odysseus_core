package de.uniol.inf.is.odysseus.wsenrich.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.metadata.UseLeftInputMetadata;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalMergeFunction;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
import de.uniol.inf.is.odysseus.wsenrich.logicaloperator.WSEnrichAO;
import de.uniol.inf.is.odysseus.wsenrich.physicaloperator.WSEnrichPO;
import de.uniol.inf.is.odysseus.wsenrich.util.ConnectionForWebservicesRegistry;
import de.uniol.inf.is.odysseus.wsenrich.util.HttpEntityToStringConverter;
import de.uniol.inf.is.odysseus.wsenrich.util.IConnectionForWebservices;
import de.uniol.inf.is.odysseus.wsenrich.util.IKeyFinder;
import de.uniol.inf.is.odysseus.wsenrich.util.IMessageManipulator;
import de.uniol.inf.is.odysseus.wsenrich.util.IRequestBuilder;
import de.uniol.inf.is.odysseus.wsenrich.util.ISoapMessageCreator;
import de.uniol.inf.is.odysseus.wsenrich.util.KeyFinderRegistry;
import de.uniol.inf.is.odysseus.wsenrich.util.MessageManipulatorRegistry;
import de.uniol.inf.is.odysseus.wsenrich.util.RequestBuilderRegistry;
import de.uniol.inf.is.odysseus.wsenrich.util.SoapMessageCreatorRegistry;
import de.uniol.inf.is.odysseus.cache.CacheEntry;
import de.uniol.inf.is.odysseus.cache.ICacheStore;
import de.uniol.inf.is.odysseus.cache.IReadOnlyCache;
import de.uniol.inf.is.odysseus.cache.MainMemoryStore;
import de.uniol.inf.is.odysseus.cache.ReadOnlyCache;
import de.uniol.inf.is.odysseus.cache.removalstrategy.IRemovalStrategy;
import de.uniol.inf.is.odysseus.cache.removalstrategy.RemovalStrategyRegistry;

public class TWSEnrichAORule extends AbstractTransformationRule<WSEnrichAO> {

	@Override
	public int getPriority() {
		return 0; // No priorization
	}

	@Override
	public void execute(WSEnrichAO logical, TransformationConfiguration transformConfig) {
		
		IDataMergeFunction<Tuple<ITimeInterval>, ITimeInterval> dataMergeFunction = 
				new RelationalMergeFunction<ITimeInterval>(logical.getOutputSchema().size());
		IMetadataMergeFunction<ITimeInterval> metaMerge = new UseLeftInputMetadata<>();
		
		ISoapMessageCreator soapMessageCreator;
		IMessageManipulator soapMessageManipulator;
		
		if(logical.getServiceMethod().equals(WSEnrichAO.SERVICE_METHOD_SOAP)) {
			soapMessageCreator = SoapMessageCreatorRegistry.getInstance(logical.getServiceMethod(), logical.getWsdlLocation(), logical.getOpeation());
			soapMessageManipulator = MessageManipulatorRegistry.getInstance(logical.getServiceMethod());
		} else {
			soapMessageCreator = null;
			soapMessageManipulator = null;
		}

		IConnectionForWebservices connection = ConnectionForWebservicesRegistry.getInstance(logical.getGetOrPost());
		IRequestBuilder requestBuilder = RequestBuilderRegistry.getInstance(logical.getMethod());
		HttpEntityToStringConverter converter = new HttpEntityToStringConverter(logical.getCharset());
		IKeyFinder keyFinder = KeyFinderRegistry.getInstance(logical.getParsingMethod());
		
		IReadOnlyCache cacheManager;
		//Create a cache if its activated by the user
		if(logical.getCache()) {
			cacheManager = createCache(logical);
		} else {
			cacheManager = null;
		}
		
		WSEnrichPO<ITimeInterval> physical = new WSEnrichPO<ITimeInterval>(
			logical.getServiceMethod(),
			logical.getMethod(),
			logical.getUrl(),
			logical.getUrlSuffix(),
			logical.getArguments(),
			logical.getOpeation(),
			logical.getReceivedData(),
			logical.getCharset(),
			logical.getParsingMethod(),
			logical.getOuterJoin(),
			logical.getKeyValueOutput(),
			logical.getMultiTupleOutput(),
			dataMergeFunction,
			metaMerge,
			connection,
			requestBuilder,
			converter,
			keyFinder,
			soapMessageCreator,
			soapMessageManipulator, 
			cacheManager);
		
		//defaultExecute(logical, physical, transformConfig, true, true);
		physical.setOutputSchema(logical.getOutputSchema());
		replace(logical, physical, transformConfig);
		retract(logical);
	}

	@Override
	public boolean isExecutable(WSEnrichAO logical,
			TransformationConfiguration config) {
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
	 * Constructor for the ReadOnly Cache with a MainMemoryCacheStore. 
	 * Caching Records are not modified, only read, Caching Records are
	 * hold in the Main Memory. If no cache Entry is found, null will be 
	 * returned. Then, the operator has to get the data from the source
	 * @param logical
	 * @return the cache
	 */
	private IReadOnlyCache createCache(WSEnrichAO logical) {
		ICacheStore<Object, CacheEntry> cacheStore = new MainMemoryStore<>(logical.getCacheSize() + 1, 1.0f);
		IRemovalStrategy removalStrategy = RemovalStrategyRegistry.getInstance(logical.getRemovalStrategy(), cacheStore);
		return new ReadOnlyCache(cacheStore, removalStrategy, logical.getExpirationTime(), logical.getCacheSize());	
	}

}
