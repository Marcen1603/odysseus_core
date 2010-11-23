package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.menu.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.AbstractChart;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.IOdysseusNodeView;

public abstract class AbstractCommand extends AbstractHandler {

	public AbstractChart openView(AbstractChart createView, IPhysicalOperator observingOperator) {
		IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			AbstractChart view = (AbstractChart)activePage.showView(createView.getViewID(), observingOperator.toString(), IWorkbenchPage.VIEW_ACTIVATE);			
			view.init(observingOperator);
			return view;
		} catch (PartInitException e) {
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
