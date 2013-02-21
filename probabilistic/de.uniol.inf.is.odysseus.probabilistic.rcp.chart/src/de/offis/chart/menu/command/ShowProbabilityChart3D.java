package de.offis.chart.menu.command;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import de.offis.chart.charts.NewProbabilityChart3D;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.menu.command.AbstractCommand;

public class ShowProbabilityChart3D extends AbstractCommand {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IPhysicalOperator op = super.getSelectedOperator(event);
//		if(op!=null){
//			openView(new ProbabilityChart3D(), op);
//		}
		
		NewProbabilityChart3D view = new NewProbabilityChart3D();
		IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			activePage.showView(view.getViewID(), "blablabla", IWorkbenchPage.VIEW_ACTIVATE);
			// TODO nullpointer wenn ProbabilityChart3D bereits auf ist und nochmal angeklickt wird.
		} catch (PartInitException e) {
			e.printStackTrace();
		}			
		view.initWithOperator(op);
		
		return null;
	}
}
