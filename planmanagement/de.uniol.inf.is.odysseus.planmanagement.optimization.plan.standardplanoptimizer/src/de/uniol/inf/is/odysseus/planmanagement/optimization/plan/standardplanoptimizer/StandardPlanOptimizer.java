package de.uniol.inf.is.odysseus.planmanagement.optimization.plan.standardplanoptimizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.TransformationException;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery;
import de.uniol.inf.is.odysseus.base.planmanagement.query.NoTransformationConfiguration;
import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.IPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.Subscription;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IEditableExecutionPlan;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.OptimizeParameter;
import de.uniol.inf.is.odysseus.planmanagement.optimization.plan.EditableExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.optimization.plan.IPlanOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.optimization.plan.PartialPlan;

public class StandardPlanOptimizer implements IPlanOptimizer {

	private void checkPhysikalPlan(IPlanOptimizable sender,
			List<IEditableQuery> queries) throws OpenFailedException,
			TransformationException, NoTransformationConfiguration {

		for (IEditableQuery query : queries) {
			if (query.getSealedRoot() == null) {
				IPhysicalOperator physicalPlan = sender
						.getCompiler()
						.transform(
								query.getSealedLogicalPlan(),
								query
										.getBuildParameter()
										.getTransformationConfiguration());

				query.initializePhysicalPlan(physicalPlan);
			}
		}
	}

	@Override
	public IEditableExecutionPlan optimizePlan(IPlanOptimizable sender,
			OptimizeParameter parameters, List<IEditableQuery> allQueries)
			throws QueryOptimizationException {

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

		for (IEditableQuery query : allQueries) {
			if (!roots.contains(query.getSealedRoot())) {
				roots.add(query.getSealedRoot());

				if (query.getSealedRoot().isSink()) {
					sourcesTmp = (ArrayList<IIterableSource<?>>) iterableSources((ISink<?>) query
							.getSealedRoot());
				} else {
					sourcesTmp = (ArrayList<IIterableSource<?>>) iterableSources((ISource<?>) query
							.getSealedRoot());
				}

				partialPlanSources = new ArrayList<IIterableSource<?>>();

				for (IIterableSource<?> iterableSource : sourcesTmp) {
					if (iterableSource.isSink()
							&& !partialPlanSources.contains(iterableSource)) {
						partialPlanSources.add(iterableSource);
					} else if (!sources.contains(iterableSource)) {
						sources.add(iterableSource);
					}
				}

				if (query.getSealedRoot().isSink()
						&& !partialPlanSources.isEmpty()) {
					ArrayList<ISink<?>> root = new ArrayList<ISink<?>>();
					root.add((ISink<?>) query.getSealedRoot());
					partialPlans.add(new PartialPlan(partialPlanSources, root,
							query.getPriority()));
				}
			}
		}

		EditableExecutionPlan newPlan = new EditableExecutionPlan();
		newPlan.setPartialPlans(partialPlans);
		newPlan.setSources(sources);

		return newPlan;
	}

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
				for (Subscription<? extends ISource<?>> subscription : pipe
						.getSubscribedTo()) {
					sources.push(subscription.target);
				}
			}
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	private ArrayList<IIterableSource<?>> iterableSources(ISink<?> sink) {
		if (sink instanceof ISource) {
			return iterableSources((ISource<?>) sink);
		}
		List<? extends Subscription<? extends ISource<?>>> slist = sink
				.getSubscribedTo();
		ArrayList<IIterableSource<?>> ret = new ArrayList<IIterableSource<?>>();
		for (Subscription<? extends ISource<?>> s : slist) {
			ret.addAll(iterableSources(s.target));
		}
		return ret;
	}
}
