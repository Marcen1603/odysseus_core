/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.dashboard.controller;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.streamconnection.DefaultStreamConnection;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.dashboard.Configuration;
import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPlugIn;
import de.uniol.inf.is.odysseus.rcp.dashboard.IConfigurationListener;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;
import de.uniol.inf.is.odysseus.script.parser.IOdysseusScriptParser;

public final class DashboardPartController implements IConfigurationListener {

	private static enum Status {
		RUNNING, STOPPED, PAUSED
	}

	private static final Logger LOG = LoggerFactory.getLogger(DashboardPartController.class);

	private final IDashboardPart dashboardPart;
	private List<Integer> queryIDs;
	private DefaultStreamConnection<IStreamObject<?>> streamConnection;

	private Status status = Status.STOPPED;

	public DashboardPartController(IDashboardPart dashboardPart) {
		this.dashboardPart = Preconditions.checkNotNull(dashboardPart, "DashboardPart for container must not be null!");
	}

	public void pause() {
		if (status == Status.PAUSED) {
			return;
		}

		streamConnection.disable();
		dashboardPart.onPause();

		status = Status.PAUSED;
	}

	public void start() throws ControllerException {
		Preconditions.checkState(status != Status.RUNNING, "Container for DashboardParts already started");
		Preconditions.checkState(status != Status.PAUSED, "Container for DashboardParts is paused and cannot be started.");

		final List<String> queryTextLines = dashboardPart.getQueryTextProvider().getQueryText();

		final IOdysseusScriptParser parser = DashboardPlugIn.getScriptParser();
		final ISession caller = OdysseusRCPPlugIn.getActiveSession();

		try {
			List<?> results = parser.execute(parser.parseScript(queryTextLines.toArray(new String[0]), caller), caller, null);
			queryIDs = getExecutedQueryIDs(results);
			List<IPhysicalOperator> roots = determineRoots(queryIDs);

			streamConnection = new DefaultStreamConnection<IStreamObject<?>>(roots);

			dashboardPart.onStart(roots);
			dashboardPart.getConfiguration().addListener(this);
			addAsListener();

			streamConnection.connect();

			status = Status.RUNNING;
		} catch (final Exception ex) {
			throw new ControllerException("Could not start query for dashboardpart", ex);
		}
	}

	private static List<IPhysicalOperator> determineRoots(Collection<Integer> queryIDs) {
		final List<IPhysicalOperator> roots = Lists.newArrayList();
		for (final Integer id : queryIDs) {
			for (final IPhysicalOperator rootOfQuery : DashboardPlugIn.getExecutor().getPhysicalRoots(id)) {
				if (!(rootOfQuery instanceof DefaultStreamConnection)) {
					roots.add(rootOfQuery);
				}
			}
		}
		return roots;
	}

	private void addAsListener() {
		String[] sinkNames = determineDashboardPartSinkNames();
		if (sinkNames.length > 0) {
			for (String sinkName : sinkNames) {
				streamConnection.addStreamElementListener(dashboardPart, sinkName.trim());
			}
		} else {
			streamConnection.addStreamElementListener(dashboardPart);
		}
	}

	private String[] determineDashboardPartSinkNames() {
		Configuration configuration = dashboardPart.getConfiguration();
		String sinkNamesString = configuration.get(Configuration.SINK_NAME_CFG);
		if (!Strings.isNullOrEmpty(sinkNamesString)) {
			return sinkNamesString.split(",");
		}
		return new String[0];
	}

	public void stop() {
		if (status == Status.STOPPED) {
			return;
		}

		streamConnection.disconnect();
		removeAsListener();
		dashboardPart.getConfiguration().removeListener(this);
		dashboardPart.onStop();

		stopQueries();

		status = Status.STOPPED;
	}

	private void stopQueries() {
		for (final Integer id : queryIDs) {
			try {
				DashboardPlugIn.getExecutor().removeQuery(id, OdysseusRCPPlugIn.getActiveSession());
			} catch (final Throwable t) {
				LOG.error("Exception during stopping query {}.", id, t);
			}
		}
		queryIDs = null;
	}

	private void removeAsListener() {
		String[] sinkNames = determineDashboardPartSinkNames();
		if (sinkNames.length > 0) {
			for (String sinkName : sinkNames) {
				streamConnection.removeStreamElementListener(dashboardPart, sinkName.trim());
			}
		} else {
			streamConnection.removeStreamElementListener(dashboardPart);
		}
	}

	@Override
	public void settingChanged(String settingName, Object oldValue, Object newValue) {
		if (settingName.equals(Configuration.SINK_NAME_CFG)) {
			removeAsListener();
			addAsListener();
		}
	}

	public boolean isStarted() {
		return status == Status.RUNNING;
	}

	public boolean isPaused() {
		return status == Status.PAUSED;
	}

	public void unpause() {
		Preconditions.checkState(status == Status.PAUSED, "Container for DashboardParts cannot be unpaused.");

		dashboardPart.onUnpause();
		streamConnection.enable();

		status = Status.RUNNING;
	}

	private static List<Integer> getExecutedQueryIDs(List<?> results) {
		final List<Integer> ids = Lists.newArrayList();

		for (final Object result : results) {
			if (result instanceof List) {
				@SuppressWarnings("rawtypes")
				final List list = (List) result;
				for (final Object obj : list) {
					if (obj instanceof Integer) {
						ids.add((Integer) obj);
					}
				}
			}
		}

		return ids;
	}
}
