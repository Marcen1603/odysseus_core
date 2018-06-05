/**
 * Copyright 2015 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.recsys.streamsimulator.data;

import java.util.Random;

/**
 * @author Cornelius Ludmann
 *
 */
public class RandomHelper {

	private static final RandomHelper INSTANCE = new RandomHelper();

	private Random random = new Random();

	private RandomHelper() {
	}

	public static RandomHelper getInstance() {
		return INSTANCE;
	}

	public synchronized int nextInt(int minInclusive, int maxExclusive) {
		return random.nextInt(maxExclusive - minInclusive) + minInclusive;
	}

	public synchronized double nextGaussian(double expectation,
			double standardDeviation) {
		return (random.nextGaussian() + expectation) * standardDeviation;
	}

	public synchronized double nextGaussian(double expectation,
			double standardDeviation, double lowerLimit, double upperLimit) {
		double d = (random.nextGaussian() + expectation) * standardDeviation;
		if (d < lowerLimit)
			return lowerLimit;
		if (d > upperLimit)
			return upperLimit;
		return d;
	}
}
