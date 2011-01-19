package de.uniol.inf.is.odysseus.rcp.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.util.ViewHelper;
import de.uniol.inf.is.odysseus.rcp.views.SourcesViewPart;

public class CollapseSourcesViewCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		SourcesViewPart viewer = ViewHelper.getView(OdysseusRCPPlugIn.SOURCES_VIEW_ID, event);
		if( viewer != null ) 
			viewer.getTreeViewer().collapseAll();
		else
			return false;
		return true;
	}

}
