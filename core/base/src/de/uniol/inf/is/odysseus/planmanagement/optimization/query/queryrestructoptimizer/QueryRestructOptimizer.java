package de.uniol.inf.is.odysseus.planmanagement.optimization.query.queryrestructoptimizer;

import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IQueryOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.ParameterDoRestruct;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.query.IQueryOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

/**
 * QueryRestructOptimizer is the standard query optimizer for odysseus. This
 * optimizer creates the physical plan for queries. Based on
 * {@link OptimizationConfiguration} a Rewrite is used and buffer are placed by an
 * {@link IBufferPlacementStrategy}.
 * 
 * @author Wolf Bauer, Tobias Witt
 * 
 */
public class QueryRestructOptimizer implements IQueryOptimizer {

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.optimization.query.IQueryOptimizer#optimizeQuery(de.uniol.inf.is.odysseus.planmanagement.optimization.IQueryOptimizable, de.uniol.inf.is.odysseus.planmanagement.query.IQuery, de.uniol.inf.is.odysseus.planmanagement.optimization.OptimizationConfiguration.OptimizationConfiguration)
	 */
	@Override
	public void optimizeQuery(IQueryOptimizable sender, IQuery query,
			OptimizationConfiguration parameters) throws QueryOptimizationException {
		optimizeQuery(sender, query, parameters, null);
	}
	
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.optimization.query.IQueryOptimizer#optimizeQuery(de.uniol.inf.is.odysseus.planmanagement.optimization.IQueryOptimizable, de.uniol.inf.is.odysseus.planmanagement.query.IQuery, de.uniol.inf.is.odysseus.planmanagement.optimization.OptimizationConfiguration.OptimizationConfiguration, Set<String>)
	 */
	@Override
	public void optimizeQuery(IQueryOptimizable sender, IQuery query,
			OptimizationConfiguration parameters, Set<String> rulesToUse) throws QueryOptimizationException {
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
			ILogicalOperator newLogicalAlgebra = compiler.rewritePlan(sealedLogicalPlan, rulesToUse);
			// set new logical plan.
			query.setLogicalPlan(newLogicalAlgebra);
		}

		// TODO: is that correct? I think this should be done if (
		// query.getRoot() || (restruct != null && restruct == ParameterDoRestruct.TRUE)
		//
		// (Wolf)
//		if (query.getRoot() == null || restruct == null
//				|| restruct == ParameterDoRestruct.FALSE) {
		if (query.getRoots() == null || query.getRoots().isEmpty() || queryShouldBeRewritten){
		try {
				// create the physical plan
				List<IPhysicalOperator> physicalPlan = compiler.transform(query.getLogicalPlan(), query.getBuildParameter()
						.getTransformationConfiguration());

				postTransformationInit(query, physicalPlan);
			} catch (Throwable e) {
				throw new QueryOptimizationException(
						"Exeception while initialize query.", e);
			}
		}
	}
	
	@Override
	public void postTransformationInit(IQuery query, List<IPhysicalOperator> physicalPlan) 
			throws QueryOptimizationException, OpenFailedException {
		
		for(IPhysicalOperator root: physicalPlan){
			addBuffers(query, root);
		}
		
		// Initialize the physical plan of the query.
		query.initializePhysicalRoots(physicalPlan);
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
			OptimizationConfiguration parameters, Set<String> rulesToUse)
			throws QueryOptimizationException {
		
		throw new RuntimeException("Does not work at the moment. At Marco: Kannst du das bitte so anpassen, " +
				"dass jetzt berücksichtigt wird, dass bei der Transformation jetzt alle Roots (können jetzt ja" +
				"auch mehrere sein) eines physischen Anfrageplans zurückgeliefert werden.");
//		ICompiler compiler = sender.getCompiler();
//		if (compiler == null) {
//			throw new QueryOptimizationException("Compiler is not loaded.");
//		}
//
//		ParameterDoRestruct restruct = parameters.getParameterDoRestruct();
//		if (restruct == null || restruct != ParameterDoRestruct.TRUE) {
//			// no restruct allowed
//			return new HashMap<IPhysicalOperator, ILogicalOperator>(0);
//		}
//
//		// create working copy of plan
//		CopyLogicalGraphVisitor<ILogicalOperator> copyVisitor = new CopyLogicalGraphVisitor<ILogicalOperator>();
//		AbstractGraphWalker walker = new AbstractGraphWalker();
//		walker.prefixWalk(query.getLogicalPlan(), copyVisitor);
//		ILogicalOperator logicalPlanCopy = copyVisitor.getResult();
//				
//		// create logical alternatives
//		List<ILogicalOperator> logicalAlternatives = compiler.createAlternativePlans(logicalPlanCopy,
//				rulesToUse);
//
//		try {
//			Map<IPhysicalOperator,ILogicalOperator> alternatives = new HashMap<IPhysicalOperator,ILogicalOperator>();
//			
//			for (ILogicalOperator logicalPlan : logicalAlternatives) {
//				// create alternative physical plans
//				List<IPhysicalOperator> physicalPlans = compiler.transformWithAlternatives(logicalPlan,
//						query.getBuildParameter().getTransformationConfiguration());	
//	
//				for (IPhysicalOperator physicalPlan : physicalPlans) {
//					addBuffers(query, physicalPlan);
//					
//					// put last sink on top
//					IPhysicalOperator oldRoot = query.getRoot();
//					if (oldRoot.isSource()) {
//						throw new QueryOptimizationException(
//								"Migration needs a sink only as operator root.");
//					}
//					IPhysicalOperator newRoot = oldRoot.clone();
//					((ISink)newRoot).subscribeToSource(physicalPlan, 0, 0, physicalPlan.getOutputSchema());
//					physicalPlan = newRoot;
//
//					alternatives.put(physicalPlan, logicalPlan);
//				}
//			}
//			
//			return alternatives;
//
//		} catch (Throwable e) {
//			throw new QueryOptimizationException(
//					"Exeception while initialize query.", e);
//		}
	}

}
