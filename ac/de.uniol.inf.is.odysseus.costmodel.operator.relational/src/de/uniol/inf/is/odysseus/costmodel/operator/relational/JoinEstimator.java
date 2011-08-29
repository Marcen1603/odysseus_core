package de.uniol.inf.is.odysseus.costmodel.operator.relational;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.costmodel.operator.DataStream;
import de.uniol.inf.is.odysseus.costmodel.operator.IDataStream;
import de.uniol.inf.is.odysseus.costmodel.operator.IOperatorEstimator;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorDetailCost;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorEstimation;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.IHistogram;
import de.uniol.inf.is.odysseus.costmodel.operator.util.CPURateSaver;
import de.uniol.inf.is.odysseus.costmodel.operator.util.EstimatorHelper;
import de.uniol.inf.is.odysseus.costmodel.operator.util.PredicateHistogramHelper;
import de.uniol.inf.is.odysseus.costmodel.operator.util.PredicateSelectivityHelper;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.predicate.TruePredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

@SuppressWarnings("rawtypes")
public class JoinEstimator implements IOperatorEstimator<JoinTIPO> {

	@Override
	public Class<JoinTIPO> getOperatorClass() {
		return JoinTIPO.class;
	}

	@Override
	public OperatorEstimation estimateOperator(JoinTIPO instance, List<OperatorEstimation> prevOperators, Map<SDFAttribute, IHistogram> baseHistograms) {

		List<Map<SDFAttribute, IHistogram>> histograms = new ArrayList<Map<SDFAttribute, IHistogram>>();
		for (OperatorEstimation estimation : prevOperators)
			histograms.add(estimation.getHistograms());

		IPredicate predicate = instance.getPredicate();
		if (predicate instanceof TruePredicate || predicate.toString().equalsIgnoreCase("true"))
			return estimateCrossProductOperator(instance, prevOperators, baseHistograms);

		OperatorEstimation estimation = new OperatorEstimation(instance);
		/** 1. Histograms **/
		// System.out.println("JoinTIPO - INPUT");
		// for( Map<SDFAttribute, IHistogram> map : histograms ) {
		// for( SDFAttribute attribute : map.keySet() ) {
		// System.out.println(attribute);
		// System.out.println(map.get(attribute).toString());
		// System.out.println();
		// }
		// }

		PredicateHistogramHelper helper = new PredicateHistogramHelper(predicate, histograms);
		estimation.setHistograms(helper.getHistograms());

		// System.out.println();
		// System.out.println("JoinTIPO - OUTPUT");
		// for( SDFAttribute attribute : estimation.getHistograms().keySet() ) {
		// System.out.println(attribute);
		// System.out.println(estimation.getHistograms().get(attribute).toString());
		// System.out.println();
		// }

		/** 2. Selectivity **/
		IDataStream c0 = prevOperators.get(0).getDataStream();
		IDataStream c1 = prevOperators.get(1).getDataStream();
		double selectivity = EstimatorHelper.getSelectivityMetadata(instance);
		if (selectivity < 0.0) {
			PredicateSelectivityHelper helper2 = new PredicateSelectivityHelper(predicate, histograms);
			double trueProp = helper2.getSelectivity();
			double combis = c0.getDataRate() * (c1.getDataRate() * c1.getIntervalLength() ) + 
							c1.getDataRate() * (c0.getDataRate() * c0.getIntervalLength() );
			double outputRate = trueProp * combis;
			double inputRate = (c0.getDataRate() * c1.getDataRate() * c0.getIntervalLength() * c1.getIntervalLength());
			
			selectivity = outputRate / inputRate;
		}
		estimation.setSelectivity(selectivity);

//		IDataStream c0 = prevOperators.get(0).getDataStream();
//		IDataStream c1 = prevOperators.get(1).getDataStream();
//		double selectivity = EstimatorHelper.getSelectivityMetadata(instance);
//		System.out.format("%-8.6f,", selectivity);
//		PredicateSelectivityHelper helper2 = new PredicateSelectivityHelper(predicate, histograms);
//		double trueProp = helper2.getSelectivity();
//		double combis = c0.getDataRate() * (c1.getDataRate() * c1.getIntervalLength() ) + 
//						c1.getDataRate() * (c0.getDataRate() * c0.getIntervalLength() );
//		double outputRate = trueProp * combis;
//		double inputRate = c0.getDataRate() * c1.getDataRate() * c0.getIntervalLength() * c1.getIntervalLength();
//		
//		double selectivity2 = outputRate / inputRate;
//		System.out.format("%-8.6f%n", selectivity2);
//		estimation.setSelectivity(selectivity < 0.0 ? selectivity2 : selectivity);

		/** 3. DataStream **/
		// join has two inputs
		double dataRate = EstimatorHelper.getDatarateMetadata(instance);
		if (dataRate < 0) {
			dataRate = selectivity * (c0.getDataRate() * c1.getDataRate() * c1.getIntervalLength() * c0.getIntervalLength() );
		}

		double intervalLength = (c0.getIntervalLength() * c1.getIntervalLength()) / (c0.getIntervalLength() + c1.getIntervalLength());
		estimation.setDataStream(new DataStream(instance, dataRate, intervalLength));

//		double dataRate = EstimatorHelper.getDatarateMetadata(instance);
//		System.out.format("%-8.6f,", dataRate);
//		double dataRate2 = selectivity * (c0.getDataRate() * c1.getDataRate() * c1.getIntervalLength() * c0.getIntervalLength() );
//		System.out.format("%-8.6f%n", dataRate2);
//		
//		double intervalLength = (c0.getIntervalLength() * c1.getIntervalLength()) / (c0.getIntervalLength() + c1.getIntervalLength());
//		estimation.setDataStream(new DataStream(instance, dataRate >= 0 ? dataRate : dataRate2, intervalLength));

		
		/** 4. DetailCost **/
		double cpu = EstimatorHelper.getAvgCPUTimeMetadata(instance);
		double cpuCost = 0.0;
		if (cpu < 0.0) {
			cpuCost = CPURateSaver.getInstance().get(instance.getClass().getSimpleName()) * ( c0.getDataRate() + c1.getDataRate() );
		} else {
			cpuCost = cpu * ( c0.getDataRate() + c1.getDataRate() ) ;
			CPURateSaver.getInstance().set(instance.getClass().getSimpleName(), cpu);
		}

//		double cpu = EstimatorHelper.getAvgCPUTimeMetadata(instance);
//		System.out.format("%-8.6f,", cpu);
//		double avgCPUTime = cpu * c0.getDataRate() * c1.getDataRate();
//		
//		double cpu2 = ACConstants.JOIN_CPU_COST_PER_TUPLE;
//		System.out.format("%-8.6f%n", cpu2);
//		double avgCPUTime2 = cpu2 * c0.getDataRate() * c1.getDataRate();

		double memCost = c0.getDataRate() * c0.getIntervalLength() + c1.getDataRate() * c1.getIntervalLength();
		estimation.setDetailCost(new OperatorDetailCost(instance, memCost, cpuCost));

		return estimation;
	}

