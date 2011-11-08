package de.uniol.inf.is.odysseus.costmodel.operator.datasrc.evaluation;

import java.util.List;

import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.IHistogram;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.EqualWidthHistogramFactory;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.FreedmanDiaconisRule;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.IHistogramFactory;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.LastNSampling;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

public class EqualWidthHistogramTest {

	public static void main(String[] args) {
		
		DataStreamGenerator gen = new DataStreamGenerator();
		
		List<Double> increasing = gen.getIncreasingStream(0, 100, 1000);
		SDFAttribute attribute = new SDFAttribute(null,"example:attribute", SDFDatatype.DOUBLE);
		
		IHistogramFactory factory = new EqualWidthHistogramFactory( attribute, new LastNSampling(1000), new FreedmanDiaconisRule() );
		for( double d : increasing ) 
			factory.addValue(d);
		
		
		IHistogram histogram = factory.create();
		
		System.out.println(histogram);
		
		System.out.println();
		
		for( int i = 0; i <= 100; i+= 10)
			printOccurences( histogram, i );
		
		for( int i = 0; i <= 90; i+= 10)
			printOccurencesRange( histogram, i, i+10);
		
		for( int i = 0; i <= 80; i+= 20)
			printOccurencesRange( histogram, i, i+20);

		printOccurencesRange( histogram, 0, 50);
		printOccurencesRange( histogram, 50, 100);
	}
	
	public static void printOccurences( IHistogram histogram, double value ) {
		System.out.println("Occurences of " + value + " : " + histogram.getOccurences(value));
	}
	
	public static void printOccurencesRange( IHistogram histogram, double from, double to ) {
		System.out.println("Occurences of range " + from + " to " + to + " : " + histogram.getOccurenceRange(from, to));
	}
}
