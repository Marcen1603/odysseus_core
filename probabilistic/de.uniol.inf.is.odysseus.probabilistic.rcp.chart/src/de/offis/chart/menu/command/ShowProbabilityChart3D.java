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
		
		IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			NewProbabilityChart3D view = (NewProbabilityChart3D)activePage.showView("de.offis.chart.charts.newprobabilitychart3d", "blablabla", IWorkbenchPage.VIEW_ACTIVATE);
			view.initWithOperator(op);
			// TODO nullpointer wenn ProbabilityChart3D bereits auf ist und nochmal angeklickt wird.
		} catch (PartInitException e) {
			e.printStackTrace();
		}			
		
		
		return null;
	}
}
