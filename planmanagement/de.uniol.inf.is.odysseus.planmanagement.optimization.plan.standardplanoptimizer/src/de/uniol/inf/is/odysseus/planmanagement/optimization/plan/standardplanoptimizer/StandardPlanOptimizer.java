package de.uniol.inf.is.odysseus.planmanagement.optimization.plan.standardplanoptimizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.TransformationException;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery;
import de.uniol.inf.is.odysseus.base.planmanagement.query.NoTransformationConfiguration;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.QueryBuildParameter;
import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.IPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IEditableExecutionPlan;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.OptimizeParameter;
import de.uniol.inf.is.odysseus.planmanagement.optimization.plan.EditableExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.optimization.plan.IPlanOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.optimization.plan.PartialPlan;

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
			List<IEditableQuery> queries) throws OpenFailedException,
			TransformationException, NoTransformationConfiguration {

		// check each query
		for (IEditableQuery query : queries) {
			// create a physical plan if none is set
			if (query.getSealedRoot() == null) {
				IPhysicalOperator physicalPlan = sender.getCompiler()
						.transform(
								query.getSealedLogicalPlan(),
								query.getBuildParameter()
										.getTransformationConfiguration());

				query.initializePhysicalPlan(physicalPlan);
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
	public IEditableExecutionPlan optimizePlan(IPlanOptimizable sender,
			OptimizeParameter parameters, List<IEditableQuery> allQueries)
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
		ArrayList<IIterableSource<?>> sources = new ArrayList<IIterableSource<?>>();
		ArrayList<IIterableSource<?>> partialPlanSources;
		ArrayList<IIterableSource<?>> sourcesTmp;

		// Get Roots, PartialPlans and IIterableSource for the execution plan.
		// Each query will be one PartialPlan. Duplicated operators will be
		// ignored.
		for (IEditableQuery query : allQueries) {
			// if the root is not checked
			if (!roots.contains(query.getSealedRoot())) {
				// Add root
				roots.add(query.getSealedRoot());

				// Get all iterable sources in the current physical plan.
				if (query.getSealedRoot().isSink()) {
					sourcesTmp = (ArrayList<IIterableSource<?>>) iterableSources((ISink<?>) query
							.getSealedRoot());
				} else {
					sourcesTmp = (ArrayList<IIterableSource<?>>) iterableSources((ISource<?>) query
							.getSealedRoot());
				}

				partialPlanSources = new ArrayList<IIterableSource<?>>();

				// store all new iterable sources as global sources and all
				// pipes as PartialPlan sources.
				for (IIterableSource<?> iterableSource : sourcesTmp) {
					// IterableSource is a Pipe
					if (iterableSource.isSink()
							&& !partialPlanSources.contains(iterableSource)) {
						partialPlanSources.add(iterableSource);
					} else if (!iterableSource.isSink() // IterableSource is a global Source
							&& !sources.contains(iterableSource)) {
						sources.add(iterableSource);
					}
				}

				// create a PartialPlan for this query
				if (query.getSealedRoot().isSink()
						&& !partialPlanSources.isEmpty()) {
					ArrayList<ISink<?>> root = new ArrayList<ISink<?>>();
					root.add((ISink<?>) query.getSealedRoot());
					partialPlans.add(new PartialPlan(partialPlanSources, root,
							query.getPriority()));
				}
			}
		}

		// Create a new execution plan with the found informations.
		EditableExecutionPlan newPlan = new EditableExecutionPlan();
		newPlan.setPartialPlans(partialPlans);
		newPlan.setSources(sources);
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
		Stack<ISource<?>> sources = new Stack<ISource<?>>();
		sources.push(source);
		while (!sources.isEmpty()) {
			ISource<?> curSource = sources.pop();
			if (curSource instanceof IIterableSource) {
				ret.add((IIterableSource<?>) curSource);
			}
			if (curSource instanceof IPipe) {
				IPipe<?, ?> pipe = (IPipe<?, ?>) curSource;
				for (PhysicalSubscription<? extends ISource<?>> subscription : pipe
						.getSubscribedTo()) {
					sources.push(subscription.getTarget());
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
				.getSubscribedTo();
		ArrayList<IIterableSource<?>> ret = new ArrayList<IIterableSource<?>>();
		for (PhysicalSubscription<? extends ISource<?>> s : slist) {
			ret.addAll(iterableSources(s.getTarget()));
		}
		return ret;
	}
}
