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

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.IHistogram;

public abstract class AbstractIntervalSamplingHistogramFactory extends AbstractSamplingHistogramFactory {

	private IIntervalCountEstimator intervalCountEstimator;
	
	public AbstractIntervalSamplingHistogramFactory(SDFAttribute attribute, ISampling sampling, IIntervalCountEstimator estimator) {
		super(attribute, sampling);
		
		this.intervalCountEstimator = estimator;
	}

	@Override
	public final IHistogram create() {
		List<Double> values = getSampledValues();
		if( values == null || values.isEmpty() ) 
			return null;
		
		int count = intervalCountEstimator.estimateIntervalCount(getSampledValues());
		return createWithIntervalCount(count);
	}

	protected abstract IHistogram createWithIntervalCount( int count );
}
