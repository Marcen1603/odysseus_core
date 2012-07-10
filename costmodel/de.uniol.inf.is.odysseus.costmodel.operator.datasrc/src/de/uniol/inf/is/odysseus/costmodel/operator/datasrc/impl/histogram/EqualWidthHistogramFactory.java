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

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.IHistogram;

public class EqualWidthHistogramFactory extends AbstractIntervalSamplingHistogramFactory {

	public EqualWidthHistogramFactory(SDFAttribute attribute, ISampling sampling, IIntervalCountEstimator estimator) {
		super(attribute, sampling, estimator);
	}

	@Override
	public synchronized IHistogram createWithIntervalCount( int intervalCount) {
		List<Double> values = getSampledValues();

		// determine intervallength
		double min = values.get(0);
		double max = values.get(values.size() - 1);
		double intervalSize = (max - min) / intervalCount;

		// determine counts of intervals
		double[] counts = new double[intervalCount];
		for (Double val : values) {
			int index = (int) ((val - min) / intervalSize);

			if (index >= counts.length) {
				counts[counts.length - 1]++;
			} else {
				counts[index]++;
			}
		}
		// create equal-width-histogram
		return new EqualWidthHistogram(getAttribute(), min, max, intervalSize, counts);
	}

	@Override
	public Collection<Double> getValues() {
		return Collections.unmodifiableCollection(getSampledValues());
	}
}