	// Kreuzprodukt
	private OperatorEstimation estimateCrossProductOperator(JoinTIPO instance, List<OperatorEstimation> prevOperators, Map<SDFAttribute, IHistogram> baseHistograms) {
		OperatorEstimation estimation = new OperatorEstimation(instance);

		OperatorEstimation op1 = prevOperators.get(0);
		OperatorEstimation op2 = prevOperators.get(1);

		/** 1. Histograms **/
//		 System.out.println("CROSS-PRODUCT - INPUT");
//		 for (SDFAttribute attribute : op1.getHistograms().keySet()) {
//		 System.out.println(attribute);
//		 System.out.println(op1.getHistograms().get(attribute).toString());
//		 System.out.println();
//		 }
//		 for (SDFAttribute attribute : op2.getHistograms().keySet()) {
//		 System.out.println(attribute);
//		 System.out.println(op2.getHistograms().get(attribute).toString());
//		 System.out.println();
//		 }

		double factor1 = op1.getDataStream().getDataRate() * op1.getDataStream().getIntervalLength();
		double factor2 = op2.getDataStream().getDataRate() * op2.getDataStream().getIntervalLength();

		Map<SDFAttribute, IHistogram> outputHistograms = new HashMap<SDFAttribute, IHistogram>();

		for (SDFAttribute attribute : op1.getHistograms().keySet()) {
			IHistogram histogram = op1.getHistograms().get(attribute).clone();

			double[] occs = histogram.getAllOccurences();
			for (int i = 0; i < histogram.getIntervalCount(); i++) {
				histogram.setOccurences(i, occs[i] * factor2);
			}

			outputHistograms.put(attribute, histogram);
		}

		for (SDFAttribute attribute : op2.getHistograms().keySet()) {
			IHistogram histogram = op2.getHistograms().get(attribute).clone();

			double[] occs = histogram.getAllOccurences();
			for (int i = 0; i < histogram.getIntervalCount(); i++) {
				histogram.setOccurences(i, occs[i] * factor1);
			}

			outputHistograms.put(attribute, histogram);
		}
//		System.out.println();
//		System.out.println("CROSS-PRODUCT - OUTPUT");
//		for (SDFAttribute attribute : outputHistograms.keySet()) {
//			System.out.println(attribute);
//			System.out.println(outputHistograms.get(attribute).toString());
//			System.out.println();
//		}
		estimation.setHistograms(outputHistograms);

		/** 2. Selektivität **/
		double selectivity = EstimatorHelper.getSelectivityMetadata(instance);
		estimation.setSelectivity(selectivity >= 0.0 ? selectivity : 1.0);

		/** 3. DataStream **/
		IDataStream c0 = prevOperators.get(0).getDataStream();
		IDataStream c1 = prevOperators.get(1).getDataStream();

		double dataRate = EstimatorHelper.getDatarateMetadata(instance);
		if (dataRate < 0) {
			dataRate = c0.getDataRate() * (c1.getIntervalLength() * c1.getDataRate()) + c1.getDataRate() * (c0.getIntervalLength() * c0.getDataRate());
		}

		double intervalLength = (c0.getIntervalLength() * c1.getIntervalLength()) / (c0.getIntervalLength() + c1.getIntervalLength());
		estimation.setDataStream(new DataStream(instance, dataRate, intervalLength));

//		IDataStream c0 = prevOperators.get(0).getDataStream();
//		IDataStream c1 = prevOperators.get(1).getDataStream();
//
//		double dataRate = EstimatorHelper.getDatarateMetadata(instance);
//		double dataRate2 = c0.getDataRate() * (c1.getIntervalLength() * c1.getDataRate()) + c1.getDataRate() * (c0.getIntervalLength() * c0.getDataRate());
//		System.out.format("%-8.6f,", dataRate);
//		System.out.format("%-8.6f%n", dataRate2);
//		
//		double intervalLength = (c0.getIntervalLength() * c1.getIntervalLength()) / (c0.getIntervalLength() + c1.getIntervalLength());
//		estimation.setDataStream(new DataStream(instance, dataRate >= 0 ? dataRate : dataRate2, intervalLength));

		/** 4. DetailCost **/
		double cpu = EstimatorHelper.getAvgCPUTimeMetadata(instance);
		double cpuCost = 0.0;
		if (cpu < 0.0) {
			cpuCost = CPURateSaver.getInstance().get(instance.getClass().getSimpleName() + "_Cross") * ( c0.getDataRate() + c1.getDataRate() );
		} else {
			cpuCost = cpu * ( c0.getDataRate() + c1.getDataRate() ) ;
			CPURateSaver.getInstance().set(instance.getClass().getSimpleName() + "_Cross", cpu);
		}

		double memCost = (c0.getDataRate() * c0.getIntervalLength() + c1.getDataRate() * c1.getIntervalLength()) * EstimatorHelper.sizeInBytes(instance.getOutputSchema());
		estimation.setDetailCost(new OperatorDetailCost(instance, memCost, cpuCost));

		return estimation;
	}
}
