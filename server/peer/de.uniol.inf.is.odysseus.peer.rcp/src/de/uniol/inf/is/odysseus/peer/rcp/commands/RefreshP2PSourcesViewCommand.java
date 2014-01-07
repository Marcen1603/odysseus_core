package de.uniol.inf.is.odysseus.peer.rcp.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.peer.rcp.views.P2PSourcesViewPart;

public class RefreshP2PSourcesViewCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Optional<P2PSourcesViewPart> optView = P2PSourcesViewPart.getInstance();
		if( optView.isPresent() ) {
			P2PSourcesViewPart view = optView.get();
			view.refreshTable();
		}
		return null;
	}

}
