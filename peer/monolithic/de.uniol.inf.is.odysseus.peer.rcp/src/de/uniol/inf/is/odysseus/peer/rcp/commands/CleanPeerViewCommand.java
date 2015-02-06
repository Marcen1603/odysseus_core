package de.uniol.inf.is.odysseus.peer.rcp.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.peer.rcp.views.peer.PeerView;

public class CleanPeerViewCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Optional<PeerView> optView = PeerView.getInstance();
		if( optView.isPresent() ) {
			
			boolean answer = MessageDialog.openQuestion(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Clean Peer View", "Are you sure to clean the PeerView from inactive peers?");
			if( answer ) {
				optView.get().clear();
			}
			
		}
		return null;
	}

}
