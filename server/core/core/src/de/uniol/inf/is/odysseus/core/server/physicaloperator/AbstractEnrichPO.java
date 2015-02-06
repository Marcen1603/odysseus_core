package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.cache.ICache;

abstract public class AbstractEnrichPO<T extends IStreamObject<M>, M extends IMetaAttribute>
		extends AbstractPipe<T, T> {

	static final Logger logger = LoggerFactory
			.getLogger(AbstractEnrichPO.class);

	private final ICache cache;
	private final IDataMergeFunction<T, M> dataMergeFunction;
	private final IMetadataMergeFunction<M> metaMergeFunction;
	private final int[] uniqueKeys;

	public AbstractEnrichPO(ICache cache,
			IDataMergeFunction<T, M> dataMergeFunction,
			IMetadataMergeFunction<M> metaMergeFunction, int[] uniqueKeys) {
		super();
		this.cache = cache;
		this.dataMergeFunction = dataMergeFunction;
		this.metaMergeFunction = metaMergeFunction;
		this.uniqueKeys = uniqueKeys;
	}

	public AbstractEnrichPO(AbstractEnrichPO<T, M> abstractEnrichPO) {
		super(abstractEnrichPO);
		this.cache = abstractEnrichPO.cache;
		this.dataMergeFunction = abstractEnrichPO.dataMergeFunction;
		this.metaMergeFunction = abstractEnrichPO.metaMergeFunction;
		this.uniqueKeys = abstractEnrichPO.uniqueKeys;
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	final protected void process_open() throws OpenFailedException {
		dataMergeFunction.init();
		internal_process_open();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(T object, int port) {
		List<IStreamObject<?>> result = null;
		Object key = null;

		if (cache != null) {
			if (uniqueKeys != null) {
				key = object.restrictedHashCode(uniqueKeys);
			} else {
				key = object.hashCode();
			}
			result = cache.get(key);
		}

		// No cache or nothing found in cache
		if (result == null) {
			result = internal_process(object);
		}

		if (result != null) {
			// Store into cache
			if (cache != null) {
				cache.put(key, result);
			}

			// Enrich and transfer
			for (int i = 0; i < result.size(); i++) {
				T output = dataMergeFunction.merge(object, (T) result.get(i),
						metaMergeFunction, Order.LeftRight);
				output.setMetadata((M) object.getMetadata().clone());
				transfer(output);
			}
		}else{
			logger.warn("Web Service returns empty result for input "+object);
		}
		
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

	@Override
	final protected void process_close() {
		if (this.cache != null) {
			// Print the Caching Statistics
			if (logger.isDebugEnabled()) {
				logger.debug("Caching Statistics: ");
				logger.debug("Cache hits: " + cache.getCacheHits());
				logger.debug("Cache miss: " + cache.getCacheMiss());
				logger.debug("Cache inserts: " + cache.getCacheInsert());
				logger.debug("Cache removes: " + cache.getCacheRemoves());
			}
			cache.close();
		}
		internal_process_close();

	}

	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {

		if (!(ipo instanceof AbstractEnrichPO)) {
			return false;
		}

		@SuppressWarnings("unchecked")
		AbstractEnrichPO<T, M> other = (AbstractEnrichPO<T, M>) ipo;
		if (!Arrays.equals(uniqueKeys, other.uniqueKeys)) {
			return false;
		}

		return true;
	}

	abstract protected void internal_process_open();

	abstract protected List<IStreamObject<?>> internal_process(T input);

	abstract protected void internal_process_close();

}
