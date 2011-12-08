package de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

public class BiasedSampling implements ISampling {

	private static final Random rand = new Random(0);

	private final List<Double> elements = new LinkedList<Double>();
	private final int maxElementCount;

	public BiasedSampling(int maxElementCount) {
		this.maxElementCount = maxElementCount;
	}

	@Override
	public synchronized void addValue(double value) {
		// add sorted
		if (!elements.isEmpty()) {
			ListIterator<Double> iterator = elements.listIterator();
			while (iterator.hasNext()) {
				Double v = iterator.next();
				if (v >= value) {
					iterator.previous();
					iterator.add(value);
					iterator.next();
					break;
				}
			}
			// end of list?
			if (!iterator.hasNext())
				elements.add(value);

		} else {
			// list is empty
			elements.add(value);
		}

		if( rand.nextDouble() < (double)elements.size() / (double)maxElementCount) {
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
