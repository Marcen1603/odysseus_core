package de.uniol.inf.is.odysseus.planmanagement.optimization.plan.standardplanoptimizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import de.uniol.inf.is.odysseus.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.IPipe;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.OptimizeParameter;
import de.uniol.inf.is.odysseus.planmanagement.optimization.plan.ExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.optimization.plan.IPlanOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.optimization.plan.PartialPlan;
import de.uniol.inf.is.odysseus.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.planmanagement.query.NoTransformationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.QueryBuildParameter;

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
	 *             {@link QueryBuildParameter}.
	 */
	private void checkPhysikalPlan(IPlanOptimizable sender,
			List<IQuery> queries) throws OpenFailedException,
			TransformationException, NoTransformationConfiguration {

		// check each query
		for (IQuery query : queries) {
			// create a physical plan if none is set
			if (query.getRoots() == null || query.getRoots().isEmpty()) {
				List<IPhysicalOperator> physicalPlan = sender.getCompiler()
						.transform(
								query.getLogicalPlan(),
								query.getBuildParameter().getTransformationConfiguration());

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
	 * de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.
	 * OptimizeParameter, java.util.List)
	 */
	@Override
	public IExecutionPlan optimizePlan(IPlanOptimizable sender,
			OptimizeParameter parameters, List<IQuery> allQueries)
			throws QueryOptimizationException {

		// check all queries
		try {
			checkPhysikalPlan(sender, allQueries);
		} catch (Exception e) {
			throw new QueryOptimizationException(
					"Error while optimizer checking Queries.", e);
		}

		ArrayList<IPhysicalOperator> roots = new ArrayList<IPhysicalOperator>();
		ArrayList<IPartialPlan> partialPlans = new ArrayList<IPartialPlan>();
		ArrayList<IIterableSource<?>> leafSources = new ArrayList<IIterableSource<?>>();
		ArrayList<IIterableSource<?>> partialPlanSources;
		ArrayList<IIterableSource<?>> sourcesTmp;

		// Get Roots, PartialPlans and IIterableSource for the execution plan.
		// Each query will be one PartialPlan. Duplicated operators will be
		// ignored.
		for (IQuery query : allQueries) {
			partialPlanSources = new ArrayList<IIterableSource<?>>();
			for(IPhysicalOperator curRoot : query.getRoots()){
			
				// if the root is not checked
				if (!roots.contains(curRoot)) {
					// Add root
					roots.add(curRoot);
	
					// Get all iterable sources in the current physical plan.
					if (curRoot.isSink()) {
						sourcesTmp = (ArrayList<IIterableSource<?>>) iterableSources((ISink<?>) curRoot);
					} else {
						sourcesTmp = (ArrayList<IIterableSource<?>>) iterableSources((ISource<?>) curRoot);
					}
	
//					partialPlanSources = new ArrayList<IIterableSource<?>>();
	
					// store all new iterable sources as global sources and all
					// pipes as PartialPlan sources.
					for (IIterableSource<?> iterableSource : sourcesTmp) {
						// IterableSource is a Pipe
						if (iterableSource.isSink()
								&& !partialPlanSources.contains(iterableSource)) {
							partialPlanSources.add(iterableSource);
						} else if (!iterableSource.isSink() // IterableSource is a
															// global Source
								&& !leafSources.contains(iterableSource)) {
							leafSources.add(iterableSource);
						}
					}
				}
			}
			
			// create a PartialPlan for this query
			
			// OLD code
//			if (query.getRoot().isSink()
//					&& !partialPlanSources.isEmpty()) {
//				ArrayList<ISink<?>> root = new ArrayList<ISink<?>>();
//				root.add((ISink<?>) query.getRoot());
//				partialPlans.add(new PartialPlan(partialPlanSources, root,
//						query.getPriority()));
//			}
			
			// NEW code
			if (!partialPlanSources.isEmpty()) {
				partialPlans.add(new PartialPlan(partialPlanSources, query.getRoots(), query.getPriority()));
			}		
			
		}

		// Create a new execution plan with the found informations.
		ExecutionPlan newPlan = new ExecutionPlan();
		newPlan.setPartialPlans(partialPlans);
		newPlan.setLeafSources(leafSources);
		newPlan.setRoots(roots);

		return newPlan;
		}

	/**
	 * Get all iterable sources of a root source. TODO: should be extracted to
	 * standard graph functions.
	 * 
	 * @param source
	 *            Root source for the search.
	 * @return List of iterable sources
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<IIterableSource<?>> iterableSources(ISource<?> source) {
		ArrayList<IIterableSource<?>> ret = new ArrayList<IIterableSource<?>>();
		List<ISource<?>> visited = new ArrayList<ISource<?>>();
		Stack<ISource<?>> sources = new Stack<ISource<?>>();
		sources.push(source);
		while (!sources.isEmpty()) {
			ISource<?> curSource = sources.pop();
			if (!visited.contains(curSource)) {
				visited.add(curSource);
				if (curSource instanceof IIterableSource) {
					ret.add((IIterableSource<?>) curSource);
				}
				if (curSource instanceof IPipe) {
					IPipe<?, ?> pipe = (IPipe<?, ?>) curSource;
					for (PhysicalSubscription<? extends ISource<?>> subscription : pipe
							.getSubscribedToSource()) {
						sources.push(subscription.getTarget());
					}
				}
			}
		}
		return ret;
	}

	/**
	 * Get all iterable sources of a root sink. TODO: should be extracted to
	 * standard graph functions.
	 * 
	 * @param sink
	 *            Root sink for the search.
	 * @return List of iterable sources
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<IIterableSource<?>> iterableSources(ISink<?> sink) {
		if (sink instanceof ISource) {
			return iterableSources((ISource<?>) sink);
		}
		Collection<? extends PhysicalSubscription<? extends ISource<?>>> slist = sink
				.getSubscribedToSource();
		ArrayList<IIterableSource<?>> ret = new ArrayList<IIterableSource<?>>();
		for (PhysicalSubscription<? extends ISource<?>> s : slist) {
			ret.addAll(iterableSources(s.getTarget()));
		}
		return ret;
	}

}
