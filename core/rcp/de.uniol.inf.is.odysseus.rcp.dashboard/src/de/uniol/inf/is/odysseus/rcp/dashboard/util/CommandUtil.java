package de.uniol.inf.is.odysseus.rcp.dashboard.util;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.rcp.dashboard.editors.DashboardPartPlacement;

public class CommandUtil {

	private CommandUtil() {
		
	}
	
	public static Optional<DashboardPartPlacement> determineSelectedDashboardPart() {
		ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getSelection();
		if( selection instanceof IStructuredSelection ) {
			IStructuredSelection strucSelection = (IStructuredSelection)selection;
			Object selectedObject = strucSelection.getFirstElement();
			if( selectedObject instanceof DashboardPartPlacement ) {
				return Optional.of((DashboardPartPlacement)selectedObject); 
			}
		}
		return Optional.absent();
	}
}
