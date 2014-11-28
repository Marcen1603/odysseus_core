package de.uniol.inf.is.odysseus.rcp.commands;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import de.uniol.inf.is.odysseus.core.procedure.StoredProcedure;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.StatusBarManager;
import de.uniol.inf.is.odysseus.rcp.util.SelectionProvider;

public class DropStoredProcedureCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		List<StoredProcedure> selectedProcs = SelectionProvider.getSelection(event);

		if (!selectedProcs.isEmpty()) {
			for (StoredProcedure selectedProc : selectedProcs) {
				OdysseusRCPPlugIn.getExecutor().removeStoredProcedure(selectedProc.getName(), OdysseusRCPPlugIn.getActiveSession());
				StatusBarManager.getInstance().setMessage(selectedProcs.size() + " stored procedures removed");
			}
		}

		return null;
	}

}
