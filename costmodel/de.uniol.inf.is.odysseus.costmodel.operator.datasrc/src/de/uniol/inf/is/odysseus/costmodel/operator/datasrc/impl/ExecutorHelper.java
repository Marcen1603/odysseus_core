/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.DataSourceManager;

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
		IPhysicalQuery query = (IPhysicalQuery) eventArgs.getValue();
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

	private static List<ISource<?>> getSources(IPhysicalQuery query) {
		List<IPhysicalOperator> physicalOperators = query.getPhysicalChilds();
		List<ISource<?>> sources = new ArrayList<ISource<?>>();

		for (IPhysicalOperator op : physicalOperators)
			if (op.isSource() && !(op.isSink()))
				sources.add((ISource<?>) op);
		return sources;
	}
}
