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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.streamconnection.DefaultStreamConnection;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPlugIn;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;
import de.uniol.inf.is.odysseus.script.parser.IOdysseusScriptParser;

public final class DashboardPartController {

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

	public IDashboardPart getDashboardPart() {
		return dashboardPart;
	}

	public void start() throws Exception {
		Preconditions.checkState(status != Status.RUNNING, "Container for DashboardParts already started");
		Preconditions.checkState(status != Status.PAUSED, "Container for DashboardParts is paused and cannot be started.");

		List<String> queryTextLines = dashboardPart.getQueryTextProvider().getQueryText();

		IOdysseusScriptParser parser = DashboardPlugIn.getScriptParser();
		ISession caller = OdysseusRCPPlugIn.getActiveSession();
		
		List<?> results = parser.execute( parser.parseScript(queryTextLines.toArray(new String[0]), caller), caller, null);  
		queryIDs = getExecutedQueryIDs(results);

		List<IPhysicalOperator> roots = Lists.newArrayList();
		for (Integer id : queryIDs) {
			for( IPhysicalOperator rootOfQuery :  DashboardPlugIn.getExecutor().getPhysicalRoots(id) ) {
				if( !(rootOfQuery instanceof DefaultStreamConnection)) {
					roots.add(rootOfQuery);
				}
			}
		}

		streamConnection = new DefaultStreamConnection<IStreamObject<?>>(roots);
		
		dashboardPart.onStart(roots);
		// FIXME: TIMO
		//streamConnection.addStreamElementListener(dashboardPart);
		streamConnection.connect();

		status = Status.RUNNING;

	}

	public void pause() {
		if (status == Status.PAUSED) {
			return;
		}

		streamConnection.disable();
		dashboardPart.onPause();

		status = Status.PAUSED;
	}

	public void unpause() {
		Preconditions.checkState(status == Status.PAUSED, "Container for DashboardParts cannot be unpaused.");

		dashboardPart.onUnpause();
		streamConnection.enable();

		status = Status.RUNNING;
	}

	public void stop() {
		if (status == Status.STOPPED) {
			return;
		}

		streamConnection.disconnect();
		dashboardPart.onStop();

		for (Integer id : queryIDs) {
			try {
				DashboardPlugIn.getExecutor().removeQuery(id, OdysseusRCPPlugIn.getActiveSession());
			} catch( Throwable t ) {
				LOG.error("Exception during stopping query {}.", id, t);
			}
		}
		queryIDs = null;

		status = Status.STOPPED;
	}

	private static List<Integer> getExecutedQueryIDs(List<?> results) {
		List<Integer> ids = Lists.newArrayList();

		for (Object result : results) {
			if (result instanceof List) {
				@SuppressWarnings("rawtypes")
				List list = (List) result;
				for (Object obj : list) {
					if (obj instanceof Integer) {
						ids.add((Integer) obj);
					}
				}
			}
		}

		return ids;
	}
}
