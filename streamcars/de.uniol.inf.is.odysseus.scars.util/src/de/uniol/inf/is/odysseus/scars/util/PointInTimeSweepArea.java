package de.uniol.inf.is.odysseus.scars.util;

import java.util.ArrayList;
import java.util.Iterator;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSweepArea;


public class PointInTimeSweepArea<M extends ITimeInterval & IProbability> extends AbstractSweepArea<MVRelationalTuple<M>> {

	@Override
	public void purgeElementsBefore(PointInTime time) {

	}

	@Override
	public Iterator<MVRelationalTuple<M>> extractElementsBefore(PointInTime time) {
		return null;
	}

	@Override
	public void insert(MVRelationalTuple<M> element) {
		this.elements.add(element);
	}

	@Override
	public void purgeElements(MVRelationalTuple<M> element, Order order) {
		PointInTime time = element.getMetadata().getStart();
		for(int i = 0; i < this.elements.size(); i++) {
			if(this.elements.get(i).getMetadata().getStart().before(time)) {
				this.elements.remove(i);
			}
		}
	}

	@Override
	public Iterator<MVRelationalTuple<M>> query(MVRelationalTuple<M> element, Order order) {
		ArrayList<MVRelationalTuple<M>> retval = new ArrayList<MVRelationalTuple<M>>();
		Iterator<MVRelationalTuple<M>> it = elements.iterator();
		PointInTime pt = element.getMetadata().getStart();
		while(it.hasNext()) {
			MVRelationalTuple<M> tuple = it.next();
			if(pt.equals(tuple.getMetadata().getStart())) {
				retval.add(tuple);
			}
		}
		return retval.iterator();
	}

	@Override
	public Iterator<MVRelationalTuple<M>> queryCopy(MVRelationalTuple<M> element, Order order) {
		return null;
	}

	@Override
	public Iterator<MVRelationalTuple<M>> extractElements(MVRelationalTuple<M> element, Order order) {
		return null;
	}

	@Override
	public boolean remove(MVRelationalTuple<M> element) {
		return this.elements.remove(element);
	}

}
