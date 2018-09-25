package de.uniol.inf.is.odysseus.relational_interval.physicaloperator;


import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public class NonNumericalHistogram<K,V extends IStreamObject<? extends ITimeInterval>> extends Histogram<K, V> {

	@Override
	public K getMedian() {
		return getMenoid();	
	}
	
}
