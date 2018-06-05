package de.uniol.inf.is.odysseus.relational_interval.physicaloperator;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

abstract public class Histogram<K, V extends IStreamObject<? extends ITimeInterval>> {

	private long size;
	private TreeMap<K, PriorityQueue<V>> h = new TreeMap<>();

	protected Set<Entry<K, PriorityQueue<V>>> getEntrySet() {
		return h.entrySet();
	}

	protected long getSize() {
		// DEBUG
		// Iterator<Entry<K, PriorityQueue<V>>> iter = getEntrySet().iterator();
		// long calcedSize = 0;
		// while (iter.hasNext()){
		// calcedSize += iter.next().getValue().size();
		// }
		// if (calcedSize != size){
		// throw new RuntimeException("ERRRORRRRR");
		// }
		return size;
	}

	public void addElement(K key, V value) {
		PriorityQueue<V> l = h.get(key);
		if (l == null) {
			// Sort elements concerning end time stamp, this order is used to
			// remove elements by end time stamp
			l = new PriorityQueue<V>(11, new Comparator<V>() {
				@Override
				public int compare(V o1, V o2) {
					return o1.getMetadata().getEnd()
							.compareTo(o2.getMetadata().getEnd());
				}
			});
			h.put(key, l);
		}
		l.add(value);
		size++;
	}

	public void cleanUp(PointInTime p) {

		Iterator<PriorityQueue<V>> listIter = h.values().iterator();
		while (listIter.hasNext()) {
			PriorityQueue<V> l = listIter.next();
			V e = l.peek();
			while (e != null && e.getMetadata().getEnd().beforeOrEquals(p)) {
				l.poll();
				size--;
				e = l.peek();
			}
			if (l.isEmpty()) {
				listIter.remove();
			}
		}
	}

	public List<K> getPercentiles(List<Double> percentiles) {
		List<K> values = new LinkedList<>();
		Iterator<Double> pIter = percentiles.iterator();
		Iterator<Entry<K, PriorityQueue<V>>> iter = getEntrySet().iterator();
		Entry<K, PriorityQueue<V>> e = null;
		long pos = 0;
		while (pIter.hasNext()) {
			long toFind = Math.round(getSize() * pIter.next());
			while (pos <= toFind && iter.hasNext()) {
				e = iter.next();
				pos += e.getValue().size();	
			}
			if (e != null) {
				values.add(e.getKey());
			}
		}
		return values;
	}

	public K getMenoid() {
		Iterator<Entry<K, PriorityQueue<V>>> iter = getEntrySet().iterator();
		Entry<K, PriorityQueue<V>> e = null;
		long center = getSize() / 2;
		long pos = 0;
		while (pos <= center && iter.hasNext()) {
			e = iter.next();
			pos += e.getValue().size();
		}
		if (e != null) {
			return e.getKey();
		}
		return null;
	}

	abstract K getMedian();

}
