package de.uniol.inf.is.odysseus.rcp.commands;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.procedure.StoredProcedure;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.StatusBarManager;

public class DropAllStoredProceduresCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISession session = OdysseusRCPPlugIn.getActiveSession();
		IExecutor executor = OdysseusRCPPlugIn.getExecutor();
		
		List<StoredProcedure> storedProcedures = executor.getStoredProcedures(session);
		if( storedProcedures.isEmpty() || !confirm() ) {
			return null;
		}
		
		for( StoredProcedure storedProcedure : storedProcedures ) {
			executor.removeStoredProcedure(storedProcedure.getName(), session);
		}
		StatusBarManager.getInstance().setMessage("Removed all stored procedures");
		
		return null;
	}

	private static boolean confirm() {
		MessageBox box = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
		box.setMessage("Are you sure to drop ALL stored procedures?");
		box.setText("Drop all stored procedures");
		return box.open() == SWT.OK;
	}
}
