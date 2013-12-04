package de.uniol.inf.is.odysseus.rcp.server.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import de.uniol.inf.is.odysseus.rcp.server.OdysseusRCPServerPlugIn;
import de.uniol.inf.is.odysseus.rcp.server.util.ViewHelper;
import de.uniol.inf.is.odysseus.rcp.server.views.source.SourcesView;

public class SetSortingInSourcesViewCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		SourcesView viewer = ViewHelper.getView(OdysseusRCPServerPlugIn.SOURCES_VIEW_ID, event);
		if( viewer != null ) {
			viewer.setSorting(!viewer.isSorting());
		}
		
		return null;
	}

}
