package de.uniol.inf.is.odysseus.probabilistic.math;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class PBox implements Map<Interval, Interval>, Iterable<Interval> {
	private SortedMap<Interval, Interval> intervals = Collections
			.synchronizedSortedMap(new TreeMap<Interval, Interval>());

	public PBox() {

	}

	@Override
	public void clear() {
		intervals.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return intervals.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return intervals.containsValue(value);
	}

	@Override
	public Set<java.util.Map.Entry<Interval, Interval>> entrySet() {
		return Collections.unmodifiableSet(intervals.entrySet());
	}

	@Override
	public Interval get(Object key) {
		return intervals.get(key);
	}

	@Override
	public boolean isEmpty() {
		return intervals.isEmpty();
	}

	@Override
	public Set<Interval> keySet() {
		return Collections.unmodifiableSet(intervals.keySet());
	}

	@Override
	public void putAll(Map<? extends Interval, ? extends Interval> m) {
		intervals.putAll(m);
		normalize();
	}

	@Override
	public Interval remove(Object key) {
		return intervals.remove(key);
	}

	@Override
	public int size() {
		return intervals.size();
	}

	@Override
	public Collection<Interval> values() {
		return Collections.unmodifiableCollection(intervals.values());
	}

	// TODO Clean this mess-up!
	@Override
	public Interval put(Interval key, Interval value) {
		Map<Interval, Interval> overlappingIntervals = new TreeMap<Interval, Interval>();
		// Check all existing intervals for overlapping
		for (Interval keyInterval : this) {
			if ((keyInterval.intersects(key))
					&& (!get(keyInterval).equals(value))) {
				overlappingIntervals.put(keyInterval, get(keyInterval));
			}
		}
		// None found, add interval with value
		if (overlappingIntervals.size() == 0) {
			intervals.put(key, value);
		} else {
			// There are overlapping intervals
			Map<Interval, Interval> subintervals = new TreeMap<Interval, Interval>();
			subintervals.put(key, value);
			for (Interval keyInterval : overlappingIntervals.keySet()) {
				// Check for any difference
				Map<Interval, Interval> tmpIntervals = new TreeMap<Interval, Interval>();
				for (Interval f : subintervals.keySet()) {
					// Create the difference with every interval
					Interval[] diff = f.difference(keyInterval);
					// If the difference is the empty interval re-add the
					// interval
					if ((diff.length == 1) && (diff[0].isEmpty())) {
						tmpIntervals.put(f, subintervals.get(f));
					} else {
						// If the difference is not the empty interval, add the
						// difference
						for (Interval d : diff) {
							if (!d.isEmpty()) {
								tmpIntervals.put(d, subintervals.get(f));
							}
						}
					}
				}
				subintervals.clear();
				subintervals.putAll(tmpIntervals);
				// Remove the existing interval and add the smaller intervals
				Interval intervalValue = intervals.remove(keyInterval);
				Interval[] splitIntervals = keyInterval.split(key);
				for (Interval splitInterval : splitIntervals) {
					if (key.contains(splitInterval)) {
						intervals
								.put(splitInterval, intervalValue.union(value));
					} else {
						intervals.put(splitInterval, intervalValue);
					}
				}
			}
			intervals.putAll(subintervals);
		}
		normalize();
		return value;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Interval keyInterval : intervals.keySet()) {
			sb.append(keyInterval).append(": ")
					.append(intervals.get(keyInterval)).append("\n");
		}
		return sb.toString();
	}

	@Override
	public Iterator<Interval> iterator() {
		return this.intervals.keySet().iterator();
	}

	private void normalize() {
		Map<Interval, Interval> tmpIntervals = new TreeMap<Interval, Interval>(
				intervals);
		clear();
		Interval tmpInterval = null;
		Interval tmpValue = null;
		for (Interval i : tmpIntervals.keySet()) {
			if ((tmpInterval != null) && (tmpValue != null)) {
				if ((tmpValue.equals(tmpIntervals.get(i)))
						&& (tmpInterval.sup() >= i.inf())) {
					intervals.remove(tmpInterval);
					intervals.put(new Interval(tmpInterval.inf(), i.sup()),
							tmpValue);
					tmpInterval = new Interval(tmpInterval.inf(), i.sup());
				} else {
					intervals.put(i, tmpIntervals.get(i));
					tmpInterval = i;
					tmpValue = tmpIntervals.get(i);
				}
			} else {
				intervals.put(i, tmpIntervals.get(i));
				tmpInterval = i;
				tmpValue = tmpIntervals.get(i);
			}
		}
	}
}
