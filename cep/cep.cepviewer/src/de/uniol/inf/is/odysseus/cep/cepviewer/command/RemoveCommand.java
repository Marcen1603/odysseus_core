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
import de.uniol.inf.is.odysseus.cep.cepviewer.list.entry.InstanceTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.list.entry.MachineTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.util.StringConst;
import de.uniol.inf.is.odysseus.cep.epa.CepOperator;

public class RemoveCommand extends AbstractHandler implements IHandler {

	@SuppressWarnings("rawtypes")
	public Object execute(ExecutionEvent event) throws ExecutionException {
		for (IViewReference a : PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getViewReferences()) {
			if (a.getId().equals(StringConst.LIST_VIEW_ID)) {
				CEPListView view = (CEPListView) a.getView(false);
				ISelection selection = view.getActiveList().getTree()
						.getSelection();
				IStructuredSelection structSelection = (IStructuredSelection) selection;
				Object selectedObject = structSelection.getFirstElement();
				if (selectedObject instanceof InstanceTreeItem) {
					InstanceTreeItem item = (InstanceTreeItem) selectedObject;
					for (CepOperator operator : view.getOperators()) {
						if (operator.getStateMachine().equals(
								item.getContent().getStateMachine())) {
							operator.getCEPEventAgent().removeCEPEventListener(
									view.getListener());
							break;
						}
					}
					if (view.getNormalList().remove(item)
							&& view.getQueryList().remove(item)
							&& view.getStatusList().remove(item)) {
						view.setInfoData();
					}
				} else if (selectedObject instanceof MachineTreeItem) {
					MachineTreeItem item = (MachineTreeItem) selectedObject;
					for (CepOperator operator : view.getOperators()) {
						if (operator.getStateMachine()
								.equals(item.getContent())) {
							operator.getCEPEventAgent().removeCEPEventListener(
									view.getListener());
							break;
						}
					}
					if (view.getNormalList().remove(item)
							&& view.getQueryList().remove(item)
							&& view.getStatusList().remove(item)) {
						view.setInfoData();
					}
				} else {
					return null;
				}
			}
		}
		return null;
	}

}
