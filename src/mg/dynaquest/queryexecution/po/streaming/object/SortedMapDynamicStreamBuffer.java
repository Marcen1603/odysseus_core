package mg.dynaquest.queryexecution.po.streaming.object;

import java.util.Iterator;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

// Spezielle Implementierung einer SweepArea
// TODO: Behandlung der Query und Remove Prdikate

public class SortedMapDynamicStreamBuffer extends DynamicStreamBuffer {
	
	SortedMap<TimeInterval, StreamExchangeElement> elements = new TreeMap<TimeInterval, StreamExchangeElement>();

	public void insert(StreamExchangeElement e) {
		elements.put(e.getValidity(), e);		
	}
	
	public void remove(StreamExchangeElement e){
		elements.remove(e.getValidity());
	}

	public Iterator<StreamExchangeElement> query(StreamExchangeElement e) {
		SortedSet<StreamExchangeElement> set = new TreeSet<StreamExchangeElement>();
		for (Entry<TimeInterval, StreamExchangeElement> es:elements.entrySet()){
			if (getQueryPredicate().evaluate(e, es.getValue())){ 
				set.add(es.getValue());
			}
		}
		return set.iterator();
	}

	public void reorganize(StreamExchangeElement e, DynamicStreamBuffer outputBuffer) {
		while (elements.size() > 0){
			TimeInterval intervall = elements.firstKey();
			StreamExchangeElement es = elements.get(intervall);
			if (getRemovePredicate().evaluate(e, es)){ 
				if (outputBuffer != null){
					outputBuffer.insert(es);
				}
				elements.remove(intervall);
			}
		}
	}

	@Override
	public int size() {
		return elements.size();
	}

	@Override
	public StreamExchangeElement top() {
		return elements.get(elements.firstKey());
	}
	

}
