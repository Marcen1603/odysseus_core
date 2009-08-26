package de.uniol.inf.is.odysseus.planmanagement.optimization.standardoptimizer;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IEditableExecutionPlan;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.optimization.AbstractOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.event.startoptimize.AbstractStartEvent;
import de.uniol.inf.is.odysseus.planmanagement.optimization.event.startoptimize.MultiQueryStartEvent;
import de.uniol.inf.is.odysseus.planmanagement.optimization.event.startoptimize.QueryStartEvent;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.OptimizeParameter;

public class StandardOptimizer extends AbstractOptimizer {

	@Override
	protected IExecutionPlan processOptimizeEvent(
			AbstractStartEvent<?> eventArgs) throws QueryOptimizationException {

		if (eventArgs instanceof MultiQueryStartEvent) {
			MultiQueryStartEvent multiEvent = (MultiQueryStartEvent) eventArgs;

			if (multiEvent.getID() == MultiQueryStartEvent.QUERIES_ADD) {
				return queriesAdded(multiEvent.getSender(), multiEvent
						.getValue(), multiEvent.getParameter());
			}
		} else if (eventArgs instanceof QueryStartEvent) {
			QueryStartEvent queryEvent = (QueryStartEvent) eventArgs;

			if (queryEvent.getID() == QueryStartEvent.QUERY_REMOVE) {
				return queryRemoved(queryEvent.getSender(), queryEvent
						.getValue(), queryEvent.getParameter());
			}
		}

		return eventArgs.getSender().getEditableExecutionPlan();
	}

	public IExecutionPlan queriesAdded(IOptimizable sender,
			ArrayList<IEditableQuery> queries, OptimizeParameter parameters)
			throws QueryOptimizationException {
		if (queries != null && queries.size() > 0) {
			for (IEditableQuery editableQuery : queries) {
				this.queryOptimizer.optimizeQuery(sender, editableQuery,
						parameters);
			}

			ArrayList<IEditableQuery> newPlan = sender.getRegisteredQueries();
			newPlan.addAll(queries);

			IEditableExecutionPlan newExecutionPlan = this.planOptimizer.optimizePlan(
					sender, parameters, newPlan);

			return this.planMigrationStrategie.migratePlan(sender,
					newExecutionPlan);
		}
		return null;
	}

	public IExecutionPlan queryRemoved(IOptimizable sender,
			IEditableQuery query, OptimizeParameter parameters)
			throws QueryOptimizationException {
		if (query != null) {
			ArrayList<IEditableQuery> newPlan = new ArrayList<IEditableQuery>(sender.getRegisteredQueries());
			newPlan.remove(query);

			IEditableExecutionPlan newExecutionPlan = this.planOptimizer.optimizePlan(
					sender, parameters, newPlan);

			return this.planMigrationStrategie.migratePlan(sender,
					newExecutionPlan);
		}
		return null;
	}
}
