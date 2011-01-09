package de.uniol.inf.is.odysseus.cep.cepviewer.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.handlers.HandlerUtil;

import de.uniol.inf.is.odysseus.cep.cepviewer.CEPListView;
import de.uniol.inf.is.odysseus.cep.cepviewer.util.StringConst;
import de.uniol.inf.is.odysseus.cep.epa.CepOperator;

import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.impl.OdysseusNodeView;

/**
 * This class defines the Handler for the AddCommand which is called by the
 * GraphEditor within the rcp component.
 * 
 * @author Christian
 */
public class AddCommand extends AbstractHandler implements IHandler {

	/**
	 * This method will be called if a new CepOperator should be added to the
	 * CEPViewer.
	 * 
	 * @param event is the event.
	 */
	@SuppressWarnings("rawtypes")
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// get the selection of the active window
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		ISelection selection = window.getActivePage().getSelection();
		if (selection != null && selection instanceof IStructuredSelection) {
			// if the selection is an Implementation of the interface IStructuredSelection get the selected element.
			IStructuredSelection structSelection = (IStructuredSelection) selection;
			Object selectedObject = structSelection.getFirstElement();
			CEPListView view = null;
			try {
				// show the CEPViewer within the workbench
				PlatformUI.getWorkbench().showPerspective(
						StringConst.PLUGIN_ID, window);
				for (IViewReference a : PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage()
						.getViewReferences()) {
					if (a.getId().equals(StringConst.LIST_VIEW_ID)) {
						view = (CEPListView) a.getView(false);
					}
				}
			} catch (WorkbenchException e) {
				// TODO: Error-Message
				e.printStackTrace();
			}
			if(selectedObject instanceof OdysseusNodeView) {
				OdysseusNodeView node = (OdysseusNodeView) selectedObject;
				if (node.getModelNode().getContent() instanceof CepOperator) {
					//if the seletcted item holds an instance of CepOperator
					CepOperator operator = (CepOperator) node.getModelNode()
							.getContent();
					// add the instance to the CEPListView
					view.addStateMaschine(operator);
				}
			}
		}
		return null;
	}

}
