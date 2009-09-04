package de.uniol.inf.is.odysseus.planmanagement.optimization.query.queryrestructoptimizer;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.base.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IQueryOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.OptimizeParameter;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.parameter.ParameterDoRestruct;
import de.uniol.inf.is.odysseus.planmanagement.optimization.query.IQueryOptimizer;

public class QueryRestructOptimizer implements IQueryOptimizer {

	@Override
	public void optimizeQuery(IQueryOptimizable sender, IEditableQuery query,
			OptimizeParameter parameters) throws QueryOptimizationException {
		ICompiler compiler = sender.getCompiler();

		if (compiler == null) {
			throw new QueryOptimizationException("Compiler is not loaded.");
		}

		ParameterDoRestruct restruct = parameters.getParameterDoRestruct();

		if (restruct != null && restruct == ParameterDoRestruct.TRUE) {
			ILogicalOperator newLogicalAlgebra = compiler.restructPlan(query
					.getSealedLogicalPlan());
			query.setLogicalPlan(newLogicalAlgebra);
		}

		if (query.getSealedRoot() == null || restruct == null
				|| restruct == ParameterDoRestruct.FALSE) {
			try {				
				IPhysicalOperator physicalPlan = compiler.transform(query
						.getSealedLogicalPlan(), query.getBuildParameter().getTransformationConfiguration());

				IBufferPlacementStrategy bufferPlacementStrategy = query.getBuildParameter().getBufferPlacementStrategy();

				if (bufferPlacementStrategy != null) {
					try {
						bufferPlacementStrategy.addBuffers(physicalPlan);
					} catch (Exception e) {
						throw new QueryOptimizationException(
								"Exeception while initialize query.", e);
					}
				}

				query.initializePhysicalPlan(physicalPlan);
			} catch (Throwable e) {
				throw new QueryOptimizationException(
						"Exeception while initialize query.", e);
			}
		}
	}
}
