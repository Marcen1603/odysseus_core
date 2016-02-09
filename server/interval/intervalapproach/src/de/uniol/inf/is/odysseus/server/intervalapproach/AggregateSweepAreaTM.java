package de.uniol.inf.is.odysseus.server.intervalapproach;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;

import de.uniol.inf.is.odysseus.core.collection.IdentityArrayList;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.AbstractTISweepArea;
import de.uniol.inf.is.odysseus.sweeparea.ISweepArea;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;

/**
 * Test for another type of sweep area with less searching
 * 
 * @author Marco Grawunder
 *
 * @param <T>
 */

public class AggregateSweepAreaTM<T extends IStreamObject<? extends ITimeInterval>> extends AbstractTISweepArea<T> {

	private static final long serialVersionUID = 2260272150231590584L;

	private final TreeMap<PointInTime, T> elementsStart = new TreeMap<>();
	private final TreeMap<PointInTime, T> elementsEnd = new TreeMap<>();

	public AggregateSweepAreaTM() {

	}

	public AggregateSweepAreaTM(AggregateSweepAreaTM<T> aggregateSweepAreaTM)
			throws InstantiationException, IllegalAccessException {
		super(aggregateSweepAreaTM);
	}

	@Override
	public ISweepArea<T> newInstance(OptionMap options) {
		return new AggregateSweepAreaTM<>();
	}

	@Override
	public ITimeIntervalSweepArea<T> clone() {
		try {
			return new AggregateSweepAreaTM<T>(this);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException("Clone error");
		}
	}

	// ----------------------------------------------------------------------------------
	// Processing methods
	// -----------------------------------------------------------------------------------

	@Override
	public void insert(T element) {
		elementsStart.put(element.getMetadata().getStart(), element);
		elementsEnd.put(element.getMetadata().getEnd(), element);
	}

	@Override
	public List<T> queryOverlapsAsListExtractOutdated(ITimeInterval interval, List<T> outdated) {

		outdated.addAll(extractElementsBeforeAsList(interval.getStart()));
		
		// find all elements with a starting point between getStart() and
		// getEnd()
		SortedMap<PointInTime, T> overlapping = elementsEnd.subMap(interval.getStart(), true, interval.getEnd(), true);
		// until now only elements are found, that end before getEnd()

		// missing elements are those that start before interval.getEnd() and
		// ends after interal.getEnd();
		IdentityArrayList<T> ret = null;

		if (overlapping.size() > 0) {
			ret = new IdentityArrayList<>();
			SortedMap<PointInTime, T> additional = elementsStart.subMap(overlapping.lastKey(), true, interval.getEnd(), true);
			ret.addAll(overlapping.values());
			ret.addAll(additional.values());
		}
		return ret;

	}
	
	@Override
	public List<T> extractOverlapsAsList(ITimeInterval interval) {
		// find all elements with a starting point between getStart() and
		// getEnd()
		SortedMap<PointInTime, T> overlapping = elementsEnd.subMap(interval.getStart(), true, interval.getEnd(), true);
		// until now only elements are found, that end before getEnd()

		// missing elements are those that start before interval.getEnd() and
		// ends after interal.getEnd();
		IdentityArrayList<T> ret = null;

		if (overlapping.size() > 0) {
			ret = new IdentityArrayList<>();
			SortedMap<PointInTime, T> additional = elementsStart.subMap(overlapping.lastKey(), true, interval.getEnd(), true);
			Iterator<Entry<PointInTime, T>> iter = overlapping.entrySet().iterator();
			while(iter.hasNext()){
				ret.add(iter.next().getValue());
				iter.remove();
			}
			ret.addAll(additional.values());
		}
		return ret;
	}
	
	public List<T> extractElementsBeforeAsList(PointInTime time){
		List<T> outdated = new ArrayList<>();
		// find out dated elements, i.e. ending before interval.start
		SortedMap<PointInTime, T> out = elementsEnd.headMap(time);
		final Iterator<Entry<PointInTime, T>> iter = out.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<PointInTime, T> next = iter.next();
			outdated.add(next.getValue());
			iter.remove();
		}

		// remove out dated from ElementStart
		NavigableMap<PointInTime, T> toRem = elementsStart.headMap(time, true);
		// This could be to much elements
		// ------ --------- ----------- ------------- ----------
		// -------------------------------------
		// here the seconds element should not be deleted
		final Iterator<Entry<PointInTime, T>> iter2 = toRem.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<PointInTime, T> next = iter2.next();
			if (next.getValue().getMetadata().getEnd().before(time)) {
				iter2.remove();
			}
		}
		return outdated;
	}


	@Override
	public PointInTime getMinTs() {
		return elementsStart.firstKey();
	}

}
