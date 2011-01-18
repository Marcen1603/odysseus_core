package de.uniol.inf.is.odysseus.cep.cepviewer.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.cep.cepviewer.CEPListView;

/**
 * This class defines the handler which is called if the entries of all lists
 * should expanded.
 * 
 * @author Christian
 */
public class ExpandAllCommand extends AbstractHandler implements IHandler {

	/**
	 * This method expands the trees of the TreeViewer widget in every list of
	 * the CEPListView.
	 * 
	 * @param event
	 *            is the ExecutionEvent fired by the Button which represents the
	 *            ExpandAll command
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// search for the reference of the CEPListView
		for (IViewReference a : PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.getViewReferences()) {
			if (a.getId().equals(CEPListView.ID)) {
				CEPListView view = (CEPListView) a.getView(false);
				// expand the Tree entries of the TreeViewers
				view.getNormalList().getTree().expandAll();
				view.getQueryList().getTree().expandAll();
				view.getStatusList().getTree().expandAll();
			}
		}
		return null;
	}

}
