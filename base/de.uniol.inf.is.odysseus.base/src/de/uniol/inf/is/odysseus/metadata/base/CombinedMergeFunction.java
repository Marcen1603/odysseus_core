package de.uniol.inf.is.odysseus.metadata.base;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.base.planmanagement.query.Query;

public class CombinedMergeFunction<T extends IClone> implements
		IMetadataMergeFunction<T> {

	protected static Logger _logger = null;

	protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(Query.class);
		}
		return _logger;
	}
	
	private ArrayList<IInlineMetadataMergeFunction<? super T>> mergeFunctions = new ArrayList<IInlineMetadataMergeFunction<? super T>>();

	public CombinedMergeFunction() {
	}

	public CombinedMergeFunction(CombinedMergeFunction<T> cmf)  {
		for (IInlineMetadataMergeFunction<? super T> mf : cmf.mergeFunctions) {
			this.mergeFunctions.add(mf.clone());
		}
	}

	@Override
	public void init() {
	}

	public void add(IInlineMetadataMergeFunction<? super T> func) {
		this.mergeFunctions.add(func);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T mergeMetadata(T left, T right) {
		T mergedData;
		try {
			mergedData = (T) left.getClass().newInstance();

			for (IInlineMetadataMergeFunction<? super T> func : mergeFunctions) {
				func.mergeInto(mergedData, left, right);
			}
			return mergedData;
		} catch (Exception e) {
			getLogger().error("could not merge metadata", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public CombinedMergeFunction<T> clone() {
		return new CombinedMergeFunction<T>(this);
	}

}
