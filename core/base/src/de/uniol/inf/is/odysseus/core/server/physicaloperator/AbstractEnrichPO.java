package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import java.util.Arrays;
import java.util.List;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.cache.ICache;

abstract public class AbstractEnrichPO<T extends IStreamObject<M>, M extends IMetaAttribute> extends
		AbstractPipe<T, T> {

	private final ICache cacheManager;
	private final IDataMergeFunction<T, M> dataMergeFunction;
	private final IMetadataMergeFunction<M> metaMergeFunction;
	private final int[] uniqueKeys;

	public AbstractEnrichPO(ICache cacheManager,
			IDataMergeFunction<T, M> dataMergeFunction,
			IMetadataMergeFunction<M> metaMergeFunction, int[] uniqueKeys) {
		super();
		this.cacheManager = cacheManager;
		this.dataMergeFunction = dataMergeFunction;
		this.metaMergeFunction = metaMergeFunction;
		this.uniqueKeys = uniqueKeys;
	}

	public AbstractEnrichPO(AbstractEnrichPO<T,M> abstractEnrichPO) {
		super(abstractEnrichPO);
		this.cacheManager = abstractEnrichPO.cacheManager;
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
		
		if (cacheManager == null) {
			result = internal_process(object);
		} else if (uniqueKeys == null) {
			result = process_with_cache(object, object.hashCode());
		} else {
			result = process_with_cache(object,
					object.restrictedHashCode(uniqueKeys));
		}
		// enrichAndTransfer
		for (int i = 0; i < result.size(); i++) {
			T output = dataMergeFunction.merge(object,
					(T) result.get(i), metaMergeFunction,
					Order.LeftRight);
			output.setMetadata((M) object.getMetadata().clone());
			transfer(output);
		}	}

	private List<IStreamObject<?>> process_with_cache(T input,
			Object key) {
		// Test if query can be answered with elements from cache
		List<IStreamObject<?>> cached = cacheManager.get(key);
		if (cached != null) {
			return cached;
		}

		// Else retrieve new elements
		List<IStreamObject<?>> queryResult = internal_process(input);

		cacheManager.put(key, queryResult);
		return queryResult;
	}


	@Override
	final protected void process_close() {
		// Print the Caching Statistics
		if (this.cacheManager != null) {
			System.out.println("Caching Statistics: ");
			System.out.println("Cache hits: " + cacheManager.getCacheHits());
			System.out.println("Cache miss: " + cacheManager.getCacheMiss());
			System.out.println("Cache inserts: "
					+ cacheManager.getCacheInsert());
			System.out.println("Cache removes: "
					+ cacheManager.getCacheRemoves());
			cacheManager.close();
		}
		internal_process_close();

	}

	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {

		if (!(ipo instanceof AbstractEnrichPO)) {
			return false;
		}

		@SuppressWarnings("unchecked")
		AbstractEnrichPO<T,M> other = (AbstractEnrichPO<T,M>) ipo;
		if (!Arrays.equals(uniqueKeys, other.uniqueKeys)) {
			return false;
		}
		;
		return true;
	}


	abstract protected void internal_process_open();
	abstract protected List<IStreamObject<?>> internal_process(T input);
	abstract protected void internal_process_close();

}
