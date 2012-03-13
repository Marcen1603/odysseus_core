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
package de.uniol.inf.is.odysseus.cep.cepviewer.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.cep.cepviewer.CEPListView;
import de.uniol.inf.is.odysseus.cep.cepviewer.listmodel.InstanceTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.listmodel.MachineTreeItem;
import de.uniol.inf.is.odysseus.cep.epa.CepOperator;
import de.uniol.inf.is.odysseus.cep.metamodel.StateMachine;

/**
 * This class defines the handler which is called if an entry of should be
 * removed.
 * 
 * @author Christian
 */
public class RemoveCommand extends AbstractHandler implements IHandler {

	/**
	 * This method removes an entry within the Tree of the TreeViewer widget in
	 * every list of the CEPListView.
	 * 
	 * @param event
	 *            is the ExecutionEvent fired by the corresponding context menu
	 *            entry
	 */
	@Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
		// search for the reference of the CEPListView
		for (IViewReference a : PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getViewReferences()) {
			if (a.getId().equals(CEPListView.ID)) {
				CEPListView view = (CEPListView) a.getView(false);
				// get the selection of the active list
				ISelection selection = view.getActiveList().getTree()
						.getSelection();
				IStructuredSelection structSelection = (IStructuredSelection) selection;
				Object selectedObject = structSelection.getFirstElement();
				if (selectedObject instanceof InstanceTreeItem) {
					// if the selection is an instance, remove it from all lists
					InstanceTreeItem item = (InstanceTreeItem) selectedObject;
					if (view.getNormalList().remove(item)
							&& view.getQueryList().remove(item)
							&& view.getStatusList().remove(item)) {
						view.getStatusList().getTree().refresh();
						view.setInfoData();
					}
				} else if (selectedObject instanceof MachineTreeItem) {
					// if the selection is a machine, remove the Listener from
					// the corresponding CepOperator
					MachineTreeItem item = (MachineTreeItem) selectedObject;
					for (CepOperator<?, ?> operator : view.getOperators()) {
						for (@SuppressWarnings("rawtypes") StateMachine sm : operator.getStateMachines()) {
							if (sm.equals(item.getContent())) {
								operator.getCEPEventAgent()
										.removeCEPEventListener(
												view.getListener());
								break;
							}
						}
					}
					// remove the machine and all instances of the machine from
					// the lists
					if (view.getNormalList().remove(item)
							&& view.getQueryList().remove(item)
							&& view.getStatusList().remove(item)) {
						view.setInfoData();
					}
				}
			}
		}
		return null;
	}

}
