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

import java.util.Objects;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.physicaloperator.interval.TITransferArea;
import de.uniol.inf.is.odysseus.core.streamconnection.DefaultStreamConnection;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartListener;

public final class DashboardPartController implements IDashboardPartListener {

	private static enum Status {
		RUNNING, STOPPED, PAUSED
	}

	private final IDashboardPart dashboardPart;
	private DefaultStreamConnection<IStreamObject<?>> streamConnection;

	private Status status = Status.STOPPED;
	private QueryExecutionHandler queryHandler;

	public DashboardPartController(IDashboardPart dashboardPart) {
		this.dashboardPart = Objects.requireNonNull(dashboardPart, "DashboardPart for container must not be null!");
		
		queryHandler = new QueryExecutionHandler(dashboardPart);
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
		// Preconditions.checkState(status != Status.RUNNING, "Container for DashboardParts already started");
		// Preconditions.checkState(status != Status.PAUSED, "Container for DashboardParts is paused and cannot be started.");

		try {
			queryHandler.start();
			
			Collection<IPhysicalOperator> queryRoots = queryHandler.getRoots();
			streamConnection = new DefaultStreamConnection<IStreamObject<?>>(queryRoots);
			if( dashboardPart.isSinkSynchronized() ) {
				streamConnection.setTransferHandler(createTransferArea());
			}
			dashboardPart.onStart(queryRoots);
			
			addAsListener();

			streamConnection.connect();

			status = Status.RUNNING;
		} catch (final Exception ex) {
			throw new ControllerException("Could not start query for dashboardpart", ex);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static ITransferArea<IStreamObject<?>, IStreamObject<?>> createTransferArea() {
		return (ITransferArea<IStreamObject<?>, IStreamObject<?>>)new TITransferArea();
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
		String sinkNamesString = dashboardPart.getSinkNames();
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
		dashboardPart.addListener(this);
		dashboardPart.onStop();

		queryHandler.stop();

		status = Status.STOPPED;
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

	public boolean isStarted() {
		return status == Status.RUNNING;
	}

	public boolean isPaused() {
		return status == Status.PAUSED;
	}

	public void unpause() {
		// Preconditions.checkState(status == Status.PAUSED, "Container for DashboardParts cannot be unpaused.");

		dashboardPart.onUnpause();
		streamConnection.enable();

		status = Status.RUNNING;
	}

	@Override
	public void dashboardPartChanged(IDashboardPart changedPart) {
		removeAsListener();
		addAsListener();
	}
	
	public Collection<IPhysicalOperator> getQueryRoots() {
		return queryHandler.getRoots();
	}
}
