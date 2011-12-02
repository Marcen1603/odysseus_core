/** Copyright [2011] [The Odysseus Team]
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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.AbstractChart;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IOdysseusNodeView;

public abstract class AbstractCommand extends AbstractHandler {

	public AbstractChart<?,?> openView(AbstractChart<?,?> createView, IPhysicalOperator observingOperator) {
		IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			String secondaryIdentifierPrefix = observingOperator.getClass().getCanonicalName()+observingOperator.getClass().hashCode();
			String secondaryIdentifier = AbstractChart.getUniqueSecondIdentifier(secondaryIdentifierPrefix);
			AbstractChart<?,?> view = (AbstractChart<?,?>)activePage.showView(createView.getViewID(), secondaryIdentifier, IWorkbenchPage.VIEW_ACTIVATE);			
			view.initWithOperator(observingOperator);
			return view;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}		

	public IPhysicalOperator getSelectedOperator(ExecutionEvent event) {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);

		ISelection selection = window.getActivePage().getSelection();
		if (selection == null)
			return null;

		IPhysicalOperator opForStream = null;
		if (selection instanceof IStructuredSelection) {

			IStructuredSelection structSelection = (IStructuredSelection) selection;
			Object selectedObject = structSelection.getFirstElement();

			if (selectedObject instanceof IQuery) {
				IQuery query = (IQuery) selectedObject;
				if (query.getRoots().size() > 0) {
					opForStream = query.getRoots().get(0);
				} else {
					opForStream = query.getPhysicalChilds().get(0);
				}
			}

			if (selectedObject instanceof IOdysseusNodeView) {
				IOdysseusNodeView nodeView = (IOdysseusNodeView) selectedObject;
				// Auswahl holen
				opForStream = nodeView.getModelNode().getContent();				
			}
		}

		if (opForStream != null) {
			// create view
			return opForStream;
		}

		return null;
	}
}
