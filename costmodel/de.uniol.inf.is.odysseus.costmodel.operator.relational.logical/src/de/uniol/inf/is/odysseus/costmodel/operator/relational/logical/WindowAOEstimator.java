/**
 * 
 */
package de.uniol.inf.is.odysseus.costmodel.operator.relational.logical;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.costmodel.operator.DataStream;
import de.uniol.inf.is.odysseus.costmodel.operator.IOperatorEstimator;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorDetailCost;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorEstimation;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.IHistogram;
import de.uniol.inf.is.odysseus.costmodel.operator.util.CPURateSaver;
import de.uniol.inf.is.odysseus.costmodel.operator.util.MemoryUsageSaver;

/**
 * @author Merlin Wasmann, Timo Michelsen
 *
 */
public class WindowAOEstimator implements IOperatorEstimator<WindowAO> {

	@Override
	public Class<WindowAO> getOperatorClass() {
		return WindowAO.class;
	}

	@Override
	public OperatorEstimation<WindowAO> estimateOperator(WindowAO instance,
			List<OperatorEstimation<?>> prevOperators,
			Map<SDFAttribute, IHistogram> baseHistograms) {
		OperatorEstimation<WindowAO> estimation = new OperatorEstimation<WindowAO>(instance);
		OperatorEstimation<?> lastOpEstimation = prevOperators.get(0);

		WindowType windowType = instance.getWindowType();
		
		/** 1. Histograms **/
		estimation.setHistograms(lastOpEstimation.getHistograms());

		/** 2. Selectivity **/
		estimation.setSelectivity(1.0);

		/** 3. Datarate **/
		// depends on the windowType
		long windowSize = instance.getWindowSize();
		long windowAdvance = instance.getWindowAdvance();
		if(windowType.equals(WindowType.TIME)) {
			windowSize = TimeUnit.MILLISECONDS.convert(windowSize, instance.getTimeUnit()) / 1000;

			if(windowAdvance > 0) {
				windowAdvance = TimeUnit.MILLISECONDS.convert(windowAdvance, instance.getTimeUnit()) / 1000;
			} else {
				windowAdvance = TimeUnit.MILLISECONDS.convert(
						instance.getWindowSlide(), instance.getTimeUnit()) / 1000;
			}
		}

		double r = lastOpEstimation.getDataStream().getDataRate();
		double g = 0;
		if (windowAdvance == 1) // gleitend zeitbasiert : gleitend elementbasiert
			// depends on the windowType
			g = (windowType.equals(WindowType.TIME) ? windowSize : windowSize / r);
		else
			// springend zeitbasiert : springend elementbasiert
			// depends on the windowType
			g = (windowType.equals(WindowType.TIME) ? windowSize / 2.0 : windowSize / (2.0 * r));

		DataStream<WindowAO> stream = new DataStream<WindowAO>(instance, r, g);
		estimation.setDataStream(stream);

		/** 4. DetailCost **/
		double cpuCost = CPURateSaver.getInstance().get(instance.getClass().getSimpleName()) * r;

		estimation.setDetailCost(new OperatorDetailCost<WindowAO>(instance, MemoryUsageSaver.get(instance), cpuCost));

		return estimation;
	}

}
