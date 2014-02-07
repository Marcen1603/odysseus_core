package de.uniol.inf.is.odysseus.relational_interval.physicaloperator;

import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public class NonNumericalHistogram<K,V extends IStreamObject<? extends ITimeInterval>> extends Histogram<K, V> {

	@Override
	K getMedian() {
		Iterator<Entry<K, PriorityQueue<V>>> iter = getEntrySet().iterator();
		Entry<K, PriorityQueue<V>> e = null;
		long center = (getSize()+1)/2;
		long pos = 0;
		while(pos < center && iter.hasNext()){
			e = iter.next();
			pos += e.getValue().size();
		}
		return e.getKey();	
	}


}
