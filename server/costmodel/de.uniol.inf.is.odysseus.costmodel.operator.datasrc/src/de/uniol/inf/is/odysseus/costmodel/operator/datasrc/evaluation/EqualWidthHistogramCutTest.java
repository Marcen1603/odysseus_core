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
package de.uniol.inf.is.odysseus.costmodel.operator.datasrc.evaluation;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.EqualWidthHistogram;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.EqualWidthHistogramFactory;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.FreedmanDiaconisRule;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.IHistogramFactory;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.IIntervalCountEstimator;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.ISampling;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.LastNSampling;

public class EqualWidthHistogramCutTest {

	public static void main(String[] args) {

		DataStreamGenerator gen = new DataStreamGenerator();
		List<Double> values = gen.getNormalDistributedStream(0, 100, 2000);
		
		SDFAttribute attribute = new SDFAttribute(null,"example:value", SDFDatatype.DOUBLE, null, null, null);
		ISampling sampling = new LastNSampling(2000);
		IIntervalCountEstimator estimator = new FreedmanDiaconisRule();
		
		IHistogramFactory equalWidthHistogramFactory = new EqualWidthHistogramFactory(attribute, sampling, estimator);
		for( Double d : values ) {
			equalWidthHistogramFactory.addValue(d);
		}
		
		EqualWidthHistogram histogram = (EqualWidthHistogram)equalWidthHistogramFactory.create();
		System.out.println(histogram.printNumbers());
		
		EqualWidthHistogram cuttedHistogram = (EqualWidthHistogram)histogram.cutLower(50);
		System.out.println(cuttedHistogram.printNumbers());
		
		cuttedHistogram = (EqualWidthHistogram)histogram.cutHigher(50);
		System.out.println(cuttedHistogram.printNumbers());
		
//		EqualWidthHistogram cuttedHistogram = (EqualWidthHistogram)histogram.cutHigher(30);
//		cuttedHistogram = (EqualWidthHistogram)cuttedHistogram.cutLower(30);
//		System.out.println(cuttedHistogram.printBars());
	}

}
