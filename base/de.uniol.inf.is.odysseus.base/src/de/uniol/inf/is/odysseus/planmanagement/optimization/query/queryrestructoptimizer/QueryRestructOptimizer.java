package de.uniol.inf.is.odysseus.planmanagement.optimization.query.queryrestructoptimizer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.base.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IQueryOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.OptimizeParameter;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.parameter.ParameterDoRestruct;
import de.uniol.inf.is.odysseus.planmanagement.optimization.query.IQueryOptimizer;
import de.uniol.inf.is.odysseus.util.AbstractGraphWalker;
import de.uniol.inf.is.odysseus.util.CopyLogicalGraphVisitor;

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
	 * @see de.uniol.inf.is.odysseus.planmanagement.optimization.query.IQueryOptimizer#optimizeQuery(de.uniol.inf.is.odysseus.planmanagement.optimization.IQueryOptimizable, de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery, de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.OptimizeParameter)
	 */
	@Override
	public void optimizeQuery(IQueryOptimizable sender, IQuery query,
			OptimizeParameter parameters) throws QueryOptimizationException {
		ICompiler compiler = sender.getCompiler();
		
		if (compiler == null) {
			throw new QueryOptimizationException("Compiler is not loaded.");
		}

		ParameterDoRestruct restruct = parameters.getParameterDoRestruct();

		// if a logical rewrite should be processed.
		ILogicalOperator sealedLogicalPlan = query
				.getLogicalPlan();
		
		boolean queryShouldBeRewritten = sealedLogicalPlan != null && restruct != null && restruct == ParameterDoRestruct.TRUE;
		if (queryShouldBeRewritten) {
			ILogicalOperator newLogicalAlgebra = compiler.restructPlan(sealedLogicalPlan);
			// set new logical plan.
			query.setLogicalPlan(newLogicalAlgebra);
		}

		// TODO: is that correct? I think this should be done if (
		// query.getRoot() || (restruct != null && restruct == ParameterDoRestruct.TRUE)
		//
		// (Wolf)
//		if (query.getRoot() == null || restruct == null
//				|| restruct == ParameterDoRestruct.FALSE) {
		if (query.getRoot() == null || queryShouldBeRewritten){
		try {
				// create the physical plan
				IPhysicalOperator physicalPlan = compiler.transform(query.getLogicalPlan(), query.getBuildParameter()
						.getTransformationConfiguration());

				postTransformationInit(query, physicalPlan);
			} catch (Throwable e) {
				throw new QueryOptimizationException(
						"Exeception while initialize query.", e);
			}
		}
	}
	
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.optimization.query.IQueryOptimizer#optimizeQuery(de.uniol.inf.is.odysseus.planmanagement.optimization.IQueryOptimizable, de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery, de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.OptimizeParameter, Set<String>)
	 */
	@Override
	public void optimizeQuery(IQueryOptimizable sender, IQuery query,
			OptimizeParameter parameters, Set<String> rulesToUse) throws QueryOptimizationException {
		ICompiler compiler = sender.getCompiler();
		
		if (compiler == null) {
			throw new QueryOptimizationException("Compiler is not loaded.");
		}

		ParameterDoRestruct restruct = parameters.getParameterDoRestruct();

		// if a logical rewrite should be processed.
		ILogicalOperator sealedLogicalPlan = query
				.getLogicalPlan();
		
		if (sealedLogicalPlan != null && restruct != null && restruct == ParameterDoRestruct.TRUE) {
			ILogicalOperator newLogicalAlgebra = compiler.restructPlan(sealedLogicalPlan, rulesToUse);
			// set new logical plan.
			query.setLogicalPlan(newLogicalAlgebra);
		}

		// TODO: is that correct? I think this should be done if (
		// query.getRoot() || (restruct != null && restruct == ParameterDoRestruct.TRUE)
		//
		// (Wolf)
//		if (query.getRoot() == null || restruct == null
//				|| restruct == ParameterDoRestruct.FALSE) {
		if (query.getRoot() == null || (restruct != null && restruct == ParameterDoRestruct.TRUE)){
		try {
				// create the physical plan
				IPhysicalOperator physicalPlan = compiler.transform(query.getLogicalPlan(), query.getBuildParameter()
						.getTransformationConfiguration());

				postTransformationInit(query, physicalPlan);
			} catch (Throwable e) {
				throw new QueryOptimizationException(
						"Exeception while initialize query.", e);
			}
		}
	}
	
	@Override
	public void postTransformationInit(IQuery query, IPhysicalOperator physicalPlan) 
			throws QueryOptimizationException, OpenFailedException {
		addBuffers(query, physicalPlan);
		
		// Initialize the physical plan of the query.
		query.initializePhysicalPlan(physicalPlan);
	}
	
	private void addBuffers(IQuery query, IPhysicalOperator physicalPlan) 
			throws QueryOptimizationException {
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
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<IPhysicalOperator,ILogicalOperator> createAlternativePlans(
			IQueryOptimizable sender, IQuery query,
			OptimizeParameter parameters, Set<String> rulesToUse)
			throws QueryOptimizationException {
		
		ICompiler compiler = sender.getCompiler();
		if (compiler == null) {
			throw new QueryOptimizationException("Compiler is not loaded.");
		}

		ParameterDoRestruct restruct = parameters.getParameterDoRestruct();
		if (restruct == null || restruct != ParameterDoRestruct.TRUE) {
			// no restruct allowed
			return new HashMap<IPhysicalOperator, ILogicalOperator>(0);
		}

		// create working copy of plan
		CopyLogicalGraphVisitor<ILogicalOperator> copyVisitor = new CopyLogicalGraphVisitor<ILogicalOperator>();
		AbstractGraphWalker walker = new AbstractGraphWalker();
		walker.prefixWalk(query.getLogicalPlan(), copyVisitor);
		ILogicalOperator logicalPlanCopy = copyVisitor.getResult();
				
		// create logical alternatives
		List<ILogicalOperator> logicalAlternatives = compiler.createAlternativePlans(logicalPlanCopy,
				rulesToUse);

		try {
			Map<IPhysicalOperator,ILogicalOperator> alternatives = new HashMap<IPhysicalOperator,ILogicalOperator>();
			
			for (ILogicalOperator logicalPlan : logicalAlternatives) {
				// create alternative physical plans
				List<IPhysicalOperator> physicalPlans = compiler.transformWithAlternatives(logicalPlan,
						query.getBuildParameter().getTransformationConfiguration());	
	
				for (IPhysicalOperator physicalPlan : physicalPlans) {
					addBuffers(query, physicalPlan);
					
					// put last sink on top
					IPhysicalOperator oldRoot = query.getRoot();
					if (oldRoot.isSource()) {
						throw new QueryOptimizationException(
								"Migration needs a sink only as operator root.");
					}
					IPhysicalOperator newRoot = oldRoot.clone();
					((ISink)newRoot).subscribeToSource(physicalPlan, 0, 0, physicalPlan.getOutputSchema());
					physicalPlan = newRoot;

					alternatives.put(physicalPlan, logicalPlan);
				}
			}
			
			return alternatives;

		} catch (Throwable e) {
			throw new QueryOptimizationException(
					"Exeception while initialize query.", e);
		}
	}

}
