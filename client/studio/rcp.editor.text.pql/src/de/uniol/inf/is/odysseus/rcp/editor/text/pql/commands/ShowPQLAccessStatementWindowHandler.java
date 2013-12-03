package de.uniol.inf.is.odysseus.rcp.editor.text.pql.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.rcp.editor.text.pql.windows.PQLAccessStatementGenWindow;

public class ShowPQLAccessStatementWindowHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		PQLAccessStatementGenWindow window = new PQLAccessStatementGenWindow(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		window.show();
		
		return null;
	}

}
