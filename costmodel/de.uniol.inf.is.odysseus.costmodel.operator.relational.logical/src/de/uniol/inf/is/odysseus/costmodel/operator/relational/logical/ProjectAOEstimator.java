/**
 * 
 */
package de.uniol.inf.is.odysseus.costmodel.operator.relational.logical;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
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
public class ProjectAOEstimator implements IOperatorEstimator<ProjectAO> {

	@Override
	public Class<ProjectAO> getOperatorClass() {
		return ProjectAO.class;
	}

	@Override
	public OperatorEstimation<ProjectAO> estimateOperator(ProjectAO instance,
			List<OperatorEstimation<?>> prevOperators,
			Map<SDFAttribute, IHistogram> baseHistograms) {
		OperatorEstimation<ProjectAO> estimation = new OperatorEstimation<ProjectAO>(instance);
		OperatorEstimation<?> lastOpEstimation = prevOperators.get(0);

		/** 1. Histograms **/
		// project has only one input
		Map<SDFAttribute, IHistogram> inputHistograms = lastOpEstimation.getHistograms();
		SDFSchema outputSchema = instance.getOutputSchema();

		// only provide those histograms, which
		// are projected from this operator
		Map<SDFAttribute, IHistogram> outputHistograms = new HashMap<SDFAttribute, IHistogram>();
		for (SDFAttribute attribute : outputSchema) {
			IHistogram histogram = inputHistograms.get(attribute);
			if (histogram != null)
				outputHistograms.put(attribute, histogram);
		}
		estimation.setHistograms(outputHistograms);

		/** 2. Selectivity **/
		estimation.setSelectivity(1.0);

		/** 3. Datarate **/
		double datarate = lastOpEstimation.getDataStream().getDataRate();
		
		estimation.setDataStream(new DataStream<ProjectAO>(instance, datarate, lastOpEstimation.getDataStream().getIntervalLength()));

		/** 4. DetailCost **/
		double cpuCost = CPURateSaver.getInstance().get(instance.getClass().getSimpleName()) * lastOpEstimation.getDataStream().getDataRate();
		
		
		estimation.setDetailCost(new OperatorDetailCost<ProjectAO>(instance, MemoryUsageSaver.get(instance), cpuCost));

		return estimation;
	}

}
