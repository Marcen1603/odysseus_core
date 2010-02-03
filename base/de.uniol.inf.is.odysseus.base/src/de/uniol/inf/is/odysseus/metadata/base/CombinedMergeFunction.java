package de.uniol.inf.is.odysseus.metadata.base;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.IClone;

public class CombinedMergeFunction<T extends IClone> implements
		IMetadataMergeFunction<T> {

	Logger logger = LoggerFactory.getLogger(CombinedMergeFunction.class);
	private ArrayList<IInlineMetadataMergeFunction<? super T>> mergeFunctions = new ArrayList<IInlineMetadataMergeFunction<? super T>>();

	public CombinedMergeFunction() {
	}

	public CombinedMergeFunction(CombinedMergeFunction<T> cmf) throws CloneNotSupportedException {
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
			logger.error("could not merge metadata", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public CombinedMergeFunction<T> clone() throws CloneNotSupportedException{
		return new CombinedMergeFunction<T>(this);
	}

}
