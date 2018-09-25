package de.uniol.inf.is.odysseus.rcp.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IViewPart;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.rcp.util.ViewHelper;
import de.uniol.inf.is.odysseus.rcp.views.storedprocedures.StoredProceduresView;

public class RefreshStoredProceduresView extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Optional<IViewPart> view = Optional.fromNullable(ViewHelper.getView("de.uniol.inf.is.odysseus.rcp.views.StoredProceduresView", event));
		if( view.isPresent() ) {
			StoredProceduresView procView = (StoredProceduresView)view.get();
			procView.refresh();
		}
		
		return null;
	}

}
