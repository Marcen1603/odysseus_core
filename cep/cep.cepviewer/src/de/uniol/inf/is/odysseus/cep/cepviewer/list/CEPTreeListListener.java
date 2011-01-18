package de.uniol.inf.is.odysseus.cep.cepviewer.list;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.cep.cepviewer.CEPAutomataView;
import de.uniol.inf.is.odysseus.cep.cepviewer.CEPQueryView;
import de.uniol.inf.is.odysseus.cep.cepviewer.CEPStateView;
import de.uniol.inf.is.odysseus.cep.cepviewer.CEPSymbolView;
import de.uniol.inf.is.odysseus.cep.cepviewer.CEPTraceView;
import de.uniol.inf.is.odysseus.cep.cepviewer.listmodel.CEPInstance;
import de.uniol.inf.is.odysseus.cep.cepviewer.listmodel.InstanceTreeItem;

/**
 * This Listener handles the selection within the source widget.
 * 
 * @author Christian
 */
public class CEPTreeListListener implements ISelectionChangedListener {

	/**
	 * This method is called if a new item within the source widget is selected.
	 */
	public void selectionChanged(SelectionChangedEvent event) {
		// get the selection of the widget
		if (event.getSelection() instanceof IStructuredSelection) {
			IStructuredSelection select = (IStructuredSelection) event
					.getSelection();
			if (select.getFirstElement() instanceof InstanceTreeItem) {
				// if the selected item is a CEPInstance show it's data
				CEPInstance instance = ((InstanceTreeItem) select
						.getFirstElement()).getContent();
				instance.createAutomata();
				select(instance);
			} else {
				// ...else reset the views
				this.clearAllViews();
			}
		}
	}

	/**
	 * This method clears all Views within the CEPViewer
	 */
	private void clearAllViews() {
		for (IViewReference reference : PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getViewReferences()) {
			if (reference.getId().equals(CEPAutomataView.ID)) {
				((CEPAutomataView) reference.getView(true)).clearView();
			} else if (reference.getId().equals(CEPQueryView.ID)) {
				((CEPQueryView) reference.getView(true)).clearView();
			} else if (reference.getId().equals(CEPStateView.ID)) {
				((CEPStateView) reference.getView(true)).clear();
			} else if (reference.getId().equals(CEPSymbolView.ID)) {
				((CEPSymbolView) reference.getView(true)).clear();
			} else if (reference.getId().equals(CEPTraceView.ID)) {
				((CEPTraceView) reference.getView(true)).clear();
			}
		}
	}

	/**
	 * This method sets the CEPInstance to all views.
	 * 
	 * @param instance
	 *            is a CEPInstance
	 */
	public void select(CEPInstance instance) {
		for (IViewReference reference : PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getViewReferences()) {
			if (reference.getId().equals(CEPAutomataView.ID)) {
				((CEPAutomataView) reference.getView(true)).showAutomata(instance);
			} else if (reference.getId().equals(CEPQueryView.ID)) {
				((CEPQueryView) reference.getView(true)).setContent(instance);
			} else if (reference.getId().equals(CEPStateView.ID)) {
				((CEPStateView) reference.getView(true)).update();
			} else if (reference.getId().equals(CEPSymbolView.ID)) {
				((CEPSymbolView) reference.getView(true)).setContent(instance);
			} else if (reference.getId().equals(CEPTraceView.ID)) {
				((CEPTraceView) reference.getView(true)).setContent(instance);
			}
		}
	}

}
