package de.uniol.inf.is.odysseus.cep.cepviewer.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.cep.cepviewer.CEPListView;
import de.uniol.inf.is.odysseus.cep.epa.CepOperator;

/**
 * This class defines the handler which is called if the entries of all lists
 * should be removed.
 * 
 * @author Christian
 */
public class RemoveAllCommand extends AbstractHandler implements IHandler {

	/**
	 * This method removes all entries within the tree of the TreeViewer widget
	 * in every list of the CEPListView.
	 * 
	 * @param event
	 *            is the ExecutionEvent fired by the Button which represents the
	 *            RemoveAll command
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// search for the reference of the CEPListView
		for (IViewReference a : PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getViewReferences()) {
			if (a.getId().equals(CEPListView.ID)) {
				CEPListView view = (CEPListView) a.getView(false);
				// remove the CEPEventListener from every CepOperator
				for (CepOperator<?, ?> operator : view.getOperators()) {
					operator.getCEPEventAgent().removeCEPEventListener(
							view.getListener());
				}
				// remove the list entries an update the status label
				view.getNormalList().removeAll();
				view.getQueryList().removeAll();
				view.getStatusList().removeAll();
				view.setInfoData();
			}
		}
		return null;
	}

}
