package de.uniol.inf.is.odysseus.rcp.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.util.ViewHelper;
import de.uniol.inf.is.odysseus.rcp.views.source.SourcesView;

public class SetSortingInSourcesViewCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		SourcesView viewer = ViewHelper.getView(OdysseusRCPPlugIn.SOURCES_VIEW_ID, event);
		if( viewer != null ) {
			viewer.setSorting(!viewer.isSorting());
		}
		
		return null;
	}

}
