package de.uniol.inf.is.odysseus.costmodel.operator.datasrc.evaluation;

import java.util.List;

import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.IHistogram;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.BiasedSampling;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.EqualWidthHistogramFactory;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.FreedmanDiaconisRule;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.IHistogramFactory;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

public class EqualWidthHistogramEvaluationBiasedSampling {

	public static void main(String[] args) {
		DataStreamGenerator gen = new DataStreamGenerator();

		// example-data
		List<Double> equalValues = gen.getEqualDistributedStream(0, 100, 10000);
		List<Double> increasingValues = gen.getIncreasingStream(0, 100, 10000);
		List<Double> normalValues = gen.getNormalDistributedStream(0, 100, 10000);
		List<Double> normalIncreasedValues = gen.getJumpingStream(0, 100, 10000);
		
		SDFAttribute attr = new SDFAttribute(null,"example:double", SDFDatatype.DOUBLE);

		/**
		 *  Equal Distributed Values
		 */
		IHistogramFactory factory3 = new EqualWidthHistogramFactory(attr, new BiasedSampling(2000), new FreedmanDiaconisRule());

		// Simulate data stream
		for (Double value : equalValues) {
			factory3.addValue(value);
		}
		
		// print results
		IHistogram hist3 = factory3.create();
		System.out.println("EqualWidth-Histogram : Equal distributed Values, Biased Sampling");
		System.out.println(hist3);
		System.out.println();

		/**
		 *  Increasing Values
		 */
		IHistogramFactory factory4 = new EqualWidthHistogramFactory(attr, new BiasedSampling(2000), new FreedmanDiaconisRule());

		// Simulate data stream
		for (Double value : increasingValues) {
			factory4.addValue(value);
		}
		
		// print results
		IHistogram hist4 = factory4.create();
		System.out.println("EqualWidth-Histogram : Increasing Values, Biased Sampling");
		System.out.println(hist4);
		System.out.println();

		/**
		 *  Normalized Values
		 */
		IHistogramFactory factory = new EqualWidthHistogramFactory(attr, new BiasedSampling(2000), new FreedmanDiaconisRule());

		// Simulate data stream
		for (Double value : normalValues) {
			factory.addValue(value);
		}
		
		// print results
		IHistogram hist = factory.create();
		System.out.println("EqualWidth-Histogram : Normalized Values, Biased Sampling");
		System.out.println(hist);
		System.out.println();
		
		/**
		 *  Normalized Increasing Values
		 */
		IHistogramFactory factory2 = new EqualWidthHistogramFactory(attr, new BiasedSampling(2000), new FreedmanDiaconisRule());

		// Simulate data stream
		for (Double value : normalIncreasedValues) {
			factory2.addValue(value);
		}
		
		// print results
		IHistogram hist2 = factory2.create();
		System.out.println("EqualWidth-Histogram : Normalized increasing Values, Biased Sampling");
		System.out.println(hist2);
		System.out.println();
	}
}
