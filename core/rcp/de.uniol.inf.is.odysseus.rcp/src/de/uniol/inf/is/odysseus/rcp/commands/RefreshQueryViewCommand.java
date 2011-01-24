package de.uniol.inf.is.odysseus.rcp.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.util.ViewHelper;
import de.uniol.inf.is.odysseus.rcp.views.QueryView;

public class RefreshQueryViewCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		QueryView part = ViewHelper.getView(OdysseusRCPPlugIn.QUERY_VIEW_ID, event);
		
		if( part != null ) {
			part.refreshTable();
			return true;
		}
		
		return false;
	}

}
