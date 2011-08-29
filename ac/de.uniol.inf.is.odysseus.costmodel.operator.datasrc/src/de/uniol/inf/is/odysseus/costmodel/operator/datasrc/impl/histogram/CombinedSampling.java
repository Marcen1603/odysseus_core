package de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

public class CombinedSampling implements ISampling {

	private static final Random rand = new Random(0);

	private final List<Double> elements = new LinkedList<Double>();
	private final int maxElementCount;

	public CombinedSampling(int maxElementCount) {
		this.maxElementCount = maxElementCount;
	}

	@Override
	public synchronized void addValue(double value) {
		// add sorted
		if (!elements.isEmpty()) {
			ListIterator<Double> iterator = elements.listIterator();
			boolean added = false;
			while (iterator.hasNext()) {
				Double v = iterator.next();
				if (v >= value) {
					iterator.previous();
					iterator.add(value);
					iterator.next();
					added = true;
					break;
				}
			}
			// end of list?
			if ( !added && !iterator.hasNext())
				elements.add(value);

		} else {
			// list is empty
			elements.add(value);
		}

		if (elements.size() > maxElementCount) {
			elements.remove(rand.nextInt(elements.size())); // remove one
															// element
		}
	}

	@Override
	public synchronized List<Double> getSampledValues() {
		return elements;
	}

	@Override
	public synchronized int getSampleSize() {
		return elements.size();
	}

}
