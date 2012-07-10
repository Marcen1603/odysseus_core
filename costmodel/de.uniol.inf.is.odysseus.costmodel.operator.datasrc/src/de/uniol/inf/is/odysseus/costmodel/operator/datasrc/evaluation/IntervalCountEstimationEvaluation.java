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

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.FreedmanDiaconisRule;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.IIntervalCountEstimator;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.ScottsRule;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.SqrtIntervalCountEstimation;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.SturgesRule;

public class IntervalCountEstimationEvaluation {

	public static void main( String[] args ) {
		
		DataStreamGenerator gen = new DataStreamGenerator();
		List<Double> generatedData = gen.getIncreasingStream(0, 1000000, 100000);
		
		IIntervalCountEstimator scottEstimator = new ScottsRule();
		IIntervalCountEstimator sturgeEstimator = new SturgesRule();
		IIntervalCountEstimator freedmanEstimator = new FreedmanDiaconisRule();
		IIntervalCountEstimator sqrtEstimator = new SqrtIntervalCountEstimation();
		
		List<Double> data = new ArrayList<Double>(generatedData.size());
		
		
		System.out.println("DataSize,Scott,Sturge,FreedmanDiaconis,Sqrt");
		
		final int STEP = 100;
		for( int i = 0; i < generatedData.size(); i += STEP ) {
			for( int j = 0; j < STEP; j++ )
				data.add( generatedData.get(i + j));
			
			int scott = scottEstimator.estimateIntervalCount(data);
			int sturge = sturgeEstimator.estimateIntervalCount(data);
			int freedman = freedmanEstimator.estimateIntervalCount(data);
			int sqrt = sqrtEstimator.estimateIntervalCount(data);
			
			// at least one interval...
			if( scott < 1 )
				scott = 1;
			if( sturge < 1 )
				sturge = 1;
			if( freedman < 1)
				freedman = 1;
			if( sqrt < 1)
				sqrt = 1;
			
			
			StringBuilder sb = new StringBuilder();
			
			sb.append(i+STEP).append(",").append(scott).append(",").append(sturge).append(",").append(freedman).append(",").append(sqrt).append("\n");
						
			System.out.print(sb.toString());
		}
		
	}
	
}
