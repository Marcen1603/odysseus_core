/**
 * 
 */
package de.uniol.inf.is.odysseus.costmodel.operator.relational.logical;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.costmodel.operator.DataStream;
import de.uniol.inf.is.odysseus.costmodel.operator.IDataStream;
import de.uniol.inf.is.odysseus.costmodel.operator.IOperatorEstimator;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorDetailCost;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorEstimation;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.IHistogram;
import de.uniol.inf.is.odysseus.costmodel.operator.util.CPURateSaver;
import de.uniol.inf.is.odysseus.costmodel.operator.util.MemoryUsageSaver;
import de.uniol.inf.is.odysseus.costmodel.operator.util.PredicateHistogramHelper;
import de.uniol.inf.is.odysseus.costmodel.operator.util.PredicateSelectivityHelper;

/**
 * @author Merlin Wasmann, Timo Michelsen
 * 
 */
public class SelectAOEstimator implements IOperatorEstimator<SelectAO> {

	@Override
	public Class<SelectAO> getOperatorClass() {
		return SelectAO.class;
	}

	@Override
	public OperatorEstimation<SelectAO> estimateOperator(SelectAO instance,
			List<OperatorEstimation<?>> prevOperators,
			Map<SDFAttribute, IHistogram> baseHistograms) {
		List<Map<SDFAttribute, IHistogram>> histograms = new ArrayList<Map<SDFAttribute, IHistogram>>();
		for (OperatorEstimation<?> estimation : prevOperators) {
			histograms.add(estimation.getHistograms());
		}

		OperatorEstimation<SelectAO> estimation = new OperatorEstimation<SelectAO>(
				instance);

		/** 1. Histograms **/
		IPredicate<?> predicate = instance.getPredicate();
		PredicateHistogramHelper helper = new PredicateHistogramHelper(
				predicate, histograms);
		estimation.setHistograms(helper.getHistograms());

		/** 2. Selectivity **/
		PredicateSelectivityHelper helper2 = new PredicateSelectivityHelper(
				predicate, histograms);
		double selectivity = helper2.getSelectivity();
		estimation.setSelectivity(selectivity);

		/** 3. Datarate **/
		IDataStream<?> c = prevOperators.get(0).getDataStream();
		double datarate = c.getDataRate() * selectivity;
		estimation.setDataStream(new DataStream<SelectAO>(instance, datarate, c
				.getIntervalLength()));

		/** 4. DetailCost **/	
		double cpuCost = CPURateSaver.getInstance().get(
					instance.getClass().getSimpleName())
					* c.getDataRate();
		
		estimation.setDetailCost(new OperatorDetailCost<SelectAO>(instance,
				MemoryUsageSaver.get(instance), cpuCost));

		return estimation;
	}

}
