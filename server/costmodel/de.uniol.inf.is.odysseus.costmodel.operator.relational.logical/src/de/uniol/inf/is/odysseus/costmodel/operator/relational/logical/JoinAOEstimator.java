package de.uniol.inf.is.odysseus.costmodel.operator.relational.logical;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.predicate.TruePredicate;
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

/**
 * @author Merlin Wasmann, Timo Michelsen
 * 
 */
public class JoinAOEstimator implements IOperatorEstimator<JoinAO> {

	@Override
	public Class<JoinAO> getOperatorClass() {
		return JoinAO.class;
	}

	@Override
	public OperatorEstimation<JoinAO> estimateOperator(JoinAO instance,
			List<OperatorEstimation<?>> prevOperators,
			Map<SDFAttribute, IHistogram> baseHistograms) {
		List<Map<SDFAttribute, IHistogram>> histograms = new ArrayList<Map<SDFAttribute, IHistogram>>();
		for (OperatorEstimation<?> estimation : prevOperators)
			histograms.add(estimation.getHistograms());

		IPredicate<?> predicate = instance.getPredicate();
		if (predicate instanceof TruePredicate
				|| predicate.toString().equalsIgnoreCase("true"))
			return estimateCrossProductOperator(instance, prevOperators);

		OperatorEstimation<JoinAO> estimation = new OperatorEstimation<JoinAO>(
				instance);
		/** 1. Histograms **/
		PredicateHistogramHelper helper = new PredicateHistogramHelper(
				predicate, histograms);
		estimation.setHistograms(helper.getHistograms());

		/** 2. Selectivity **/
		IDataStream<?> c0 = prevOperators.get(0).getDataStream();
		IDataStream<?> c1 = prevOperators.get(1).getDataStream();

		PredicateSelectivityHelper helper2 = new PredicateSelectivityHelper(
				predicate, histograms);
		double trueProp = helper2.getSelectivity();
		double combis = c0.getDataRate()
				* (c1.getDataRate() * c1.getIntervalLength())
				+ c1.getDataRate()
				* (c0.getDataRate() * c0.getIntervalLength());
		double outputRate = trueProp * combis;
		double inputRate = (c0.getDataRate() * c1.getDataRate()
				* c0.getIntervalLength() * c1.getIntervalLength());

		double selectivity = outputRate / inputRate;

		estimation.setSelectivity(selectivity);

		/** 3. DataStream **/
		// join has two inputs
		double dataRate = selectivity
				* (c0.getDataRate() * c1.getDataRate() * c1.getIntervalLength() * c0
						.getIntervalLength());

		double intervalLength = (c0.getIntervalLength() * c1
				.getIntervalLength())
				/ (c0.getIntervalLength() + c1.getIntervalLength());
		estimation.setDataStream(new DataStream<JoinAO>(instance, dataRate,
				intervalLength));

		/** 4. DetailCost **/
		double cpuCost = CPURateSaver.getInstance().get(
				instance.getClass().getSimpleName())
				* (c0.getDataRate() + c1.getDataRate());
		double memCost = (c0.getDataRate() * c0.getIntervalLength() + c1
				.getDataRate() * c1.getIntervalLength())
				* EstimatorHelper.sizeInBytes(instance.getOutputSchema());

		estimation.setDetailCost(new OperatorDetailCost<JoinAO>(instance,
				memCost, cpuCost));

		return estimation;
	}

	// Kreuzprodukt
	private static OperatorEstimation<JoinAO> estimateCrossProductOperator(
			JoinAO instance, List<OperatorEstimation<?>> prevOperators) {
		OperatorEstimation<JoinAO> estimation = new OperatorEstimation<JoinAO>(
				instance);

		OperatorEstimation<?> op1 = prevOperators.get(0);
		OperatorEstimation<?> op2 = prevOperators.get(1);

		/** 1. Histograms **/
		double factor1 = op1.getDataStream().getDataRate()
				* op1.getDataStream().getIntervalLength();
		double factor2 = op2.getDataStream().getDataRate()
				* op2.getDataStream().getIntervalLength();

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
		estimation.setHistograms(outputHistograms);

		/** 2. Selektivität **/
		estimation.setSelectivity(1.0);

		/** 3. DataStream **/
		IDataStream<?> c0 = prevOperators.get(0).getDataStream();
		IDataStream<?> c1 = prevOperators.get(1).getDataStream();

		double dataRate = c0.getDataRate()
				* (c1.getIntervalLength() * c1.getDataRate())
				+ c1.getDataRate()
				* (c0.getIntervalLength() * c0.getDataRate());

		double intervalLength = (c0.getIntervalLength() * c1
				.getIntervalLength())
				/ (c0.getIntervalLength() + c1.getIntervalLength());
		estimation.setDataStream(new DataStream<JoinAO>(instance, dataRate,
				intervalLength));

		/** 4. DetailCost **/

		double cpuCost = CPURateSaver.getInstance().get(
				instance.getClass().getSimpleName() + "_Cross")
				* (c0.getDataRate() + c1.getDataRate());

		double memCost = (c0.getDataRate() * c0.getIntervalLength() + c1
				.getDataRate() * c1.getIntervalLength())
				* EstimatorHelper.sizeInBytes(instance.getOutputSchema());

		estimation.setDetailCost(new OperatorDetailCost<JoinAO>(instance,
				memCost, cpuCost));

		return estimation;
	}

}
