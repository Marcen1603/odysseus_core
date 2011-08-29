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
import de.uniol.inf.is.odysseus.physicaloperator.UnionPO;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

@SuppressWarnings("rawtypes")
public class UnionPOEstimator implements IOperatorEstimator<UnionPO> {

	@Override
	public Class<UnionPO> getOperatorClass() {
		return UnionPO.class;
	}

	@Override
	public OperatorEstimation estimateOperator(UnionPO instance, List<OperatorEstimation> prevOperators, Map<SDFAttribute, IHistogram> baseHistograms) {
		List<Map<SDFAttribute, IHistogram>> histograms = new ArrayList<Map<SDFAttribute, IHistogram>>();
		List<IDataStream> inputStreams = new ArrayList<IDataStream>();
		for (OperatorEstimation estimation : prevOperators) {
			histograms.add(estimation.getHistograms());
			inputStreams.add(estimation.getDataStream());
		}

		OperatorEstimation estimation = new OperatorEstimation(instance);

		/** 1. Histograms **/
		// merge all histograms to one
		Map<SDFAttribute, IHistogram> output = new HashMap<SDFAttribute, IHistogram>();

		for (Map<SDFAttribute, IHistogram> map : histograms) {
			for (SDFAttribute attribute : map.keySet()) {

				if (!output.containsKey(attribute)) {
					output.put(attribute, map.get(attribute).clone());
				} else {

					// merge histograms by simply adding
					IHistogram first = output.get(attribute);
					IHistogram second = map.get(attribute);

					IHistogram histMoreIntervals = null;
					IHistogram histLessIntervals = null;
					if (first.getIntervalCount() > second.getIntervalCount()) {
						histMoreIntervals = first;
						histLessIntervals = second;
					} else {
						histMoreIntervals = second;
						histLessIntervals = first;
					}

					double[] borders = histMoreIntervals.getIntervalBorders();
					IHistogram histResult = histMoreIntervals.clone();
					for (int i = 0; i < borders.length - 1; i++) {
						double occ1 = histMoreIntervals.getOccurences(borders[i]);
						double occ2 = histLessIntervals.getOccurenceRange(borders[i], borders[i + 1]);
						histResult.setOccurences(i, occ1 + occ2);
					}

					output.put(attribute, histResult);
				}

			}
		}
		estimation.setHistograms(output);

		/** 2. Selectivity **/
		estimation.setSelectivity(1.0);

		/** 3. Datarate **/
		// merge data rates
		double unionDataRate = EstimatorHelper.getDatarateMetadata(instance);
		double temp = 0.0;
		if (unionDataRate < 0.0) {
			unionDataRate = 0.0;
			for (IDataStream ods : inputStreams) {
				unionDataRate += ods.getDataRate();
				temp += (ods.getIntervalLength() * ods.getDataRate());
			}
		} else {
			for (IDataStream ods : inputStreams) {
				temp += (ods.getIntervalLength() * ods.getDataRate());
			}
		}
		double unionIntervalLength = temp / unionDataRate;
		estimation.setDataStream(new DataStream(instance, unionDataRate, unionIntervalLength));

//		for (OperatorEstimation e : prevOperators) {
//			System.out.format("%-8.6f,", e.getDataStream().getDataRate());
//		}
//
//		double unionDataRate = EstimatorHelper.getDatarateMetadata(instance);
//		System.out.format("%-8.6f,", unionDataRate);		
//		double temp = 0.0;
//		double unionDataRate2 = 0.0;
//		for (IDataStream ods : inputStreams) {
//			unionDataRate2 += ods.getDataRate();
//			temp += (ods.getIntervalLength() * ods.getDataRate());
//		}
//		System.out.format("%-8.6f\n", unionDataRate2);
//		double realDataRate = unionDataRate >= 0.0 ? unionDataRate : unionDataRate2;
//
//		double unionIntervalLength = temp / realDataRate;
//		estimation.setDataStream(new DataStream(instance, unionDataRate, unionIntervalLength));

		/** 4. DetailCost **/
		double rateSum = 0.0;
		double minRate = Double.MAX_VALUE;
		double maxRate = Double.MIN_VALUE;
		for (IDataStream inputStream : inputStreams) {
			double rate = inputStream.getDataRate();
			if (rate > maxRate)
				maxRate = rate;
			if (rate < minRate)
				minRate = rate;

			rateSum += rate;
		}

		int sizeOfTuple = EstimatorHelper.sizeInBytes(instance.getOutputSchema());
		double mem = (maxRate / minRate) * sizeOfTuple;

		double cpu = EstimatorHelper.getAvgCPUTimeMetadata(instance);
//		System.out.format("%8.6f\n", cpu);
		double cpuCost = 0.0;
		if (cpu < 0.0)
			cpuCost = CPURateSaver.getInstance().get(instance.getClass().getSimpleName()) * rateSum;
		else {
			cpuCost = cpu * rateSum;
			CPURateSaver.getInstance().set(instance.getClass().getSimpleName(), cpu);
		}
		
		estimation.setDetailCost(new OperatorDetailCost(instance, mem, cpuCost));

		return estimation;
	}
}
