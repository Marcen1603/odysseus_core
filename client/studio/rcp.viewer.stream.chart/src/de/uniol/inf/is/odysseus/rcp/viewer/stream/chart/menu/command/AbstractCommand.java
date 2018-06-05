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

import java.util.Collection;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.IHasRoots;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.viewer.editors.ChooseOperatorWindow;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.AbstractJFreeChart;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.Activator;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IOdysseusNodeView;

public abstract class AbstractCommand extends AbstractHandler {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractCommand.class);

	public void openView(AbstractJFreeChart<?, ?> createView, Collection<IPhysicalOperator> observingOperators) {
		IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			for( IPhysicalOperator observingOperator : observingOperators ) {
				String secondaryIdentifierPrefix = observingOperator.getClass().getCanonicalName() + observingOperator.getClass().hashCode();
				String secondaryIdentifier = AbstractJFreeChart.getUniqueSecondIdentifier(secondaryIdentifierPrefix);
				AbstractJFreeChart<?, ?> view = (AbstractJFreeChart<?, ?>) activePage.showView(createView.getViewID(), secondaryIdentifier, IWorkbenchPage.VIEW_ACTIVATE);
				view.initWithOperator(observingOperator);
			}
		} catch (Exception e) {
			LOG.error("Could not show view for chart", e);
		}
	}

	public Collection<IPhysicalOperator> getSelectedOperators(ExecutionEvent event) {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);

		ISelection selection = window.getActivePage().getSelection();
		if (selection == null)
			return null;

		Collection<IPhysicalOperator> opsForStream = null;
		if (selection instanceof IStructuredSelection) {

			IStructuredSelection structSelection = (IStructuredSelection) selection;
			Object selectedObject = structSelection.getFirstElement();

			// FIXME: Find another solution to remove dependencies to
			// IPhysicalQuery
			if (selectedObject instanceof IHasRoots) {
				IHasRoots query = (IHasRoots) selectedObject;
				opsForStream = chooseOperator(query.getRoots());
			}

			if (selectedObject instanceof IOdysseusNodeView) {
				IOdysseusNodeView nodeView = (IOdysseusNodeView) selectedObject;
				// Auswahl holen
				opsForStream = Lists.newArrayList(nodeView.getModelNode().getContent());
			}

			if (selectedObject instanceof Integer) {
				Integer queryID = (Integer) selectedObject;
				IExecutor executor = Activator.getExecutor();
				opsForStream = chooseOperator(executor.getPhysicalRoots(queryID, OdysseusRCPPlugIn.getActiveSession()));
			}
		}

		return opsForStream;
	}

	private static Collection<IPhysicalOperator> chooseOperator(List<IPhysicalOperator> operators) {
		if (operators.size() == 1) {
			return operators;
		}

		ChooseOperatorWindow wnd = new ChooseOperatorWindow(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), operators);
		if( wnd.open() == Window.OK ) {
			return wnd.getSelectedOperator();
		}
		
		return Lists.newArrayList();
	}
}
