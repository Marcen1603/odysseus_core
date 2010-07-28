package de.uniol.inf.is.odysseus.rcp.editor.navigator.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

import de.uniol.inf.is.odysseus.rcp.editor.navigator.view.NavigatorViewPart;

public class RefreshCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		NavigatorViewPart part = Helper.getNavigatorViewPart(event);
		part.getTreeViewer().refresh();
		return null;
	}

}
