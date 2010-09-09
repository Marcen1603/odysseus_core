package de.uniol.inf.is.odysseus.rcp.sources.view.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

import de.uniol.inf.is.odysseus.rcp.sources.view.part.SourcesViewPart;

public class CollapseAllCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		SourcesViewPart viewer = Helper.getTreeViewer(event);
		if( viewer != null ) 
			viewer.getTreeViewer().collapseAll();
		else
			return false;
		return true;
	}

}
