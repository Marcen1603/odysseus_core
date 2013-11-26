package de.uniol.inf.is.odysseus.wsenrich.physicaloperator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.uniol.inf.is.odysseus.cache.ICache;
import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;

abstract public class AbstractEnrichPO<T extends IMetaAttribute> extends
		AbstractPipe<Tuple<T>, Tuple<T>> {

	private final ICache cacheManager;
	private final IDataMergeFunction<Tuple<T>, T> dataMergeFunction;
	private final IMetadataMergeFunction<T> metaMergeFunction;
	private final int[] uniqueKeys;

	public AbstractEnrichPO(ICache cacheManager,
			IDataMergeFunction<Tuple<T>, T> dataMergeFunction,
			IMetadataMergeFunction<T> metaMergeFunction, int[] uniqueKeys) {
		super();
		this.cacheManager = cacheManager;
		this.dataMergeFunction = dataMergeFunction;
		this.metaMergeFunction = metaMergeFunction;
		this.uniqueKeys = uniqueKeys;
	}

	public AbstractEnrichPO(AbstractEnrichPO<T> abstractEnrichPO) {
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
	protected void process_open() throws OpenFailedException {
		dataMergeFunction.init();
	}

	@Override
	protected void process_next(Tuple<T> inputTuple, int port) {
		List<IStreamObject<?>> result = null;
		
		if (cacheManager == null) {
			result = process(inputTuple);
		} else if (uniqueKeys == null) {
			result = process_with_cache(inputTuple, inputTuple.hashCode());
		} else {
			result = process_with_cache(inputTuple,
					inputTuple.hashCodeOfSpecifiedAttributes(uniqueKeys));
		}
		enrichAndTransfer(inputTuple, result);
	}

	private List<IStreamObject<?>> process_with_cache(Tuple<T> inputTuple,
			Object key) {
		// Test if query can be answered with elements from cache
		List<IStreamObject<?>> cachedTuples = cacheManager.get(key);
		if (cachedTuples != null) {
			return cachedTuples;
		}

		// Else retrieve new elements
		List<IStreamObject<?>> queryResult = process(inputTuple);

		cacheManager.put(key, queryResult);
		return queryResult;
	}

	@SuppressWarnings("unchecked")
	private void enrichAndTransfer(Tuple<T> inputTuple,
			List<IStreamObject<?>> elements) {
		for (int i = 0; i < elements.size(); i++) {
			Tuple<T> outputTuple = dataMergeFunction.merge(inputTuple,
					(Tuple<T>) elements.get(i), metaMergeFunction,
					Order.LeftRight);
			outputTuple.setMetadata((T) inputTuple.getMetadata().clone());
			transfer(outputTuple);
		}
	}

	abstract protected ArrayList<IStreamObject<?>> process(Tuple<T> inputTuple);


	@Override
	protected void process_close() {
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

	}

	public IDataMergeFunction<Tuple<T>, T> getDataMergeFunction() {
		return dataMergeFunction;
	}

	public IMetadataMergeFunction<T> getMetaMergeFunction() {
		return metaMergeFunction;
	}

	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {

		if (!(ipo instanceof AbstractEnrichPO)) {
			return false;
		}

		@SuppressWarnings("unchecked")
		AbstractEnrichPO<T> other = (AbstractEnrichPO<T>) ipo;
		if (!Arrays.equals(uniqueKeys, other.uniqueKeys)) {
			return false;
		}
		;
		return true;
	}

}
