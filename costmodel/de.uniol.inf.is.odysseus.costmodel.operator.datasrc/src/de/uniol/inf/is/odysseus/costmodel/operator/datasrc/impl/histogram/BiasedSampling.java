/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
