package de.uniol.inf.is.odysseus.relational_interval.physicaloperator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import de.uniol.inf.is.odysseus.core.collection.SerializablePair;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public class TopKDataStructure<T extends Tuple<M>, M extends ITimeInterval> {

	final HashMap<Tuple<M>, SerializablePair<Double, T>> elementIndex = new HashMap<>();
	final ArrayList<SerializablePair<Double, T>> topK = new ArrayList<>();

	final Comparator<SerializablePair<Double, T>> comparator;
	final boolean orderByTimestamp;
	final private int[] uniqueAttributes;

	public TopKDataStructure(Comparator<SerializablePair<Double, T>> comparator, boolean orderByTimestamp,
			int[] uniqueAttributes) {
		this.comparator = comparator;
		this.orderByTimestamp = orderByTimestamp;
		this.uniqueAttributes = uniqueAttributes;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<SerializablePair<Double, T>> getCopyOfTopkList() {
		return (ArrayList<SerializablePair<Double, T>>) topK.clone();
	}

	public void removeSame(T object) {
		Tuple<M> key = object.restrict(uniqueAttributes, true);
		SerializablePair<Double, T> e = elementIndex.get(key);
		if (e != null){
			topK.remove(e);
		}
	}

	void insertSorted(SerializablePair<Double, T> scoredObject) {
		// 1. find position to insert with binary search
		int pos = Collections.binarySearch(topK, scoredObject, comparator);
		if (pos < 0) {
			topK.add((-(pos) - 1), scoredObject);
		} else {
			if (orderByTimestamp) {
				while (pos > 0 && topK.get(pos).getE1().equals(scoredObject.getE1())) {
					pos--;
				}
			}
			topK.add(pos, scoredObject);
		}
		updateElementIndex(scoredObject, false);
	}

	public void add(int index, SerializablePair<Double, T> element, boolean testExistance) {
		updateElementIndex(element, testExistance);
		topK.add(index, element);
	}

	private void updateElementIndex(SerializablePair<Double, T> element, boolean testExistance) {
		if (uniqueAttributes != null) {
			Tuple<M> key = element.getE2().restrict(uniqueAttributes, true);
			if (testExistance) {
				if (elementIndex.containsKey(key)) {
					topK.remove(element);
				}
			}
			elementIndex.put(key, element);
		}
	}

	public Iterator<SerializablePair<Double, T>> iterator() {
		return topK.iterator();
	}

	public int size() {
		return topK.size();
	}

	public boolean add(SerializablePair<Double, T> element, boolean testExistance) {
		updateElementIndex(element, testExistance);
		return topK.add(element);
	}

	public void clear() {
		topK.clear();
		elementIndex.clear();
	}

	public SerializablePair<Double, T> get(int index) {
		return topK.get(index);
	}

	@Override
	public String toString() {
		return topK.toString();
	}
}
