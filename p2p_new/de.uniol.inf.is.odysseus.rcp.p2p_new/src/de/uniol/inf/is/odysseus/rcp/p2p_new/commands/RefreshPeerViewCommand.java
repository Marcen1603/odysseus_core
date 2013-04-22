package de.uniol.inf.is.odysseus.rcp.p2p_new.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.rcp.p2p_new.views.PeerView;

public class RefreshPeerViewCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Optional<PeerView> optView = PeerView.getInstance();
		if( optView.isPresent() ) {
			PeerView view = optView.get();
			
			view.refresh();
		}
		
		return null;
	}

}
