package de.uniol.inf.is.odysseus.costmodel.operator.datasrc.evaluation;

import java.util.List;

import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.IHistogram;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.EqualDepthHistogramFactory;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.EqualWidthHistogramFactory;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.FreedmanDiaconisRule;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.IHistogramFactory;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.LastNSampling;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

public class HistogramTypeEvaluation {

	private static List<Double> equalDistributed;
	private static List<Double> increasing;
	private static List<Double> normalized;
	private static List<Double> jumping;
	private static IHistogram histWidthEqual;
	private static IHistogram histWidthIncreasing;
	private static IHistogram histWidthNormalized;
	private static IHistogram histWidthJumping;
	private static IHistogram histDepthEqual;
	private static IHistogram histDepthNormalized;
	private static IHistogram histDepthIncreasing;
	private static IHistogram histDepthJumping;

	public static void main( String[] args ) {
		prepare();
		
		testSelectivities(90, 100);
		testSelectivities(70, 100);
	}
	
	private static void prepare() {
		// Evaluation of different Histogram types
		DataStreamGenerator gen = new DataStreamGenerator();
		
		equalDistributed = gen.getEqualDistributedStream(0, 100, 2000);
		increasing = gen.getIncreasingStream(0, 100, 2000);
		normalized = gen.getNormalDistributedStream(0, 100, 2000);
		jumping = gen.getJumpingStream(0, 100, 2000);
		SDFAttribute attribute = new SDFAttribute("src:example");

		// Create Histograms
		
		// EqualWidth
		IHistogramFactory equalDistributedFactory = new EqualWidthHistogramFactory(attribute, new LastNSampling(2000), new FreedmanDiaconisRule());
		IHistogramFactory increasingFactory = new EqualWidthHistogramFactory(attribute, new LastNSampling(2000), new FreedmanDiaconisRule());
		IHistogramFactory normalizedFactory = new EqualWidthHistogramFactory(attribute, new LastNSampling(2000), new FreedmanDiaconisRule());
		IHistogramFactory jumpingFactory = new EqualWidthHistogramFactory(attribute, new LastNSampling(2000), new FreedmanDiaconisRule());
		
		//EqualDepth
		IHistogramFactory equalDistributedFactory2 = new EqualDepthHistogramFactory(attribute, new LastNSampling(2000), new FreedmanDiaconisRule());
		IHistogramFactory increasingFactory2 = new EqualDepthHistogramFactory(attribute, new LastNSampling(2000), new FreedmanDiaconisRule());
		IHistogramFactory normalizedFactory2 = new EqualDepthHistogramFactory(attribute, new LastNSampling(2000), new FreedmanDiaconisRule());
		IHistogramFactory jumpingFactory2 = new EqualDepthHistogramFactory(attribute, new LastNSampling(2000), new FreedmanDiaconisRule());
				
		// Adding Values
		insertValues(equalDistributedFactory, equalDistributed);
		insertValues(increasingFactory, increasing);
		insertValues(normalizedFactory, normalized);
		insertValues(jumpingFactory, jumping);
		
		insertValues(equalDistributedFactory2, equalDistributed);
		insertValues(increasingFactory2, increasing);
		insertValues(normalizedFactory2, normalized);
		insertValues(jumpingFactory2, jumping);
		
		histWidthEqual = equalDistributedFactory.create();
		histWidthIncreasing = increasingFactory.create();
		histWidthNormalized = normalizedFactory.create();
		histWidthJumping = jumpingFactory.create();
		
		histDepthEqual = equalDistributedFactory2.create();
		histDepthIncreasing = increasingFactory2.create();
		histDepthNormalized = normalizedFactory2.create();
		histDepthJumping = jumpingFactory2.create();
	}
	
	private static void testSelectivities( double from, double to ) {
		
		// create real selectivities
		double equalSelectivity = getSelectivity(equalDistributed, from, to);
		double increasingSelectivity = getSelectivity(increasing, from, to);
		double normalizedSelectivity = getSelectivity(normalized, from, to);
		double jumpingSelectivity = getSelectivity(jumping, from, to);
		
		if( isDoubleEqual(from, to)) {
			System.out.println("** Attribute = " + from + " **");
		} else {
			System.out.println("** " + from + " <= Attribute < " + to + " **");
		}
		System.out.println();
		System.out.println("Real Selectivities");
		System.out.println("\tEqualDistributed : " + equalSelectivity);
		System.out.println("\tIncreasing       : " + increasingSelectivity);
		System.out.println("\tNormalized       : " + normalizedSelectivity);
		System.out.println("\tJumping          : " + jumpingSelectivity);
		System.out.println();
		
		// Get Selectivities from Histograms
		System.out.println("Equal Width:");
		System.out.println("\tEqualDistributed : " + getSelectivity(histWidthEqual, from, to));
		System.out.println("\tIncreasing       : " + getSelectivity(histWidthIncreasing, from, to));
		System.out.println("\tNormalized       : " + getSelectivity(histWidthNormalized, from, to));
		System.out.println("\tJumping          : " + getSelectivity(histWidthJumping, from, to));
		
		System.out.println();
		System.out.println("Equal Depth:");
		System.out.println("\tEqualDistributed : " + getSelectivity(histDepthEqual, from, to));
		System.out.println("\tIncreasing       : " + getSelectivity(histDepthIncreasing, from, to));
		System.out.println("\tNormalized       : " + getSelectivity(histDepthNormalized, from, to));
		System.out.println("\tJumping          : " + getSelectivity(histDepthJumping, from, to));
		
	}
	
	private static void insertValues( IHistogramFactory factory, List<Double> values ) {
		for( Double value : values ) {
			factory.addValue(value);
		}
	}
	
	private static double getSelectivity( IHistogram hist, double from, double to ) {
		if( isDoubleEqual(from, to) ) {
			double occurences = hist.getOccurences(from);
			return occurences / hist.getValueCount();
		} else {
			double occurences = hist.getOccurenceRange(from, to);
			return occurences / hist.getValueCount();
		}
	}
	
	private static double getSelectivity( List<Double> list, double from, double to ) {
		int count = 0;
		if( !isDoubleEqual(from, to) ) {
			for( Double val : list ) {
				if( val >= from && val < to ) 
					count++;
			}
		} else {
			for( Double val : list ) {
				if( isDoubleEqual(val, from) ) 
					count++;
			}
		}
		return (double)count / list.size();
	}
	
	private static boolean isDoubleEqual( double a, double b ) {
		return Math.abs(a - b) < 0.00001;
	}
}
