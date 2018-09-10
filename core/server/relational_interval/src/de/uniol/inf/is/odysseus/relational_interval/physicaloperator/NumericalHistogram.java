package de.uniol.inf.is.odysseus.relational_interval.physicaloperator;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;


public class NumericalHistogram<K extends Number,V extends IStreamObject<? extends ITimeInterval>> extends Histogram<K,V>{

	@SuppressWarnings("unchecked")
	@Override
	K getMedian() {
		if (getSize() % 2 != 0){
			return getMenoid();
		}else{
			Iterator<Entry<K, PriorityQueue<V>>> iter = getEntrySet().iterator();
			Entry<K, PriorityQueue<V>> e = null;
			long center = getSize()/2;
			long pos = 0;
			while(pos < center){
				e = iter.next();
				pos += e.getValue().size();
			}
			
			// Center is last element in current set
			if (e != null) {
				if (center == pos && iter.hasNext()) {
					Entry<K, PriorityQueue<V>> e2 = iter.next();
					Double numMedian = (e.getKey().doubleValue() + e2.getKey().doubleValue()) / 2.0;
					return (K) numMedian;
				}
				return e.getKey();
			}
			return null;
		}
	}
}
