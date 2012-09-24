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
package de.uniol.inf.is.odysseus.rcp.queryview.logical;

import java.util.Collection;
import java.util.List;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.ui.handlers.IHandlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.rcp.views.query.IQueryViewData;
import de.uniol.inf.is.odysseus.rcp.views.query.IQueryViewDataProvider;
import de.uniol.inf.is.odysseus.rcp.views.query.QueryView;

public class LogicalQueryViewDataProvider implements IQueryViewDataProvider, IDoubleClickListener, KeyListener {

	private static final Logger LOG = LoggerFactory.getLogger(LogicalQueryViewDataProvider.class);
	private QueryView view;
	
	@Override
	public void init(QueryView view) {
		this.view = view;
		this.view.getTableViewer().addDoubleClickListener(this);
		this.view.getTableViewer().getTable().addKeyListener(this);
	}

	@Override
	public Collection<? extends IQueryViewData> getData() {
		IExecutor executor = LogicalQueryViewDataProviderPlugIn.getExecutor();
		
		List<ILogicalQuery> logicalQueries = getLogicalQueries(executor);
		
		return Lists.transform(logicalQueries, new Function<ILogicalQuery, IQueryViewData>() {
			@Override
			public IQueryViewData apply(ILogicalQuery logicalQuery) {
				return new LogicalQueryViewData(logicalQuery);
			}
		});
	}

	@Override
	public void dispose() {
		this.view.getTableViewer().removeDoubleClickListener(this);
		this.view.getTableViewer().getTable().removeKeyListener(this);
		this.view = null;
	}

	@Override
	public void doubleClick(DoubleClickEvent event) {
		executeCommand("de.uniol.inf.is.odysseus.rcp.commands.CallGraphEditorCommand");
	}
	
	private static List<ILogicalQuery> getLogicalQueries( IExecutor executor ) {
		Collection<Integer> logicalQueryIds = executor.getLogicalQueryIds();
		
		List<ILogicalQuery> logicalQueries = Lists.newArrayListWithCapacity(logicalQueryIds.size());
		for( Integer id : logicalQueryIds ) {
			logicalQueries.add( executor.getLogicalQueryById(id));
		}
		
		return logicalQueries;
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
}
