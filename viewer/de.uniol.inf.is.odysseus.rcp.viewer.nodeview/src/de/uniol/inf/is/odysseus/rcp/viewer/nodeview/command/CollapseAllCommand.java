package de.uniol.inf.is.odysseus.rcp.viewer.nodeview.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

public class CollapseAllCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Helper.getTreeViewer(event).collapseAll();

		return null;
	}

}
