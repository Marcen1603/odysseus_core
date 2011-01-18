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

import de.uniol.inf.is.odysseus.cep.cepviewer.Activator;
import de.uniol.inf.is.odysseus.cep.cepviewer.CEPListView;
import de.uniol.inf.is.odysseus.cep.cepviewer.exception.CEPListViewNotFoundException;
import de.uniol.inf.is.odysseus.cep.cepviewer.exception.CEPViewerNotShownException;
import de.uniol.inf.is.odysseus.cep.cepviewer.exception.NoCepOperatorSelectedException;
import de.uniol.inf.is.odysseus.cep.cepviewer.listmodel.CEPInstance;
import de.uniol.inf.is.odysseus.cep.epa.CepOperator;
import de.uniol.inf.is.odysseus.cep.epa.StateMachineInstance;

import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.impl.OdysseusNodeView;

/**
 * This class defines the handler for the AddCommand which is called by the
 * GraphEditor within the rcp component.
 * 
 * @author Christian
 */
public class AddCommand extends AbstractHandler implements IHandler {

	/**
	 * This method will be called if a new CepOperator should be added to the
	 * CEPViewer.
	 * 
	 * @param event
	 *            is the event.
	 */
	@SuppressWarnings("rawtypes")
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		// get the CepOperator
		CepOperator operator = this.getCepOperator(window);
		if (operator == null) {
			throw new NoCepOperatorSelectedException();
		}
		try {
			// show the CEPViewer in the workbench
			window.getWorkbench()
					.showPerspective(Activator.PLUGIN_ID, window);
			// get a reference of the CEPListView and add the CepOperator
			CEPListView listView = this.getCEPListView();
			if (listView == null) {
				throw new CEPListViewNotFoundException();
			}
			// initialize ICEPEventListener
			listView.getOperators().add(operator);
			operator.getCEPEventAgent().addCEPEventListener(listView.getListener());
			// add the instances of the operator
			for (Object instance : operator.getInstances()) {
					CEPInstance newInstance = new CEPInstance(
							(StateMachineInstance<?>) instance);
					listView.getNormalList().addToTree(newInstance);
					listView.getQueryList().addToTree(newInstance);
					listView.getStatusList().addToTree(newInstance);
			}
			listView.setInfoData();
			return null;
		} catch (WorkbenchException exception) {
			throw new CEPViewerNotShownException();
		}
	}

	/**
	 * This method gets a reference of the CEPListView or null, if the View
	 * couldn't be found.
	 * 
	 * @return a reference of the CEPListView or null
	 */
	private CEPListView getCEPListView() {
		for (IViewReference reference : PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getViewReferences()) {
			if (reference.getId().equals(CEPListView.ID)) {
				return (CEPListView) reference.getView(false);
			}
		}
		return null;
	}

	/**
	 * This method checks if the selection of the current View is an instance of
	 * the class CepOperator and returns this instance. If it's not, this method
	 * return null.
	 * 
	 * @return a CepOperator or null
	 */
	@SuppressWarnings("rawtypes")
	private CepOperator getCepOperator(IWorkbenchWindow window) {
		// get the selection of the current view
		ISelection selection = window.getActivePage().getSelection();
		// if (selection instanceof IStructuredSelection) {
		IStructuredSelection structSelection = (IStructuredSelection) selection;
		if (structSelection.getFirstElement() instanceof OdysseusNodeView) {
			// if the selected item is a node within the view
			OdysseusNodeView node = (OdysseusNodeView) structSelection
					.getFirstElement();
			if (node.getModelNode().getContent() instanceof CepOperator) {
				// if the seletcted item holds an instance of CepOperator
				return (CepOperator) node.getModelNode().getContent();
			}
		}
		System.out.println("BF");
		return null;
	}

}
