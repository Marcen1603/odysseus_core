package de.uniol.inf.is.odysseus.probabilistic.math;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class Evidence<T extends Comparable<T>> implements Serializable, Cloneable, Comparable<Evidence<T>>, Set<T> {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2620269042062403026L;
    private final Set<T>      events           = new TreeSet<T>();

    public Evidence(final T event) {
        this.events.add(event);
    }

    public Evidence(final T... events) {
        for (final T event : events) {
            this.events.add(event);
        }
    }

    public Evidence(final Collection<T> events) {
        for (final T event : events) {
            this.events.add(event);
        }
    }

    public Set<T> getEvents() {
        return Collections.unmodifiableSet(this.events);
    }

    public Evidence<T> union(final Evidence<T> other) {
        final Evidence<T> union = this.clone();
        union.events.addAll(other.getEvents());
        return union;
    }

    public Evidence<T> intersection(final Evidence<T> other) {
        final Evidence<T> intersection = new Evidence<T>(this.getEvents());
        intersection.events.retainAll(other.getEvents());
        return intersection;
    }

    public Evidence<T> difference(final Evidence<T> other) {
        final Evidence<T> difference = this.union(other);
        difference.events.removeAll(this.intersection(other).getEvents());
        return difference;
    }

    public List<Evidence<T>> powerSet() {
        final List<Evidence<T>> powerSet = new ArrayList<Evidence<T>>();
        for (final T event : this) {
            final List<Evidence<T>> extend = new ArrayList<Evidence<T>>();
            for (final Evidence<T> base : powerSet) {
                extend.add(base.clone());
            }
            powerSet.addAll(extend);
            final Evidence<T> singleton = new Evidence<T>(Collections.singleton(event));
            powerSet.add(singleton);
        }
        return powerSet;
    }

    public boolean isSuperSet(final Evidence<T> evidence) {
        return this.getEvents().containsAll(evidence.getEvents());
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    @Override
    public Evidence<T> clone() {
        return new Evidence<T>(this.getEvents());
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<T> iterator() {
        return this.getEvents().iterator();
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final Evidence<T> other) {
        if (this.size() != other.size()) {
            return this.size() - other.size();
        }

        final Iterator<T> thisIter = this.iterator();
        final Iterator<T> otherIter = other.iterator();
        while (thisIter.hasNext()) {
            final int compare = thisIter.next().compareTo(otherIter.next());
            if (compare != 0) {
                return compare;
            }
        }
        return 0;
    }

    /*
     * (non-Javadoc)
     * @see java.util.Set#add(java.lang.Object)
     */
    @Override
    public boolean add(final T event) {
        return this.events.add(event);
    }

    /*
     * (non-Javadoc)
     * @see java.util.Set#addAll(java.util.Collection)
     */
    @Override
    public boolean addAll(final Collection<? extends T> events) {
        return this.events.addAll(events);
    }

    /*
     * (non-Javadoc)
     * @see java.util.Set#clear()
     */
    @Override
    public void clear() {
        this.events.clear();
    }

    /*
     * (non-Javadoc)
     * @see java.util.Set#contains(java.lang.Object)
     */
    @Override
    public boolean contains(final Object event) {
        return this.events.contains(event);
    }

    /*
     * (non-Javadoc)
     * @see java.util.Set#containsAll(java.util.Collection)
     */
    @Override
    public boolean containsAll(final Collection<?> events) {
        return this.events.containsAll(events);
    }

    /*
     * (non-Javadoc)
     * @see java.util.Set#remove(java.lang.Object)
     */
    @Override
    public boolean remove(final Object event) {
        return this.events.remove(event);
    }

    /*
     * (non-Javadoc)
     * @see java.util.Set#removeAll(java.util.Collection)
     */
    @Override
    public boolean removeAll(final Collection<?> events) {
        return this.events.removeAll(events);
    }

    /*
     * (non-Javadoc)
     * @see java.util.Set#retainAll(java.util.Collection)
     */
    @Override
    public boolean retainAll(final Collection<?> events) {
        return this.events.retainAll(events);
    }

    /*
     * (non-Javadoc)
     * @see java.util.Set#size()
     */
    @Override
    public int size() {
        return this.events.size();
    }

    /*
     * (non-Javadoc)
     * @see java.util.Set#toArray()
     */
    @Override
    public Object[] toArray() {
        return this.events.toArray();
    }

    /*
     * (non-Javadoc)
     * @see java.util.Set#toArray(T[])
     */
    @SuppressWarnings("hiding")
    @Override
    public <T> T[] toArray(final T[] type) {
        return this.events.toArray(type);
    }

    /*
     * (non-Javadoc)
     * @see java.util.Set#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return this.events.isEmpty();
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + (this.events == null ? 0 : this.events.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        @SuppressWarnings("unchecked")
        final Evidence<T> other = (Evidence<T>) obj;
        if (this.events == null) {
            if (other.events != null) {
                return false;
            }
        }
        else if (!this.events.equals(other.events)) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.events.toString();
    }
}
