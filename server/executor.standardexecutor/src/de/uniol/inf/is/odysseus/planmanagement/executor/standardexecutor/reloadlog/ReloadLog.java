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
/** Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.uniol.inf.is.odysseus.planmanagement.executor.standardexecutor.reloadlog;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.queryadded.IQueryAddedListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * 
 * @author Dennis Geesen Created at: 17.08.2011
 */
public class ReloadLog implements IQueryAddedListener, IPlanModificationListener {

	private static Logger logger = LoggerFactory.getLogger(ReloadLog.class);
	public static final String LOG_FILENAME = OdysseusConfiguration.instance.get("reloadLogStoreFilename");

	private List<QueryEntry> queries = new ArrayList<QueryEntry>();

	public ReloadLog() {
	}
	
	/**
	 * The executor, if there is one bound.
	 */
	private static Optional<IServerExecutor> cExecutor;
	
	/**
	 * Binds an executor. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param executor
	 *            The executor to bind.
	 */
	public void bindExecutor(IExecutor executor) {
		IServerExecutor serverExecutor = (IServerExecutor) executor;
		serverExecutor.addQueryAddedListener(this);
		serverExecutor.addPlanModificationListener(this);
		cExecutor = Optional.of(serverExecutor);
	}

	/**
	 * Unbinds an executor. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param executor
	 *            The executor to unbind.
	 */
	public void unbindExecutor(IExecutor executor) {
		if (cExecutor.isPresent() && cExecutor.get() == executor) {
			IServerExecutor serverExecutor = (IServerExecutor) executor;
			serverExecutor.removeQueryAddedListener(this);
			serverExecutor.removePlanModificationListener(this);
			cExecutor = Optional.absent();
		}

	}

	@Override
	public void queryAddedEvent(String query, List<Integer> queryIds, QueryBuildConfiguration buildConfig, String parserID, ISession user, Context context) {
		logger.debug("Query added to log: " + query);
		QueryEntry qe = new QueryEntry();
		qe.parserID = parserID;
		qe.query = query;
		qe.transCfgID = buildConfig.getName();
		qe.username = user.getUser().getName();
		synchronized (queries) {
			queries.add(qe);
		}
		saveState();
	}

	private void saveState() {
		FileWriter writer = null;
		try {
			logger.debug("Save queries in log.");
			synchronized (queries) {
				writer = new FileWriter(LOG_FILENAME);
				for (QueryEntry e : this.queries) {
					writer.write(e.toString());
				}
			}
		} catch (Exception ex) {
			logger.error("Error while saving queries for reload", ex);
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (Exception ex) {
				logger.error("Error while closing reload log file", ex);
			}
		}

	}

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		if (PlanModificationEventType.QUERY_REMOVE.equals(eventArgs.getEventType())) {
			String queryText = ((IPhysicalQuery) eventArgs.getValue()).getQueryText();
			logger.debug("Removing query from log: " + queryText);
			synchronized (queries) {
				Iterator<QueryEntry> iterator = this.queries.iterator();
				while (iterator.hasNext()) {
					if (iterator.next().query.equals(queryText)) {
						iterator.remove();
					}
				}
				saveState();
			}
		}
		
	}

}