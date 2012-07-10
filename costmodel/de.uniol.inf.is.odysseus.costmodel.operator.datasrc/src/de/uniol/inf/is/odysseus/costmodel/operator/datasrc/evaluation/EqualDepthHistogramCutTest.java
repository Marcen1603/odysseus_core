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
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.EqualDepthHistogram;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.EqualDepthHistogramFactory;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.FreedmanDiaconisRule;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.IHistogramFactory;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.IIntervalCountEstimator;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.ISampling;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.LastNSampling;

public class EqualDepthHistogramCutTest {

	public static void main(String[] args) {

		DataStreamGenerator gen = new DataStreamGenerator();
		List<Double> values = gen.getNormalDistributedStream(0, 100, 2000);
		
		SDFAttribute attribute = new SDFAttribute(null,"example:value", SDFDatatype.DOUBLE);
		ISampling sampling = new LastNSampling(2000);
		IIntervalCountEstimator estimator = new FreedmanDiaconisRule();
		
		IHistogramFactory histogramFactory = new EqualDepthHistogramFactory(attribute, sampling, estimator);
		for( Double d : values ) {
			histogramFactory.addValue(d);
		}
		
		EqualDepthHistogram histogram = (EqualDepthHistogram)histogramFactory.create();
		System.out.println(histogram.printNumbers());

		EqualDepthHistogram cuttedHistogram = (EqualDepthHistogram)histogram.cutLower(50);
		System.out.println(cuttedHistogram.printNumbers());
		
		cuttedHistogram = (EqualDepthHistogram)histogram.cutHigher(50);
		System.out.println(cuttedHistogram.printNumbers());
//		
//		EqualDepthHistogram cuttedHistogram = (EqualDepthHistogram)histogram.cutHigher(30);
//		cuttedHistogram = (EqualDepthHistogram)cuttedHistogram.cutLower(30);
//		System.out.println(cuttedHistogram.printBars());
	}

}
