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
package de.uniol.inf.is.odysseus.rcp.queryview.physical;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.ui.handlers.IHandlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.rcp.views.query.IQueryViewData;
import de.uniol.inf.is.odysseus.rcp.views.query.IQueryViewDataProvider;
import de.uniol.inf.is.odysseus.rcp.views.query.QueryView;

public class PhysicalQueryViewDataProvider implements IQueryViewDataProvider, IPlanModificationListener, IDoubleClickListener, KeyListener {

	private static final String QUERY_NAME_SEPARATOR = ", ";
	private static final Logger LOG = LoggerFactory.getLogger(PhysicalQueryViewDataProvider.class);
	private QueryView view;

	@Override
	public void init(QueryView view) {
		this.view = view;

		view.getTableViewer().addDoubleClickListener(this);
		view.getTableViewer().getTable().addKeyListener(this);
		listenToExecutor();
	}

	@Override
	public void dispose() {
		view.getTableViewer().removeDoubleClickListener(this);
		view.getTableViewer().getTable().removeKeyListener(this);
		this.view = null;
		unlistenToExecutor();
	}

	private void unlistenToExecutor() {
		IServerExecutor executor = PhysicalQueryViewDataProviderPlugIn.getServerExecutor();
		executor.removePlanModificationListener(this);
	}

	private void listenToExecutor() {
		IServerExecutor executor = PhysicalQueryViewDataProviderPlugIn.getServerExecutor();
		executor.addPlanModificationListener(this);
	}

	@Override
	public Collection<? extends IQueryViewData> getData() {

		IServerExecutor serverExecutor = PhysicalQueryViewDataProviderPlugIn.getServerExecutor();
		Collection<IPhysicalQuery> queries = serverExecutor.getExecutionPlan().getQueries();

		List<IQueryViewData> result = new ArrayList<IQueryViewData>();
		for (IPhysicalQuery query : queries) {
			result.add(new PhysicalQueryViewData(
						query.getID(), 
						getQueryStatus(query), 
						query.getPriority(), 
						query.getLogicalQuery().getParserId(), 
						getQueryUser(query), 
						query.getLogicalQuery().getQueryText(),
						query.getName()));
		}

		return result;
	}

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		view.refreshTable();
	}

	@Override
	public void doubleClick(DoubleClickEvent event) {
		executeCommand("de.uniol.inf.is.odysseus.rcp.commands.CallGraphEditorCommand");
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.keyCode == 127) { // delete-key
			executeCommand("de.uniol.inf.is.odysseus.rcp.commands.RemoveQueryCommand");
		}
	}

	private void executeCommand( String cmdID ) {
		IHandlerService handlerService = (IHandlerService) view.getSite().getService(IHandlerService.class);
		try {
			handlerService.executeCommand(cmdID, null);
		} catch (Exception ex) {
			LOG.error("Exception during executing command {}.", cmdID, ex);
		}		
	}

	@SuppressWarnings("unused")
	private static String getQueryName(IPhysicalQuery query) {
		List<IPhysicalOperator> roots = query.getRoots();
		StringBuilder sb = new StringBuilder();
		
		for( int i = 0; i < roots.size(); i++ ) {
			String name = roots.get(i).getName();
			if( !Strings.isNullOrEmpty(name)) {
				sb.append(name);
				if( i < roots.size() - 1) {
					sb.append(QUERY_NAME_SEPARATOR);
				}
			}
		}
		return sb.toString();
	}

	private static String getQueryStatus(IPhysicalQuery q) {
		return q.isOpened() ? "Running" : "Inactive";
	}

	private static String getQueryUser(IPhysicalQuery query) {
		if (query.getLogicalQuery()!= null){
			if (query.getLogicalQuery().getUser() != null){
				return query.getLogicalQuery().getUser().getUser().getName();
			}
		}
		return "[No user]";
	}
}
