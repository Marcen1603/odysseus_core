package de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.DataSourceManager;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

public class ExecutorHelper implements IPlanModificationListener {

	private static Logger _logger = null;

	protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(ExecutorHelper.class);
		}
		return _logger;
	}

	private IExecutor executor = null;

	public void bindExecutor(IExecutor executor) {
		this.executor = executor;
		if (executor instanceof IServerExecutor) {
			((IServerExecutor) this.executor).addPlanModificationListener(this);
		}
		getLogger().debug("Executor bound");
	}

	public void unbindExecutor(IExecutor executor) {
		if (this.executor == executor) {
			if (executor instanceof IServerExecutor) {
				((IServerExecutor)this.executor).removePlanModificationListener(this);
			}
			this.executor = null;

			getLogger().debug("Executor unbound");
		}
	}

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		IQuery query = (IQuery) eventArgs.getValue();
		if (PlanModificationEventType.QUERY_ADDED.equals(eventArgs
				.getEventType())) {
			getLogger().debug("New query added.");

			for (ISource<?> src : getSources(query))
				DataSourceManager.getInstance().addSource(src);

		} else if (PlanModificationEventType.QUERY_REMOVE.equals(eventArgs
				.getEventType())) {
			getLogger().debug("Query removed");

			for (ISource<?> src : getSources(query))
				DataSourceManager.getInstance().removeSource(src);

		}
	}

	private List<ISource<?>> getSources(IQuery query) {
		List<IPhysicalOperator> physicalOperators = query.getPhysicalChilds();
		List<ISource<?>> sources = new ArrayList<ISource<?>>();

		for (IPhysicalOperator op : physicalOperators)
			if (op instanceof ISource && !(op instanceof ISink))
				sources.add((ISource<?>) op);
		return sources;
	}
}
