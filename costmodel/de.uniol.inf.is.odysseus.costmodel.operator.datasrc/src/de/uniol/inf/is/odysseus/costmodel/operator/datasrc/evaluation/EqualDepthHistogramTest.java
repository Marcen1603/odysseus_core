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
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.IHistogram;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.EqualDepthHistogramFactory;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.FreedmanDiaconisRule;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.IHistogramFactory;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.LastNSampling;

public class EqualDepthHistogramTest {

	public static void main(String[] args) {
		
		DataStreamGenerator gen = new DataStreamGenerator();
		
		List<Double> increasing = gen.getIncreasingStream(0, 100, 1000);
		SDFAttribute attribute = new SDFAttribute(null,"example:attribute", SDFDatatype.DOUBLE);
		
		IHistogramFactory factory = new EqualDepthHistogramFactory( attribute, new LastNSampling(1000), new FreedmanDiaconisRule() );
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
