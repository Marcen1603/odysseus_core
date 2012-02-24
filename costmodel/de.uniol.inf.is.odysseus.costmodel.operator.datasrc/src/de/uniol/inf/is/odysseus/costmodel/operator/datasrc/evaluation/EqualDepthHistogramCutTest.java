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
