package de.uniol.inf.is.odysseus.costmodel.operator.datasrc.evaluation;

import java.util.List;

import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.EqualWidthHistogram;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.EqualWidthHistogramFactory;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.FreedmanDiaconisRule;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.IHistogramFactory;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.IIntervalCountEstimator;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.ISampling;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.LastNSampling;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

public class EqualWidthHistogramCutTest {

	public static void main(String[] args) {

		DataStreamGenerator gen = new DataStreamGenerator();
		List<Double> values = gen.getNormalDistributedStream(0, 100, 2000);
		
		SDFAttribute attribute = new SDFAttribute("example:value");
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
