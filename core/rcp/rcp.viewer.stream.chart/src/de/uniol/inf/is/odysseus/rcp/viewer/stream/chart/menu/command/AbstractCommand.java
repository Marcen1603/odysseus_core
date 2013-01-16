/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.menu.command;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.rcp.viewer.editors.ChooseOperatorWindow;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.AbstractJFreeChart;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.Activator;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IOdysseusNodeView;

public abstract class AbstractCommand extends AbstractHandler {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractCommand.class);
	
	public AbstractJFreeChart<?,?> openView(AbstractJFreeChart<?,?> createView, IPhysicalOperator observingOperator) {
		IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			String secondaryIdentifierPrefix = observingOperator.getClass().getCanonicalName()+observingOperator.getClass().hashCode();
			String secondaryIdentifier = AbstractJFreeChart.getUniqueSecondIdentifier(secondaryIdentifierPrefix);
			AbstractJFreeChart<?,?> view = (AbstractJFreeChart<?,?>)activePage.showView(createView.getViewID(), secondaryIdentifier, IWorkbenchPage.VIEW_ACTIVATE);			
			view.initWithOperator(observingOperator);
			return view;
		} catch (Exception e) {
			LOG.error("Could not show view for chart", e);
		}
		return null;
	}		

	public IPhysicalOperator getSelectedOperator(ExecutionEvent event) {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);

		ISelection selection = window.getActivePage().getSelection();
		if (selection == null)
			return null;

		Optional<IPhysicalOperator> opForStream = null;
		if (selection instanceof IStructuredSelection) {

			IStructuredSelection structSelection = (IStructuredSelection) selection;
			Object selectedObject = structSelection.getFirstElement();

			if (selectedObject instanceof IPhysicalQuery) {
				IPhysicalQuery query = (IPhysicalQuery) selectedObject;
				opForStream = chooseOperator(query.getRoots());
			}

			if (selectedObject instanceof IOdysseusNodeView) {
				IOdysseusNodeView nodeView = (IOdysseusNodeView) selectedObject;
				// Auswahl holen
				opForStream = Optional.of(nodeView.getModelNode().getContent());				
			}
			
			if( selectedObject instanceof Integer ) {
                Integer queryID = (Integer)selectedObject;
                IExecutor executor = Activator.getExecutor();
                if( executor instanceof IServerExecutor ) {
                    IServerExecutor serverExecutor = (IServerExecutor)executor;
                    opForStream = chooseOperator(serverExecutor.getPhysicalRoots(queryID));
                } else {
                    LOG.error("Could not show charts outside server.");
                }
			}
		}

		if (opForStream != null && opForStream.isPresent()) {
			return opForStream.get();
		}

		return null;
	}
	
	private static Optional<IPhysicalOperator> chooseOperator( List<IPhysicalOperator> operators ) {
		if( operators.size() == 1 ) {
			return Optional.of(operators.get(0));
		}
		
		ChooseOperatorWindow wnd = new ChooseOperatorWindow(PlatformUI.getWorkbench().getDisplay(), operators);
		wnd.show();
		
		if( wnd.isCanceled() ) {
			return Optional.absent();
		} else {
			return Optional.of(wnd.getSelectedOperator());
		}
	}
}
