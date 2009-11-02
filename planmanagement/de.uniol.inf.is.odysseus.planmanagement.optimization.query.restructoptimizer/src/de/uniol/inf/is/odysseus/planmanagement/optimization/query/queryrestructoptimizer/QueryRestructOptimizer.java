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

/**
 * QueryRestructOptimizer is the standard query optimizer for odysseus. This
 * optimizer creates the physical plan for queries. Based on
 * {@link OptimizeParameter} a Rewrite is used and buffer are placed by an
 * {@link IBufferPlacementStrategy}.
 * 
 * @author Wolf Bauer
 * 
 */
public class QueryRestructOptimizer implements IQueryOptimizer {

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.optimization.query.IQueryOptimizer#optimizeQuery(de.uniol.inf.is.odysseus.planmanagement.optimization.IQueryOptimizable, de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery, de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.OptimizeParameter)
	 */
	@Override
	public void optimizeQuery(IQueryOptimizable sender, IEditableQuery query,
			OptimizeParameter parameters) throws QueryOptimizationException {
		ICompiler compiler = sender.getCompiler();
		
		if (compiler == null) {
			throw new QueryOptimizationException("Compiler is not loaded.");
		}

		ParameterDoRestruct restruct = parameters.getParameterDoRestruct();

		// if a logical rewrite should be processed.
		if (restruct != null && restruct == ParameterDoRestruct.TRUE) {
			ILogicalOperator newLogicalAlgebra = compiler.restructPlan(query
					.getSealedLogicalPlan());
			// set new logical plan.
			query.setLogicalPlan(newLogicalAlgebra);
		}

		// TODO: is that correct? I think this should be done if (
		// query.getSealedRoot() || (restruct != null && restruct == ParameterDoRestruct.TRUE)
		//
		// (Wolf)
//		if (query.getSealedRoot() == null || restruct == null
//				|| restruct == ParameterDoRestruct.FALSE) {
		if (query.getSealedRoot() == null || (restruct != null && restruct == ParameterDoRestruct.TRUE)){
		try {
				// create the physical plan
				IPhysicalOperator physicalPlan = compiler.transform(query
						.getSealedLogicalPlan(), query.getBuildParameter()
						.getTransformationConfiguration());

				IBufferPlacementStrategy bufferPlacementStrategy = query
						.getBuildParameter().getBufferPlacementStrategy();

				// add Buffer
				if (bufferPlacementStrategy != null) {
					try {
						bufferPlacementStrategy.addBuffers(physicalPlan);
					} catch (Exception e) {
						throw new QueryOptimizationException(
								"Exeception while initialize query.", e);
					}
				}

				// Initialize the physical plan of the query.
				query.initializePhysicalPlan(physicalPlan);
			} catch (Throwable e) {
				throw new QueryOptimizationException(
						"Exeception while initialize query.", e);
			}
		}
	}
}
