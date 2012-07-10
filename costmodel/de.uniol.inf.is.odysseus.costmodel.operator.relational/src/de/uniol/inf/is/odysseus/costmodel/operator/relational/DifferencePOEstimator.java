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
package de.uniol.inf.is.odysseus.costmodel.operator.relational;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.costmodel.operator.DataStream;
import de.uniol.inf.is.odysseus.costmodel.operator.IDataStream;
import de.uniol.inf.is.odysseus.costmodel.operator.IOperatorEstimator;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorDetailCost;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorEstimation;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.IHistogram;
import de.uniol.inf.is.odysseus.costmodel.operator.util.CPURateSaver;
import de.uniol.inf.is.odysseus.costmodel.operator.util.EstimatorHelper;
import de.uniol.inf.is.odysseus.intervalapproach.AntiJoinTIPO;

@SuppressWarnings("rawtypes")
public class DifferencePOEstimator implements IOperatorEstimator<AntiJoinTIPO> {

	@Override
	public Class<AntiJoinTIPO> getOperatorClass() {
		return AntiJoinTIPO.class;
	}

	@Override
	public OperatorEstimation estimateOperator(AntiJoinTIPO instance, List<OperatorEstimation> prevOperators, Map<SDFAttribute, IHistogram> baseHistograms) {
		
		OperatorEstimation prevOp1 = prevOperators.get(0);
		OperatorEstimation prevOp2 = prevOperators.get(1);
		
		OperatorEstimation estimation = new OperatorEstimation(instance);
		
		
		/** 1. Selectivity **/
		IDataStream c0 = prevOp1.getDataStream();
		IDataStream c1 = prevOp2.getDataStream();
		double selectivity = EstimatorHelper.getSelectivityMetadata(instance);
		double falsePropability = 1.0; //WS, dass zwei Tupel unterschiedlich sind
		if( selectivity < 0.0 ) {
			double truePropability = 1.0;
			for (SDFAttribute attribute : instance.getOutputSchema()) {
				truePropability *= getEqualsSelectivity(prevOp1.getHistograms().get(attribute), prevOp2.getHistograms().get(attribute));
			}
			falsePropability = (1 - truePropability);
			double output = c0.getDataRate() * ( c1.getDataRate() * c1.getIntervalLength() * falsePropability );
			selectivity = output / ( c0.getDataRate() * c1.getDataRate() * c1.getIntervalLength() * c0.getIntervalLength()) ;
		} else {
			falsePropability = selectivity * c0.getIntervalLength();
		}
		estimation.setSelectivity(selectivity);

//		IDataStream c0 = prevOp1.getDataStream();
//		IDataStream c1 = prevOp2.getDataStream();
//		double selectivity = EstimatorHelper.getSelectivityMetadata(instance);
//		double falsePropability = selectivity * c0.getIntervalLength();; //WS, dass zwei Tupel unterschiedlich sind
//		System.out.format("%-8.6f, ", selectivity);
//		
//		double truePropability = 1.0;
//		for (SDFAttribute attribute : instance.getOutputSchema()) {
//			truePropability *= getEqualsSelectivity(prevOp1.getHistograms().get(attribute), prevOp2.getHistograms().get(attribute));
//		}
//		double falsePropability2 = (1 - truePropability);
//		double output = c0.getDataRate() * ( c1.getDataRate() * c1.getIntervalLength() * falsePropability2 );
//		double selectivity2 = output / ( c0.getDataRate() * c1.getDataRate() * c1.getIntervalLength() * c0.getIntervalLength()) ;
//		System.out.format("%-8.6f\n", selectivity2);
//		
//		falsePropability = falsePropability >= 0 ? falsePropability : falsePropability2; 
//		estimation.setSelectivity(selectivity);


		/** 2. Histograms **/
//		System.out.println("DifferencePO - INPUT");
//		for (SDFAttribute attribute : prevOp1.getHistograms().keySet()) {
//			System.out.println(attribute);
//			System.out.println( prevOp1.getHistograms().get(attribute).toString());
//			System.out.println();
//		}
//		
		Map<SDFAttribute, IHistogram> histograms = new HashMap<SDFAttribute, IHistogram>();
		for( SDFAttribute attribute : prevOp1.getHistograms().keySet() ) {
			IHistogram histogram = prevOp1.getHistograms().get(attribute).clone();
			
			double[] borders = histogram.getIntervalBorders();
			for( int i = 0; i < borders.length - 1; i++ ) {
				histogram.setOccurences(i, histogram.getOccurences(borders[i]) * falsePropability);
			}
			
			histograms.put(attribute, histogram);
		}
		
//		System.out.println("DifferencePO - OUTPUT");
//		for (SDFAttribute attribute : histograms.keySet()) {
//			System.out.println(attribute);
//			System.out.println( histograms.get(attribute).toString());
//			System.out.println();
//		}
//
		estimation.setHistograms(histograms);
				
		/** 3. Datastream **/
		IDataStream prevStream = prevOp1.getDataStream();
		IDataStream prevStream2 = prevOp2.getDataStream();
		double datarate = EstimatorHelper.getDatarateMetadata(instance);
		if( datarate < 0.0 ) {
			datarate = prevStream.getDataRate() * falsePropability;
		}
		double intervalLength = ( Math.abs( prevStream.getIntervalLength() - prevStream2.getIntervalLength())) / 2.0;
		estimation.setDataStream(new DataStream(instance, datarate, intervalLength));

//		IDataStream prevStream = prevOp1.getDataStream();
//		IDataStream prevStream2 = prevOp2.getDataStream();
//		double datarate = EstimatorHelper.getDatarateMetadata(instance);
//		double datarate2 = prevStream.getDataRate() * falsePropability;
//		System.out.format("%-8.6f,", datarate);
//		System.out.format("%-8.6f%n", datarate2);
//		
//		double intervalLength = ( Math.abs( prevStream.getIntervalLength() - prevStream2.getIntervalLength())) / 2.0;
//		estimation.setDataStream(new DataStream(instance, datarate >= 0 ? datarate : datarate2, intervalLength));

		
		/** 4. DetailCost **/
		double cpu = EstimatorHelper.getMedianCPUTimeMetadata(instance);
//		System.out.format("%-8.6f\n", cpu);
		double cpuCost = 0.0;
		if( cpu < 0.0 ) {
			cpuCost = CPURateSaver.getInstance().get(instance.getClass().getSimpleName()) *  
					(c0.getDataRate() * c1.getDataRate() * c1.getIntervalLength() +
					 c1.getDataRate() * c0.getDataRate() * c0.getIntervalLength() );
		} else {
			cpuCost = CPURateSaver.getInstance().get(instance.getClass().getSimpleName()) *  
					(c0.getDataRate() * c1.getDataRate() * c1.getIntervalLength() +
							 c1.getDataRate() * c0.getDataRate() * c0.getIntervalLength() );
			CPURateSaver.getInstance().set(instance.getClass().getSimpleName(), cpu);
		}
		double memCost = EstimatorHelper.sizeInBytes(instance.getOutputSchema()) * ( c0.getDataRate() * c0.getIntervalLength() + c1.getDataRate() * c1.getIntervalLength());
		estimation.setDetailCost(new OperatorDetailCost(instance, memCost, cpuCost));
		
		return estimation;
	}
	
	private static double getEqualsSelectivity( IHistogram hist1, IHistogram hist2 ) {
		if( hist1 == null || hist2 == null )
			return 1.0;
		
		IHistogram hist1Relative = hist1.toRelative();
		IHistogram hist2Relative = hist2.toRelative();
		
		// Calcualte Selectivity
		double[] borders = hist2Relative.getIntervalBorders();
		double selectivity = 0.0;
		for( int i = 0; i < borders.length - 1; i++ ) {
			double intervalStart = borders[i];
			double intervalEnd = borders[i+1];
			
			double prob1 = hist2Relative.getOccurences(intervalStart);
			double prob2 = hist1Relative.getOccurenceRange(intervalStart, intervalEnd);
			double prob = prob1 * prob2;
			selectivity += prob;
		}
		
		return selectivity;
	}

}
