package de.uniol.inf.is.odysseus.planmanagement.optimization.plan.standardplanoptimizer;


import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.NoCompilerLoadedException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.plan.ExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.optimization.plan.IPlanOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.optimization.plan.PartialPlan;
import de.uniol.inf.is.odysseus.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.planmanagement.query.NoTransformationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;

/**
 * StandardPlanOptimizer is the standard plan optimizer used by odysseus. New
 * queries are checked if they've got a physical plan. If not a new one is
 * created. After this check a new execution plan is created based on all
 * registered queries.
 * 
 * @author Wolf Bauer
 * 
 */
public class StandardPlanOptimizer implements IPlanOptimizer {

	/**
	 * Checks if each query has a physical plan. If not a new one based on the
	 * logical plan is created.
	 * 
	 * @param sender
	 *            Plan optimization requester.
	 * @param queries
	 *            Queries to check.
	 * @throws OpenFailedException
	 *             An {@link Exception} while opening the physical plan of an
	 *             query.
	 * @throws TransformationException
	 *             An {@link Exception} while transforming the logical plan of
	 *             an query.
	 * @throws NoTransformationConfiguration
	 *             An {@link Exception} because no
	 *             {@link TransformationConfiguration} is set. The
	 *             {@link TransformationConfiguration} should be set as
	 *             {@link QueryBuildConfiguration}.
	 * @throws CompilerException 
	 */
	private void checkPhysikalPlan(IPlanOptimizable sender, List<IQuery> queries)
			throws OpenFailedException, TransformationException,
			NoTransformationConfiguration, NoCompilerLoadedException {

		// check each query
		for (IQuery query : queries) {
			// create a physical plan if none is set
			if (query.getRoots() == null || query.getRoots().isEmpty()) {
				List<IPhysicalOperator> physicalPlan = sender.getCompiler()
						.transform(
								query.getLogicalPlan(),
								query.getBuildParameter()
										.getTransformationConfiguration());

				query.initializePhysicalRoots(physicalPlan);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.planmanagement.optimization.plan.IPlanOptimizer
	 * #optimizePlan
	 * (de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanOptimizable,
	 * de
	 * .uniol.inf.is.odysseus.planmanagement.optimization.OptimizationConfiguration
	 * . OptimizationConfiguration, java.util.List)
	 */
	@Override
	public IExecutionPlan optimizePlan(IPlanOptimizable sender,
			OptimizationConfiguration parameters, List<IQuery> allQueries)
			throws QueryOptimizationException {

		// check all queries
		try {
			checkPhysikalPlan(sender, allQueries);
		} catch (Exception e) {
			throw new QueryOptimizationException(
					"Error while optimizer checking Queries.", e);
		}

		//ArrayList<IPhysicalOperator> roots = new ArrayList<IPhysicalOperator>();
		ArrayList<IPartialPlan> partialPlans = new ArrayList<IPartialPlan>();
		ArrayList<IIterableSource<?>> leafSources = new ArrayList<IIterableSource<?>>();
		ArrayList<IIterableSource<?>> partialPlanSources;

		// Get Roots, PartialPlans and IIterableSource for the execution plan.
		// Each query will be one PartialPlan. Duplicated operators will be
		// ignored.
		for (IQuery query : allQueries) {
			partialPlanSources = new ArrayList<IIterableSource<?>>();
			//roots.addAll(query.getRoots());

			// store all new iterable sources as global sources and all
			// pipes as PartialPlan sources.
			List<IPhysicalOperator> queryOps = new ArrayList(
					query.getPhysicalChilds());
			queryOps.addAll(query.getRoots());

			for (IPhysicalOperator operator : queryOps) {
				IIterableSource<?> iterableSource = null;
				if (operator instanceof IIterableSource) {
					iterableSource = (IIterableSource) operator;
					// IterableSource is a Pipe
					if (iterableSource.isSink()
							&& !partialPlanSources.contains(iterableSource)) {
						partialPlanSources.add(iterableSource);
					} else if (!iterableSource.isSink() // IterableSource
														// is a
														// global Source
							&& !leafSources.contains(iterableSource)) {
						leafSources.add(iterableSource);
					}
				}
			}

			// create a PartialPlan for this query
			if (!partialPlanSources.isEmpty()) {
				partialPlans.add(new PartialPlan(partialPlanSources, query
						.getRoots(), query.getPriority(), query));
			}

		} // for (IQuery query : allQueries)

		// Create a new execution plan with the found informations.
		ExecutionPlan newPlan = new ExecutionPlan();
		newPlan.setPartialPlans(partialPlans);
		newPlan.setLeafSources(leafSources);
		//newPlan.setRoots(roots);

		return newPlan;
	}

}
