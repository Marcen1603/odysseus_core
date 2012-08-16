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
    private final SortedMap<Interval, Interval> intervals = Collections
                                                                  .synchronizedSortedMap(new TreeMap<Interval, Interval>());

    public PBox() {

    }

    @Override
    public void clear() {
        this.intervals.clear();
    }

    @Override
    public boolean containsKey(final Object key) {
        return this.intervals.containsKey(key);
    }

    @Override
    public boolean containsValue(final Object value) {
        return this.intervals.containsValue(value);
    }

    @Override
    public Set<java.util.Map.Entry<Interval, Interval>> entrySet() {
        return Collections.unmodifiableSet(this.intervals.entrySet());
    }

    @Override
    public Interval get(final Object key) {
        return this.intervals.get(key);
    }

    @Override
    public boolean isEmpty() {
        return this.intervals.isEmpty();
    }

    @Override
    public Set<Interval> keySet() {
        return Collections.unmodifiableSet(this.intervals.keySet());
    }

    @Override
    public void putAll(final Map<? extends Interval, ? extends Interval> m) {
        this.intervals.putAll(m);
        this.normalize();
    }

    @Override
    public Interval remove(final Object key) {
        return this.intervals.remove(key);
    }

    @Override
    public int size() {
        return this.intervals.size();
    }

    @Override
    public Collection<Interval> values() {
        return Collections.unmodifiableCollection(this.intervals.values());
    }

    // TODO Clean this mess-up!
    @Override
    public Interval put(final Interval key, final Interval value) {
        final Map<Interval, Interval> overlappingIntervals = new TreeMap<Interval, Interval>();
        // Check all existing intervals for overlapping
        for (final Interval keyInterval : this) {
            if ((keyInterval.intersects(key)) && (!this.get(keyInterval).equals(value))) {
                overlappingIntervals.put(keyInterval, this.get(keyInterval));
            }
        }
        // None found, add interval with value
        if (overlappingIntervals.size() == 0) {
            this.intervals.put(key, value);
        }
        else {
            // There are overlapping intervals
            final Map<Interval, Interval> subintervals = new TreeMap<Interval, Interval>();
            subintervals.put(key, value);
            for (final Interval keyInterval : overlappingIntervals.keySet()) {
                // Check for any difference
                final Map<Interval, Interval> tmpIntervals = new TreeMap<Interval, Interval>();
                for (final Interval f : subintervals.keySet()) {
                    // Create the difference with every interval
                    final Interval[] diff = f.difference(keyInterval);
                    // If the difference is the empty interval re-add the
                    // interval
                    if ((diff.length == 1) && (diff[0].isEmpty())) {
                        tmpIntervals.put(f, subintervals.get(f));
                    }
                    else {
                        // If the difference is not the empty interval, add the
                        // difference
                        for (final Interval d : diff) {
                            if (!d.isEmpty()) {
                                tmpIntervals.put(d, subintervals.get(f));
                            }
                        }
                    }
                }
                subintervals.clear();
                subintervals.putAll(tmpIntervals);
                // Remove the existing interval and add the smaller intervals
                final Interval intervalValue = this.intervals.remove(keyInterval);
                final Interval[] splitIntervals = keyInterval.split(key);
                for (final Interval splitInterval : splitIntervals) {
                    if (key.contains(splitInterval)) {
                        this.intervals.put(splitInterval, intervalValue.union(value));
                    }
                    else {
                        this.intervals.put(splitInterval, intervalValue);
                    }
                }
            }
            this.intervals.putAll(subintervals);
        }
        this.normalize();
        return value;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (final Interval keyInterval : this.intervals.keySet()) {
            sb.append(keyInterval).append(": ").append(this.intervals.get(keyInterval)).append("\n");
        }
        return sb.toString();
    }

    @Override
    public Iterator<Interval> iterator() {
        return this.intervals.keySet().iterator();
    }

    private void normalize() {
        final Map<Interval, Interval> tmpIntervals = new TreeMap<Interval, Interval>(this.intervals);
        this.clear();
        Interval tmpInterval = null;
        Interval tmpValue = null;
        for (final Interval i : tmpIntervals.keySet()) {
            if ((tmpInterval != null) && (tmpValue != null)) {
                if ((tmpValue.equals(tmpIntervals.get(i))) && (tmpInterval.sup() >= i.inf())) {
                    this.intervals.remove(tmpInterval);
                    this.intervals.put(new Interval(tmpInterval.inf(), i.sup()), tmpValue);
                    tmpInterval = new Interval(tmpInterval.inf(), i.sup());
                }
                else {
                    this.intervals.put(i, tmpIntervals.get(i));
                    tmpInterval = i;
                    tmpValue = tmpIntervals.get(i);
                }
            }
            else {
                this.intervals.put(i, tmpIntervals.get(i));
                tmpInterval = i;
                tmpValue = tmpIntervals.get(i);
            }
        }
    }
}
