package de.uniol.inf.is.odysseus.planmanagement.optimization.query.queryrestructoptimizer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.base.CopyLogicalPlanVisitor;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.base.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IQueryOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.OptimizeParameter;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.parameter.ParameterDoRestruct;
import de.uniol.inf.is.odysseus.planmanagement.optimization.query.IQueryOptimizer;
import de.uniol.inf.is.odysseus.util.AbstractTreeWalker;

/**
 * QueryRestructOptimizer is the standard query optimizer for odysseus. This
 * optimizer creates the physical plan for queries. Based on
 * {@link OptimizeParameter} a Rewrite is used and buffer are placed by an
 * {@link IBufferPlacementStrategy}.
 * 
 * @author Wolf Bauer, Tobias Witt
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
		ILogicalOperator sealedLogicalPlan = query
				.getSealedLogicalPlan();
		
		if (sealedLogicalPlan != null && restruct != null && restruct == ParameterDoRestruct.TRUE) {
			ILogicalOperator newLogicalAlgebra = compiler.restructPlan(sealedLogicalPlan);
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
				IPhysicalOperator physicalPlan = compiler.transform(query.getLogicalPlan(), query.getBuildParameter()
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
	
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.optimization.query.IQueryOptimizer#optimizeQuery(de.uniol.inf.is.odysseus.planmanagement.optimization.IQueryOptimizable, de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery, de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.OptimizeParameter, Set<String>)
	 */
	@Override
	public void optimizeQuery(IQueryOptimizable sender, IEditableQuery query,
			OptimizeParameter parameters, Set<String> rulesToUse) throws QueryOptimizationException {
		ICompiler compiler = sender.getCompiler();
		
		if (compiler == null) {
			throw new QueryOptimizationException("Compiler is not loaded.");
		}

		ParameterDoRestruct restruct = parameters.getParameterDoRestruct();

		// if a logical rewrite should be processed.
		ILogicalOperator sealedLogicalPlan = query
				.getSealedLogicalPlan();
		
		if (sealedLogicalPlan != null && restruct != null && restruct == ParameterDoRestruct.TRUE) {
			ILogicalOperator newLogicalAlgebra = compiler.restructPlan(sealedLogicalPlan, rulesToUse);
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
				IPhysicalOperator physicalPlan = compiler.transform(query.getLogicalPlan(), query.getBuildParameter()
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


	@Override
	public Map<IPhysicalOperator,ILogicalOperator> createAlternativePlans(
			IQueryOptimizable sender, IEditableQuery query,
			OptimizeParameter parameters, Set<String> rulesToUse)
			throws QueryOptimizationException {
		// TODO nur angepasste Logik von optimizeQuery, muss aber verschiedene 
		// Plaene liefern, die z.B. durch Vertauschen von Joins erzeugt werden
		
		ICompiler compiler = sender.getCompiler();

		if (compiler == null) {
			throw new QueryOptimizationException("Compiler is not loaded.");
		}

		ParameterDoRestruct restruct = parameters.getParameterDoRestruct();

		// create working copy of plan
		ILogicalOperator logicalPlanCopy = AbstractTreeWalker.prefixWalk(query.getSealedLogicalPlan(), 
				new CopyLogicalPlanVisitor());
		
		ILogicalOperator newLogicalAlgebra = null;
		if (restruct != null && restruct == ParameterDoRestruct.TRUE) {
			if (rulesToUse==null) {
				newLogicalAlgebra = compiler.restructPlan(logicalPlanCopy);
			} else {
				newLogicalAlgebra = compiler.restructPlan(logicalPlanCopy,rulesToUse);
			}
		}

		if ((restruct != null && restruct == ParameterDoRestruct.TRUE)){
		try {
				// create the physical plan
				IPhysicalOperator physicalPlan = compiler.transform(newLogicalAlgebra,
						query.getBuildParameter().getTransformationConfiguration());	

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
				
				// put last sink on top
				IPhysicalOperator oldRoot = query.getRoot();
				if (oldRoot.isSource()) {
					throw new QueryOptimizationException(
							"Migration needs a sink only as operator root.");
				}
				IPhysicalOperator newRoot = oldRoot.clone();
				((ISink)newRoot).subscribeToSource(physicalPlan, 0, 0, physicalPlan.getOutputSchema());
				physicalPlan = newRoot;
				
				Map<IPhysicalOperator,ILogicalOperator> alternatives = new HashMap<IPhysicalOperator, ILogicalOperator>(1);
				alternatives.put(physicalPlan, newLogicalAlgebra);
				
				return alternatives;

			} catch (Throwable e) {
				throw new QueryOptimizationException(
						"Exeception while initialize query.", e);
			}
		}
		
		return new HashMap<IPhysicalOperator, ILogicalOperator>(0);
	}

}
