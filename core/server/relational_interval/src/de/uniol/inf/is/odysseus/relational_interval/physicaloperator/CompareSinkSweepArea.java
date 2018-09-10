package de.uniol.inf.is.odysseus.relational_interval.physicaloperator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.AbstractTISweepArea;
import de.uniol.inf.is.odysseus.sweeparea.ISweepArea;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;

public class CompareSinkSweepArea<T extends IStreamObject<? extends ITimeInterval>> extends AbstractTISweepArea<T> {

	private static final long serialVersionUID = 918201320871324341L;

	public CompareSinkSweepArea(CompareSinkSweepArea<T> compareSinkSweepArea) throws InstantiationException, IllegalAccessException {
		super(compareSinkSweepArea);
	}
	
	public CompareSinkSweepArea() {
		super();
	}
	

	// Help method for compare area
	@SuppressWarnings("unchecked")
	public List<T> extractEqualElementsStartingEquals(T element, double tolerance, boolean compareMeta) {
		ArrayList<T> retval = new ArrayList<T>();
		synchronized (getElements()) {
			Iterator<T> li = getElements().iterator();
			while (li.hasNext()) {
				T s_hat = li.next();
				if (s_hat.getMetadata().getStart().equals(element.getMetadata().getStart())) {
					// if (s_hat.equalsTolerance(element, tolerance)) {
					if (s_hat.equals((IStreamObject<IMetaAttribute>) element, compareMeta)) {
						retval.add(s_hat);
						li.remove();
					}
				} else {
					break;

				}
			}
		}
		return retval;
	}

	@Override
	public ISweepArea<T> newInstance(OptionMap options) {
		return new CompareSinkSweepArea<T>();
	}

	@Override
	public ITimeIntervalSweepArea<T> clone() {
		try {
			return new CompareSinkSweepArea<T>(this);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
